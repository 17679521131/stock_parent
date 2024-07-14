package com.hzy.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author daocaoaren
 * @date 2024/7/15 04:02
 * @description :
 */
@SpringBootTest
public class TestPasswordEncoder {
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 测试密码加密
     */
    @Test
    public void testPasswordEncoder(){
        String pwd = "123456";
        //将密码加密encode = $2a$10$EB6xP9uqzVaynaZYfFHkv.IJ1tAI7y91Uq8lQCEeFGfhTUUPvaW3G
        String encodePwd = passwordEncoder.encode(pwd);
        System.out.println("encode = " + encodePwd);
    }
    /**
     * 测试密码匹配
     */
    @Test
    public void testPasswordEncoder2(){
        String pwd = "123456";
        //将密码加密encode = $2a$10$EB6xP9uqzVaynaZYfFHkv.IJ1tAI7y91Uq8lQCEeFGfhTUUPvaW3G
        String encodePwd = "$2a$10$EB6xP9uqzVaynaZYfFHkv.IJ1tAI7y91Uq8lQCEeFGfhTUUPvaW3G" ;
        boolean matches = passwordEncoder.matches(pwd, encodePwd);
        System.out.println(matches? "密码正确" : "密码错误");


    }
}
