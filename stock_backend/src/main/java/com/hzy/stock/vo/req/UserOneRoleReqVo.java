package com.hzy.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/22 15:08
 * @description :
 */
@Data
public class UserOneRoleReqVo {

    /**
     * 用户的ID
     */
    private Long userId;

    /**
     * 要添加的角色id
     */
    private List<Long> roleIds;
}
