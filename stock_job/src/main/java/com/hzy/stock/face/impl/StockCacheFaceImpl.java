package com.hzy.stock.face.impl;

import com.hzy.stock.face.StockCacheFace;
import com.hzy.stock.mapper.StockBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author daocaoaren
 * @date 2024/7/21 22:16
 * @description : 定义股票缓存层的实现
 */
@Component("stockCacheFace")
public class StockCacheFaceImpl implements StockCacheFace {

    @Autowired
    private StockBusinessMapper stockBusinessMapper;


    @Cacheable(cacheNames = "stock",key="'stockCodes'")
    @Override
    public List<String> getAllStockCodeWithPrefix() {
        //获取所有的个股集合  3000+ 由于数据量太多，直接拼接会导致url过长，使对方服务器拒绝访问
        List<String> allStockCode = stockBusinessMapper.getAllStockCode();
        //将个股添加大盘业务前缀 sh，sz
        allStockCode = allStockCode.stream().map(code->code.startsWith("6")?"sh"+code:"sz"+code).collect(Collectors.toList());
        return allStockCode;
    }
}
