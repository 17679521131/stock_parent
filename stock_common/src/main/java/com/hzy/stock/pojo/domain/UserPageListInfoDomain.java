package com.hzy.stock.pojo.domain;

import com.hzy.stock.pojo.entity.SysUser;
import lombok.Data;

/**
 * @author daocaoaren
 * @date 2024/7/22 12:45
 * @description : 用户分页查询信息返回数据结果，由于很多属性和SysUser相同所以继承SysUser避免冗余
 */
@Data
public class UserPageListInfoDomain extends SysUser {

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 更新人姓名
     */
    private String updateUserName;
}
