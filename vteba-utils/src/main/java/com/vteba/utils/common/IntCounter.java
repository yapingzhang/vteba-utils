package com.vteba.utils.common;

import java.util.concurrent.atomic.AtomicInteger;

public class IntCounter {
	private static final AtomicInteger integer = new AtomicInteger(1);
	
	private IntCounter() {
	}

	public static int getAndIncrement() {
		return integer.getAndIncrement();
	}
	
	public static int getAndDecrement() {
		return integer.getAndDecrement();
	}
	
}
