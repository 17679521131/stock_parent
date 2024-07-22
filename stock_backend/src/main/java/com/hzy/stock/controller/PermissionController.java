package com.hzy.stock.controller;

import com.hzy.stock.pojo.entity.SysPermission;
import com.hzy.stock.service.PermissionService;
import com.hzy.stock.vo.req.PermissionAddVo;
import com.hzy.stock.vo.req.PermissionUpdateVo;
import com.hzy.stock.vo.resp.LoginRespPermission;
import com.hzy.stock.vo.resp.PermissionTreeNodeVo;
import com.hzy.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/22 17:38
 * @description : 有关权限的接口
 */
@RestController
@RequestMapping("/api")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取树状权限数据集合
     * @return
     */
    @GetMapping("/permissions/tree/all")
    public R<List<LoginRespPermission>> getPermissionsTreeAll(){
        return permissionService.selectAllPermissions();
    }


    /**
     * 查询所有权限集合
     * @return
     */
    @GetMapping("/permissions")
    public R<List<SysPermission>> getAllPermissions(){
        return permissionService.getAllPermissions();
    }

    /**
     * 添加权限时回显权限树,仅仅显示目录和菜单
     * @return
     */
    @GetMapping("/permissions/tree")
    public R<List<PermissionTreeNodeVo>> getPermissionsTree(){
        return permissionService.getPermissionsTree();
    }


    /**
     * 权限添加按钮
     * @param vo
     * @return
     */
    @PostMapping("/permission")
    public R<String> addPermission(@RequestBody PermissionAddVo vo){
        return permissionService.addPermission(vo);
    }


    /**
     * 更新修改权限按钮
     * @param vo
     * @return
     */
    @PutMapping("/permission")
    public R<String> updatePermission(@RequestBody PermissionUpdateVo vo){
        return permissionService.updatePermission(vo);
    }


    /**
     * 删除权限按钮菜单
     * @param permissionId
     * @return
     */
    @DeleteMapping("/permission/{permissionId}")
    public R<String> deletePermission(@PathVariable("permissionId") Long permissionId){
        return permissionService.deletePermission(permissionId);
    }


}
