package com.hzy.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/22 20:02
 * @description : 添加角色信息前端要请求的数据封装在这
 */
@Data
public class RoleAddVo {
    private String name;
    private String description;
    private List<Long> permissionsIds;

}
