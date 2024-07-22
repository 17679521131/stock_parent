package com.hzy.stock.vo.req;

import lombok.Data;

/**
 * @author daocaoaren
 * @date 2024/7/22 21:39
 * @description : 角色分页查询
 */
@Data
public class RolePageVo {
    /**
     * 当前页
     */
    private Integer pageNum=1;
    /**
     * 每页大小
     */
    private Integer pageSize=15;
}
