package com.hzy.stock.service.impl;

import com.hzy.stock.mapper.SysUserMapper;
import com.hzy.stock.pojo.entity.SysUser;
import com.hzy.stock.service.UserService;
import com.hzy.stock.vo.req.LoginReqVo;
import com.hzy.stock.vo.resp.LoginRespVo;
import com.hzy.stock.vo.resp.R;
import com.hzy.stock.vo.resp.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author daocaoaren
 * @description 定义用户服务实现
 * @createDate 2024-07-14 21:07:19
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    @Override
    public SysUser findByUserName(String userName) {
        return sysUserMapper.findUserInfoByName(userName);
    }

    /**
     * 用户登录
     * @param loginReqVo
     * @return
     */
    @Override
    public R<LoginRespVo> login(LoginReqVo loginReqVo) {
        //1.判断参数是否合法
        if (loginReqVo == null || StringUtils.isBlank(loginReqVo.getUsername()) || StringUtils.isBlank(loginReqVo.getPassword()) || StringUtils.isBlank(loginReqVo.getCode())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        //2.根据用户名去数据库中查询用户信息，获取密码的密文
        SysUser sysUser = sysUserMapper.findUserInfoByName(loginReqVo.getUsername());
        if (sysUser == null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        String password = sysUser.getPassword();
        //3.调用匹配器匹配前端传送来的密码和数据库中的密文是否对应
        boolean result = passwordEncoder.matches(loginReqVo.getPassword(), password);
        if (!result) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //4.响应
        LoginRespVo loginRespvo = new LoginRespVo();
//        loginRespvo.setUsername(sysUser.getUsername());
//        loginRespvo.setNickName(sysUser.getNickName());
//        loginRespvo.setPhone(sysUser.getPhone());
//        loginRespvo.setId(sysUser.getId());
        //由于LoginRespVo和SysUser的有些属性相同，但是SysUser中属性太多了，所以使用工具类
        //必须保证属性名称和类型一致，否则会报错
        BeanUtils.copyProperties(sysUser, loginRespvo);

        return R.ok(loginRespvo);
    }
}
