package com.hzy.stock.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统日志
 * @TableName sys_log
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysLog implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户操作：DELETE ADD GET UPDATE
     */
    private String operation;

    /**
     * 响应时间,单位毫秒
     */
    private Integer time;

    /**
     * 请求方法（控制层方法全限定名）
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}