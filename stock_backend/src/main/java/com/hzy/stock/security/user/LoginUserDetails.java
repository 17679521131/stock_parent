package com.hzy.stock.security.user;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hzy.stock.vo.resp.LoginRespPermission;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/23 13:38
 * @description : 定义用户详情对象类
 */
@Data
public class LoginUserDetails implements UserDetails {

    private String username;
//    @Override
//    public String getUsername() {
//        return null;
//    }

    private String password;
//    @Override
//    public String getPassword() {
//        return null;
//    }


    private List<GrantedAuthority> authorities;
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }

    /**
     * true：账户没有过期
     */
    private boolean isAccountNonExpired=true;
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }


    /**
     * true:账户没有锁定
     */
    private boolean isAccountNonLocked=true;
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }

    /**
     * true:密码没有过期
     */
    private boolean isCredentialsNonExpired=true;
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }

    /**
     * true:账户可用
     */
    private boolean isEnabled=true;
//    @Override
//    public boolean isEnabled() {
//        return false;
//    }

    //其他自定义字段
    /**
     * 用户ID
     * 将Long类型数字进行json格式转化时，转成String格式类型
     */
   // @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;


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

}
