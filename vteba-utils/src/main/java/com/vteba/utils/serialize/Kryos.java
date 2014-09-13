package com.vteba.utils.serialize;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Lists;
import com.vteba.utils.json.Node;

/**
 * 基于kryo的序列化转换器。
 * @author yinlei
 * date 2013-4-6 下午2:50:09
 */
public class Kryos {
	private static ThreadLocal<Kryo> threadLocal = new ThreadLocal<Kryo>();

//	private static Kryos instance = new Kryos();
//
//	public static Kryos get() {
//		return instance;
//	}

	private Kryos() {
	}

	/**
	 * 获得当前线程的Kryo
	 * @return Kryo实例
	 * @author yinlei
	 * date 2013-4-6 下午2:50:52
	 */
	public Kryo getKryo() {
		Kryo kryo = threadLocal.get();
		if (kryo == null) {
		    // throw new IllegalStateException("没有获得当前线程绑定的Kryo实例，请先注册，再调用。");
			kryo = new Kryo();
			kryo.setRegistrationRequired(true);
			kryo.setReferences(true);
			kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

			registDefault(kryo);
			
			//*********注册自定义对象**********//
			//kryo.register(Customer.class);

			threadLocal.set(kryo);
		}
		return kryo;
	}

	/**
     * 获得当前线程的Kryo。
     * <p>序列化和反序列化一般是分开的，这里是不是没用？同一个线程内操作是可以的，序列化和反序列化分开的话就获取不到了。
     * <p>当前对象要序列化多个对象怎么办？获取当前线程的Kryo后，每次都会注册。
     * @return Kryo实例
     * @author yinlei
     * date 2013-4-6 下午2:50:52
     */
    private static Kryo getKryo(Class<?> clazz) {
        Kryo kryo = threadLocal.get();
        if (kryo == null) {
            kryo = new Kryo();
            kryo.setRegistrationRequired(true);
            kryo.setReferences(true);
            kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
            registDefault(kryo);
            
            //*********注册自定义对象**********//
            kryo.register(clazz);
            threadLocal.set(kryo);
        }
        return kryo;
    }
	
	/**
	 * 将对象序列化为byte[]，对象要注册
	 * @param object 要转换的对象
	 * @return 字节数组
	 * @author yinlei
	 * date 2013-4-6 下午2:51:28
	 */
	public static byte[] toBytes(Object object) {
	    if (object == null) {
            return null;
        }
		Kryo converter = getKryo(object.getClass());
		Output output = new Output(2048, 2 * 1024 * 1024);
		converter.writeObject(output, object);

		return output.toBytes();
	}

	/**
	 * 将字节数组反序列化为对象，对象要注册
	 * @param bytes 被转换的字节数组
	 * @param clazz 要转换的对象类型
	 * @return 要转换的对象实例
	 * @author yinlei
	 * date 2013-4-6 下午2:52:11
	 */
	public static <T> T fromBytes(byte[] bytes, Class<T> clazz) {
	    if (bytes == null) {
	        return null;
	    }
		Kryo converter = getKryo(clazz);
		Input input = new Input(bytes);
		return converter.readObject(input, clazz);
	}

	/**
	 * 将字节数组反序列化为对象List（ArrayList），对象要注册
	 * 
	 * @param bytes
	 *            被转换的字节数组
	 * @return 反序列化后的对象List
	 * @author yinlei date 2013-4-6 下午2:51:11
	 */
	public static <T> List<T> byteToList(byte[] bytes) {
		Kryo kryo = getKryo(ArrayList.class);
		Input input = new Input(bytes);
		@SuppressWarnings("unchecked")
		List<T> list = kryo.readObject(input, ArrayList.class);
		return list;
	}

	/**
     * 将字节数组反序列化为对象Map（HashMap），对象要注册
     * 
     * @param bytes
     *            被转换的字节数组
     * @return 反序列化后的对象Map
     * @author yinlei date 2013-4-6 下午3:51:11
     */
	public static <K, V> Map<K, V> byteToMap(byte[] bytes) {
		Kryo kryo = getKryo(HashMap.class);
		Input input = new Input(bytes);
		@SuppressWarnings("unchecked")
		Map<K, V> map = kryo.readObject(input, HashMap.class);
		return map;
	}

	/**
     * 将字节数组反序列化为对象Set（HashSet），对象要注册
     * 
     * @param bytes
     *            被转换的字节数组
     * @return 反序列化后的对象Set
     * @author yinlei date 2013-4-6 下午4:51:11
     */
	public static <T> Set<T> byteToSet(byte[] bytes) {
		Kryo kryo = getKryo(HashSet.class);
		Input input = new Input(bytes);
		@SuppressWarnings("unchecked")
		Set<T> set = kryo.readObject(input, HashSet.class);
		return set;
	}

	/**
	 * 将对象序列化为字节数组，对象无需注册，性能稍低。字节数变大
	 * @param object 要被序列化的对象
	 * @return 字节数组
	 * @author yinlei date 2013-4-7 下午9:52:43
	 */
	public static byte[] serialize(Object object) {
	    if (object == null) {
	        return null;
	    }
	    Output out = null;
	    try {
	        Kryo kryo = new Kryo();
	        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
	        out = new Output(2 * 1024, 2 * 1024 * 1024);
	        kryo.writeClassAndObject(out, object);
	        return out.toBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }
        }
	}

	/**
	 * 将字节数组反序列化为对象，对象无需注册，性能稍低
	 * @param bytes 要反序列化的字节数组
	 * @return 反序列化后的对象
	 * @author yinlei date 2013-4-7 下午9:54:00
	 */
	public static <T> T deserialize(byte[] bytes) {
	    if (bytes == null) {
	        return null;
	    }
	    Input input = null;
	    try {
	        Kryo kryo = new Kryo();
	        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
	        input = new Input(bytes);
	        @SuppressWarnings("unchecked")
	        T obj = (T) kryo.readClassAndObject(input);
	        return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (input != null) {
                input.close();
                input = null;
            }
        }
	}
	
	/**
	 * 深拷贝对象。完全新的一个对象。
	 * @param object 被拷贝对象
	 * @param <T> 被拷贝对象类型
	 * @return A new Object
	 */
	public static <T> T copy(Object object) {
		Kryo kryo = new Kryo();
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		@SuppressWarnings("unchecked")
		T t = (T) kryo.copy(object);
		return t;
	}
	
	/**
     * 浅拷贝对象。新对象的内容将和原对象共享。还是原来的引用。
     * @param object 被拷贝对象
     * @param <T> 被拷贝对象类型
     * @return A new Object
     */
	public static <T> T copyShallow(Object object) {
		Kryo kryo = new Kryo();
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
		@SuppressWarnings("unchecked")
		T t = (T) kryo.copyShallow(object);
		return t;
	}
	
	public static void main(String[] aa) {
	    Node node = new Node();
	    node.setName("hasdf");
	    List<Node> children = Lists.newArrayList();
	    Node node2 = new Node();
	    node2.setName("aa");
	    children.add(node2);
	    
	    node.setChildren(children);
	    
	    long d = System.currentTimeMillis();
	    byte[] aaa = Kryos.serialize(node);
	    Kryos.deserialize(aaa);
	    System.out.println(System.currentTimeMillis() - d);
	    System.out.println(aaa.length);
	    
	    d = System.currentTimeMillis();
	    byte[] bbb = Kryos.toBytes(node);
	    Kryos.fromBytes(bbb, Node.class);
	    System.out.println(System.currentTimeMillis() - d);
	    System.out.println(bbb.length);
	    
	    List<Node> nodes = Lists.newArrayList();
	    byte[] bytes = Kryos.toBytes(nodes);
	    nodes = Kryos.byteToList(bytes);
	    System.out.println(bytes.length);
	    
	}
	
	/**
     * 注册基本对象
     * @param kryo
     */
    private static void registDefault(Kryo kryo) {
        kryo.register(Class.class);
        kryo.register(byte[].class);
        kryo.register(char[].class);
        kryo.register(short[].class);
        kryo.register(int[].class);
        kryo.register(long[].class);
        kryo.register(float[].class);
        kryo.register(double[].class);
        kryo.register(boolean[].class);
        kryo.register(String[].class);
        kryo.register(Object[].class);
        
        kryo.register(StringBuffer.class);
        kryo.register(StringBuilder.class);
        
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(CopyOnWriteArrayList.class);
        kryo.register(Vector.class);
        
        kryo.register(HashMap.class);
        kryo.register(TreeMap.class);
        kryo.register(LinkedHashMap.class);
        kryo.register(ConcurrentHashMap.class);

        kryo.register(HashSet.class);
        kryo.register(TreeSet.class);
        kryo.register(LinkedHashSet.class);
        kryo.register(CopyOnWriteArraySet.class);

        kryo.register(BigInteger.class);
        kryo.register(BigDecimal.class);
        kryo.register(Currency.class);
        
        kryo.register(EnumSet.class);

        kryo.register(Date.class);
        kryo.register(java.sql.Date.class);
        kryo.register(Calendar.class);
        kryo.register(TimeZone.class);
    }
}
