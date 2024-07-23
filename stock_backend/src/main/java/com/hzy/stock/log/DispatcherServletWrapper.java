package com.hzy.stock.log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author daocaoaren
 * @summary 自定义 DispatcherServletWrapper 来分派 AxinHttpServletRequestWrapper
 */
public class DispatcherServletWrapper extends org.springframework.web.servlet.DispatcherServlet {

    /**
     * 包装成我们自定义的request
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.doDispatch(new HttpServletRequestWrapper(request), response);
    }
}