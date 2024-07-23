package com.hzy.stock.mapper;

import com.hzy.stock.pojo.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author daocaoaren
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-07-14 21:04:19
* @Entity com.hzy.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     * 查所有存在的角色信息
     * @return
     */
    List<SysRole> findAllInfo();

    /**
     * 查询所有角色信息
     * @return
     */
    List<SysRole> findAllRoleInfo();


    /**
     * 根据角色ID查询所有角色信息
     * @param id
     * @return
     */
    List<SysRole> getRoleByUserId(@Param("id") Long id);

}
