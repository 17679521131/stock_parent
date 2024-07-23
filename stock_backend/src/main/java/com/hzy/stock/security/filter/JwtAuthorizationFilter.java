package com.hzy.stock.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzy.stock.security.utils.JwtTokenUtil;
import com.hzy.stock.vo.resp.R;
import com.hzy.stock.vo.resp.ResponseCode;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/23 19:42
 * @description : 定义授权过滤器，本质就是一切请求，获取请求头的token，然后进行校验
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    /**
     * 过滤执行方法
     * @param request
     * @param response
     * @param filterChain 过滤器链
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.从请求头获取token字符串
        String tokenStr = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        //2.合法性判断
        //2.1 判断是否为空或者null
        if (StringUtils.isBlank(tokenStr)) {
            //如果票据为空，则放行，但是此时安全上下文中肯定没有认证成功的票据，后续的过滤器如果得不到这个票据，则自动拒绝
            filterChain.doFilter(request,response);
            return;
        }
        //响应数据格式json
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //编码格式
        response.setCharacterEncoding("UTF-8");
        //2.2 检查票据是否合法
        Claims claims = JwtTokenUtil.checkJWT(tokenStr);
        if (claims==null) {
            //票据不合法，过滤器链终止
            R<Object> error = R.error(ResponseCode.INVALID_TOKEN);
            //响应
            response.getWriter().write(new ObjectMapper().writeValueAsString(error));
            return;
        }
        //2.3 从票据字符串中获取用户名和权限信息，并组装UsernamePasswordAuxxxToken对象
        String username = JwtTokenUtil.getUsername(tokenStr);
        //["P5","ROLE_ADMIN"]
        String roles = JwtTokenUtil.getUserRole(tokenStr);
        String stripStr = StringUtils.strip(roles, "[]");
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stripStr);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        //3.将封装的认证票据存入security的安全上下文，这样后续的过滤器可直接从安全上下文中获取用户相关的权限信息
        //以线程为维度：当前访问结束，那么线程回收，上下文的凭证也会被回收
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //4.发行请求，后续的过滤器，比如：认证过滤器发现如果上下文中存在token对象的话，无需认证
        filterChain.doFilter(request,response);
    }
}
