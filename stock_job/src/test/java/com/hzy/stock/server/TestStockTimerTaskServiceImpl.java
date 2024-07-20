package com.hzy.stock.server;

import com.google.common.collect.Lists;
import com.hzy.stock.mapper.StockBusinessMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author daocaoaren
 * @date 2024/7/17 22:47
 * @description :
 */
@SpringBootTest
public class TestStockTimerTaskServiceImpl {

    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    /**
     * 测试是否可以拿到原始数据
     */
    @Test
    public void test01() throws InterruptedException {
        //stockTimerTaskService.getInnerMarketInfo();
        //stockTimerTaskService.getStockRtIndex();
        //stockTimerTaskService.getStockSectorRtIndex();

        //目的让主线程休眠，等子线程执行完毕
        //Thread.sleep(5000);

        //stockTimerTaskService.getOuterMarketInfo();
    }


    @Test
    public void test02(){
        List<String> allStockCode = stockBusinessMapper.getAllStockCode();
        System.out.println("allStockCode = " + allStockCode);
        //添加大盘业务前缀sh，sz
        allStockCode = allStockCode.stream().map(code -> code.startsWith("6")?"sh"+code:"sz"+code).collect(Collectors.toList());
        System.out.println("allStockCode = " + allStockCode);
        //将所有个股编码组成的大的集合拆分成若干小的结合40-》15，15，10
        //使用谷歌的工具包guava实现拆分
        List<List<String>> partition = Lists.partition(allStockCode, 15);
        partition.forEach(list -> System.out.println("list = " + list));
    }

}
