/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vteba.io;

import com.vteba.utils.common.ResourceUtils;

/**
 * 加载资源的策略接口。 (e.. class path or file system resources)
 * 
 * <p>{@link DefaultResourceLoader} 是一个可以用于ApplicationContext外的独立实现。
 *
 * @author Juergen Hoeller
 * @author 尹雷
 * @since 10.03.2004
 * @see Resource
 */
public interface ResourceLoader {

	/** 从类路径加载资源 class path: "classpath:" 伪 URL 前缀 */
	String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;


	/**
	 * 返回指定资源的资源引用。资源引用应该总是一个可重用的资源描述符。
	 * 并且允许执行{@link Resource#getInputStream()}这个方法。
	 * <p><ul>
	 * <li>必须支持全限定的 URLs，例如： "file:C:/test.dat".
	 * <li>必须支持classpath 伪-URLs，例如 "classpath:test.dat".
	 * <li>应该支持相对文件路径，例如 "WEB-INF/test.dat".
	 * </ul>
	 * (具体取决于实现)
	 * <p>资源引用并不保证，一定是一个存在的资源，你需要去
	 * 调用{@link Resource#exists}检查资源是否存在。
	 * @param location 资源路径
	 * @return 和底层相关的资源引用（句柄）
	 * @see #CLASSPATH_URL_PREFIX
	 * @see com.vteba.io.Resource#exists
	 * @see com.vteba.io.Resource#getInputStream
	 */
	Resource getResource(String location);

	/**
	 * 暴漏ResourceLoader所使用的ClassLoader
	 * 
	 * <p>客户端可以以统一的方式访问ClassLoader，而不是依赖于当前线程绑定的ClassLoader。
	 * 
	 * @return the ClassLoader 除非SystemClassLoader不可访问，否则不会是null
	 * @see com.vteba.io.utils.ClassUtils#getDefaultClassLoader()
	 */
	ClassLoader getClassLoader();

}
