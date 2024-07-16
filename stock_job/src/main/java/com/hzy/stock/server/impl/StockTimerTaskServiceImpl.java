package com.hzy.stock.server.impl;

import com.google.common.collect.Lists;
import com.hzy.stock.mapper.StockBlockRtInfoMapper;
import com.hzy.stock.mapper.StockBusinessMapper;
import com.hzy.stock.mapper.StockMarketIndexInfoMapper;
import com.hzy.stock.mapper.StockRtInfoMapper;
import com.hzy.stock.pojo.entity.StockBlockRtInfo;
import com.hzy.stock.pojo.entity.StockMarketIndexInfo;
import com.hzy.stock.pojo.entity.StockRtInfo;
import com.hzy.stock.pojo.vo.StockInfoConfig;
import com.hzy.stock.server.StockTimerTaskService;
import com.hzy.stock.utils.DateTimeUtil;
import com.hzy.stock.utils.IdWorker;
import com.hzy.stock.utils.ParseType;
import com.hzy.stock.utils.ParserStockInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author daocaoaren
 * @date 2024/7/17 22:09
 * @description :
 */
@Service
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IdWorker idWorker;  //注入雪花算法生成id

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;



    /**
     * 必须保证该对象无状态，就是别的对象不能调用方法修改他里面的数据，当然这个对象修改不了数据
     */
    private HttpEntity<Object> httpEntity;

    /**
     * 获取国内大盘的实时数据
     */
    @Override
    public void getInnerMarketInfo() {
        //第一阶段：采集原始数据
        //设置请求地址String.join()方法可以将集合做拼接，第一个参数是拼接的符号，第二个参数是要拼接的集合
        String marketUrl = stockInfoConfig.getMarketUrl()+String.join(",",stockInfoConfig.getInner());
      /*  //设置请求头参数,添加防盗链和用户标识
        HttpHeaders httpHeaders = new HttpHeaders();
        //添加用户标识
        httpHeaders.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3.1 Safari/605.1.15");
        //添加防盗链
        httpHeaders.set("Referer","https://finance.sina.com.cn/");
        //将请求头填充到请求实体对象中
        HttpEntity<Map> entity = new HttpEntity<>(httpHeaders);*/
        //发起请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(marketUrl, HttpMethod.GET, httpEntity, String.class);
        //判断是否访问成功
        if(responseEntity.getStatusCodeValue()!=200){
            //访问失败，抛出异常,显示当前请求失败
            log.error("当前时间点:{},采集数据失败,http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),responseEntity.getStatusCodeValue());
            //优化这里可以添加功能发生异常给运维或者工作人员发邮件微信等，
            //当没请求异常了也没有数据解析所以直接退出
            return;
        }
        //获取到原始数据
        /*var hq_str_sh000001="上证指数,2970.8081,2976.3043,2962.8549,2972.7949,2957.9480,0,0,282307991,305469105978,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2024-07-17,15:30:39,00,";
          var hq_str_sz399001="深证成指,8860.753,8877.018,8835.144,8884.792,8809.133,0.000,0.000,33806544344,369613482661.803,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,2024-07-17,15:00:03,00";*/
        String jsData = responseEntity.getBody();
        log.error("当前时间点:{},采集数据内容{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),responseEntity.getBody());
        //第二阶段：用Java正则表达式解析原始数据
        //定义正则表达式
        String reg = "var hq_str_(.+)=\"(.+)\";";
        //编译表达式
        Pattern pattern = Pattern.compile(reg);
        //匹配字符串
        Matcher matcher = pattern.matcher(jsData);
        //创建存储数据的集合
        ArrayList<StockMarketIndexInfo> list = new ArrayList<>();
        while (matcher.find()){
            //获取大盘编码
            String marketCode = matcher.group(1);
            //获取其他信息
            String otherInfo = matcher.group(2);
            String[] splitArr  = otherInfo.split(",");
            //大盘名称
            String marketName=splitArr[0];
            //获取当前大盘的开盘点数
            BigDecimal openPoint=new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint=new BigDecimal(splitArr[2]);
            //获取大盘的当前点数
            BigDecimal curPoint=new BigDecimal(splitArr[3]);
            //获取大盘最高点
            BigDecimal maxPoint=new BigDecimal(splitArr[4]);
            //获取大盘的最低点
            BigDecimal minPoint=new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt=Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol=new BigDecimal(splitArr[9]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30] + " " + splitArr[31]).toDate();
            //将解析数据封装到实体类中
            StockMarketIndexInfo stockMarketIndexInfo = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .curPoint(curPoint)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeVolume(tradeVol)
                    .tradeAmount(tradeAmt)
                    .curTime(curTime)
                    .build();
            //将数据返回给list集合
            list.add(stockMarketIndexInfo);
        }
        //定义日志消息答应当前采集的数据
        log.info("采集的当前大盘数据：{}",list);
        //调用mapper接口将数据保存到数据库中
        int row = stockMarketIndexInfoMapper.insertSelectData(list);//因为底层id和curTime建立了唯一索引，所以不能挖掘用一时间数据会报错
        //int row=2;
        if (row>0) {
            //大盘数据采集完毕通知backend工程刷新缓存
            //发送日期对象，接收方通过接收的日期与当前时间比对能判断出数据延迟的时长，用于运维通知
            rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());

            log.info("当前时间点:{},插入数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        }else {
            log.info("当前时间点:{},插入数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        }

    }


    /**
     * 定义获取分钟级个股票数据
     */
    @Override
    public void getStockRtIndex() {
        //获取所有的个股集合  3000+ 由于数据量太多，直接拼接会导致url过长，使对方服务器拒绝访问
        List<String> allStockCode = stockBusinessMapper.getAllStockCode();
        //将个股添加大盘业务前缀 sh，sz
        allStockCode = allStockCode.stream().map(code->code.startsWith("6")?"sh"+code:"sz"+code).collect(Collectors.toList());
        //将所有个股编码拆分成若干个小集合
        Lists.partition(allStockCode, 15).forEach(codes->{
            //分批次采集数据 设置url地址
            String url = stockInfoConfig.getMarketUrl()+String.join(",",codes);
            /*//添加请求头
            HttpHeaders httpHeaders = new HttpHeaders();
            //添加用户标识
            httpHeaders.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3.1 Safari/605.1.15");
            //添加防盗链
            httpHeaders.set("Referer","https://finance.sina.com.cn/");
            //创建请求实体
            HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);*/
            //发送请求
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            if (responseEntity.getStatusCodeValue()!=200) {
                log.error("当前时间点:{},请求个股数据失败,状态码:{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),responseEntity.getStatusCodeValue());
                return;
            }
            //获取响应体js数据
            String body = responseEntity.getBody();
            //调用工具类解析获取各个数据
            List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(body, ParseType.ASHARE);
            log.info("当前时间点:{},采集的个股数据:{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            //将调用mapper接口数据保存到数据库中  //批量插入
            int row = stockRtInfoMapper.insertAllData(list);
            if (row>0) {
                log.info("当前时间点:{},插入数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            }else {
                log.info("当前时间点:{},插入数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            }
        });
    }

    /**
     * 获取数板块数据插入数据库
     */
    @Override
    public void getStockSectorRtIndex() {
        //获取板块数据请求的url地址
        String blockUrl = stockInfoConfig.getBlockUrl();
        //发起请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(blockUrl, HttpMethod.GET, httpEntity, String.class);
        if (responseEntity.getStatusCodeValue()!=200) {
            log.error("当前时间点:{},请求板块数据失败,状态码:{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),responseEntity.getStatusCodeValue());
            return;
        }
        //获取响应体js数据
        String body = responseEntity.getBody();
        //调用工具类解析获取各个数据
        List<StockBlockRtInfo> list = parserStockInfoUtil.parse4StockBlock(body);
        log.info("板块数据量：{}",list.size());
        log.info("当前时间点:{},采集的板块数据:{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        //数据分片保存到数据库下 行业板块类目大概50个，可每小时查询一次即可
        Lists.partition(list,20).forEach(lists->{
            //将调用mapper接口数据保存到数据库中  //批量插入
            int row = stockBlockRtInfoMapper.insertAllData(lists);
            if (row>0) {
                log.info("当前时间点:{},插入数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),lists);
            }else {
                log.info("当前时间点:{},插入数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),lists);
            }
        });



    }


    /**
     * bean生命周期的初始化方法回掉
     */
    @PostConstruct
    public void initData() {
        //添加请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        //添加用户标识
        httpHeaders.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3.1 Safari/605.1.15");
        //添加防盗链
        httpHeaders.set("Referer","https://finance.sina.com.cn/");
        //初始化httpEntity
        httpEntity = new HttpEntity<>(httpHeaders);
    }
}
