package com.vteba.utils.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.vteba.utils.charstr.Char;
import com.vteba.utils.common.PropUtils;
import com.vteba.utils.json.FastJsonUtils;
import com.vteba.utils.json.JacksonUtils;

/**
 * HttpClient工具类。提供提交JavaBean返回JavaBean数据，中间是Json格式 <br>
 * 
 * @author yinlei
 * @since 2014-8-14 18:04
 */
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
    private static HttpHost httpHost = null;
    static {
        String hostName = PropUtils.get("host");
        int port = PropUtils.getInt("port");
        String schema = PropUtils.get("scheme");
        // 创建目标主机
        httpHost = new HttpHost(hostName, port, schema);
    }
    
    
    /**
     * 调用搜牛接口，返回一个JSONObject，可以做进一步的处理，取出来某一节点等。
     * @param params 参数值
     * @param urlPath 接口url
     * @return JSONObject
     */
    public static <T> T invoke(final Map<String, String> params, final String urlPath, final Class<T> clazz) {
        // 创建http post请求
        HttpPost httpPost = buildHttpPost(params, urlPath);
        // 发起调用，返回Json object
        T obj = resolveBean(clazz, httpPost);
        return obj;
    }
    
    /**
     * 调用搜牛接口，返回一个JSONObject，可以做进一步的处理，取出来某一节点等。
     * @param params 参数值
     * @param urlPath 接口url
     * @return JSONObject
     */
    public static JSONObject invoke(final Map<String, String> params, final String urlPath) {
        // 创建http post请求
        HttpPost httpPost = buildHttpPost(params, urlPath);
        // 发起调用，返回Json object
        JSONObject jsonObject = resolveJsonObject(httpPost);
        return jsonObject;
    }
    
    /**
     * 调用搜牛接口，返回List，参数是键值对的形式。
     * @param params 参数Map
     * @param urlPath 调用的接口名
     * @param resultClass 结果对象类
     * @return 结果List
     */
    public static <T> List<T> invokeList(final Map<String, String> params, final String urlPath, Class<T> resultClass) {
        // 创建http post请求
        HttpPost httpPost = buildHttpPost(params, urlPath);
        // 发起调用，处理请求结果
        List<T> result = resolveResult(resultClass, httpPost);
        return result;
    }
    
    /**
     * 调用问财接口时使用，返回JavaBean。底层使用Jackson来实现的。可以改变属性的值，字段不一致也可以处理。
     * @param params 参数值
     * @param urlPath 接口url
     * @param resultClass 结果类型
     * @return &lt;T&gt; JavaBean实体
     */
    public static <T> T invokeForBean(final Map<String, String> params, final String urlPath, final Class<T> resultClass) {
        // 创建http post请求
        HttpPost httpPost = buildHttpPost(params, urlPath);
        // 发起调用，处理请求结果
        T result = resolveForBean(httpPost, resultClass);
        return result;
    }
    
    /**
     * 调用问财接口时使用，返回List&lt;T&gt;。底层使用Jackson来实现的。可以改变属性的值，字段不一致也可以处理。
     * @param params 参数值
     * @param urlPath 接口url
     * @param resultClass 结果类型
     * @return List&lt;T&gt;
     */
    public static <T> List<T> invokeForList(final Map<String, String> params, final String urlPath, final Class<T> resultClass) {
        // 创建http post请求
        HttpPost httpPost = buildHttpPost(params, urlPath);
        // 发起调用，处理请求结果
        List<T> result = resolveForList(httpPost, resultClass);
        return result;
    }

    /**
     * 发起http请求，返回处理结果。
     * @param httpPost HttpPost
     * @param resultClass 结果类型
     * @return 结果List
     */
    private static <T> List<T> resolveForList(final HttpPost httpPost, final Class<T> resultClass) {
        CollectionType collectionType = JacksonUtils.get().constructCollectionType(List.class, resultClass);
        List<T> list = JacksonUtils.get().fromJson(resolve(httpPost, 1), collectionType);
        return list;
    }
    
    /**
     * 发起http请求，返回处理结果。
     * @param httpPost HttpPost
     * @param resultClass 结果类型
     * @return 结果List
     */
    private static <T> T resolveForBean(final HttpPost httpPost, final Class<T> resultClass) {
        T entity = JacksonUtils.get().fromJson(resolve(httpPost, 1), resultClass);
        return entity;
    }
    
    /**
     * 发起http请求，返回字节数组
     * @param httpPost HttpPost
     * @param type 可以扩展
     * @return byte[]
     */
    private static byte[] resolve(final HttpPost httpPost, int type) {
        byte[] bytes = null;
        try {
            HttpHost host = null;
            if (type == 1) {
                host = httpHost;
            }
            // 创建客户端
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            // 发起调用
            CloseableHttpResponse httpResponse = httpClient.execute(host, httpPost);
            int status = httpResponse.getStatusLine().getStatusCode();
            if (status == 200) {// 结果正常
                bytes = EntityUtils.toByteArray(httpResponse.getEntity());
                //if (LOGGER.isInfoEnabled()) {
                    //LOGGER.info("调用接口返回200正常，url=[{}]，字节size=[{}]", httpPost.getURI().toString(), bytes.length);
                //}
            } else {// 结果异常
                LOGGER.error("请求的urlPath错误[{}]，响应码是[{}]", httpPost.getURI().toString(), status);
            }
            httpResponse.close();
            httpClient.close();
        } catch (ClientProtocolException e) {
            LOGGER.error("[{}]请求，客户端协议错误。", httpPost.getURI().toString(), e.getMessage());
        } catch (IOException e) {
            LOGGER.error("[{}]请求IO错误。", httpPost.getURI().toString(), e);
        }
        return bytes;
    }
    
    /**
     * 根据请求参数和请求url地址，构建http post请求。
     * @param params 请求参数
     * @param urlPath 请求url地址
     * @return HttpPost实例
     */
    private static HttpPost buildHttpPost(final Map<String, String> params, final String urlPath) {
        // 构建请求uri
        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder();
            // 设置参数
            for (Entry<String, String> entry : params.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
            // 设置请求的路径
            uriBuilder.setPath(urlPath);
            uri = uriBuilder.build();
        } catch (URISyntaxException e1) {
            LOGGER.error("HttpClient创建URI错误。可能传递的参数错误。", e1.getMessage());
            return null;
        }
        
        // 设置post请求
        HttpPost httpPost = new HttpPost(uri);
        // 设置post请求体
        List<NameValuePair> list = URLEncodedUtils.parse(uri, "UTF-8");
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("设置http请求体参数错误，不支持的编码。", e.getMessage());
        }
        RequestConfig config = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();
        httpPost.setConfig(config);
        return httpPost;
    }
    
    /**
     * 发起http请求，返回处理结果。
     * @param resultClass 结果类型
     * @param httpPost HttpClient
     * @return 结果List
     */
    private static <T> List<T> resolveResult(final Class<T> resultClass, final HttpPost httpPost) {
        List<T> object = FastJsonUtils.fromJsonArray(resolve(httpPost, 1), resultClass);
        return object;
    }

    /**
     * 发起http请求，返回处理结果。
     * @param resultClass 结果类型
     * @param httpPost HttpClient
     * @return 结果List
     */
    private static <T> T resolveBean(final Class<T> resultClass, final HttpPost httpPost) {
        T object = FastJsonUtils.fromJson(resolve(httpPost, 1), resultClass);
        return object;
    }
    
    /**
     * 处理返回JSONObject的调用。统一返回JSONObject，然后需要特殊的处理都可以从这里获取。
     * 包括获取某一节点的值，以及某一节点JSONArray等等。
     * @param httpPost HttpPost实例
     * @return JSONObject
     */
    private static JSONObject resolveJsonObject(final HttpPost httpPost) {
        String json = new String(resolve(httpPost, 1), Char.UTF8);
        return JSON.parseObject(json);
    }
    
}

