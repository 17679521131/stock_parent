package com.hzy.stock.config;

import com.hzy.stock.pojo.vo.TaskThreadPoolInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author daocaoaren
 * @date 2024/7/19 14:19
 * @description : 定义线程池的配置类
 */
@Configuration
public class TaskExecutePoolConfig {

    /**
     * 将存储线程池配置的信息的类注入进来
     */
    @Autowired
    private TaskThreadPoolInfo taskThreadPoolInfo;

    /**
     * 构建线程池的bean对象
     * @return
     */
    @Bean(name = "threadPoolTaskExecutor", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置核型线程数
        executor.setCorePoolSize(taskThreadPoolInfo.getCorePoolSize());
        //设置最大线程数
        executor.setMaxPoolSize(taskThreadPoolInfo.getMaxPoolSize());
        //设置任务队列容量
        executor.setQueueCapacity(taskThreadPoolInfo.getQueueCapacity());
        //设置空闲线程最大存活时间
        executor.setKeepAliveSeconds(taskThreadPoolInfo.getKeepAliveSeconds());
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //将设置的参数初始化
        executor.initialize();
        return executor;
    }


}
