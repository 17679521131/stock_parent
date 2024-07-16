package com.hzy.stock.mapper;

import com.hzy.stock.pojo.vo.StockInfoConfig;
import com.hzy.stock.utils.DateTimeUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author daocaoaren
 * @date 2024/7/16 20:35
 * @description :
 */
@SpringBootTest
public class TestGetStockTradeAmtMin {

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

    @Autowired
    private StockInfoConfig stockInfoConfig;


    @Test
    public void test(){
        //1.获取T日和T-1日的开始时间和结束时间
        //1.1 获取最近股票有效交易时间点--T日时间范围
        DateTime lastDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //获取当日最近的开盘时间
        DateTime openDateTime = DateTimeUtil.getOpenDate(lastDateTime);
        //转换成Java中的data，方便jdbc识别
        Date lastDate = lastDateTime.toDate();
        Date openDate = openDateTime.toDate();
        //TODO  mock数据
        lastDate=DateTime.parse("2022-01-03 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        openDate=DateTime.parse("2022-01-03 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<String> innerCode = stockInfoConfig.getInner();
        List<Map> stockTradeAmtMin = stockMarketIndexInfoMapper.getStockTradeAmtMin(innerCode, openDate, lastDate);
        System.out.println("stockTradeAmtMin = " + stockTradeAmtMin);
    }
}
