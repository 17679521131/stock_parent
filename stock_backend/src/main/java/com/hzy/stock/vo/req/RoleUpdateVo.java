package com.hzy.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/22 20:58
 * @description : 角色信息编辑请求参数表
 */
@Data
public class RoleUpdateVo {
    private Long id;
    private String name;
    private String description;
    private List<Long> permissionsIds;

}
