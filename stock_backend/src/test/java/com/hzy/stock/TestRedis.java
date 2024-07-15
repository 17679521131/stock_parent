package com.hzy.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author daocaoaren
 * @date 2024/7/15 13:52
 * @description :
 */
@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void testRedis(){
        //存值
        redisTemplate.opsForValue().set("name","daocaoaren");
        //获取值
        String name = (String) redisTemplate.opsForValue().get("name");

        System.out.println(name);
    }
}
