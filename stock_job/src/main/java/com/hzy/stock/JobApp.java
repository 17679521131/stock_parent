package com.hzy.stock;

import com.hzy.stock.pojo.vo.TaskThreadPoolInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author daocaoaren
 * @date 2024/7/17 16:10
 * @description : 启动类
 */
@SpringBootApplication
@MapperScan("com.hzy.stock.mapper")
@EnableConfigurationProperties({TaskThreadPoolInfo.class})
public class JobApp {
    public static void main(String[] args) {
        SpringApplication.run(JobApp.class, args);
    }
}
