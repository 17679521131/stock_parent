package com.hzy.stock.service;

import com.hzy.stock.pojo.domain.InnerMarketDomain;
import com.hzy.stock.pojo.domain.StockBlockDomain;
import com.hzy.stock.vo.resp.R;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/15 17:52
 * @description :
 */

public interface StockService {
    /**
     * 获取国内最新大盘的数据
     * @return
     */
    R<List<InnerMarketDomain>> getInnerMarketInfo();

    /**
     * 获取股票板块数据,获取上海深圳板块最新数据，以交易总金额降序查询，取前十条数据
     * @return
     */
    R<List<StockBlockDomain>> getStockBlockInfoLimit();
}
