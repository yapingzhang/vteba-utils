package com.vteba.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;

import com.vteba.utils.reflection.ReflectUtils;

/**
 * Utility for detecting and accessing JBoss VFS in the classpath.
 *
 * <p>As of Spring 4.0, this class supports VFS 3.x on JBoss AS 6+ (package
 * {@code org.jboss.vfs}) and is in particular compatible with JBoss AS 7 and
 * WildFly 8.
 *
 * <p>Thanks go to Marius Bogoevici for the initial patch.
 * <b>Note:</b> This is an internal class and should not be used outside the framework.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 3.0.3
 */
public abstract class VfsUtils {

	private static final String VFS3_PKG = "org.jboss.vfs.";
	private static final String VFS_NAME = "VFS";

	private static Method VFS_METHOD_GET_ROOT_URL = null;
	private static Method VFS_METHOD_GET_ROOT_URI = null;

	private static Method VIRTUAL_FILE_METHOD_EXISTS = null;
	private static Method VIRTUAL_FILE_METHOD_GET_INPUT_STREAM;
	private static Method VIRTUAL_FILE_METHOD_GET_SIZE;
	private static Method VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED;
	private static Method VIRTUAL_FILE_METHOD_TO_URL;
	private static Method VIRTUAL_FILE_METHOD_TO_URI;
	private static Method VIRTUAL_FILE_METHOD_GET_NAME;
	private static Method VIRTUAL_FILE_METHOD_GET_PATH_NAME;
	private static Method VIRTUAL_FILE_METHOD_GET_CHILD;

	protected static Class<?> VIRTUAL_FILE_VISITOR_INTERFACE;
	protected static Method VIRTUAL_FILE_METHOD_VISIT;

	private static Field VISITOR_ATTRIBUTES_FIELD_RECURSE = null;
	private static Method GET_PHYSICAL_FILE = null;

	static {
		ClassLoader loader = VfsUtils.class.getClassLoader();
		try {
			Class<?> vfsClass = loader.loadClass(VFS3_PKG + VFS_NAME);
			VFS_METHOD_GET_ROOT_URL = ReflectUtils.findMethod(vfsClass, "getChild", URL.class);
			VFS_METHOD_GET_ROOT_URI = ReflectUtils.findMethod(vfsClass, "getChild", URI.class);

			Class<?> virtualFile = loader.loadClass(VFS3_PKG + "VirtualFile");
			VIRTUAL_FILE_METHOD_EXISTS = ReflectUtils.findMethod(virtualFile, "exists");
			VIRTUAL_FILE_METHOD_GET_INPUT_STREAM = ReflectUtils.findMethod(virtualFile, "openStream");
			VIRTUAL_FILE_METHOD_GET_SIZE = ReflectUtils.findMethod(virtualFile, "getSize");
			VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED = ReflectUtils.findMethod(virtualFile, "getLastModified");
			VIRTUAL_FILE_METHOD_TO_URI = ReflectUtils.findMethod(virtualFile, "toURI");
			VIRTUAL_FILE_METHOD_TO_URL = ReflectUtils.findMethod(virtualFile, "toURL");
			VIRTUAL_FILE_METHOD_GET_NAME = ReflectUtils.findMethod(virtualFile, "getName");
			VIRTUAL_FILE_METHOD_GET_PATH_NAME = ReflectUtils.findMethod(virtualFile, "getPathName");
			GET_PHYSICAL_FILE = ReflectUtils.findMethod(virtualFile, "getPhysicalFile");
			VIRTUAL_FILE_METHOD_GET_CHILD = ReflectUtils.findMethod(virtualFile, "getChild", String.class);

			VIRTUAL_FILE_VISITOR_INTERFACE = loader.loadClass(VFS3_PKG + "VirtualFileVisitor");
			VIRTUAL_FILE_METHOD_VISIT = ReflectUtils.findMethod(virtualFile, "visit", VIRTUAL_FILE_VISITOR_INTERFACE);

			Class<?> visitorAttributesClass = loader.loadClass(VFS3_PKG + "VisitorAttributes");
			VISITOR_ATTRIBUTES_FIELD_RECURSE = ReflectUtils.findField(visitorAttributesClass, "RECURSE");
		}
		catch (ClassNotFoundException ex) {
			throw new IllegalStateException("Could not detect JBoss VFS infrastructure", ex);
		}
	}

	protected static Object invokeVfsMethod(Method method, Object target, Object... args) throws IOException {
		try {
			return method.invoke(target, args);
		}
		catch (InvocationTargetException ex) {
			Throwable targetEx = ex.getTargetException();
			if (targetEx instanceof IOException) {
				throw (IOException) targetEx;
			}
			ReflectUtils.handleInvocationTargetException(ex);
		}
		catch (Exception ex) {
			ReflectUtils.handleReflectionException(ex);
		}

		throw new IllegalStateException("Invalid code path reached");
	}

	static boolean exists(Object vfsResource) {
		try {
			return (Boolean) invokeVfsMethod(VIRTUAL_FILE_METHOD_EXISTS, vfsResource);
		}
		catch (IOException ex) {
			return false;
		}
	}

	static boolean isReadable(Object vfsResource) {
		try {
			return ((Long) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource) > 0);
		}
		catch (IOException ex) {
			return false;
		}
	}

	static long getSize(Object vfsResource) throws IOException {
		return (Long) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_SIZE, vfsResource);
	}

	static long getLastModified(Object vfsResource) throws IOException {
		return (Long) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_LAST_MODIFIED, vfsResource);
	}

	static InputStream getInputStream(Object vfsResource) throws IOException {
		return (InputStream) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_INPUT_STREAM, vfsResource);
	}

	static URL getURL(Object vfsResource) throws IOException {
		return (URL) invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URL, vfsResource);
	}

	static URI getURI(Object vfsResource) throws IOException {
		return (URI) invokeVfsMethod(VIRTUAL_FILE_METHOD_TO_URI, vfsResource);
	}

	static String getName(Object vfsResource) {
		try {
			return (String) invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_NAME, vfsResource);
		}
		catch (IOException ex) {
			throw new IllegalStateException("Cannot get resource name", ex);
		}
	}

	static Object getRelative(URL url) throws IOException {
		return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, url);
	}

	static Object getChild(Object vfsResource, String path) throws IOException {
		return invokeVfsMethod(VIRTUAL_FILE_METHOD_GET_CHILD, vfsResource, path);
	}

	static File getFile(Object vfsResource) throws IOException {
		return (File) invokeVfsMethod(GET_PHYSICAL_FILE, vfsResource);
	}

	static Object getRoot(URI url) throws IOException {
		return invokeVfsMethod(VFS_METHOD_GET_ROOT_URI, null, url);
	}

	// protected methods used by the support sub-package

	protected static Object getRoot(URL url) throws IOException {
		return invokeVfsMethod(VFS_METHOD_GET_ROOT_URL, null, url);
	}

	protected static Object doGetVisitorAttribute() {
		return ReflectUtils.getField(VISITOR_ATTRIBUTES_FIELD_RECURSE, null);
	}

	protected static String doGetPath(Object resource) {
		return (String) ReflectUtils.invokeMethod(VIRTUAL_FILE_METHOD_GET_PATH_NAME, resource);
	}

}
