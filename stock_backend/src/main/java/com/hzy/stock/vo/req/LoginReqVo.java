package com.hzy.stock.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author daocaoaren
 * @date 2024/7/15 03:50
 * @description :请求登入vo
 */
@Data
@ApiModel("登入请求对象")
public class LoginReqVo {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "username")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", name = "password")
    private String password;
    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码", name = "code")
    private String code;

    /**
     * redis保存的SessionId,也就是到时候根据key获取在redis中保存一分钟的校验码code
     */
    @ApiModelProperty(value = "redis保存的SessionId,也就是到时候根据key获取在redis中保存一分钟的校验码code", name = "sessionId")
    private String sessionId;
}
