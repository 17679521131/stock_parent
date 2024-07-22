package com.hzy.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.hzy.stock.Exception.BusinessException;
import com.hzy.stock.constant.StockConstant;
import com.hzy.stock.mapper.SysRoleMapper;
import com.hzy.stock.mapper.SysUserMapper;
import com.hzy.stock.mapper.SysUserRoleMapper;
import com.hzy.stock.pojo.domain.UserPageListInfoDomain;
import com.hzy.stock.pojo.entity.SysPermission;
import com.hzy.stock.pojo.entity.SysRole;
import com.hzy.stock.pojo.entity.SysUser;
import com.hzy.stock.pojo.entity.SysUserRole;
import com.hzy.stock.service.PermissionService;
import com.hzy.stock.service.UserService;
import com.hzy.stock.utils.IdWorker;
import com.hzy.stock.vo.req.*;
import com.hzy.stock.vo.resp.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    public SysUserRoleMapper sysUserRoleMapper;

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
        //由于LoginRespVo和SysUser的有些属性相同，但是SysUser中属性太多了，所以使用工具类 属性复制
        //必须保证属性名称和类型一致，否则会报错
        BeanUtils.copyProperties(sysUser, loginRespvo);

        //获取指定用户的权限集合 添加获取侧边栏数据和按钮权限的结合信息
        List<SysPermission> permissions = permissionService.findPermissionsByUserId(sysUser.getId());
        //获取树状权限菜单菜单数据，调用permissionService方法
        List<LoginRespPermission> menus = permissionService.getTree(permissions,0l,true);
        //获取菜单按钮集合
        List<String> authBtnPerms = permissions.stream()
                .filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType() == 3)
                .map(per -> per.getCode()).collect(Collectors.toList());
        loginRespvo.setMenus(menus);
        loginRespvo.setPermissions(authBtnPerms);
        //生成token
        loginRespvo.setAccessToken(sysUser.getId()+":"+sysUser.getUsername());

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

    /**
     * 多条件查询用户分页信息条件包含：分页信息 用户创建日期范围
     * @param userPageReqVo
     * @return
     */
    @Override
    public R<PageResult> getUserListPage(UserPageReqVo userPageReqVo) {
        //分别线获取前端传来的页码以及每页显示多少条数据
        PageHelper.startPage(userPageReqVo.getPageNum(),userPageReqVo.getPageSize());

        //根据多条件查询用户信息
        List<UserPageListInfoDomain> users = sysUserMapper.findUserAllInfoByPage(userPageReqVo.getUsername(),userPageReqVo.getNickName(),userPageReqVo.getStartTime(),userPageReqVo.getEndTime());
        //将返回的结果集封装到PageInfo中
        PageResult<UserPageListInfoDomain> pageResult = new PageResult<>(new PageInfo<>(users));
        return R.ok(pageResult);
    }

    @Override
    public R<String> addUserInfo(UserAddVo userAddVo) {
        //先判断用户是否存在
        SysUser sysUser = sysUserMapper.findUserInfoByName(userAddVo.getUsername());
        if(sysUser != null){
            //如果存在，则返回错误信息
            throw new BusinessException(ResponseCode.ACCOUNT_EXISTS_ERROR.getMessage());
        }
        SysUser user = new SysUser();
        //将userAddVo中的数据复制到user中
        BeanUtils.copyProperties(userAddVo,user);
        //设置用户的id
        user.setId(idWorker.nextId());
        //将用户密码加密
        user.setPassword(passwordEncoder.encode(userAddVo.getPassword()));
        //添加跟新时间和创建时间
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        //设置状态是否删除
        user.setDeleted(1);

        //否则插入数据
        int row = sysUserMapper.insert(user);
        if(row != 1){
            //如果插入失败，则返回错误信息
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }

        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 获取用户的角色信息
     * @param userId
     * @return
     */
    @Override
    public R<Map> getUserRolesInfo(Long userId) {

        //根据用户的id查询用户的角色id集合
        List<Long> roleIds = sysUserRoleMapper.findRolesIdByUserId(userId);
        //查询所有用户的集合
        List<SysRole> roles = sysRoleMapper.findAllInfo();


        HashMap<String, List> map = new HashMap<>();
        map.put("allRole",roles);
        map.put("ownRoleIds",roleIds);

        return R.ok(map);
    }

    /**
     * 更新用户的角色信息
     * @param vo 前端传递来的数据，用户的id，角色id集合
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> updateUserRolesInfo(UserOneRoleReqVo vo) {
        //先判断该用户的id是否纯在
        if(vo.getUserId() == null){
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        //先删除用户原来拥有的角色信息,然后再插入用户的新角色
        sysUserRoleMapper.deleteByUserId(vo.getUserId());
        //判断传递来的角色集合是否为空
        if(CollectionUtils.isEmpty(vo.getRoleIds())){
            //如果为空，说明用户的角色信息全部清空了
            return R.ok(ResponseCode.SUCCESS.getMessage());
        }
        //否则，循环插入用户的新角色信息
        //封装用户的角色信息
        List<SysUserRole> list = new ArrayList<>();
        for (Long roleId : vo.getRoleIds()) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setId(idWorker.nextId());
            sysUserRole.setUserId(vo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRole.setCreateTime(new Date());
            list.add(sysUserRole);
        }
        //批量插入用户角色信息
        int row = sysUserRoleMapper.insertUserRoleBatch(list);

        if(row == 0){
            //如果插入失败，则返回错误信息
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 批量删除用户信息
     * @param ids 用户ID集合
     * @return
     */
    @Override
    public R<String> deleteUserInfo(List<Long> ids) {
        //判断id是否为空
        if(CollectionUtils.isEmpty(ids)){
            //如果为空，则返回错误信息
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
        //删除用户就是将delete改变为0不完全删除
        int row = sysUserMapper.updateStatusForDelete(ids);
        if(row == 0){
            //如果删除失败，则返回错误信息
            throw new BusinessException(ResponseCode.ERROR.getMessage());
        }
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 根据用户id查询用户个人资料
     * @param id 用户id
     * @return
     */
    @Override
    public R<UserInfoRespVo> getUserInfo(Long id) {
        //判断id是否纯在
        if(id == null){
            //如果id为空，则返回错误信息
            return R.error(ResponseCode.DATA_ERROR);
        }
        //根据id查询用户信息
        SysUser user = sysUserMapper.selectByPrimaryKey(id);
        UserInfoRespVo userInfoRespVo = new UserInfoRespVo();
        //将信息复制到个人资料里面
        BeanUtils.copyProperties(user,userInfoRespVo);
        return R.ok(userInfoRespVo);
    }

    /**
     * 修改用户信息
     * @param vo
     * @return
     */
    @Override
    public R<String> updateUserInfo(UserUpdateInfoVo vo) {
        //判断请求的参数是否为空
        if(vo == null){
            //如果为空，则返回错误信息
            throw new BusinessException(ResponseCode.DATA_ERROR.getMessage());
        }
        //将数据复制到sysUser类中
        SysUser user = new SysUser();
        BeanUtils.copyProperties(vo,user);
        //设置跟新时间
        user.setUpdateTime(new Date());
        int row = sysUserMapper.updateByPrimaryKey(user);

        return R.ok(ResponseCode.SUCCESS.getMessage());
    }


}
