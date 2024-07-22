package com.hzy.stock.service;

import com.hzy.stock.vo.req.RoleAddVo;
import com.hzy.stock.vo.req.RolePageVo;
import com.hzy.stock.vo.req.RoleUpdateVo;
import com.hzy.stock.vo.resp.PageResult;
import com.hzy.stock.vo.resp.R;

import java.util.Set;

/**
 * @author daocaoaren
 * @date 2024/7/22 17:12
 * @description :
 */
public interface RoleService {
    /**
     * 分页查询角色信息
     * @param vo
     * @return
     */
    R<PageResult> getRolesAllInfo(RolePageVo vo);

    /**
     * 添加角色和角色关联权限
     * @param roleAddVo
     * @return
     */
    R<String> addRoleAndPermission(RoleAddVo roleAddVo);

    /**
     * 根据角色id查找对应的权限id集合
     * @param roleId
     * @return
     */
    R<Set<String>> getPermissionIdByRoleId(String roleId);

    /**
     * 添加角色和角色关联权限,编辑角色信息提交的数据
     * @param vo
     * @return
     */
    R<String> updateRoleAndPermission(RoleUpdateVo vo);

    /**
     * 根据角色id删除角色
     * @param roleId
     * @return
     */
    R<String> deleteRoleById(Long roleId);

    /**
     * 更新用户的状态信息
     * @param roleId 角色id
     * @param status 状态 1.正常 0：禁用
     * @return
     */
    R<String> updateRoleStatus(Long roleId, Integer status);
}
