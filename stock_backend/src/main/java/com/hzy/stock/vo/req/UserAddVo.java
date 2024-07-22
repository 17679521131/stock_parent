package com.hzy.stock.vo.req;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author daocaoaren
 * @date 2024/7/22 13:31
 * @description : 添加用户信息，从前端传递来的数据
 */
@Data
public class UserAddVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     *
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 性别1表示男，0表示女
     */
    private Integer sex;
    /**
     *创建来源
     */
    private Integer createWhere;
    /**
     * 用户状态，1表示正常，2表示锁定
     */
    private Integer status;
}
