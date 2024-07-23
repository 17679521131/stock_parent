package com.hzy.stock.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hzy.stock.vo.resp.R;
import com.hzy.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : daocaoaren
 * @date : 2024/7/23 20:27
 * @description 用户无权限访问拒绝的处理器
 */
@Slf4j
public class StockAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 没有认证的用户访问资源时拒绝的处理器
     * @param request
     * @param response
     * @param ex 拒绝时抛出的异常
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        log.warn("匿名用户拒绝访问，原因：{}",ex.getMessage());
        //设置响应格式和编码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        //编码格式
        R<Object> error = R.error(ResponseCode.ANONMOUSE_NOT_PERMISSION);
        //响应
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }

}
