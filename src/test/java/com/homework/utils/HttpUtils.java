package com.homework.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class HttpUtils {
    private static Logger logger = Logger.getLogger(HttpUtils.class);

    /**
     * call方法
     * @param
     * @return
     * @throws Exception
     */
    public static HttpResponse call(String method, String url, String params, String contentType, Map<String, String> headers){
        try {
            if ("post".equalsIgnoreCase(method)) {
                if ("json".equalsIgnoreCase(contentType)) {
                    return post_json(url, params,headers);
                } else if ("form".equalsIgnoreCase(contentType)) {
                    //String params=caseInfo.getParams().replace(":","=").replace(",","&").replace("\"","").replace("{","").replace("}","");
                    params = json2Form(params);
                    return post_form(url, params,headers);
                } else {
                    System.out.println("method = " + method + ", url = " + url + ", params = " + params + ", contentType = " + contentType);
                }
            } else if ("get".equalsIgnoreCase(method)) {
                return get(url,headers);
            } else if ("patch".equalsIgnoreCase(method)) {
                return patch(url, params,headers);
            }else {
                System.out.println("method = " + method + ", url = " + url + ", params = " + params + ", contentType = " + contentType);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            }

        return null;
    }

    /**
     * 发送get请求
     * @param url
     * @throws Exception
     */
    public static HttpResponse get(String url, Map<String, String> headers) throws Exception{


        HttpGet get = new HttpGet(url);

        addHeaders(get,headers);

        HttpClient client = HttpClients.createDefault();
        HttpResponse resp = client.execute(get);

        return resp;

    }

    /**
     * 发送post请求
     * @param url
     * @param params
     * @throws Exception
     */
    public static HttpResponse post_json(String url, String params,Map<String,String> headers) throws Exception{

        HttpPost post = new HttpPost(url);

        addHeaders(post,headers);

        StringEntity pentity = new StringEntity(params,"utf-8");
        post.setEntity(pentity);

        HttpClient client = HttpClients.createDefault();
        HttpResponse resp = client.execute(post);

        return resp;

    }

    /**
     * 发送post请求(参数是form-data格式)
     * @param url
     * @param params  key=value&key2=value2
     * @return
     * @throws Exception
     */
    public static HttpResponse post_form(String url, String params, Map<String,String> headers) throws Exception{

        HttpPost post = new HttpPost(url);

        addHeaders(post,headers);

        StringEntity pentity = new StringEntity(params,"utf-8");
        post.setEntity(pentity);

        HttpClient client = HttpClients.createDefault();
        HttpResponse resp = client.execute(post);

        return resp;

    }

    /**
     * 发送patch请求
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static HttpResponse patch(String url, String params, Map<String, String> headers) throws Exception {

        HttpPatch patch = new HttpPatch(url);

//        patch.addHeader("X-Lemonban-Media-Type", "lemonban.v2");
//        patch.addHeader("Content-Type", "application/json");
        addHeaders(patch,headers);
        StringEntity pentity = new StringEntity(params, "utf-8");
        patch.setEntity(pentity);

        HttpClient client = HttpClients.createDefault();
        HttpResponse resp = client.execute(patch);

        return resp;
    }

    /**
     * 获取响应头，响应体，响应码
     * @param response    http响应对象
     * @throws Exception
     */
    public static String printResult(HttpResponse response)  {
        try {
        //获取响应体
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity);

        //获取响应头
            Header[] headers = response.getAllHeaders();

        //获取响应码
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info(body);
            logger.info(Arrays.toString(headers));
            logger.info(statusCode);
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 把json格式的数据转成form-data
     * @param json
     * @return
     */
    public static String json2Form(String json){
        Map<String, String> map = JSONObject.parseObject(json, Map.class);
        Set<String> keys =map.keySet();
        String result ="";
        for (String key : keys) {
            result += key + "=" + map.get(key) + "&";
        }
        result = result.substring(0,result.length()-1);
        return result;
    }

    /**
     * 添加请求头
     * @param request
     * @param headers
     */
    public static void addHeaders(HttpRequest request, Map<String, String> headers){
        for(String key:headers.keySet()){
            request.addHeader(key, headers.get(key));
        }
    }

}
