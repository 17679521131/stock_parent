package com.hzy.stock.server;

/**
 * @author daocaoaren
 * @date 2024/7/17 22:07
 * @description : 定义股票采集数据服务的接口
 */
public interface StockTimerTaskService {

    /**
     * 获取国内大盘的实时数据
     */
    void getInnerMarketInfo();

    /**
     * 定义获取分钟级个股票数据
     */
    void getStockRtIndex();

    /**
     * 获取板块数据采集功能
     */
    void getStockSectorRtIndex();

    /**
     * 获取国外大盘的实时数据
     */
    void getOuterMarketInfo();
}
