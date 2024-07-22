package com.hzy.stock.vo.resp;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/19 23:45
 * @description : 用户返回权限数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "用户返回权限数据")
public class LoginRespPermission {

    /**
     * 角色ID
     */
    private Long id;
    /**
     * 角色标题
     */
    private String title;
    /**
     * 角色图标
     */
    private String icon;
    /**
     * 路由地址URL
     */
    private String path;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 菜单数结构
     */
    private List<LoginRespPermission> children;
}
