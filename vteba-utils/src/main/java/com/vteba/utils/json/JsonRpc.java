package com.vteba.utils.json;

/**
 * JsonRpc 2.0协议，参数格式封装，不确定的泛型参数使用Object。
 * <p>序列化成json是没有问题的，但是从json反序列化回来，就需要使用TypeReference指定泛型参数。
 * <p>还有一种方式就是将P,R都使用具体的POJO类型重新定义个Json Bean来进行封装。
 * @author yinlei
 * @date 2013-10-28
 */
public class JsonRpc<P, R> {
	private Integer id;
	private String jsonrpc = "2.0";// 默认2.0
	private String method;
	private P params;
	private R result;
	private Error error;
	
	public JsonRpc() {
		super();
	}

	/**
	 * 一般用于发送查询条件
	 * @param id id
	 * @param method 调用的方法名
	 * @param params 调用方法的参数
	 */
	public JsonRpc(Integer id, String method, P params) {
		super();
		this.id = id;
		this.method = method;
		this.params = params;
	}

	/**
	 * 一般用于返回正确的结果
	 * @param id id
	 * @param result 调用方法返回的结果
	 */
	public JsonRpc(Integer id, R result) {
		super();
		this.id = id;
		this.result = result;
	}

	/**
	 * 一般用于返回调用错误的记过
	 * @param error 错误信息
	 */
	public JsonRpc(Error error) {
		super();
		this.error = error;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJsonrpc() {
		return jsonrpc;
	}

	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public P getParams() {
		return params;
	}

	/**
	 * 设置请求参数，//一般使用POJO，基本类型，可能无法保存类型信息，例如日期会变成字符串//
	 * <p>序列化成json是没有问题的，但是从json反序列化回来，就需要使用TypeReference指定泛型参数。
	 * @param params 参数pojo
	 */
	public void setParams(P params) {
		this.params = params;
	}

	public R getResult() {
		return result;
	}

	/**
	 * 设置返回结果，//一般使用POJO封装。基本类型，可能无法保存类型信息，例如日期会变成字符串//
	 * <p>序列化成json是没有问题的，但是从json反序列化回来，就需要使用TypeReference指定泛型参数。
	 * @param result 调用结果，pojo
	 */
	public void setResult(R result) {
		if (error != null) {
			throw new RuntimeException("已经设置了error，jsonrpc-2.0中，result和error不能同时出现。");
		}
		this.result = result;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		if (result != null) {
			throw new RuntimeException("已经设置了result，jsonrpc-2.0中，result和error不能同时出现。");
		}
		this.error = error;
	}

	public static class Error {
		private Integer code;
		private String message;
		
		public Error() {
			
		}
		
		public Error(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
	}
}
