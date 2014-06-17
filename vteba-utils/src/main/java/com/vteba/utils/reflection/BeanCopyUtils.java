package com.vteba.utils.reflection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sf.cglib.beans.BeanCopier;

import org.apache.commons.lang3.StringUtils;

import com.vteba.lang.bytecode.MethodAccess;

/**
 * 字节码操作JavaBean，属性复制及Bean和Map转换。
 * @author skmbw
 * @date 2013-2-27 下午8:14:07
 */
public class BeanCopyUtils {
	
	/** 缓存BeanCopier */
	private static ConcurrentMap<String, BeanCopier> copierCache = new ConcurrentHashMap<String, BeanCopier>();
	
	private static final BeanCopyUtils instance = new BeanCopyUtils();

	private BeanCopyUtils() {
		
	}
    
    /**
     * 获得BeanCopierFactory单例
     */
    public static BeanCopyUtils getInstance() {
        return instance;
    } 
    
    /**
     * 创建JavaBean属性复制器，使用CGLIB实现
     * @param sourceClass 源类
     * @param targetClass 目标类
     * @return net.sf.cglib.beans.BeanCopier 实例
     */
    public BeanCopier createBeanCopier(Class<?> sourceClass, Class<?> targetClass) {
        StringBuilder sb = new StringBuilder(sourceClass.getName()).append("To").append(targetClass.getSimpleName());
        final String key = sb.toString();
        BeanCopier beanCopier = copierCache.get(key);
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(sourceClass, targetClass, false);
            copierCache.put(key, beanCopier);
        }
        return beanCopier;
    } 
    
    /**
     * 依据属性名相等，将JavaBean fromObject的属性复制到目标JavaBean toObject。
     * @param fromObject 源JavaBean
     * @param toObject 目标JavaBean
     */
    public void beanCopy(Object fromObject, Object toObject) { 
        BeanCopier beanCopier = createBeanCopier(fromObject.getClass(), toObject.getClass()); 
        beanCopier.copy(fromObject, toObject, null);
    }
    
    /**
     * 将Bean转换为Map
     * @param fromBean 源JavaBean
     * @param toMap 目标Map
     */
    public void beanToMap(Object fromBean, Map<String, Object> toMap) {
        MethodAccess methodAccess = AsmUtils.get().createMethodAccess(fromBean.getClass());
        String[] methodNames = methodAccess.getMethodNames(); 
        for (String methodName : methodNames){ 
            if (methodName.startsWith("get")){ 
                Object value = methodAccess.invoke(fromBean, methodName, (Object[])null); 
                toMap.put(StringUtils.uncapitalize(methodName.substring(3)), value); 
            } 
        }
    } 
    
    /**
     * 将Map转换为JavaBean
     * @param toBean 目标JavaBean
     * @param fromMap 源Map
     */
    public void mapToBean(Object toBean, Map<String, Object> fromMap) { 
        MethodAccess methodAccess = AsmUtils.get().createMethodAccess(toBean.getClass());
        for (Map.Entry<String, Object> entry : fromMap.entrySet()){ 
            StringBuilder methodName = new StringBuilder("set").append(StringUtils.capitalize(entry.getKey())); 
            methodAccess.invoke(toBean, methodName.toString(), entry.getValue()); 
        }
    }
    
//    @SuppressWarnings("unused")
//	public static void main(String[] a) {
//    	ConstructorAccess<EmpUser> constructorAccess = BeanCopyUtils.getInstance().createConstructorAccess(EmpUser.class);
//    	System.out.println(constructorAccess);
//    	MethodAccess access = BeanCopyUtils.getInstance().createMethodAccess(EmpUser.class);
//    	System.out.println(access);
//    	
//    	EmpUser target = new EmpUser();
//    	target.setUsername("asdf");
////    	try {
////			target.getClass().getDeclaredField("username").setAccessible(true);
////		} catch (SecurityException e) {
////			e.printStackTrace();
////		} catch (NoSuchFieldException e) {
////			e.printStackTrace();
////		}
//    	//String usern = (String)BeanCopierFactory.getInstance().getFieldByIndex(target, "username");
//    	String usern2 = (String)BeanCopyUtils.getInstance().getField(target, "username");
//    	long d = System.currentTimeMillis();
//    	for (int i = 0; i < 10000000; i++) {
//    		//String username = (String)BeanCopierFactory.getInstance().invokeMethod(target, "getUsername", (Object[])null);
//    	}
//    	System.out.println(System.currentTimeMillis() - d);
//    	long d2 = System.currentTimeMillis();
//    	for (int i = 0; i < 10000000; i++) {
//    		//String username2 = (String)BeanCopierFactory.getInstance().invokeMethodByIndex(target, "getUsername", (Object[])null);
//    	}
//    	
//    	System.out.println(System.currentTimeMillis() - d2);
//    }
}
