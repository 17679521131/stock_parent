package com.hzy.stock.service;

import com.hzy.stock.pojo.entity.SysUser;
import com.hzy.stock.vo.req.LoginReqVo;
import com.hzy.stock.vo.resp.LoginRespVo;
import com.hzy.stock.vo.resp.R;

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
}
