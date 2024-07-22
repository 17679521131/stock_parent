package com.hzy.stock.service;

import com.hzy.stock.pojo.entity.SysPermission;
import com.hzy.stock.vo.req.PermissionAddVo;
import com.hzy.stock.vo.req.PermissionUpdateVo;
import com.hzy.stock.vo.resp.LoginRespPermission;
import com.hzy.stock.vo.resp.PermissionTreeNodeVo;
import com.hzy.stock.vo.resp.R;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/21 23:48
 * @description :
 */
public interface PermissionService {

    /**
     * 根据用户id查询用户的所有权限
     * @param id 用户id
     * @return
     */
    List<SysPermission> findPermissionsByUserId(Long id);


    /**
     * @param permissions 权限树状集合
     * @param pid 权限父id，顶级权限的pid默认为0
     * @param isOnlyMenuType true:遍历到菜单，  false:遍历到按钮
     * type: 目录1 菜单2 按钮3
     * @return
     */
    List<LoginRespPermission> getTree(List<SysPermission> permissions, long pid, boolean isOnlyMenuType);

    /**
     * 获取树状权限集合
     * @return
     */
    R<List<LoginRespPermission>> selectAllPermissions();

    /**
     * 获取所有权限的全部信息
     * @return
     */
    R<List<SysPermission>> getAllPermissions();

    /**
     * 添加权限时回显权限树,仅仅显示目录和菜单
     * @return
     */
    R<List<PermissionTreeNodeVo>> getPermissionsTree();

    /**
     * 添加权限按钮
     * @param vo
     * @return
     */
    R<String> addPermission(PermissionAddVo vo);

    /**
     * 修改权限按钮
     * @param vo
     * @return
     */
    R<String> updatePermission(PermissionUpdateVo vo);

    /**
     * 删除权限按钮菜单
     * @param permissionId
     * @return
     */
    R<String> deletePermission(Long permissionId);
}
