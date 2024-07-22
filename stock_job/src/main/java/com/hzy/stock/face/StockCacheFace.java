package com.hzy.stock.face;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/21 22:12
 * @description :
 */
public interface StockCacheFace {
    /**
     * 获取所有的股票编码，并添加上证或深证的股票前缀编号：sh，sz
     */
    List<String> getAllStockCodeWithPrefix();
}
