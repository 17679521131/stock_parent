package com.hzy.stock.service.impl;

import com.hzy.stock.mapper.StockBlockRtInfoMapper;
import com.hzy.stock.mapper.StockMarketIndexInfoMapper;
import com.hzy.stock.pojo.domain.InnerMarketDomain;
import com.hzy.stock.pojo.domain.StockBlockDomain;
import com.hzy.stock.pojo.entity.StockMarketIndexInfo;
import com.hzy.stock.pojo.vo.StockInfoConfig;
import com.hzy.stock.service.StockService;
import com.hzy.stock.utils.DateTimeUtil;
import com.hzy.stock.vo.resp.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/15 17:55
 * @description :
 */
@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;

    /**
     * 获取国内最新大盘的数据
     * @return
     */
    @Override
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {
        //1.获取股票的最新交易时间点（精确到分钟，秒和毫秒设置为0）
        DateTime newDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date newDate = newDateTime.toDate();
        //模拟数据
        newDate = DateTime.parse("2021-12-28 09:31:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //2.获取大盘编码集合
        List<String> innerCode = stockInfoConfig.getInner();
        //3.调用mapper接口查询数据
        List<InnerMarketDomain> data =  stockMarketIndexInfoMapper.getMarketInfo(newDate,innerCode);
        //4.封装返回数据
        return R.ok(data);
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
        lastDate=DateTime.parse("2021-12-21 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //根据时间获取股票板块数据
        List<StockBlockDomain> data = stockBlockRtInfoMapper.getStockBlockInfoLimit(lastDate);
        //封装返回数据
        return R.ok(data);
    }


}
