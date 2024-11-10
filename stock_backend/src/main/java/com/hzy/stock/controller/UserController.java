package com.hzy.stock.controller;

import com.hzy.stock.pojo.entity.SysUser;
import com.hzy.stock.service.UserService;
import com.hzy.stock.vo.req.*;
import com.hzy.stock.vo.resp.LoginRespVo;
import com.hzy.stock.vo.resp.PageResult;
import com.hzy.stock.vo.resp.R;
import com.hzy.stock.vo.resp.UserInfoRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author daocaoaren
 * @date 2024/7/14 22:07
 * @description : 定义用户web层接口资源bean
 */
@RestController  // 定义一个restful风格的接口 是@Controller+@ResponseBody的结合体
@RequestMapping("/api")  // 定义接口路径
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据用户名获取用户信息  用于测试
     * @param name
     * @return
     */
    @ApiOperation(value = "根据用户名获取用户信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "userName",value = "用户名",required = true,dataTypeClass = String.class,type = "path")})
    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String name){
        return userService.findByUserName(name);
    }


    /**
     * 用户登录功能
     * @param loginReqVo
     * @return
     */
//    @ApiOperation(value = "用户登录功能")
//    @PostMapping("/login")
//    public R<LoginRespVo> login(@RequestBody LoginReqVo loginReqVo){
//        return userService.login(loginReqVo);
//    }

    /**
     * 生成图片验证码功能
     * @return
     */
    @ApiOperation(value = "生成图片验证码功能")
    @GetMapping("/captcha")
    public R<Map> getCaptcha(){
        return userService.getCaptcha();
    }


    /**
     * 多条件综合查询，查询用户所有信息
     */
    @PostMapping("/users")
    public R<PageResult> getUserListPage(@RequestBody UserPageReqVo userPageReqVo){
        return userService.getUserListPage(userPageReqVo);
    }

    /**
     * 添加用户信息
     */
    @PostMapping("/user")
    public R<String> addUser(@RequestBody UserAddVo userAddVo){
        return userService.addUserInfo(userAddVo);
    }

    /**
     * 获取角色拥有的角色信息
     */
    @GetMapping("/user/roles/{userId}") //路径传参数
    public R<Map> getUserRolesInfo(@PathVariable("userId") Long userId){
        return userService.getUserRolesInfo(userId);
    }

    /**
     * 更新用户角色信息
     */
    @PutMapping("/user/roles")
    public R<String> updateUserRolesInfo(@RequestBody UserOneRoleReqVo vo){
        return userService.updateUserRolesInfo(vo);
    }

    /**
     * 批量删除用户信息
     */
    @DeleteMapping("/user")
    public R<String> deleteUserInfo(@RequestBody List<Long> ids){
        return userService.deleteUserInfo(ids);
    }

    /**
     * 根据用户id获取用户信息，完成个人资料展示
     */
    @GetMapping("/user/info/{userId}")
    public R<UserInfoRespVo> getUserInfo(@PathVariable("userId") Long userId){
        return userService.getUserInfo(userId);
    }


    /**
     * 根据id更新用户信息
     */
    @PutMapping("/user")
    public R<String> updateUserInfo(@RequestBody UserUpdateInfoVo vo){
        return userService.updateUserInfo(vo);
    }


    /**
     * 用户更新密码
     * @param vo
     * @return
     */
    @PutMapping("/user/password")
    public R<String> updateUserPassword(@RequestBody UserPwdVo vo){
        return userService.updateUserPassword(vo);
    }

    /**
     * 重制用户密码
     * @param userId
     * @return
     */
    @GetMapping("/user/password/{userId}")
    public R<String> resetUserPassword(@PathVariable("userId") Long userId){
        return userService.resetUserPassword(userId);
    }


}
