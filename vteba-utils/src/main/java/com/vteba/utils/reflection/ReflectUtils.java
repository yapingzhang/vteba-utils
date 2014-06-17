package com.vteba.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.vteba.lang.bytecode.MethodAccess;
import com.vteba.utils.common.TypeConverter;
import com.vteba.utils.consts.Consts;
import com.vteba.utils.web.RequestContextHolder;
import com.vteba.utils.web.ServletUtils;

/**
 * 反射工具类。
 * 访问对象(私有)变量，方法，获取泛型类型等。
 * @author yinlei 
 * date 2012-5-7 上午10:28:19
 */
public class ReflectUtils {
	private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
	private static final String GET = "get";
	private static final String SET = "set";
	
	/**
	 * 调用对象obj的某一属性的Getter方法
	 * @param obj 包含某一属性的对象
	 * @param propertyName 某一属性
	 * @return
	 */
	public static Object invokeGetterMethod(Object obj, String propertyName) {
		MethodAccess methodAccess = MethodAccess.get(obj.getClass());
		String getterMethodName = GET + StringUtils.capitalize(propertyName);//首字母大写
		return methodAccess.invoke(obj, getterMethodName, (Object[])null);
	}
	/**
	 * 调用Setter方法,使用value的Class来查找Setter方法
	 * @param obj 包含某一属性的对象
	 * @param propertyName 某一属性
	 * @param value 备用的对象
	 */
	public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
		MethodAccess methodAccess = MethodAccess.get(obj.getClass());
		String getterMethodName = SET + StringUtils.capitalize(propertyName);//首字母大写
		methodAccess.invoke(obj, getterMethodName, value);
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数
	 * @param obj 包含要读取的属性的对象
	 * @param fieldName 要读取的属性
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		//FieldAccess fieldAccess = FieldAccess.get(obj.getClass());
		//Field field = getAccessibleField(obj, fieldName);
		//Object object = fieldAccess.get(obj, fieldName);
		//return object;
		//field.setAccessible(false);
		Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}
		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.info("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}
	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数
	 * @param obj 包含要设置的属性的对象
	 * @param fieldName 要设置的属性
	 * @param value 要设置的属性的值
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		//AsmUtils.getInstance().setField(obj, fieldName, value);
		
		Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.info("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 获取某一对象某一属性的定义所在的类DeclaredField，并强制设置为可访问
	 * 循环向上转型，如向上转型到Object仍无法找到, 返回null
	 * @param obj 包含某一属性的对象
	 * @param fieldName 某一属性名
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Assert.notNull(obj, "object不能为空");
		Assert.hasText(fieldName, "fieldName");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				//Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}
	
	/**
	 * 直接调用对象方法,无视private/protected修饰符
	 * @param obj 方法所在的对象
	 * @param methodName 要调用的方法名
	 * @param args 调用方法的参数类型
	 * @return 方法的调用结果
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Object[] args) {
		MethodAccess methodAccess = MethodAccess.get(obj.getClass());
		return methodAccess.invoke(obj, methodName, args);
	}
	/**
	 * 直接调用对象方法,无视private/protected修饰符,用于一次性调用的情况.
	 * @param obj 方法所在的对象
	 * @param methodName 要调用的方法名
	 * @param parameterTypes 调用方法的参数类型
	 * @param args 调用方法的参数
	 * @return
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertExceptionToUnchecked(e);
		}
	}
	/**
	 * 获取对象的DeclaredMethod,并强制设置为可访问
	 * 循环向上转型,若向上转型到Object仍无法找到, 返回null
	 * 用于方法需要被多次调用的情况，先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 * @param obj 包含对象的方法
	 * @param methodName 方法名
	 * @param parameterTypes 方法的参数类型
	 * @return
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,final Class<?>... parameterTypes){
		Assert.notNull(obj, "object不能为空");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
				method.setAccessible(true);
				return method;
			} catch (NoSuchMethodException e) {
				//Method不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/**
	 * 反射，获得Class定义中声明的泛型参数的类型，如无法找到，返回null
	 * 如：public UserDao&ltUser&gt
	 * @param clazz 泛型参数所在的类的class，如：UserDao.class
	 * @return 第一个泛型参数的类型User.class，如果没有找到返回null
	 */
	public static <T> Class<T>  getClassGenericType(Class<? extends Object> clazz) {
		return getClassGenericType(clazz, 0);
	}
	
	/**
	 * 通过反射，获得Class定义中声明的父类的泛型参数的类型，如无法找到，返回null
	 * 如public UserDao&ltUser, Long&gt
	 * @param 泛型参数所在的类的class
	 * @param index 第几个泛型参数，从 0开始
	 * @return 泛型参数类型，没有返回null
	 */
	public static <T> Class<T> getClassGenericType(Class<? extends Object> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			logger.info(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return null;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			logger.info("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
			return null;
		}
		if (!(params[index] instanceof Class)) {
			logger.info(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return null;
		}
		@SuppressWarnings("unchecked")
		Class<T> ret = (Class<T>) params[index];
		return ret;
	}
	
	
	/**
	 * 将反射时的checked exception转换为unchecked exception
	 * @param e 要转换的异常
	 */
	public static RuntimeException convertExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException("Reflection Exception.", e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}
	
	/**
	 * 如果对象的String或者boolean属性值为""，则将其转换为null。并且返回hql的where语句的hql查询条件。
	 */
	public static <T> String emptyToNull(T object){
		if (object == null) {
			throw new RuntimeException("object is null.");
		}
		Class<?> clazz = object.getClass();
		String hql = toBuildHql(clazz, object);
		return hql;
	}
	
	/**
	 * 如果对象的String属性值为""，则将其转换为null。同时构建hql语句。（业务有问题，只处理了string属性）
	 * @param object 要转换的对象
	 */
	public static Map<String, Object> emptyToNulls4(Object object) {
		StringBuilder hql = new StringBuilder("select tbs from ")
			.append(object.getClass().getSimpleName())
			.append(" tbs where 1=1");
		Map<String, Object> params = new HashMap<String, Object>();
		MethodAccess methodAccess = AsmUtils.get().createMethodAccess(object.getClass());
		String[] methodNames = methodAccess.getMethodNames();
		Class<?>[][] paramTypes = methodAccess.getParameterTypes();
		int i = 0;
		for (Class<?>[] paramType : paramTypes) {
			if (paramType.length > 0) {
				Class<?> clazz = paramType[0];
				if (clazz == String.class) {
					String methodName = methodNames[i];
					if (methodName.startsWith(SET)) {
						String attrName = methodName.substring(3);
						Object value = methodAccess.invoke(object, GET + attrName);
						if (value != null && value.equals("")) {
							hql.append(" and ").append(attrName).append(" = :").append(attrName);
							params.put(attrName, value);
							//这一句没有调用的必要了，提升性能。
							//methodAccess.invoke(object, methodName, new Object[]{ null });
						}
					}
				}
			}
			i++;
		}
		params.put(Consts.HQL, hql.toString());
		return params;
	}
	
	//性能最差，字节码获取方法，字节码获取其值
	public static Map<String, Object> emptyToNulls1(Object object) {
		StringBuilder hql = new StringBuilder("select tbs from ")
			.append(object.getClass().getSimpleName())
			.append(" tbs where 1=1");
		Map<String, Object> params = new HashMap<String, Object>();
		MethodAccess methodAccess = AsmUtils.get().createMethodAccess(object.getClass());
		String[] methodNames = methodAccess.getMethodNames();
		Class<?>[][] paramTypes = methodAccess.getParameterTypes();
		int i = 0;
		for (Class<?>[] paramType : paramTypes) {
			if (paramType.length > 0) {
				Class<?> clazz = paramType[0];
				String methodName = methodNames[i];
				if (methodName.startsWith(SET)) {
					String attrName = methodName.substring(3);
					Object value = null;
					if (clazz == boolean.class || clazz == Boolean.class) {
						value = methodAccess.invoke(object, "is" + attrName);
					} else {
						value = methodAccess.invoke(object, GET + attrName);
					}
					if (value != null && value.equals("")) {
						hql.append(" and ").append(attrName).append(" = :").append(attrName);
						params.put(attrName, value);
					}
				}
			}
			i++;
		}
		params.put(Consts.HQL, hql.toString());
		return params;
	}
	
	//字节码获取方法，request取值，手工转换类型值
	public static Map<String, Object> emptyToNulls6(Object object) {
		StringBuilder hql = new StringBuilder("select tbs from ")
			.append(object.getClass().getSimpleName())
			.append(" tbs where 1=1");
		Map<String, Object> params = new HashMap<String, Object>();
		MethodAccess methodAccess = AsmUtils.get().createMethodAccess(object.getClass());
		String[] methodNames = methodAccess.getMethodNames();
		Class<?>[][] paramTypes = methodAccess.getParameterTypes();
		int i = 0;
		for (Class<?>[] paramType : paramTypes) {
			if (paramType.length > 0) {
				Class<?> clazz = paramType[0];
				String methodName = methodNames[i];
				if (methodName.startsWith(SET)) {
					String attrName = StringUtils.uncapitalize(methodName.substring(3));
					String value = RequestContextHolder.getRequest().getParameter(attrName);

					if (value != null && !value.equals("")) {
						Object typedValue = TypeConverter.convertValue(value, clazz);
						hql.append(" and ").append(attrName).append(" = :").append(attrName);
						params.put(attrName, typedValue);
					}
				}
			}
			i++;
		}
		params.put(Consts.HQL, hql.toString());
		return params;
	}
	
	//性能次差，使用字节码获取方法，request获取其值
	public static Map<String, Object> emptyToNulls2(Object object) {
		StringBuilder hql = new StringBuilder("select tbs from ")
			.append(object.getClass().getSimpleName())
			.append(" tbs where 1=1");
		Map<String, Object> params = new HashMap<String, Object>();
		MethodAccess methodAccess = AsmUtils.get().createMethodAccess(object.getClass());
		String[] methodNames = methodAccess.getMethodNames();
		for (String methodName : methodNames) {
			if (methodName.startsWith(SET)) {
				String attrName = StringUtils.uncapitalize(methodName.substring(3));
				Object value = RequestContextHolder.getRequest().getParameter(attrName);
				if (value != null && !value.equals("")) {
					hql.append(" and ").append(attrName).append(" = :").append(attrName);
					params.put(attrName, value);
				}
			}
		}
		params.put(Consts.HQL, hql.toString());
		return params;
	}
	
	/**
	 * 反射获得Field，request获取其值，遍历对象构建hql语句和参数。返回Map。（相对性能最好）
	 * @param object 要遍历的对象
	 * @return hql语句和参数
	 */
	public static Map<String, Object> emptyToNulls(Object object) {
		StringBuilder hql = new StringBuilder("select tbs from ")
			.append(object.getClass().getSimpleName())
			.append(" tbs where 1=1");
		Map<String, Object> params = new HashMap<String, Object>();
		Field[] fieldList = object.getClass().getDeclaredFields();
		for (Field field : fieldList) {
			String fieldName = field.getName();
			if (fieldName.equals("serialVersionUID")) {
				continue;
			}
			Object value = RequestContextHolder.getRequest().getParameter(fieldName);
			if (value != null && !value.equals("")) {
				hql.append(" and ").append(fieldName).append(" = :").append(fieldName);
				params.put(fieldName, value);
			}
		}
		params.put(Consts.HQL, hql.toString());
		return params;
	}
	
	/**
	 * 构建hql查询语句，获取属性使用反射（只获取Fileds）,然后使用request获取值，手动转换。
	 * @param object 携带参数的对象
	 * @return hql语句
	 */
	public static Map<String, Object> buildHql(Object object) {
		StringBuilder hql = new StringBuilder("select tbs from ")
			.append(object.getClass().getSimpleName())
			.append(" tbs where 1=1");
		Map<String, Object> params = new HashMap<String, Object>();
		Field[] fieldList = object.getClass().getDeclaredFields();
		for (Field field : fieldList) {
			String fieldName = field.getName();
			if (fieldName.equals("serialVersionUID")) {
				continue;
			}
			String value = RequestContextHolder.getRequest().getParameter(fieldName);
			if (value != null && !value.equals("")) {
				Object typedValue = TypeConverter.simpleConvertValue(value, field.getType());
				hql.append(" and ").append(fieldName).append(" = :").append(fieldName);
				params.put(fieldName, typedValue);
			}
		}
		params.put(Consts.HQL, hql.toString());
		return params;
	}
	
	protected static void convertStringToNull(Class<?> clazz, Object obj)
			throws RuntimeException {
		if (clazz.getSimpleName().equals("Object")) {
			return;
		}
		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length <= 0) {
			throw new RuntimeException("object have nothing fields.");
		}
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			String name = field.getName();
			if (name.equals("serialVersionUID")) {
				continue;
			}
			Object value = null;
			try {
				value = field.get(obj);
				if (value != null && value.equals("")) {
					field.set(obj, null);
				} else if (value != null && value.toString().equals("false")) {
					String param = ServletUtils.getParameter(name);
					if (param != null && param.equals("")) {
						field.set(obj, null);
					}
				}
			} catch (Exception e) {
				throw convertExceptionToUnchecked(e);
			}
		}
	}
	
	protected static String toBuildHql(Class<?> clazz, Object obj) throws RuntimeException {
		StringBuilder sb = new StringBuilder();
		if (clazz.getSimpleName().equals("Object")) {
			return "";
		}
		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length <= 0) {
			throw new RuntimeException("object have nothing fields.");
		}
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			String name = field.getName();
			if (name.equals("serialVersionUID")) {
				continue;
			}
			Object value = null;
			try {
				value = field.get(obj);
				if (value != null && value.toString().equals("")) {
					field.set(obj, null);
				} else if (value != null && value.toString().equals("false")) {
					String param = ServletUtils.getParameter(name);
					if (param != null && param.equals("")) {
						field.set(obj, null);
					} else {//该值就是false
						sb.append(name);
						sb.append(" = :");
						sb.append(name);
					}
				} else if (value != null) {//该值非空
					sb.append(name);
					sb.append(" = ");
					sb.append(value);
				}
			} catch (Exception e) {
				throw convertExceptionToUnchecked(e);
			}
		}
		return sb.toString();
	}
}
