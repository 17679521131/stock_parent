package com.hzy.stock.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Configuration;

/**
 * @author daocaoaren
 * @date 2024/7/18 15:35
 * @description : 定义rabbitmq相关配置,由于发送者job工程已经创建好了主题交换机和队列，我们这里只需要监听队列消费消息即可
 */
@Configuration
public class MqConfig {
    /**
     * 重新定义消息序列化的方式，改为基于json格式序列化和反序列化
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
