package com.hzy.stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author daocaoaren
 * @date 2024/7/15 03:58
 * @description :定义公共配置bean
 */
@Configuration
public class CommonConfig {

    /**
     * 定义一个密码加密、匹配器bean，匹配账号密码是否一对
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
