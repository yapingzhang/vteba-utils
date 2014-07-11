package com.vteba.utils.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
//import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;

public class HttpUtils {
        @RequestMapping
        public final static void main(String[] args) throws Exception {
     
            // 初始化，此处构造函数就与3不同
            HttpClient httpclient = HttpClientBuilder.create().build();
     
            HttpHost targetHost = new HttpHost("www.baidu.com", 80, "http");
            //HttpGet httpget = new HttpGet("http://www.apache.org/");
            HttpGet httpget = new HttpGet("/");
     
            // 查看默认request头部信息
            System.out.println("Accept-Charset:" + httpget.getFirstHeader("Accept-Charset"));
            // 以下这条如果不加会发现无论你设置Accept-Charset为gbk还是utf-8，他都会默认返回gb2312（本例针对google.cn来说）
            httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2)");
            // 用逗号分隔显示可以同时接受多种编码
            httpget.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
            httpget.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
            // 验证头部信息设置生效
            System.out.println("Accept-Charset:" + httpget.getFirstHeader("Accept-Charset").getValue());
     
            HttpPost httpPost = new HttpPost("/user/save");
            
            httpPost.setHeader("content-type", "text/plain;charset=utf-8");
            
            //HttpEntity postHttpEntity = new StringEntity(json, charset)
//            List<NameValuePair> list = new ArrayList<NameValuePair>();
//            URI uri = new URIBuilder()
//                .addParameter("userName", "yinlei尹雷")
//                .setPath("/test/userServlet")
//                .build();
//            
//            list = URLEncodedUtils.parse(uri, "UTF-8");
//            
//            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
//            
//            httpPost.setEntity(urlEncodedFormEntity);
            
            // Execute HTTP request
            System.out.println("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(targetHost, httpget);
            //HttpResponse response = httpclient.execute(httpget);
     
            System.out.println("----------------------------------------");
            System.out.println("Location: " + response.getLastHeader("Location"));
            System.out.println(response.getStatusLine().getStatusCode());
            System.out.println(response.getLastHeader("Content-Type"));
            System.out.println(response.getLastHeader("Content-Length"));
            
            System.out.println("----------------------------------------");
     
            // 判断页面返回状态判断是否进行转向抓取新链接
            int statusCode = response.getStatusLine().getStatusCode();
            if ((statusCode == HttpStatus.SC_MOVED_PERMANENTLY) ||
                    (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) ||
                    (statusCode == HttpStatus.SC_SEE_OTHER) ||
                    (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                // 此处重定向处理  此处还未验证
                String newUri = response.getLastHeader("Location").getValue();
                httpclient = HttpClientBuilder.create().build();
                httpget = new HttpGet(newUri);
                response = httpclient.execute(httpget);
            }
     
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            
            // 查看所有返回头部信息
            Header headers[] = response.getAllHeaders();
            int ii = 0;
            while (ii < headers.length) {
                System.out.println(headers[ii].getName() + ": " + headers[ii].getValue());
                ++ii;
            }
            
            // If the response does not enclose an entity, there is no need
            // to bother about connection release
            if (entity != null) {
                // 将源码流保存在一个byte数组当中，因为可能需要两次用到该流，
                byte[] bytes = EntityUtils.toByteArray(entity);
                String charSet = "";
                
                // 如果头部Content-Type中包含了编码信息，那么我们可以直接在此处获取
                charSet = ContentType.getOrDefault(entity).getCharset().name();
                System.out.println("In header: " + charSet);
                // 如果头部中没有，那么我们需要 查看页面源码，这个方法虽然不能说完全正确，因为有些粗糙的网页编码者没有在页面中写头部编码信息
                if (charSet == "") {
                    String regEx = "(?=<meta).*?(?<=charset=[\\'|\\\"]?)([[a-z]|[A-Z]|[0-9]|-]*)";
                    Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(new String(bytes));  // 默认编码转成字符串，因为我们的匹配中无中文，所以串中可能的乱码对我们没有影响
                    boolean result = m.find();
                    System.out.println(result);
                    if (m.groupCount() == 1) {
                        charSet = m.group(1);
                    } else {
                        charSet = "";
                    }
                }
                System.out.println("Last get: " + charSet);
                // 至此，我们可以将原byte数组按照正常编码专成字符串输出（如果找到了编码的话）
                System.out.println("Encoding string is: " + new String(bytes, charSet));
            }
     
        }
     
}

