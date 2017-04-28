package xdean.rpc.main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import xdean.rpc.api.IRpcRegister;
import xdean.rpc.base.Constants;
import xdean.rpc.util.AssertUtil;
import xdean.rpc.util.CommonUtil;
import xdean.rpc.util.HttpUtil;
import xdean.rpc.util.JSONUtil;
import xdean.rpc.util.ReflectUtil;
import xdean.rpc.util.log.Logger;

public class HttpRpcRegister implements IRpcRegister, Constants {

	private static Map<String, HttpRpcRegister> map = new HashMap<>();

	public static HttpRpcRegister getInstance() {
		return new HttpRpcRegister();
	}

	public static HttpRpcRegister getInstance(String nameSpace) {
		HttpRpcRegister hrr = map.get(nameSpace);
		if (hrr != null)
			return hrr;
		hrr = new HttpRpcRegister();
		map.put(nameSpace, hrr);
		return hrr;
	}

	private Map<String, Object> objectMap = new ConcurrentHashMap<>();

	// this.registerRemoteObject("testString", new String("Hello World"));

	private HttpRpcRegister() {
	}

	@Override
	public <T> boolean registerRemoteObject(String objectName, T t) {
		if (objectMap.containsKey(objectName))
			return false;
		objectMap.put(objectName, t);
		return true;
	}

	@Override
	public <T> boolean unregisterRemoteObject(String objectName, T t) {

		return objectMap.remove(objectName, t);
	}

	public String handleHttp(String request) {
		try {
			Map<KEY, String> paramMap = HttpUtil.getEnumMapFromJSON(request, KEY.class);
			switch (REQUEST_TYPE.valueOf(paramMap.get(KEY.REQUEST_TYPE))) {
			case REQUEST_OBJECT:
				return requestObject(paramMap);
			case INVOKE_METHOD:
				return invokeMethod(paramMap);
			}
		} catch (Exception e) {
			Logger.d.print(this, "", e);
			Map<Object, Object> map = new HashMap<>();
			try {
				map.put(KEY.THROWABLE, CommonUtil.serialize(e));
			} catch (IOException e1) {
				throw new Error(e1);
			}
			return JSONUtil.mapToString(map);
		}
		return null;
	}

	private String requestObject(Map<KEY, String> paramMap) {
		AssertUtil.assertContainsAll(paramMap, KEY.OBJECT_CLASS, KEY.OBJECT_NAME);
		Object object = getObjectByName(paramMap.get(KEY.OBJECT_NAME));
		AssertUtil.assertInstanceOf(object, paramMap.get(KEY.OBJECT_CLASS), true);
		return "{}";
	}

//	private static final String TOSTRING_SIGN;
//	private static final Method TOSTRING_METHOD;
//	static {
//		try {
//			TOSTRING_METHOD = Object.class.getMethod("toString", new Class<?>[] {});
//			TOSTRING_SIGN = TOSTRING_METHOD.toString();
//		} catch (Exception e) {
//			throw new Error(e);
//		}
//	}

	private String invokeMethod(Map<KEY, String> paramMap) throws IOException {
		AssertUtil.assertContainsAll(paramMap, KEY.OBJECT_CLASS, KEY.OBJECT_NAME, KEY.METHOD_SIGN, KEY.PARAMS);

		Object targetObject = getObjectByName(paramMap.get(KEY.OBJECT_NAME));

		String className = paramMap.get(KEY.OBJECT_CLASS);
		AssertUtil.assertInstanceOf(targetObject, className, true);

		Method method;
		String methodSignature = paramMap.get(KEY.METHOD_SIGN);
//		if (methodSignature.equals(TOSTRING_SIGN))
//			method = TOSTRING_METHOD;
//		else
			method = ReflectUtil.getMethod(ReflectUtil.getClass(className), methodSignature);

		List<Object> paramObjects = new ArrayList<>();
		if (method.getParameterCount() > 0) {
			List<String> params = JSONUtil.stringToList(paramMap.get(KEY.PARAMS));
			params.stream().forEach(st -> {
				Map<KEY, String> map = HttpUtil.getEnumMapFromJSON(st, KEY.class);
				AssertUtil.assertContainsAll(map, KEY.OBJECT_CLASS, KEY.OBJECT_VALUE);
				paramObjects.add(ReflectUtil.parseObject(map.get(KEY.OBJECT_CLASS), map.get(KEY.OBJECT_VALUE)));
			});
		}
		Object result = null;
		Throwable th = null;
		try {
			result = method.invoke(targetObject, paramObjects.toArray());
		} catch (InvocationTargetException e) {
			// Class<?>[] exceptionTypes = method.getExceptionTypes();
			// boolean shouldThrow =
			// Arrays.asList(exceptionTypes).contains(e.getCause().getClass());
			// if(shouldThrow)
			// th = e.getCause();
			th = e.getCause();
		} catch (Exception e) {
			throw new IOException(e);
		}
		return getResponseByResult(result, th);
	}

	private Object getObjectByName(String objectName) {
		Object object = objectMap.get(objectName);
		Objects.requireNonNull(object, String.format("Object [%s] not found!", objectName));
		return object;
	}

	private String getResponseByResult(Object result, Throwable th) throws IOException {
		Map<Object, Object> map = new HashMap<>();
		map.put(KEY.OBJECT_VALUE, CommonUtil.serializeIfPossiable(result));
		if (th != null)
			map.put(KEY.THROWABLE, CommonUtil.serialize(th));
		return JSONUtil.mapToString(map);
	}
}
