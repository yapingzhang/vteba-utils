package com.vteba.utils.reflection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import sun.misc.Unsafe;

/**
 * 获取sun私有的反射操作类实例，sun.misc.Unsafe。性能上比原生反射要快8倍，
 * 但是还是没有字节码快，是字节码的1/3。
 * @author 尹雷 
 * @since 2013-12-3
 */
@SuppressWarnings("restriction")
public class UnsafeUtils {
    private static final Map<Class<?>, Map<String, Field>> fieldCache = new HashMap<Class<?>, Map<String, Field>>();
    
    private static final Unsafe unsafe;
    static {
        Unsafe value = null;
        try {
            Class<?> clazz = Class.forName("sun.misc.Unsafe");
            Field field = clazz.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            value = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Error to get theUnsafe", e);
        }
        unsafe = value;
    }

    protected static final Unsafe getUnsafe() {
        return unsafe;
    }
    
    /**
     * 获得反射字段field的偏移量
     * @param field 反射字段
     * @return 偏移量
     */
    public static final long getOffset(Field field) {
        return unsafe.objectFieldOffset(field);
    }
    
    /**
     * 获得反射字段field的偏移量
     * @param object 属性所在的对象
     * @param fieldName 反射字段名
     * @return 偏移量
     */
    public static final long getOffset(Object object, String fieldName) {
        Field field = getField(object, fieldName);
        return unsafe.objectFieldOffset(field);
    }
    
    protected static Field getCachedField(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        Map<String, Field> fieldsMap = fieldCache.get(clazz);
        Field field = null;
        if (fieldsMap == null) {
            fieldsMap = new HashMap<String, Field>();
            field = getField(object, fieldName);
            if (field != null) {
                fieldsMap.put(fieldName, field);
                fieldCache.put(clazz, fieldsMap);
            }
        } else {
            field = fieldsMap.get(fieldName);
            if (field == null) {
                field = getField(object, fieldName);
                fieldsMap.put(fieldName, field);
            }
        }
        return field;
    }
    
    /**
     * 获取某一对象某一属性的定义所在的类DeclaredField，并强制设置为可访问
     * 没有找到，抛出异常
     * @param object 包含某一属性的对象
     * @param fieldName 某一属性名
     */
    public static Field getField(final Object object, final String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("No such Field [" + fieldName + "] on Object: " + object.getClass().getName());
        }
    }
    
    /**
     * 重载的便捷方法。获取某一对象的属性值，用于单次调用。如果在循环中多次调用，请使用
     * {@link #getObject(Object, long)}。offset参数，请使用{@link #getOffset(Field)}获得。
     * @param object 对象
     * @param fieldName 属性名
     * @return 对象属性值
     */
    public static final Object getObject(Object object, String fieldName) {
        Field field = getField(object, fieldName);
        long offset = getOffset(field);
        return unsafe.getObject(object, offset);
    }
    
    /**
     * 重载的便捷方法。获取某一对象的属性值，用于单次调用。如果在循环中多次调用，请使用
     * {@link #getObject(Object, long)}。offset参数，请使用{@link #getOffset(Field)}获得。
     * @param object 对象
     * @param field 属性名
     * @return 对象属性值
     */
    public static final Object getObject(Object object, Field field) {
        long offset = getOffset(field);
        return unsafe.getObject(object, offset);
    }
    
    /**
     * 获取某一对象的属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}方法
     * @return 对象属性值
     */
    public static final Object getObject(Object object, long offset) {
        return unsafe.getObject(object, offset);
    }
    
    /**
     * 重载的便捷方法。设置对象属性值，用于单次调用，循环中调用请使用{@link #setObject(Object, long, Object)}。
     * @param object 对象
     * @param fieldName 属性名
     * @param fieldValue 属性值
     */
    public static final void setObject(Object object, String fieldName, Object fieldValue) {
        Field field = getField(object, fieldName);
        long offset = getOffset(field);
        unsafe.putObject(object, offset, fieldValue);
    }

    /**
     * 重载的便捷方法。设置对象属性值，用于单次调用，循环中调用请使用{@link #setObject(Object, long, Object)}。
     * @param object 对象
     * @param field 属性名
     * @param fieldValue 属性值
     */
    public static final void setObject(Object object, Field field, Object fieldValue) {
        long offset = getOffset(field);
        unsafe.putObject(object, offset, fieldValue);
    }
    
    /**
     * 设置对象属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}。
     * @param fieldValue 属性值
     */
    public static final void setObject(Object object, long offset, Object fieldValue) {
        unsafe.putObject(object, offset, fieldValue);
    }
    
    /**
     * 重载的便捷方法。获取某一对象的属性值，用于单次调用。如果在循环中多次调用，请使用
     * {@link #getString(Object, long)}。offset参数，请使用{@link #getOffset(Field)}获得。
     * @param object 对象
     * @param fieldName 属性名
     * @return 对象属性值
     */
    public static final String getString(Object object, String fieldName) {
        Field field = getField(object, fieldName);
        long offset = getOffset(field);
        return (String) unsafe.getObject(object, offset);
    }
    
    /**
     * 重载的便捷方法。获取某一对象的属性值，用于单次调用。如果在循环中多次调用，请使用
     * {@link #getString(Object, long)}。offset参数，请使用{@link #getOffset(Field)}获得。
     * @param object 对象
     * @param field 属性名
     * @return 对象属性值
     */
    public static final String getString(Object object, Field field) {
        long offset = getOffset(field);
        return (String) unsafe.getObject(object, offset);
    }
    
    /**
     * 获取某一对象的String属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}方法
     * @return 对象属性值
     */
    public static final String getString(Object object, long offset) {
        return (String) unsafe.getObject(object, offset);
    }
    
    /**
     * 重载的便捷方法。设置对象String属性值，用于单次调用，循环中调用请使用{@link #setString(Object, long, Object)}。
     * @param object 对象
     * @param fieldName 属性名
     * @param fieldValue 属性值
     */
    public static final void setString(Object object, String fieldName, Object fieldValue) {
        Field field = getField(object, fieldName);
        long offset = getOffset(field);
        unsafe.putObject(object, offset, fieldValue);
    }

    /**
     * 重载的便捷方法。设置对象String属性值，用于单次调用，循环中调用请使用{@link #setString(Object, long, Object)}。
     * @param object 对象
     * @param field 属性名
     * @param fieldValue 属性值
     */
    public static final void setString(Object object, Field field, Object fieldValue) {
        long offset = getOffset(field);
        unsafe.putObject(object, offset, fieldValue);
    }
    
    /**
     * 设置对象String属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}。
     * @param fieldValue 属性值
     */
    public static final void setString(Object object, long offset, Object fieldValue) {
        unsafe.putObject(object, offset, fieldValue);
    }
    
    /**
     * 重载的便捷方法。获取对象int属性值，用于单次调用，循环中调用请使用{@link #getInt(Object, long)}。
     * @param object 对象
     * @param fieldName 属性名
     * @return 对象属性值
     */
    public static int getInt(Object object, String fieldName) {
        Field field = getField(object, fieldName);
        long offset = getOffset(field);
        return unsafe.getInt(object, offset);
    }

    /**
     * 重载的便捷方法。获取对象int属性值，用于单次调用，循环中调用请使用{@link #getInt(Object, long)}。
     * @param object 对象
     * @param field 属性
     * @return 对象属性值
     */
    public static int getInt(Object object, Field field) {
        long offset = getOffset(field);
        return unsafe.getInt(object, offset);
    }
    
    /**
     * 获取某一对象的int属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}方法
     * @return 对象属性值
     */
    public static int getInt(Object object, long offset) {
        return unsafe.getInt(object, offset);
    }
    
    /**
     * 重载的便捷方法。设置对象int属性值，用于单次调用，循环中调用请使用{@link #setInt(Object, long, Object)}。
     * @param object 对象
     * @param fieldName 属性名
     * @param value 属性值
     */
    public static void setInt(Object object, String fieldName, int value) {
        Field field = getField(object, fieldName);
        long offset = getOffset(field);
        unsafe.putInt(object, offset, value);
    }
    
    /**
     * 重载的便捷方法。设置对象int属性值，用于单次调用，循环中调用请使用{@link #setInt(Object, long, Object)}。
     * @param object 对象
     * @param field 属性
     * @param value 属性值
     */
    public static void setInt(Object object, Field field, int value) {
        long offset = getOffset(field);
        unsafe.putInt(object, offset, value);
    }
    
    /**
     * 设置对象int属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}
     * @param value 属性值
     */
    public static void setInt(Object object, long offset, int value) {
        unsafe.putInt(object, offset, value);
    }
    
    /**
     * 重载的便捷方法。获取对象long属性值，用于单次调用，循环中调用请使用{@link #getLong(Object, long)}。
     * @param object 对象
     * @param fieldName 属性名
     * @return 对象属性值
     */
    public static long getLong(Object object, String fieldName) {
        Field field = getField(object, fieldName);
        long offset = getOffset(field);
        return unsafe.getLong(object, offset);
    }

    /**
     * 重载的便捷方法。获取对象long属性值，用于单次调用，循环中调用请使用{@link #getLong(Object, long)}。
     * @param object 对象
     * @param field 属性
     * @return 对象属性值
     */
    public static long getLong(Object object, Field field) {
        long offset = getOffset(field);
        return unsafe.getLong(object, offset);
    }
    
    /**
     * 获取某一对象的long属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}方法
     * @return 对象属性值
     */
    public static long getLong(Object object, long offset) {
        return unsafe.getLong(object, offset);
    }
    
    /**
     * 重载的便捷方法。设置对象long属性值，用于单次调用，循环中调用请使用{@link #setLong(Object, long, Object)}。
     * @param object 对象
     * @param fieldName 属性名
     * @param value 属性值
     */
    public static void setLong(Object object, String fieldName, long value) {
        Field field = getField(object, fieldName);
        long offset = getOffset(field);
        unsafe.putLong(object, offset, value);
    }
    
    /**
     * 重载的便捷方法。设置对象long属性值，用于单次调用，循环中调用请使用{@link #setLong(Object, long, Object)}。
     * @param object 对象
     * @param field 属性
     * @param value 属性值
     */
    public static void setLong(Object object, Field field, long value) {
        long offset = getOffset(field);
        unsafe.putLong(object, offset, value);
    }
    
    /**
     * 设置对象long属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}
     * @param value 属性值
     */
    public static void setLong(Object object, long offset, long value) {
        unsafe.putLong(object, offset, value);
    }
    
    /**
     * 重载的便捷方法。获取对象double属性值，用于单次调用，循环中调用请使用{@link #getDouble(Object, long)}。
     * @param object 对象
     * @param fieldName 属性名
     * @return double
     */
    public static double getDouble(Object object, String fieldName) {
        Field field = getField(object, fieldName);
        long offset = getOffset(field);
        return unsafe.getDouble(object, offset);
    }

    /**
     * 重载的便捷方法。获取对象double属性值，用于单次调用，循环中调用请使用{@link #getDouble(Object, long)}。
     * @param object 对象
     * @param field 属性
     * @return double
     */
    public static double getDouble(Object object, Field field) {
        long offset = getOffset(field);
        return unsafe.getDouble(object, offset);
    }
    
    /**
     * 获取某一对象的double属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}方法
     * @return 对象属性值
     * @return double
     */
    public static double getDouble(Object object, long offset) {
        return unsafe.getDouble(object, offset);
    }
    
    /**
     * 重载的便捷方法。设置对象double属性值，用于单次调用，循环中调用请使用{@link #setDouble(Object, long, Object)}。
     * @param object 对象
     * @param fieldName 属性名
     * @param value 属性值
     */
    public static void setDouble(Object object, String fieldName, double value) {
        Field field = getField(object, fieldName);
        long offset = getOffset(field);
        unsafe.putDouble(object, offset, value);
    }
    
    /**
     * 重载的便捷方法。设置对象double属性值，用于单次调用，循环中调用请使用{@link #setDouble(Object, long, Object)}。
     * @param object 对象
     * @param field 属性
     * @param value 属性值
     */
    public static void setDouble(Object object, Field field, double value) {
        long offset = getOffset(field);
        unsafe.putDouble(object, offset, value);
    }
    
    /**
     * 设置对象double属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}
     * @param value 属性值
     */
    public static void setLong(Object object, long offset, double value) {
        unsafe.putDouble(object, offset, value);
    }
    
    /**
     * 重载的便捷方法。获取对象boolean属性值，用于单次调用，循环中调用请使用{@link #getBoolean(Object, long)}。
     * @param object 对象
     * @param fieldName 属性名
     * @return boolean
     */
    public static boolean getBoolean(Object object, String fieldName) {
        Field field = getCachedField(object, fieldName);
        long offset = getOffset(field);
        return unsafe.getBoolean(object, offset);
    }

    /**
     * 重载的便捷方法。获取对象boolean属性值，用于单次调用，循环中调用请使用{@link #getBoolean(Object, long)}。
     * @param object 对象
     * @param field 属性
     * @return boolean
     */
    public static boolean getBoolean(Object object, Field field) {
        long offset = getOffset(field);
        return unsafe.getBoolean(object, offset);
    }
    
    /**
     * 获取某一对象的boolean属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}方法
     * @return 对象属性值
     * @return boolean
     */
    public static boolean getBoolean(Object object, long offset) {
        return unsafe.getBoolean(object, offset);
    }
    
    /**
     * 重载的便捷方法。设置对象boolean属性值，用于单次调用，循环中调用请使用{@link #setBoolean(Object, long, Object)}。
     * @param object 对象
     * @param fieldName 属性名
     * @param value 属性值
     */
    public void setBoolean(Object object, String fieldName, boolean value) {
        Field field = getCachedField(object, fieldName);
        long offset = getOffset(field);
        unsafe.putBoolean(object, offset, value);
    }
    
    /**
     * 重载的便捷方法。设置对象boolean属性值，用于单次调用，循环中调用请使用{@link #setBoolean(Object, long, Object)}。
     * @param object 对象
     * @param field 属性
     * @param value 属性值
     */
    public void setBoolean(Object object, Field field, boolean value) {
        long offset = getOffset(field);
        unsafe.putBoolean(object, offset, value);
    }
    
    /**
     * 设置对象boolean属性值，如果在循环中，多次调用，请使用该方法。
     * @param object 对象
     * @param offset 属性偏移量，见{@link #getOffset(Field)}
     * @param value 属性值
     */
    public void setBoolean(Object object, long offset, boolean value) {
        unsafe.putBoolean(object, offset, value);
    }
    
//    public static void main(String[] args) {
//        TestBean testBean = new TestBean();
//        Field nameField = getField(testBean, "name");
//        Field ageField = getField(testBean, "age");
//        long nameOffset = getOffset(nameField);
//        long ageOffset = getOffset(ageField);
//        long t = System.nanoTime();
//        getObject(testBean, nameOffset);
//        getObject(testBean, ageOffset);
//        System.out.println("Unsafe的反射时间" + (System.nanoTime() -t));
//        
//        FieldAccess fieldAccess = AsmUtils.get().createFieldAccess(TestBean.class);
//        t = System.nanoTime();
//        fieldAccess.get(testBean, "age");
//        fieldAccess.get(testBean, "name");
//        System.out.println("ReflectAsm的field反射时间" + (System.nanoTime() -t));
//        
//        MethodAccess methodAccess = AsmUtils.get().createMethodAccess(TestBean.class);
//        
//        t = System.nanoTime();
//        methodAccess.invoke(testBean, "getAge");
//        methodAccess.invoke(testBean, "getName");
//        System.out.println("ReflectAsm的method反射时间" + (System.nanoTime() -t));
//        
//        t = System.nanoTime();
//        Field[] fields = testBean.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            try {
//                field.setAccessible(true);
//                field.get(testBean);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("原生反射时间：" + (System.nanoTime() -t));
//    }
}
