package com.hzy.stock.controller;

import com.hzy.stock.log.annotation.StockLog;
import com.hzy.stock.service.RoleService;
import com.hzy.stock.vo.req.RoleAddVo;
import com.hzy.stock.vo.req.RolePageVo;
import com.hzy.stock.vo.req.RoleUpdateVo;
import com.hzy.stock.vo.resp.PageResult;
import com.hzy.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author daocaoaren
 * @date 2024/7/22 17:07
 * @description : 角色管理相关接口
 */
@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询角色信息
     * @param vo
     * @return
     */
    @PostMapping("/roles")
    public R<PageResult> getRolesAllInfo(@RequestBody RolePageVo vo){
        return roleService.getRolesAllInfo(vo);
    }


    /**
     * 添加角色和角色关联权限
     * @param roleAddVo
     * @return
     */
    @StockLog("添加角色和角色关联权限")
    @PostMapping("/role")
    public R<String> addRoleAndPermission(@RequestBody RoleAddVo roleAddVo){
        return roleService.addRoleAndPermission(roleAddVo);
    }

    /**
     * 根据角色id查找对应的权限id集合
     * @param roleId
     * @return
     */
    @GetMapping("/role/{roleId}")
    public R<Set<String>> getPermissionIdByRoleId(@PathVariable("roleId") String roleId){
        return roleService.getPermissionIdByRoleId(roleId);
    }


    /**
     * 添加角色和角色关联权限,编辑角色信息提交的数据
     * @param vo
     * @return
     */
    @StockLog("添加角色和角色关联权限,编辑角色信息提交的数据")
    @PutMapping("/role")
    public R<String> updateRoleAndPermission(@RequestBody RoleUpdateVo vo){
        return roleService.updateRoleAndPermission(vo);
    }

    /**
     * 根据角色id删除角色
     * @param roleId
     * @return
     */
    @StockLog("删除角色")
    @DeleteMapping("/role/{roleId}")
    public R<String> deleteRole(@PathVariable("roleId") Long roleId){
        return roleService.deleteRoleById(roleId);
    }


    /**
     * 更新用户的状态信息
     * @param roleId 角色id
     * @param status 状态 1.正常 0：禁用
     * @return
     */
    @StockLog("更新用户的状态信息")
    @PostMapping("/role/{roleId}/{status}")
    public R<String> updateRoleStatus(@PathVariable("roleId") Long roleId, @PathVariable("status") Integer status){
        return roleService.updateRoleStatus(roleId, status);
    }




}
