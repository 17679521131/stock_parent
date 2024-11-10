package com.hzy.stock.vo.req;

import lombok.Data;

/**
 * @author daocaoaren
 * @date 2024/11/10 00:18
 * @description :
 */
@Data
public class UserPwdVo {

    private String id;

    private String oldPwd;
    private String newPwd;
}
