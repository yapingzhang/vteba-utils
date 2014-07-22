package com.vteba.utils.serialize;

import static com.esotericsoftware.kryo.util.Util.className;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;

import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.util.Util;
import com.esotericsoftware.reflectasm.ConstructorAccess;

/**
 * 反射的实例化有问题，内部对象也是null。会出现空指针异常。重载他
 * @author yinlei 
 * @since 2014-7-22
 */
class Kryo extends com.esotericsoftware.kryo.Kryo {

    /** Returns a new instantiator for creating new instances of the specified type. By default, an instantiator is returned that
     * uses reflection if the class has a zero argument constructor, an exception is thrown. If a
     * {@link #setInstantiatorStrategy(InstantiatorStrategy) strategy} is set, it will be used instead of throwing an exception. */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ObjectInstantiator<?> newInstantiator (final Class type) {
        if (!Util.isAndroid) {
            // Use ReflectASM if the class is not a non-static member class.
            Class enclosingType = type.getEnclosingClass();
            boolean isNonStaticMemberClass = enclosingType != null && type.isMemberClass()
                && !Modifier.isStatic(type.getModifiers());
            if (!isNonStaticMemberClass) {
                try {
                    final ConstructorAccess access = ConstructorAccess.get(type);
                    return new ObjectInstantiator() {
                        public Object newInstance () {
                            try {
                                return access.newInstance();
                            } catch (Exception ex) {
                                throw new KryoException("Error constructing instance of class: " + className(type), ex);
                            }
                        }
                    };
                } catch (Exception ignored) {
                }
            }
        }
        // Reflection.
        try {
            Constructor ctor;
            try {
                ctor = type.getConstructor((Class[])null);
            } catch (Exception ex) {
                ctor = type.getDeclaredConstructor((Class[])null);
                ctor.setAccessible(true);
            }
            final Constructor constructor = ctor;
            return new ObjectInstantiator() {
                public Object newInstance () {
                    try {
                        return constructor.newInstance();
                    } catch (Exception ex) {
                        throw new KryoException("Error constructing instance of class: " + className(type), ex);
                    }
                }
            };
        } catch (Exception ignored) {
        }
        
        if (getInstantiatorStrategy() == null) {
            if (type.isMemberClass() && !Modifier.isStatic(type.getModifiers())) {
                throw new KryoException("Class cannot be created (non-static member class): " + className(type));
            } else {
                throw new KryoException("Class cannot be created (missing no-arg constructor): " + className(type));
            }
        }
        // InstantiatorStrategy.
        return getInstantiatorStrategy().newInstantiatorOf(type);
    }
    
}
