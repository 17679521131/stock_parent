package com.hzy.stock.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hzy.stock.vo.resp.R;
import com.hzy.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : daocaoaren
 * @date : 2024/7/23 20:00
 * @description 用户无权限访问拒绝的处理器
 */
@Slf4j
public class StockAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 用户合法，但是无权访问资源时处理器
     * @param request
     * @param response
     * @param ex 拒绝时抛出的异常
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
        log.warn("当前用户访问资源拒绝，原因：{}",ex.getMessage());
        //设置响应格式和编码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        //编码格式
        R<Object> error = R.error(ResponseCode.NOT_PERMISSION);
        //响应
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}
