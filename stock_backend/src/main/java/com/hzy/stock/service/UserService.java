package com.hzy.stock.service;

import com.hzy.stock.pojo.entity.SysUser;
import com.hzy.stock.vo.req.*;
import com.hzy.stock.vo.resp.LoginRespVo;
import com.hzy.stock.vo.resp.PageResult;
import com.hzy.stock.vo.resp.R;
import com.hzy.stock.vo.resp.UserInfoRespVo;

import java.util.List;
import java.util.Map;

/**
 * @author daocaoaren
 * @description 定义用户服务接口
 * @createDate 2024-07-14 21:04:19
 */
public interface UserService {
    /**
     * 生成图片验证码功能
     * @return
     */
    R<Map> getCaptcha();

    /**
     * 根据用户名称查询用户信息
     * @param userName
     * @return
     */
    SysUser findByUserName(String userName);

    /**
     * 用户登录
     * @param loginReqVo
     * @return
     */
    R<LoginRespVo> login(LoginReqVo loginReqVo);

    /**
     * 获取用户信息分页查询条件包含：分页信息 用户创建日期范围
     * @param userPageReqVo
     * @return
     */
    R<PageResult> getUserListPage(UserPageReqVo userPageReqVo);

    /**
     * 创建用户信息
     * @param userAddVo
     * @return
     */
    R<String> addUserInfo(UserAddVo userAddVo);

    /**
     * 获取用户角色信息
     * @param userId
     * @return
     */
    R<Map> getUserRolesInfo(Long userId);

    /**
     * 更新用户角色信息
     * @param vo
     * @return
     */
    R<String> updateUserRolesInfo(UserOneRoleReqVo vo);

    /**
     * 批量删除用户信息
     * @param ids 用户ID集合
     * @return
     */
    R<String> deleteUserInfo(List<Long> ids);

    /**
     * 根据用户ID查询用户的个人资料
     * @param id
     * @return
     */
    R<UserInfoRespVo> getUserInfo(Long id);

    /**
     * 跟新用户信息
     */
    R<String> updateUserInfo(UserUpdateInfoVo vo);

    /**
     * 修改用户密码
     * @param vo
     * @return
     */
    R<String> updateUserPassword(UserPwdVo vo);

    /**
     * 重置用户密码
     * @param userId
     * @return
     */
    R<String> resetUserPassword(Long userId);
}
