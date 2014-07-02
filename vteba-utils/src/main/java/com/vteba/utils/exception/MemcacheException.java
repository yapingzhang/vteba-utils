package com.vteba.utils.exception;

/**
 * 将XMemcached的已检测异常，改为免检异常。
 * @author yinlei
 * @date 2013年9月28日 下午10:15:46
 */
public class MemcacheException extends RuntimeException {

	private static final long serialVersionUID = -7249968693998352125L;

	public MemcacheException() {
		super();
	}

	public MemcacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public MemcacheException(String message) {
		super(message);
	}

	public MemcacheException(Throwable cause) {
		super(cause);
	}

}
