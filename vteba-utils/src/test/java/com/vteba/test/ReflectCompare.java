package com.vteba.test;


import java.lang.reflect.Field;

import sun.misc.Unsafe;

import com.vteba.lang.bytecode.FieldAccess;
import com.vteba.utils.reflection.UnsafeUtils;

@SuppressWarnings("restriction")
public class ReflectCompare {
    private static final int count = 10000000;
    private static FieldAccess fieldAccess = FieldAccess.get(TestBean.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        long duration = testUnsafe();
        System.out.println("使用unsafe调用 for  " + count
                           + " times, duration: " + duration);
        duration = testIntCommon();
        System.out.println("int 使用普通反射 for  " + count
                + " times, duration: " + duration);
        
        duration = testDirect();
        System.out.println("直接调用 for  " + count
                + " times, duration: " + duration);
        
        duration = testKryo();
        System.out.println("使用kryo for  " + count
                + " times, duration: " + duration);
    }

    private static long testUnsafe() {
//        long start = System.currentTimeMillis();
//        sun.misc.Unsafe unsafe = getUnsafe();
//        int temp = count;
////        long c = System.currentTimeMillis();
//        Field field = getIntField();
////        System.out.println(System.currentTimeMillis() - c);
//        long offset = unsafe.objectFieldOffset(field);
//        while (temp-- > 0) {
//            unsafe.getInt(new TestBean(), offset);
//        }
//        return System.currentTimeMillis() - start;
        UnsafeUtils.getInt(new TestBean(), "age");
        long start = System.currentTimeMillis();
        int temp = count;
        TestBean testBean = new TestBean();
        Field field = UnsafeUtils.getField(testBean, "name");
        long offset = UnsafeUtils.getOffset(field);
        while (temp-- > 0) {
            UnsafeUtils.getString(testBean, offset);
        }
        return System.currentTimeMillis() - start;
    }

    private static long testDirect() {
        long start = System.currentTimeMillis();
        
        int temp = count;
        
        TestBean testBean = new TestBean();
        while (temp-- > 0) {
            testBean.getAge();
        }
        return System.currentTimeMillis() - start;
    }
    
    private static long testKryo() {
        long start = System.currentTimeMillis();
        
        int temp = count;
        int index = fieldAccess.getIndex("age");
        while (temp-- > 0) {
            TestBean testBean = new TestBean();
            fieldAccess.get(testBean, index);
        }
        return System.currentTimeMillis() - start;
    }
    
    private static long testIntCommon() {
        long start = System.currentTimeMillis();
        int temp = count;
        Field field = getIntField();
        field.setAccessible(true);
        while (temp-- > 0) {
            TestBean bean = new TestBean();
            try {
                field.get(bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static final sun.misc.Unsafe unsafe;
    static {
        sun.misc.Unsafe value = null;
        try {
            Class<?> clazz = Class.forName("sun.misc.Unsafe");
            Field field = clazz.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            value = (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("error to get theUnsafe", e);
        }
        unsafe = value;
    }

    public static final sun.misc.Unsafe getUnsafe() {
        return unsafe;
    }

    private static final Field intField;
    private static final Field stringField;
    static {
        try {
            intField = TestBean.class.getDeclaredField("age");
            stringField = TestBean.class.getDeclaredField("name");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("failed to init testbean field", e);
        }
    }

    public static final Field getIntField() {
        return intField;
    }

    public static final Field getStringField() {
        return stringField;
    }

    
}

