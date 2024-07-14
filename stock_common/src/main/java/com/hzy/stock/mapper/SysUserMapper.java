package com.hzy.stock.mapper;

import com.hzy.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
* @author daocaoaren
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-07-14 21:04:19
* @Entity com.hzy.stock.pojo.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    //根据名字查询用户
    SysUser findUserInfoByName(@Param("userName") String userName);

}
