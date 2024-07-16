package com.hzy.stock.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author daocaoaren
 * @date 2024/7/18 14:54
 * @description : 定义rabbitmq相关配置
 */
@Configuration
public class MqConfig {

    /**
     * 重新定义序列化方式，由默认的jdk方式改为基于json格式序列化和反序列化
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 自定义主题交换机
     * @return
     */
    @Bean
    public TopicExchange stockTopicExchange() {
        /**
         * 参数1：exchange名称，参数2：是否持久化，参数3：是否自动删除
         */
        return new TopicExchange("stockExchange",true, false);
    }


    /**
     * 定义队列
     * @return
     */
    @Bean
    public Queue innerMarketQueue(){
        /**
         * 参数1：队列名称，参数2：是否持久化
         */
        return new Queue("innerMarketQueue",true);
    }

    /**
     * 绑定国内大盘队列到股票主题交换机
     * @return
     */
    @Bean
    public Binding bindingInnerMarketQueue(){
        /**
         * 参数1：队列名称，参数2：交换机名称，参数3：路由key
         */
       return BindingBuilder.bind(innerMarketQueue()).to(stockTopicExchange()).with("inner.market");
    }

}
