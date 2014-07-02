package com.vteba.utils.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 关于异常的工具类.
 * @author yinlei
 */
public class Exceptions {
	//private static final Logger logger = LoggerFactory.getLogger(Exceptions.class);
	
	/**
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

//	public static MemcachedException memcacheException(Exception e) {
//		logger.warn(e.getMessage());
//		return new MemcachedException(e.getMessage(), e);
//	}
	
	/**
	 * 将ErrorStack转化为String.
	 */
	public static String getStackTraceAsString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * 判断异常是否由某些底层的异常引起.
	 */
	public static boolean isCausedBy(Exception ex, Class<?>... causeExClasses) {
		Throwable cause = ex;
		while (cause != null) {
			for (Class<?> causeClass : causeExClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}
}
