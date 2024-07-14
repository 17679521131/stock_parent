package com.hzy.stock.controller;

import com.hzy.stock.pojo.entity.SysUser;
import com.hzy.stock.service.UserService;
import com.hzy.stock.vo.req.LoginReqVo;
import com.hzy.stock.vo.resp.LoginRespVo;
import com.hzy.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author daocaoaren
 * @date 2024/7/14 22:07
 * @description : 定义用户web层接口资源bean
 */
@RestController  // 定义一个restful风格的接口 是@Controller+@ResponseBody的结合体
@RequestMapping("/api")  // 定义接口路径
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据用户名获取用户信息
     * @param name
     * @return
     */
    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String name){
        return userService.findByUserName(name);
    }


    /**
     * 用户登录功能
     * @param loginReqVo
     * @return
     */
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo loginReqVo){
        return userService.login(loginReqVo);

    }

}
