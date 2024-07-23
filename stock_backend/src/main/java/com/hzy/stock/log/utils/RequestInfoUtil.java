package com.hzy.stock.log.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by itheima
 * @Date 2022/6/9
 * @Description 获取请求参数
 */
public class RequestInfoUtil {

    /**
     * 获取http请求数据，包括url restfull  json等
     * @param request
     *      在springmvc中如果请求的数据是异步json格式数据
     *      那么就需要从数据流对象中获取数据，但是默认的实现仅仅只能获取一次
     *      也就是说当读取完流对象中的数据后，流自动关闭，导致拦截器的后置处理器获取不了数据
     * @return
     */
    public static Map<String,Object> getRequestData(HttpServletRequest request) {
        //请求参数封装
        HashMap<String, Object> reqData = new HashMap<>();
        //获取请求参数
        //参数传递方式：form+url | restFull | ajax-json
        if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType())
                || MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(request.getContentType())) {
            //获取ajax json数据
            String bodyData = getRequestBody(request);
            reqData.put("bodyData",bodyData);
        }
        if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(request.getContentType())){
            //获取form表单数据
            Map<String, String[]> formData = request.getParameterMap();
            reqData.put("formData",formData);
        }
        String queryData = request.getQueryString();
        if (StringUtils.isNotBlank(queryData)) {
            reqData.put("queryString",queryData);
        }
        Object pathData = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (ObjectUtils.isNotEmpty(pathData)) {
            reqData.put("pathData",pathData);
        }
        return reqData;
    }

    /**
     * 获取请求体数据流信息
     * @param request
     * @return
     */
    private static String getRequestBody(HttpServletRequest request)  {
        String reqData=null;
        try {
            //获取缓冲数据
            BufferedReader reader = request.getReader();
            //读取数据
            String bodyData=null;
            String tmp=null;
            StringBuffer sb = new StringBuffer();
            while ((tmp= reader.readLine()) !=null){
                sb.append(tmp);
            }
            //重新将数据读入
            reqData = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取body数据
        return reqData;
    }
}
