package com.hzy.stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author daocaoaren
 * @date 2024/7/17 16:13
 * @description : 定义一个访问http的配置类
 */
@Configuration
public class HttpClientConfig {

    /**
     * 将RestTemplate注入到IOC容器中
     * @return
     */
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
