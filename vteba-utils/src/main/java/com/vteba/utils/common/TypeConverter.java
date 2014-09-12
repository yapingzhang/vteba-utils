package com.vteba.utils.common;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Default type conversion. Converts among numeric types and also strings.<br>
 * Contains the basic type mapping code from OGNL.
 * 类型转换器
 * @author Luke Blanshard (blanshlu@netscape.net)
 * @author Drew Davidson (drew@ognl.org)
 * @author yinlei
 */
public class TypeConverter {
    private static final String NULL_STRING = "null";
    private static Map<Class<?>, Object> primitiveDefaults = new HashMap<Class<?>, Object>();
    
    static {
    	Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
        map.put(Boolean.TYPE, Boolean.FALSE);
        map.put(Byte.TYPE, Byte.valueOf((byte) 0));
        map.put(Short.TYPE, Short.valueOf((short) 0));
        map.put(Character.TYPE, new Character((char) 0));
        map.put(Integer.TYPE, Integer.valueOf(0));
        map.put(Long.TYPE, Long.valueOf(0L));
        map.put(Float.TYPE, new Float(0.0F));
        map.put(Double.TYPE, new Double(0.0D));
        map.put(BigInteger.class, new BigInteger("0"));
        map.put(BigDecimal.class, new BigDecimal("0.0"));
        primitiveDefaults = Collections.unmodifiableMap(map);
    }
    
    public TypeConverter() {
        
    }
    
    /**
     * Returns the value converted numerically to the given class type
     * 
     * This method also detects when arrays are being converted and converts the
     * components of one array to the type of the other.
     * 
     * @param value
     *            an object to be converted to the given type
     * @param toType
     *            class type to be converted to
     * @return converted value of the type given, or value if the value cannot
     *         be converted to the given type.
     */
	@SuppressWarnings("unchecked")
	public static Object convertValue(Object value, Class<?> toType) {
        Object result = null;

        if (value != null) {
            /* If array -> array then convert components of array individually */
            if (value.getClass().isArray() && toType.isArray()) {
                Class<?> componentType = toType.getComponentType();

                result = Array.newInstance(componentType, Array.getLength(value));
                for (int i = 0, icount = Array.getLength(value); i < icount; i++) {
                    Array.set(result, i, convertValue(Array.get(value, i), componentType));
                }
            } else {
				if ((toType == Integer.class) || (toType == Integer.TYPE)) {
					result = Integer.valueOf((int) longValue(value));
				} else if ((toType == Long.class) || (toType == Long.TYPE)) {
                    result = Long.valueOf(longValue(value));
                } else if (toType == String.class) {
                    result = stringValue(value);
                } else if ((toType == Double.class) || (toType == Double.TYPE)) {
                    result = new Double(doubleValue(value));
                } else if ((toType == Boolean.class) || (toType == Boolean.TYPE)) {
					result = booleanValue(value) ? Boolean.TRUE : Boolean.FALSE;
				} else if ((toType == Byte.class) || (toType == Byte.TYPE)) {
					result = Byte.valueOf((byte) longValue(value));
				} else if ((toType == Character.class) || (toType == Character.TYPE)) {
					result = new Character((char) longValue(value));
				} else if ((toType == Short.class) || (toType == Short.TYPE)) {
					result = Short.valueOf((short) longValue(value));
				} else if ((toType == Float.class) || (toType == Float.TYPE)) {
					result = new Float(doubleValue(value));
				} else if (toType == BigInteger.class) {
					result = bigIntValue(value);
				} else if (toType == BigDecimal.class) {
					result = bigDecValue(value);
				} else if (Enum.class.isAssignableFrom(toType)) {
					result = enumValue((Class<Enum<?>>) toType, value);
				}
            }
        } else {
            if (toType.isPrimitive()) {
                result = primitiveDefaults.get(toType);
            }
        }
        return result;
    }

    public static <T> T simpleConvertValue(String value, Class<T> toType) {
        Object result = null;
        if (value != null) {
        	if (toType == String.class) {
				result = value;
			} else if ((toType == Integer.class) || (toType == Integer.TYPE)) {
				result = Integer.valueOf(value);
			} else if ((toType == Long.class) || (toType == Long.TYPE)) {
                result = Long.valueOf(value);
            } else if ((toType == Double.class) || (toType == Double.TYPE)) {
				result = Double.valueOf(value);
			} else if ((toType == Boolean.class) || (toType == Boolean.TYPE)) {
				result = booleanValue(value) ? Boolean.TRUE : Boolean.FALSE;
			} else if (toType == Date.class) {
				try {
					result = DateFormat.getInstance().parse(value);
				} catch (ParseException e) {
					// do nothing
				}
			} else if (toType == BigInteger.class) {
				result = bigIntValue(value);
			} else if (toType == BigDecimal.class) {
				result = bigDecValue(value);
			} else if ((toType == Byte.class) || (toType == Byte.TYPE)) {
				result = Byte.valueOf((byte) longValue(value));
			} else if ((toType == Character.class) || (toType == Character.TYPE)) {
				result = new Character((char) longValue(value));
			} else if ((toType == Short.class) || (toType == Short.TYPE)) {
				result = Short.valueOf((short) longValue(value));
			} else if ((toType == Float.class) || (toType == Float.TYPE)) {
				result = new Float(doubleValue(value));
			} 
        } else {
            if (toType.isPrimitive()) {
                result = primitiveDefaults.get(toType);
            }
        }
        @SuppressWarnings("unchecked")
        T t = (T) result;
        return t;
    }
	
    /**
     * 转换ID
     * @param value key值
     * @param toType String，Integer，Long
     * @return 转换后的key
     */
    public static <T> T convertId(Object value, Class<T> toType) {
        Object result = null;
        if (value != null) {
            if (toType == String.class) {
                result = value.toString();
            } else if ((toType == Integer.class)) {
                result = Integer.valueOf(value.toString());
            } else if ((toType == Long.class)) {
                result = Long.valueOf(value.toString());
            } else if (toType == BigInteger.class) {
                result = bigIntValue(value);
            } else if (toType == BigDecimal.class) {
                result = bigDecValue(value);
            } 
        } else {
            if (toType.isPrimitive()) {
                result = primitiveDefaults.get(toType);
            }
        }
        @SuppressWarnings("unchecked")
        T t = (T) result;
        return t;
    }
    
    /**
     * Evaluates the given object as a boolean: if it is a Boolean object, it's
     * easy; if it's a Number or a Character, returns true for non-zero objects;
     * and otherwise returns true for non-null objects.
     * 
     * @param value
     *            an object to interpret as a boolean
     * @return the boolean value implied by the given object
     */
    public static boolean booleanValue(Object value) {
        if (value == null)
            return false;
        Class<?> c = value.getClass();
        if (c == Boolean.class)
            return ((Boolean) value).booleanValue();
        if (c == Character.class)
            return ((Character) value).charValue() != 0;
        if (value instanceof Number)
            return ((Number) value).doubleValue() != 0;
        return true; // non-null
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Enum<?> enumValue(Class toClass, Object o) {
        Enum<?> result = null;
        if (o == null) {
            result = null;
        } else if (o instanceof String[]) {
            result = Enum.valueOf(toClass, ((String[]) o)[0]);
        } else if (o instanceof String) {
            result = Enum.valueOf(toClass, (String) o);
        }
        return result;
    }

    /**
     * Evaluates the given object as a long integer.
     * 
     * @param value
     *            an object to interpret as a long integer
     * @return the long integer value implied by the given object
     * @throws NumberFormatException
     *             if the given object can't be understood as a long integer
     */
    public static long longValue(Object value) throws NumberFormatException {
        if (value == null)
            return 0L;
        Class<?> c = value.getClass();
        if (c.getSuperclass() == Number.class)
            return ((Number) value).longValue();
        if (c == Boolean.class)
            return ((Boolean) value).booleanValue() ? 1 : 0;
        if (c == Character.class)
            return ((Character) value).charValue();
        return Long.parseLong(stringValue(value, true));
    }

    /**
     * Evaluates the given object as a double-precision floating-point number.
     * 
     * @param value
     *            an object to interpret as a double
     * @return the double value implied by the given object
     * @throws NumberFormatException
     *             if the given object can't be understood as a double
     */
    public static double doubleValue(Object value) throws NumberFormatException {
        if (value == null)
            return 0.0;
        Class<?> c = value.getClass();
        if (c.getSuperclass() == Number.class)
            return ((Number) value).doubleValue();
        if (c == Boolean.class)
            return ((Boolean) value).booleanValue() ? 1 : 0;
        if (c == Character.class)
            return ((Character) value).charValue();
        String s = stringValue(value, true);

        return (s.length() == 0) ? 0.0 : Double.parseDouble(s);
    }

    /**
     * Evaluates the given object as a BigInteger.
     * 
     * @param value
     *            an object to interpret as a BigInteger
     * @return the BigInteger value implied by the given object
     * @throws NumberFormatException
     *             if the given object can't be understood as a BigInteger
     */
    public static BigInteger bigIntValue(Object value)
            throws NumberFormatException {
        if (value == null)
            return BigInteger.valueOf(0L);
        Class<?> c = value.getClass();
        if (c == BigInteger.class)
            return (BigInteger) value;
        if (c == BigDecimal.class)
            return ((BigDecimal) value).toBigInteger();
        if (c.getSuperclass() == Number.class)
            return BigInteger.valueOf(((Number) value).longValue());
        if (c == Boolean.class)
            return BigInteger.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
        if (c == Character.class)
            return BigInteger.valueOf(((Character) value).charValue());
        return new BigInteger(stringValue(value, true));
    }

    /**
     * Evaluates the given object as a BigDecimal.
     * 
     * @param value
     *            an object to interpret as a BigDecimal
     * @return the BigDecimal value implied by the given object
     * @throws NumberFormatException
     *             if the given object can't be understood as a BigDecimal
     */
    public static BigDecimal bigDecValue(Object value)
            throws NumberFormatException {
        if (value == null)
            return BigDecimal.valueOf(0L);
        Class<?> c = value.getClass();
        if (c == BigDecimal.class)
            return (BigDecimal) value;
        if (c == BigInteger.class)
            return new BigDecimal((BigInteger) value);
        if (c.getSuperclass() == Number.class)
            return new BigDecimal(((Number) value).toString());
        if (c == Boolean.class)
            return BigDecimal.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
        if (c == Character.class)
            return BigDecimal.valueOf(((Character) value).charValue());
        return new BigDecimal(stringValue(value, true));
    }

    /**
     * Evaluates the given object as a String and trims it if the trim flag is
     * true.
     * 
     * @param value
     *            an object to interpret as a String
     * @return the String value implied by the given object as returned by the
     *         toString() method, or "null" if the object is null.
     */
    public static String stringValue(Object value, boolean trim) {
        String result;

        if (value == null) {
            result = NULL_STRING;
        } else {
            result = value.toString();
            if (trim) {
                result = result.trim();
            }
        }
        return result;
    }

    /**
     * Evaluates the given object as a String.
     * 
     * @param value
     *            an object to interpret as a String
     * @return the String value implied by the given object as returned by the
     *         toString() method, or "null" if the object is null.
     */
    public static String stringValue(Object value) {
        return stringValue(value, false);
    }
}
