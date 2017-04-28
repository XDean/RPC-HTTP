package xdean.rpc.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import xdean.rpc.api.InnerSerializable;

public enum ReflectUtil {
	;

	public static Class<?> getClass(String className) {
		Class<?> clz = null;
		try {
			clz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			try {
				ClassLoader.getSystemClassLoader().loadClass(className);
				clz = Class.forName(className);
			} catch (ClassNotFoundException e1) {
				throw new Error("TO CHECK YOUR CODE, THIS ERROR SHOULD NEVER OCCUR!", e1);
			}
		}
		return clz;
	}

	public static Method getMethod(Class<?> clz, String methodSignature) {
		return Stream.of(clz, Object.class)
				.map(Class::getMethods)
				.flatMap(Stream::of)
				.filter(m -> m.toString().equals(methodSignature))
				.findAny()
				.orElseThrow(() -> new NoSuchElementException(
						String.format("There is no method like %s in Class[%s]", methodSignature, clz.getName())));
	}

	public static Object parseObject(String className, String objectValue) {
		switch (className) {
		case "int":
		case "java.lang.Integer":
			return Integer.valueOf(objectValue);
		case "short":
		case "java.lang.Short":
			return Short.valueOf(objectValue);
		case "long":
		case "java.lang.Long":
			return Long.valueOf(objectValue);
		case "double":
		case "java.lang.Double":
			return Double.valueOf(objectValue);
		case "float":
		case "java.lang.Float":
			return Float.valueOf(objectValue);
		case "boolean":
		case "java.lang.Boolean":
			if (objectValue.toLowerCase().equals("true"))
				return Boolean.TRUE;
			else if (objectValue.toLowerCase().equals("false"))
				return Boolean.FALSE;
			else
				throw new IllegalArgumentException(
						String.format("The String %s cannot parse as boolean.", objectValue));
		case "char":
		case "java.lang.Character":
			if (objectValue.length() == 1)
				return new Character(objectValue.charAt(0));
			else
				throw new IllegalArgumentException(String.format("The String %s cannot parse as char.", objectValue));
		case "byte":
		case "java.lang.Byte":
			return Byte.valueOf(objectValue);
		case "java.lang.String":
			return objectValue;
		case "void":
			return Void.TYPE;
		default:
			try {
				Class<?> clz = getClass(className);
				if (clz.isEnum()) {
					return getEnumValue(clz, objectValue);
				} else if (isImplementOfInterface(clz, InnerSerializable.class)) {

					return ((InnerSerializable) clz.newInstance()).deserialize(objectValue);
				} else if (isImplementOfInterface(clz, Serializable.class)) {
					return CommonUtil.deserialize(objectValue);
				} else {
					return JSONUtil.getObjectFromJSON(objectValue, getClass(className));
				}
			} catch (Exception e) {
				// XXX: how to handle this exception
				System.out.println(objectValue);
				System.out.println(className);
				e.printStackTrace();
				throw new RuntimeException(className + ", " + objectValue);
			}
		}
	}

	public static <T> Object getEnumValue(Class<T> enumClz, String objectValue) {
		T[] enums = enumClz.getEnumConstants();
		for (int i = 0; i < enums.length; i++)
			if (enums[i].toString().equals(objectValue))
				return enums[i];
		return null;
	}

	/**
	 * {@code Class#getInterfaces()} and {@code Class#getSuperclass()} can just
	 * find declared interfaces. This method is to check all of the class or
	 * interface's super interface
	 * 
	 * @param clz
	 * @param inter
	 * @return
	 */
	public static boolean isImplementOfInterface(Class<?> clz, Class<?> inter) {
		if (clz == null || inter == null)
			return false;
		if (clz.equals(inter))
			return true;
		if (clz.equals(Object.class))
			return false;
		if (isImplementOfInterface(clz.getSuperclass(), inter))
			return true;
		for (Class<?> i : clz.getInterfaces()) {
			if (isImplementOfInterface(i, inter))
				return true;
		}
		return false;
	}
}
