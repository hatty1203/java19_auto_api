package com.homework.utils;

import com.alibaba.fastjson.JSONPath;
import com.homework.constants.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class Authorization {
    public static Map<String, Object> VARS = new HashMap<>();

    /**
     * 把响应体（转换成String类型）中的参数提取出来, 并存到VARS中
     * @param body
     * @param expression
     * @param key
     */
    public static void json2Vars(String body, String expression, String key) {
        if (StringUtils.isNoneBlank((body))) {
            Object obj = JSONPath.read(body, expression);
            if (obj != null) {
                VARS.put(key, obj);
            }
        }
    }

    /**
     * 获取带token的请求头Map集合
     * @return
     */
    public static Map<String, String> getTokenHeader() {
        Object token = Authorization.VARS.get("${token}");
        Map<String, String> headers = new HashMap<>();
        //创建token请求头
        headers.put("Authorization", "Bearer " + token);
        //把基础请求头加入请求头中
        headers.putAll(Constants.HEAGERS);
        return headers;
    }
}
