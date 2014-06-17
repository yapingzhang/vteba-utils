package com.vteba.utils.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vteba.utils.json.FastJsonUtils;

/**
 * HTML工具类。
 * @author yinlei
 * date 2013-8-23 下午7:13:16
 */
public class HtmlUtils {
	private static Logger logger = LoggerFactory.getLogger(HtmlUtils.class);
	
	/**
	 * 输出json格式数据。
	 * @param response HttpServletResponse
	 * @param json JSON字符串
	 * @author yinlei
	 * date 2013-8-23 下午7:15:08
	 */
	public static void writerJson(HttpServletResponse response, String json) {
		writer(response, json);
	}
	
	/**
	 * 输出JSON格式的数据。
	 * @param response HttpServletResponse
	 * @param object 响应的数据，底层使用fastjson将其序列化成json字符串
	 * @author yinlei
	 * date 2013-8-23 下午7:18:12
	 */
	public static void writerJson(HttpServletResponse response, Object object){
		response.setContentType("application/json");
		writer(response, FastJsonUtils.toJson(object));
	}
	
	/**
	 * 输出HTML代码
	 * @param response HttpServletResponse
	 * @param htmlStr 要输出的html代码
	 * @author yinlei
	 * date 2013-8-23 下午7:16:51
	 */
	public static void writerHtml(HttpServletResponse response, String htmlStr) {
		writer(response, htmlStr);
	}
	
	/**
	 * 输出html响应。
	 * @param response HttpServletResponse
	 * @param str 要输出的内容
	 * @author yinlei
	 * date 2013-8-23 下午7:16:08
	 */
	private static void writer(HttpServletResponse response, String str){
		try {
			//设置页面不缓存
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out= null;
			out = response.getWriter();
			out.print(str);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("响应html代码错误[{}]", str);
		}
	} 
}
