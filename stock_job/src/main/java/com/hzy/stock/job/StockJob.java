package com.hzy.stock.job;

import com.hzy.stock.server.StockTimerTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author daocaoaren
 * @date 2024/7/18 22:14
 * @description : 定义xxljob任务执行器bean
 */
@Component
public class StockJob {

    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("myJobHandler")
    public void demoJobHandler() throws Exception {
        System.out.println("当前时间点为:"+ DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 定时获取A股个股数据
     */
    @XxlJob("getStockInfos")
    public void getStockInfos() throws Exception {
        stockTimerTaskService.getStockRtIndex();
    }

    /**
     * 定时采集A股大盘数据
     */
    @XxlJob("getInnerMarketInfo")
    public void getInnerMarketInfo() throws Exception {
        stockTimerTaskService.getInnerMarketInfo();
    }

    /**
     * 定时采集A股板块数据
     */
    @XxlJob("getStockSectorRtIndex")
    public void getStockSectorRtIndex() throws Exception {
        stockTimerTaskService.getStockSectorRtIndex();
    }

    /**
     * 定时采集国外大盘数据
     */
    @XxlJob("getOuterMarketInfo")
    public void getOuterMarketInfo() throws Exception {
        stockTimerTaskService.getOuterMarketInfo();
    }

}
