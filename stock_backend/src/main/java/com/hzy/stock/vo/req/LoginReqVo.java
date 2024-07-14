package com.hzy.stock.vo.req;

import lombok.Data;

/**
 * @author daocaoaren
 * @date 2024/7/15 03:50
 * @description :请求登入vo
 */
@Data
public class LoginReqVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String code;
}
