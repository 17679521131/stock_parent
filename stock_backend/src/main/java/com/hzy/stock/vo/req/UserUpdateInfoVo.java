package com.hzy.stock.vo.req;

import lombok.Data;

/**
 * @author daocaoaren
 * @date 2024/7/22 16:52
 * @description : 用户更新数据请求参数封装类
 */
@Data
public class UserUpdateInfoVo {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String nickName;
    private String realName;
    private Integer sex;
    private Integer createWhere;
    private Integer status;
}
