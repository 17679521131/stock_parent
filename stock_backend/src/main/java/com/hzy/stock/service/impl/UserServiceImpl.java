package com.hzy.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import com.hzy.stock.constant.StockConstant;
import com.hzy.stock.mapper.SysUserMapper;
import com.hzy.stock.pojo.entity.SysUser;
import com.hzy.stock.service.UserService;
import com.hzy.stock.utils.IdWorker;
import com.hzy.stock.vo.req.LoginReqVo;
import com.hzy.stock.vo.resp.LoginRespVo;
import com.hzy.stock.vo.resp.R;
import com.hzy.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author daocaoaren
 * @description 定义用户服务实现
 * @createDate 2024-07-14 21:07:19
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据用户名查询用户信息 用于测试
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
        if (loginReqVo == null || StringUtils.isBlank(loginReqVo.getUsername()) || StringUtils.isBlank(loginReqVo.getPassword())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        //判断校验码和sessionId是有效
        if (StringUtils.isBlank(loginReqVo.getCode()) || StringUtils.isBlank(loginReqVo.getSessionId())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        //判断验证码是否正确
        //1.从redis中获取验证码
        String redisCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + loginReqVo.getSessionId());
        //判断验证码是否正确
        if(StringUtils.isBlank(redisCode) || !redisCode.equals(loginReqVo.getCode())) {
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //根据用户名去数据库中查询用户信息，获取密码的密文
        SysUser sysUser = sysUserMapper.findUserInfoByName(loginReqVo.getUsername());
        if (sysUser == null) {
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        String password = sysUser.getPassword();
        //调用匹配器匹配前端传送来的密码和数据库中的密文是否对应
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



    /**
     * 生成登入页面的验证码功能呢
     * @return
     */
    @Override
    public R<Map> getCaptcha() {
        //1.生成图片验证吗图片
        /**
         * hutool包下的CaptchaUtil工具类，用于生成校验码
         * 参数1：图片的宽度，参数2：图片的高度，参数3：验证码的数量，参数4：干扰线的数量
         */
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        //设置校验码背景颜色
        lineCaptcha.setBackground(Color.LIGHT_GRAY);
        //自定义生成校验码的规则
        /*lineCaptcha.setGenerator(new CodeGenerator() {
            @Override
            public String generate() {
                //自定义校验码生成逻辑
                return null;
            }

            @Override
            public boolean verify(String s, String s1) {
                //匹配校验码逻辑
                return false;
            }
        });*/
        //获取校验码
        String code = lineCaptcha.getCode();
        //获取经过base64编码处理的图片数据
        String imageData = lineCaptcha.getImageBase64();
        //生成sessionId ,由于前端接收的long类型数据太长的话会造成精度损失，所以我们这里将这个转换为String类型传递给前端
        String sessionId = String.valueOf(idWorker.nextId());
        log.info("当前图片的校验码:{},会话id:{}",code, sessionId);//打印日志，方便测试
        //将sessionId为key和验证码为value放入到redis中,并设置过期时间为60秒(模拟session过期时间)
        //加Ck的原因是因为如果要统计有多少给键值对，也就是说方便统计有多少人在登入
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX +sessionId, code,1, TimeUnit.MINUTES);
        //将sessionId和验证码放入到map中
        Map<String,String> map = new HashMap<>();
        map.put("imageData", imageData);
        map.put("sessionId", sessionId);
        //响应数据
        return R.ok(map);
    }
}
