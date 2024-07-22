package com.hzy.stock.vo.resp;

import lombok.Data;

/**
 * @author daocaoaren
 * @date 2024/7/22 16:24
 * @description : 用户个人资料封装
 */
@Data
public class UserInfoRespVo {
    private Long id;
    private String username;
    private String phone;
    private String nickName;
    private String realName;
    private Integer sex;
    private Integer status;
    private String  email;


}
