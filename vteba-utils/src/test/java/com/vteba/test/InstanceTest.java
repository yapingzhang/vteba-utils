package com.vteba.test;

import org.junit.Test;

import com.vteba.lang.bytecode.ConstructorAccess;
import com.vteba.utils.common.ClassUtils;
import com.vteba.utils.reflection.AsmUtils;
import com.vteba.utils.reflection.ReflectUtils;

public class InstanceTest {
	@Test
	public void test() {
		long d = System.nanoTime();
		ReflectUtils.instantiate(InstanceTest.class);
		System.out.println(System.nanoTime() - d);
		
		
		//ConstructorAccess<InstanceTest> access = AsmUtils.get().createConstructorAccess(clazz);
		ConstructorAccess<InstanceTest> access = AsmUtils.get().createConstructorAccess(InstanceTest.class);
		d = System.nanoTime();
		
		String className = "com.vteba.test.InstanceTest";
		Class<InstanceTest> clazz = ClassUtils.forName(className);
		access = AsmUtils.get().createConstructorAccess(clazz);
		access.newInstance();
		System.out.println(System.nanoTime() - d);
	}
}
