package com.hzy.stock.log.interceptor;

import com.google.gson.Gson;

import com.hzy.stock.constant.StockConstant;
import com.hzy.stock.log.annotation.StockLog;
import com.hzy.stock.log.utils.RequestInfoUtil;
import com.hzy.stock.mapper.SysLogMapper;
import com.hzy.stock.pojo.entity.SysLog;
import com.hzy.stock.utils.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author daocaoaren
 * @Date 2024/07/23
 * @Description 日志拦截器，采集用户访问行为
 */
@Component
//@Setter
public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //从reqeust获取请求基本信息
         if(handler instanceof HandlerMethod ) {
             HandlerMethod method = (HandlerMethod) handler;
             if (method.getMethod().isAnnotationPresent(StockLog.class)) {
                 //开始存入request域
                 request.setAttribute("startTimPoint",System.currentTimeMillis());
                 //封装日志对象
                 //获取用户信息
                 String userInfo = request.getHeader(StockConstant.TOKEN_HEADER);
                 String userName=null;
                 Long userId=null;
                 if (StringUtils.isBlank(userInfo) || !userInfo.contains(":")) {
                     userName="匿名用户";
                 }else {
                     //TODO 后续jwt解析获取用户信息
                     String[] infos = userInfo.split(":");
                     userId=Long.valueOf(infos[0]);
                     userName=infos[1];
                 }
                 //获取操作描述
                 StockLog anno = method.getMethod().getAnnotation(StockLog.class);
                 String opDes = anno.value();
                 //获取请求方法全限定名称
                 String methodName=method.getMethod().getDeclaringClass().getName()+"."+method.getMethod().getName();
                 //获取请求IP
                 String remoteIP = request.getRemoteAddr();
                 //获取请求参数
                 Map<String, Object> reqData = RequestInfoUtil.getRequestData(request);
                 //解析获取用户id和用户名称信息
                 SysLog log = SysLog.builder().id(idWorker.nextId())
                         .params(new Gson().toJson(reqData))
                         .ip(remoteIP)
                         .operation(opDes)
                         .username(userName)
                         .userId(userId)
                         .method(methodName)
                         .createTime(new Date())
                         .build();
                 request.setAttribute("log",log);
             }
         }
        return true;
    }

    /**
     * 最终执行
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //从request对象中获取预置属性
        Object startTimPoint = request.getAttribute("startTimPoint");
        Object log = request.getAttribute("log");
        if (startTimPoint!=null && log!=null){
            //开销时间
            Long takeTime=System.currentTimeMillis()- ((Long) startTimPoint);
            SysLog sysLog = (SysLog) log;
            sysLog.setTime(takeTime.intValue());
            //线程异步入库
            //sysLogMapper.insert(sysLog);
            threadPoolTaskExecutor.execute(()->{
                sysLogMapper.insert(sysLog);
            });
        }

    }


}