package com.hzy.stock.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author daocaoaren
 * @date 2024/7/15 13:41
 * @description : 自定义redis序列化方式，避免使用默认的jdk序列化方式
 * 由于使用jdk序列化的方式的阅读性比较差且序列化后的内容数据比较大会占用很大的内存，所以我们换一种序列化方式
 */
@Configuration
public class CacheConfig {

    /**
     * 构建本地缓存的bean
     * @return
     */
    @Bean
    public Cache<String,Object> caffieineCache(){
        Cache<String, Object> cache = Caffeine
                .newBuilder()
                .maximumSize(200)//设置缓存数量上限
//                .expireAfterAccess(1, TimeUnit.SECONDS)//访问1秒后删除
//                .expireAfterWrite(1,TimeUnit.SECONDS)//写入1秒后删除
                .initialCapacity(100)// 初始的缓存空间大小
                .recordStats()//开启统计
                .build();
        return cache;
    }


    /**
     * 配置redisTemplate bean，自定义数据的序列化方式，这里的名字必须为redisTemplate，否则场景依赖会自动装配，这里定义就覆盖了自动转配的默认jdk序列化方式
     * @param factory 连接redis的工厂，底层有依赖启动自动根据cache文件里面的参数自动装配好了
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(@Autowired RedisConnectionFactory factory){
        //1.创建RedisTemplate对象
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //2.为不同的数据结构设置不同的序列化方案
        //设置key序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        //设置value序列化方式
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        //设置hash中field字段序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        //设置hash中value的序列化方式
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        //5.初始化参数设置
        template.afterPropertiesSet();
        return template;

    }
}
