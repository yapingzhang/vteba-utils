package com.vteba.utils.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.vteba.utils.json.Node;

/**
 * 获得多层泛型类型
 * @author 尹雷 
 * @since 2013-7-21
 * @param <T> 要获取的泛型类型
 */
public class TypeRef<T> {

    private final Type type;

    public TypeRef(){
        Type superClass = getClass().getGenericSuperclass();
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }
    
    public static final Type LIST_STRING = new TypeRef<List<String>>() {}.getType();
    public static final Type LIST_NODE = new TypeRef<List<Node>>() {}.getType();
}
