package com.hzy.stock.mq;

import com.github.benmanes.caffeine.cache.Cache;
import com.hzy.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author daocaoaren
 * @date 2024/7/18 15:42
 * @description : 定义股票相关mq消息监听
 */
@Component
@Slf4j
public class StockMQMsgListener {
    @Autowired
    private Cache<String,Object> caffeineCache;

    @Autowired
    private StockService stockService;




    @RabbitListener(queues = "innerMarketQueue")
    public void  refreshInnerMarketInfo(Date startDate){
        //统计当前时间与发送时间的差值，如果大于一分钟则警告
        //获取时间差毫秒值
        long diffTime = DateTime.now().getMillis() - new DateTime(startDate).getMillis();
        if(diffTime > 60000l){
            log.error("大盘消息发送时间:{},与当前时间相差{}毫秒",startDate, diffTime);
        }
        //刷新缓存
        //删除旧的数据
        caffeineCache.invalidate("innerMarketKey");
        //调用服务方法刷新数据
        stockService.getInnerMarketInfo();

    }
}
