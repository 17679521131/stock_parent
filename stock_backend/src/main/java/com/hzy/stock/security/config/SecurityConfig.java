package com.hzy.stock.security.config;

import com.hzy.stock.security.filter.JwtAuthorizationFilter;
import com.hzy.stock.security.filter.JwtLoginAuthenticationFilter;

import com.hzy.stock.security.handler.StockAccessDeniedHandler;
import com.hzy.stock.security.handler.StockAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity//开启web安全设置生效
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启对springSecurity注解功能支持
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 定义公共的无需被拦截的资源
     * @return
     */
    private String[] getPubPath(){
        //公共访问资源
        String[] urls = {
                "/**/*.css","/**/*.js","/favicon.ico","/doc.html",
                "/druid/**","/webjars/**","/v2/api-docs","/api/captcha",
                "/swagger/**","/swagger-resources/**","/swagger-ui.html"
        };
        return urls;
    }
    /**
     * 配置资源权限绑定设置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //登出功能,指定登出的url地址
        http.logout().logoutUrl("/api/logout").invalidateHttpSession(true);
        //开启允许iframe 嵌套。security默认禁用ifram跨域与缓存
        http.headers().frameOptions().disable().cacheControl().disable();
        //session禁用
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();//禁用跨站请求伪造
        http.authorizeRequests()//对资源进行认证处理
                .antMatchers(getPubPath()).permitAll()//公共资源都允许访问
                .anyRequest().authenticated();  //除了上述资源外，其它资源，只有 认证通过后，才能有权访问
        //将自定义的过滤器加入security过滤器链，且在默认的认证过滤器之前执行
        http.addFilterBefore( jwtLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        //配置授权过滤器，它是资源安全的屏障，优先级最高，所以要在自定义的认证过滤器之前
        http.addFilterBefore(jwtAuthorizationFilter(),JwtLoginAuthenticationFilter.class);
        //配置访问拒绝处理器
        http.exceptionHandling().accessDeniedHandler(new StockAccessDeniedHandler())
                .authenticationEntryPoint(new StockAuthenticationEntryPoint());

    }

    /**
     * 定义认证过滤器
     * @return
     * @throws Exception
     */
    @Bean
    public JwtLoginAuthenticationFilter jwtLoginAuthenticationFilter() throws Exception {
        JwtLoginAuthenticationFilter authenticationFilter = new JwtLoginAuthenticationFilter("/api/login");
        //设置认证管理器
        authenticationFilter.setAuthenticationManager(authenticationManagerBean());
        //注入redis模板对象
        authenticationFilter.setRedisTemplate(redisTemplate);
        return authenticationFilter;
    }

    /**
     * 配置授权过滤器
     * @return
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter();
    }

    /**
     * 定义密码加密匹配器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}