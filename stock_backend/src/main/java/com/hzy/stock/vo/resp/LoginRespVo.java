package com.hzy.stock.vo.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/15 03:52
 * @description :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("登入响应对象")
public class LoginRespVo {
    /**
     * 用户ID
     * 将Long类型数字进行json格式转化时，转成String格式类型
     */
    @ApiModelProperty("用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 邮件
     */
    private String email;

    /**
     * 侧边栏权限树（不包含按钮权限）
     */
    private List<LoginRespPermission> menus;

    /**
     * 按钮权限标识
     */
    private List<String> permissions;

    /**
     * 响应前端token
     */
    private String accessToken;

}
