package com.hzy.stock.security.service;

import com.google.common.base.Strings;
import com.hzy.stock.mapper.SysRoleMapper;
import com.hzy.stock.mapper.SysUserMapper;
import com.hzy.stock.pojo.entity.SysPermission;
import com.hzy.stock.pojo.entity.SysRole;
import com.hzy.stock.pojo.entity.SysUser;
import com.hzy.stock.security.user.LoginUserDetails;
import com.hzy.stock.service.PermissionService;
import com.hzy.stock.service.RoleService;
import com.hzy.stock.vo.resp.LoginRespPermission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author daocaoaren
 * @date 2024/7/23 12:57
 * @description : 定义获取用户详情服务bean
 */
@Service("userDetailsService")
public class LoginUserDetailService implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 根据传入的用户名称获取用户相关的详情信息：用户名，密文密码，权限集合等。。。
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名称，获取数据库中用户的核心信息
        SysUser dbUser = sysUserMapper.findUserInfoByName(username);
        if (dbUser==null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        //2.组装 UserDetails对象
        //需要用户名，密文密码，权限集合（web接口绑定表示的集合）
        //其他字段id，email，phone等等
        //获取指定用户的权限集合 添加获取侧边栏数据和按钮权限的结合信息
        List<SysPermission> permissions = permissionService.findPermissionsByUserId(dbUser.getId());
        List<SysRole> roles =  sysRoleMapper.getRoleByUserId(dbUser.getId());
        //获取树状权限菜单菜单数据，调用permissionService方法
        List<LoginRespPermission> menus = permissionService.getTree(permissions,0l,true);
        //获取菜单按钮集合
        List<String> authBtnPerms = permissions.stream()
                .filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType() == 3)
                .map(per -> per.getCode()).collect(Collectors.toList());
        //获取springSecurity的权限标识 ROLE_角色名称 + 权限自身标识
        ArrayList<String> ps = new ArrayList<>(); //两个权限加在一起
        List<String> perms = permissions.stream().filter(per -> StringUtils.isNotBlank(per.getPerms()))
                .map(pre-> pre.getPerms()).collect(Collectors.toList());

        ps.addAll(perms);
        List<String> rs = roles.stream().map(r -> "ROLE_" + r.getName()).collect(Collectors.toList());
        ps.addAll(rs);
        //将用户用于的权限标识转换成权限对象，因为上面获取的是字符形式
        String[] psArray = ps.toArray(new String[perms.size()]);
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(psArray);
        //将用户信息封装成UserDetails对象
        LoginUserDetails userDetails = new LoginUserDetails();
        BeanUtils.copyProperties(dbUser,userDetails);
        userDetails.setMenus(menus);
        userDetails.setPermissions(authBtnPerms);
        userDetails.setAuthorities(authorityList);
        return userDetails;
    }
}
