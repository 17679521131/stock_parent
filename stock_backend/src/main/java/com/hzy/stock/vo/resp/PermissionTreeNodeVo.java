package com.hzy.stock.vo.resp;

import lombok.Data;

/**
 * @author daocaoaren
 * @date 2024/7/22 22:11
 * @description :
 */
@Data
public class PermissionTreeNodeVo {
    /**
     * 权限ID
     */
    private Long id;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * 菜单等级 1.目录 2.菜单 3.按钮
     */
    private int level;
}
