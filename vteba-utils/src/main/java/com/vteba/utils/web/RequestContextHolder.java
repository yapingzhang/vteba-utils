package com.vteba.utils.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * Holder class to expose the web request/response in the form of a thread-bound
 * {@link ServletWebRequest} object.
 * <p>Use {@link com.vteba.web.filter.RequestContextFilter} to expose
 * the current web request.
 * @author yinlei
 * @date 2012-8-30
 */
public abstract class RequestContextHolder  {
	
	private static final ThreadLocal<ServletWebRequest> requestHolder = new ThreadLocal<ServletWebRequest>();

	/**
	 * Reset the ServletWebRequest for the current thread.
	 */
	public static void resetServletWebRequest() {
		requestHolder.remove();
	}

	/**
	 * Bind the given ServletWebRequest to the current thread.
	 * @param servletWebRequest the RequestAttributes to expose,
	 * or {@code null} to reset the thread-bound context
	 * @param inheritable whether to expose the RequestAttributes as inheritable
	 * for child threads (using an {@link InheritableThreadLocal})
	 */
	public static void setServletWebRequest(ServletWebRequest servletWebRequest) {
		if (servletWebRequest == null) {
			resetServletWebRequest();
		} else {
			requestHolder.set(servletWebRequest);
		}
	}

	/**
	 * Return the ServletWebRequest currently bound to the thread.
	 * @return the ServletWebRequest currently bound to the thread,
	 * or {@code null} if none bound
	 */
	public static ServletWebRequest getServletWebRequest() {
		ServletWebRequest attributes = requestHolder.get();
		return attributes;
	}
	
	/**
	 * the HttpServletRequest currently bound to the thread
	 * @return the HttpServletRequest currently bound to the thread,
	 * or {@code null} if none bound
	 * @author yinlei
	 * date 2013-8-30 下午9:36:24
	 */
	public static HttpServletRequest getRequest() {
		return getServletWebRequest().getRequest();
	}
	
	/**
	 * the HttpServletResponse currently bound to the thread
	 * @return the HttpServletResponse currently bound to the thread,
	 * or {@code null} if none bound
	 * @author yinlei
	 * date 2013-8-30 下午9:36:28
	 */
	public static HttpServletResponse getResponse() {
		return getServletWebRequest().getResponse();
	}
	
	/**
	 * the HttpSession currently bound to the thread
	 * @return the HttpSession currently bound to the thread,
	 * or {@code null} if none bound
	 * @author yinlei
	 * date 2013-8-30 下午9:36:31
	 */
	public static HttpSession getSession() {
		return getServletWebRequest().getRequest().getSession();
	}
}

