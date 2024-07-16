package com.hzy.stock.config;

import com.hzy.stock.pojo.vo.StockInfoConfig;
import com.hzy.stock.utils.IdWorker;
import com.hzy.stock.utils.ParserStockInfoUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.tags.Param;

/**
 * @author daocaoaren
 * @date 2024/7/15 03:58
 * @description :定义公共配置bean
 */
@Configuration
@EnableConfigurationProperties({StockInfoConfig.class})//开启对相关配置对象的加载
public class CommonConfig {
    /**
     * 基于雪花算法生成id
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        //参数1：机器id，参数2：机房id，这个一般运维人员管理
        return new IdWorker(1L,2L);
    }

    /**
     * 定义解析股票大盘，外盘，个股，板块相关信息的工具类bean
     * @param idWorker
     * @return
     */
    @Bean
    public ParserStockInfoUtil parserStockInfoUtil(IdWorker idWorker){
        return new ParserStockInfoUtil(idWorker);
    }


}
