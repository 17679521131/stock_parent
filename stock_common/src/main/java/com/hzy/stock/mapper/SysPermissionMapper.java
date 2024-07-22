package com.hzy.stock.mapper;

import com.hzy.stock.pojo.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author daocaoaren
* @description 针对表【sys_permission(权限表（菜单）)】的数据库操作Mapper
* @createDate 2024-07-14 21:04:19
* @Entity com.hzy.stock.pojo.entity.SysPermission
*/
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    /**
     * 根据用户id查询用户的所有权限
     * @param id
     * @return
     */
    List<SysPermission> findPermissionsByUserId(@Param("id") Long id);

    /**
     * 查询所有的权限集合
     * @return
     */
    List<SysPermission> findAll();

    /**
     * 根据权限父类id查询对应子集权限
     * @param permissionId
     * @return
     */
    int findChildrenCountByParentId(@Param("permissionId") Long permissionId);

}
