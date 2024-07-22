package com.hzy.stock.vo.req;

import lombok.Data;

/**
 * @author daocaoaren
 * @date 2024/7/22 23:30
 * @description :
 */
@Data
public class PermissionUpdateVo {
    /**
     * 权限id
     */
    private Long id;

    /**
     * 菜单等级 0 顶级目录 1.目录 2 菜单 3 按钮
     */
    private Integer type;

    private String title;
    private Long pid;
    /**
     * 对应资源路径
     *  1.如果类型是目录，则url为空
     *  2.如果类型是菜单，则url对应路由地址
     *  3.如果类型是按钮，则url对应是访问接口的地址
     */
    private String url;
    /**
     * 只有菜单类型有名称，默认是路由的名称
     */
    private String name;
    private String icon;
    /**
     * 1.基于springSecrutiry约定的权限过滤便是
     */
    private String perms;
    /**
     * 请求方式：get put delete post等
     */
    private String method;
    /**
     * vue按钮回显控制辨识
     */
    private String code;
    /**
     * 排序
     */
    private Integer orderNum;
}
