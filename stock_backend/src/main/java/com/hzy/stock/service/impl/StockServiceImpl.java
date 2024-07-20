package com.hzy.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzy.stock.mapper.*;
import com.hzy.stock.pojo.domain.*;
import com.hzy.stock.pojo.entity.StockBlockRtInfo;
import com.hzy.stock.pojo.entity.StockMarketIndexInfo;
import com.hzy.stock.pojo.vo.StockInfoConfig;
import com.hzy.stock.service.StockService;
import com.hzy.stock.utils.DateTimeUtil;
import com.hzy.stock.vo.resp.PageResult;
import com.hzy.stock.vo.resp.R;
import com.hzy.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author daocaoaren
 * @date 2024/7/15 17:55
 * @description :
 */
@Service
@Slf4j //打印日志的注解
public class StockServiceImpl implements StockService {
    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;

    /**
     * 注入本地缓存的bean
     */
    @Autowired
    private Cache<String,Object> caffineCache;

    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    /**
     * 获取国内最新大盘的数据
     * @return
     */
    @Override
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {
        //默认从本地缓存加入数据，如果不存在就去数据库加载并同步到本地缓存
        //在开盘周期内，本地缓存默认有效一分钟
        R<List<InnerMarketDomain>> result = (R<List<InnerMarketDomain>>) caffineCache.get("innerMarketKey", key->{
            //1.获取股票的最新交易时间点（精确到分钟，秒和毫秒设置为0）
            DateTime newDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
            Date newDate = newDateTime.toDate();
            //TODO:模拟数据
            newDate = DateTime.parse("2021-12-28 09:31:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

            //2.获取大盘编码集合
            List<String> innerCode = stockInfoConfig.getInner();
            //3.调用mapper接口查询数据
            List<InnerMarketDomain> data =  stockMarketIndexInfoMapper.getMarketInfo(newDate,innerCode);
            return R.ok(data);
        });
        //4.封装返回数据
        return result;
    }

    /**
     * 获取股票板块数据,获取上海深圳板块最新数据，以交易总金额降序查询，取前十条数据
     * @return
     */
    @Override
    public R<List<StockBlockDomain>> getStockBlockInfoLimit() {
        //1.获取股票最新交易时间点
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO mock数据,后续删除
        lastDate=DateTime.parse("2021-12-21 09:30:01", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //根据时间获取股票板块数据
        List<StockBlockDomain> data = stockBlockRtInfoMapper.getStockBlockInfoLimit(lastDate);
        //封装返回数据
        return R.ok(data);
    }

    /**
     * 分页查询个股票最新数据，并按照涨幅排序查询,展示出股票涨幅最大的数据，也就是涨幅榜查看更多功能，列举出所有个股的情况
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public R<PageResult<StockUpdownDomain>> getStockPageInfo(Integer page, Integer pageSize) {
        //1.设置PageHelper分页参数
        PageHelper.startPage(page, pageSize);
        //2.获取当前最新的股票交易时间点
        Date newDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO mock数据,后续删除
        newDate=DateTime.parse("2022-05-20 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //3.调用mapper接口查询
        List<StockUpdownDomain> data = stockRtInfoMapper.getNewDateStockPageInfo(newDate);
        if(CollectionUtils.isEmpty(data)){
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        //4.组装PageInfo对象，获取分页的具体信息,因为PageInfo包含了丰富的分页信息，而部分分页信息是前端不需要的
        //PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(date);
        // 将获取的数据放到PageInfo中，在PageResult中有构造器，将从数据库中获取的数据封装到PageInfo中，
        // 统计条数并且将数据封装到rows结果集中
//        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(new PageInfo<>(data));
        //5.封装响应数据
        return R.ok(pageResult);
    }

    /**
     * 设计股票涨幅榜模块，需求是查询涨幅榜最大的前4条数据展示在前端，将个股涨跌幅最大的四条个股显示在涨幅榜
     * @return
     */
    @Override
    public R<List<StockUpdownDomain>> getStockIncreaseMax() {
        //1.获取股票最新交易时间点
        Date newDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO mock数据,后续删除
        newDate=DateTime.parse("2022-05-20 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.调用mapper接口查询
        List<StockUpdownDomain> data =  stockRtInfoMapper.getStockIncreaseMax(newDate);
        //3.封装响应数据
        return R.ok(data);
    }

    /**
     * 统计股票最新交易日内每分钟的涨跌停的股票数量
     * @return
     */
    @Override
    public R<Map<String, List>> getStockUpdownCount() {
        //1.获取最新股票交易的时间点（截止时间）
        DateTime newDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //TODO 先mock数据,后续删除
        newDateTime = DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date newendDate = newDateTime.toDate();
        //获取最新交易时间对应的开盘点
        Date newstartDate = DateTimeUtil.getOpenDate(newDateTime).toDate();

        //统计涨停数据
        List<Map> upData = stockRtInfoMapper.getStockUpDownCount(newstartDate, newendDate,1);
        //统计跌停数据
        List<Map> downData = stockRtInfoMapper.getStockUpDownCount(newstartDate, newendDate,0);
        //封装返回数据
        HashMap<String, List> newUpDownData = new HashMap<>();
        newUpDownData.put("upList", upData);
        newUpDownData.put("downList", downData);

        return R.ok(newUpDownData);
    }

    /**
     * 导出指定页码最新股票数据到excel
     * @param response  响应对象
     * @param page  当前页
     * @param pageSize 每页大小
     * @return
     */
    @Override
    public void exportStockInFoByPage(HttpServletResponse response, Integer page, Integer pageSize) {
        //1.获取分页数据
        R<PageResult<StockUpdownDomain>> r = this.getStockPageInfo(page, pageSize);
        PageResult<StockUpdownDomain> data = r.getData();
        List<StockUpdownDomain> stockUpdownDomains = data.getRows();
        //2.将数据导出到excel中
        //设置响应excel文件格式类型
        response.setContentType("application/vnd.ms-excel");
        //2.设置响应数据的编码格式
        response.setCharacterEncoding("utf-8");
        //3.设置默认的文件名称
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        try {
            String fileName = URLEncoder.encode("stockRt", "UTF-8");
            //设置默认文件名称：兼容一些特殊浏览器
            response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xlsx");
            //4.响应excel流
            EasyExcel
                    .write(response.getOutputStream(),StockUpdownDomain.class)
                    .sheet("股票涨幅信息")
                    .doWrite(stockUpdownDomains);
        } catch (IOException e) {
           log.error("当前导出数据异常，当前页：{},每页大小：{},异常信息：{}",page,pageSize,e.getMessage());
           //通知前端异常，稍后重试
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            R<Object> error = R.error(ResponseCode.ERROR);

            try {
                String jsonData =  new ObjectMapper().writeValueAsString(error);
                response.getWriter().write(jsonData);
            } catch (IOException ex) {
                log.error("响应错误信息失败");
            }
        }


    }

    /**
     * 统计A股大盘T日和T-1日成交量对比功能（成交量为沪深两市成交量之和），实现成交量功能
     * @return
     */
    @Override
    public R<Map<String, List>> getStockTradeAmt() {
        //1.获取T日和T-1日的开始时间和结束时间
        //1.1 获取最近股票有效交易时间点--T日时间范围
        DateTime lastDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //获取当日最近的开盘时间
        DateTime openDateTime = DateTimeUtil.getOpenDate(lastDateTime);
        //转换成Java中的data，方便jdbc识别
        Date lastDate = lastDateTime.toDate();
        Date openDate = openDateTime.toDate();
        //TODO  mock数据
        openDate=DateTime.parse("2022-01-03 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        lastDate=DateTime.parse("2022-01-03 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();


        //获取上一个交易日的时间
        DateTime preLastDateTime = DateTimeUtil.getPreviousTradingDay(lastDateTime);
        //获取上一个交易日最近的开盘时间
        DateTime preOpenDateTime = DateTimeUtil.getOpenDate(preLastDateTime);
        //转换成Java中的data，方便jdbc识别
        Date preLastDate = preLastDateTime.toDate();
        Date preOpenDate = preOpenDateTime.toDate();
        //TODO  mock数据
        preOpenDate=DateTime.parse("2022-01-02 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        preLastDate=DateTime.parse("2022-01-02 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();


        //2.获取上证和深证的配置的大盘id
        //2.1 获取大盘的id集合
        List<String> innerCode = stockInfoConfig.getInner();

        //查询T日当前时间的大盘交易数量
        List<Map> TData = stockMarketIndexInfoMapper.getStockTradeAmtMin(innerCode,openDate,lastDate);
        if(CollectionUtils.isEmpty(TData)){
            TData =  new ArrayList<>();
        }

        //查询T-1日当前时间的大盘交易数量
        List<Map> T_1Data = stockMarketIndexInfoMapper.getStockTradeAmtMin(innerCode,preOpenDate,lastDate);
        if(CollectionUtils.isEmpty(T_1Data)){
            T_1Data =  new ArrayList<>();
        }

        //3.封装返回数据
        HashMap<String, List> allDate = new HashMap<>();
        allDate.put("amtList", TData);
        allDate.put("yesAmtList", T_1Data);


        return R.ok(allDate);
    }

    /**
     * 统计当前时间下各个股票的涨幅区间的数量，完善各股涨跌图，涨幅区间分别为<-7% -7%~-5% -5%~-3% -3%~0% 0%~3% 3%~5% 5%~7% 7%>
     * @return
     */
    @Override
    public R<Map> getStockIncreaseRangeInfo() {
        //获取股票的最新交易时间点
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date newDate = dateTime.toDate();
        //TODO  mock数据
        newDate=DateTime.parse("2022-01-06 09:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //调用mapper接口获取数据
        List<Map> stockIncreaseRange = stockRtInfoMapper.getStockIncreaseRangeInfoByDate(newDate);
        //获取有序的涨幅区间标题集合
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        //将顺序的涨幅区间内的每个元素转花成map集合可以实现前端的顺序展示
        //方式一解决前端显示数据不顺序的问题
       /* List<Map> lastAllDate = new ArrayList<>();
        for (String title : upDownRange) {
            Map tmp = null;
            for (Map map : stockIncreaseRange) {
                if(map.containsValue(title)){
                    tmp = map;
                    break;
                }
            }

            if (tmp == null){
                //这个区间没有数据，就将这个数据弄成零返回给前端
                tmp = new HashMap<>();
                tmp.put("count",0);
                tmp.put("title",title);
            }
            lastAllDate.add(tmp);
        }*/
        //方式二解决前端显示数据不顺序的问题stream流
        List<Map> lastAllDate = upDownRange.stream().map(title -> {
            Optional<Map> result = stockIncreaseRange.stream().filter(map -> map.containsValue(title)).findFirst();
            if (result.isPresent()) {

                return result.get();
            } else {
                Map tmp = new HashMap<>();
                tmp.put("count", 0);
                tmp.put("title", title);
                return tmp;
            }
        }).collect(Collectors.toList());
        //封装返回数据
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("time", dateTime.toString("yyyy-MM-dd HH:mm:ss"));
        resultMap.put("infos",lastAllDate);
        return R.ok(resultMap);
    }

    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据； //在涨幅榜点击查看更多再点击单个股票触发
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param stockCode 股票编码
     * @return
     */
    @Override
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String stockCode) {
        //1.获取股票的最新有效交易时间
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date endDate = dateTime.toDate();

        //TODO  mock数据
        endDate=DateTime.parse("2021-12-30 14:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //根据最新有效交易时间获取股票最新开盘时间
        DateTime openDate = DateTimeUtil.getOpenDate(dateTime);
        Date startDate = openDate.toDate();
        //TODO  mock数据
        startDate=DateTime.parse("2021-12-30 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.调用mapper接口，根据开盘时间和当前时间和股票编码查询分时数据
        List<Stock4MinuteDomain> data = stockRtInfoMapper.getStockInfoByDateAndCode(stockCode,startDate,endDate);
        //判断非空
        if(CollectionUtils.isEmpty(data)){
            data = new ArrayList<>();
        }
        //3.封装返回数据
        return R.ok(data);
    }

    /**
     * 单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * 默认查询历史10天的数据；
     * 因为要将日K线数据绘制到图表上不能只画一天的所以说获取多天的
     * @param stockCode 股票编码
     * @return
     */
    @Override
    public R<List<Stock4EveryDayDomain>> stockScreenDKLine(String stockCode) {
        //1.获取股票的最新有效交易时间
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date endDate = endDateTime.toDate();
        //TODO  mock数据
        endDate=DateTime.parse("2022-01-07 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //根据前10天的每日各股数据
        DateTime startDateTime = endDateTime.minusDays(10);//往前计算10天，比如当天一月11日，推前十天就是一月一日
        Date startDate = startDateTime.toDate();
        //TODO  mock数据
        startDate=DateTime.parse("2022-01-01 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //2.调用mapper接口，根据起始时间和当前时间和股票编码查询分时数据  实现方法一
        //List<Stock4EveryDayDomain> data =  stockRtInfoMapper.getStockInfoByEveryDay(stockCode,startDate,endDate);
        //由于后续数据量大会进行分库分表，所以我们这里不能嵌套查询，也就是不能以查询的sql作为条件再查询
        //后续的查询会跨库跨表所以我们将其拆分为两个sql语句来使用
        //先查询指定日期范围内的每日最大时间，封装到list集合中返回实现方法二
        List<Date> dateList = stockRtInfoMapper.getStockInfoEveryDay(stockCode,startDate,endDate);
        //再根据收盘时间获取日K线数据
        List<Stock4EveryDayDomain> data = stockRtInfoMapper.getStockInfoBySelectEverDay(stockCode,dateList);

        //判断非空
        if(CollectionUtils.isEmpty(data)){
            data = new ArrayList<>();
        }
        //3.封装返回数据
        return R.ok(data);
    }

    /**
     * 获取最新外盘指数
     * @return
     */
    @Override
    public R<List<OuterMarketDomain>> getOutMaketInfo() {
        //由于国外开盘时间不稳定，所以根据时间和大盘点数降序获取前四条数据即可
        //调用mapper接口获取最新外盘指数
        List<OuterMarketDomain> data = stockOuterMarketIndexInfoMapper.getOutMarketInfoByDate();
        //判断非空
        if(CollectionUtils.isEmpty(data)){
            data = new ArrayList<>();
            log.error("没有查询到数据");
        }
        return R.ok(data);
    }

    /**
     * 股票搜索功能，模糊查询返回股票编码名称和编码
     * @param searchStr 模糊输入的股票半编码
     * @return
     */
    @Override
    public R<List<Map>> getStockNameAndCode(String searchStr) {
        //根据输入的字符串返回对应的股票编码和名称，调用mapper接口
        List<Map> list =  stockBusinessMapper.getStockNameAndCodeByStr(searchStr);

        return R.ok(list);
    }

    /**
     * 获取股票业务信息
     * @param code 股票编码
     * @return
     */
    @Override
    public R<StockBusinessDomain> getStockBusinessInfo(String code) {
        //调用mapper接口获取股票业务信息
        StockBusinessDomain stockBusiness =  stockBusinessMapper.getStockBusinessInfoByCode(code);
        return R.ok(stockBusiness);
    }

    /**
     * 获取个股票最分时新行情数据
     * @param code 股票编码
     * @return
     */
    @Override
    public R<StockNewPriceDomain> getStockNewPriceInfo(String code) {
        //获取最新有效时间
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date endDate = dateTime.toDate();
        //调用mapper接口根据股票编码获取最新分时数据
        //TODO  mock数据
        endDate=DateTime.parse("2022-01-05 09:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        StockNewPriceDomain stockNewPrice =  stockRtInfoMapper.getStockNewPriceByCode(code,endDate);
        //返回数据
        return R.ok(stockNewPrice);
    }

    /**
     * 获取股票最新交易量数据
     * @param code 股票编码
     * @return
     */
    @Override
    public R<List<StockNewTransactionDomain>> getStockNewTransactionInfo(String code) {
        //直接调用接口将时间降序查询显示前十条数据即可
        List<StockNewTransactionDomain> stockNewTransactions =  stockRtInfoMapper.getStockNewTransactionByCode(code);
        return R.ok(stockNewTransactions);
    }

    /**
     * 获取股票周K线数据
     * @param code 股票编码
     * @return
     */
    @Override
    public R<List<Stock4EveryWeekDomain>> stockScreenWeekKLine(String code) {
        //获取当前最新股票有效时间
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date endDate = dateTime.toDate();
        //TODO  mock数据
        endDate=DateTime.parse("2022-01-14 15:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //获取前一周有效时间
        DateTime startDateTime = dateTime.minusWeeks(20);
        DateTime openDate = DateTimeUtil.getOpenDate(startDateTime);
        Date startDate = openDate.toDate();
        startDate = DateTime.parse("2022-01-05 09:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //调用mapper接口获取周K线数据
        List<Stock4EveryWeekDomain> list =  stockRtInfoMapper.getStockWeekKLineByCode(code,startDate,endDate);
        //判断非空
        if(CollectionUtils.isEmpty(list)){
            list = new ArrayList<>();
        }
        return R.ok(list);
    }


}
