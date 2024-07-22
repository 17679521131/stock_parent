package com.hzy.stock.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzy.stock.Exception.BusinessException;
import com.hzy.stock.mapper.SysRoleMapper;
import com.hzy.stock.mapper.SysRolePermissionMapper;
import com.hzy.stock.pojo.entity.SysRole;
import com.hzy.stock.pojo.entity.SysRolePermission;
import com.hzy.stock.service.RoleService;
import com.hzy.stock.utils.IdWorker;
import com.hzy.stock.vo.req.RoleAddVo;
import com.hzy.stock.vo.req.RolePageVo;
import com.hzy.stock.vo.req.RoleUpdateVo;
import com.hzy.stock.vo.resp.PageResult;
import com.hzy.stock.vo.resp.R;
import com.hzy.stock.vo.resp.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author daocaoaren
 * @date 2024/7/22 17:13
 * @description :
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    /**
     * 分页查询当前角色信息
     * @param vo
     * @return
     */
    @Override
    public R<PageResult> getRolesAllInfo(RolePageVo vo) {
        //根据分页查询角色信息
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        //查询角色信息
        List<SysRole> roles =  sysRoleMapper.findAllInfo();

        //返回分页结果
        PageResult<SysRole> pageResult = new PageResult<>(new PageInfo<>(roles));

        return R.ok(pageResult);
    }

    /**
     * 添加角色和角色的权限
     * @param roleAddVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> addRoleAndPermission(RoleAddVo roleAddVo) {
        //判断是否为空
        if(roleAddVo == null){
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
        //获取角色的名字和说明
        String name = roleAddVo.getName();
        String description = roleAddVo.getDescription();
        //组装角色
        SysRole sysRole = new SysRole();
        sysRole.setId(idWorker.nextId());
        sysRole.setName(name);
        sysRole.setDescription(description);
        sysRole.setStatus(1);
        sysRole.setDeleted(1);
        sysRole.setCreateTime(new Date());
        sysRole.setUpdateTime(new Date());
        //调用方法添加角色
        int row = sysRoleMapper.insert(sysRole);
        if(row != 1){
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        //添加角色对应的权限
        List<Long> permissionsIds = roleAddVo.getPermissionsIds();
        List<SysRolePermission> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionsIds)) {
            //组装角色权限中间表
            for (Long permissionsId : permissionsIds) {
                SysRolePermission sysRolePermission = new SysRolePermission();
                sysRolePermission.setId(idWorker.nextId());
                sysRolePermission.setRoleId(sysRole.getId());
                sysRolePermission.setPermissionId(permissionsId);
                sysRolePermission.setCreateTime(new Date());
                list.add(sysRolePermission);
            }
        }
        //批量添加
        int count = sysRolePermissionMapper.addRolePermissionInfo(list);
        if(count == 0){
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 根据角色id查找对应的权限id集合
     * @param roleId
     * @return
     */
    @Override
    public R<Set<String>> getPermissionIdByRoleId(String roleId) {
        //根据角色id查询权限集合
        Set<String> set = sysRolePermissionMapper.getPermissionIdByRoleId(roleId);
        if (set == null) {
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(set);
    }

    /**
     * 添加角色和角色关联权限,编辑角色信息提交的数据
     * @param vo
     * @return
     */
    @Override
    public R<String> updateRoleAndPermission(RoleUpdateVo vo) {
        //判断传递的参数是否为空
        if(vo == null){
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
        //先修改角色表的信息
        //将数据封装到实体类中
        SysRole sysRole = new SysRole();
        sysRole.setId(vo.getId());
        sysRole.setName(vo.getName());
        sysRole.setDescription(vo.getDescription());
        sysRole.setUpdateTime(new Date());
        //修改角色表
        int row = sysRoleMapper.updateByPrimaryKeySelective(sysRole);
        if (row !=1){
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        //修改角色权限中间表
        //先删除原来的权限再添加新权限
        int count = sysRolePermissionMapper.deleteByRoleId(vo.getId());
        //组装新的权限信息
        List<SysRolePermission> list = new ArrayList<>();
        for (Long permissionsId : vo.getPermissionsIds()) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setId(idWorker.nextId());
            sysRolePermission.setRoleId(vo.getId());
            sysRolePermission.setPermissionId(permissionsId);
            sysRolePermission.setCreateTime(new Date());
            list.add(sysRolePermission);
        }
        //批量插入新的权限
        int count1 = sysRolePermissionMapper.addRolePermissionInfo(list);
        if (count1 == 0){
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 根据角色id删除角色
     * @param roleId
     * @return
     */
    @Override
    public R<String> deleteRoleById(Long roleId) {
        //判断参数是否为空
        if(roleId == null){
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
        //逻辑删除，将状态改为0即可
        SysRole role = SysRole.builder().id(roleId).deleted(0).updateTime(new Date()).build();

        int row = sysRoleMapper.updateByPrimaryKeySelective(role);

        if (row != 1){
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }

        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 更新用户的状态信息
     * @param roleId 角色id
     * @param status 状态 1.正常 0：禁用
     * @return
     */
    @Override
    public R<String> updateRoleStatus(Long roleId, Integer status) {
        //组装数据
        SysRole role = SysRole.builder().id(roleId).status(status).updateTime(new Date()).build();
        //修改状态
        int row = sysRoleMapper.updateByPrimaryKeySelective(role);

        if (row != 1){
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }
}
