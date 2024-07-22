package com.hzy.stock.mapper;

import com.hzy.stock.pojo.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author daocaoaren
* @description 针对表【sys_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2024-07-14 21:04:19
* @Entity com.hzy.stock.pojo.entity.SysUserRole
*/
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    /**
     * 根据用户的id查询他的所有角色id
     * @param userId
     * @return
     */
    List<Long> findRolesIdByUserId(@Param("userId") Long userId);

    /**
     * 根据用户的id删除用户对应的角色信息
     * @param userId
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 批量插入用户角色信息
     * @param list
     * @return
     */
    int insertUserRoleBatch(@Param("list") List<SysUserRole> list);
}
