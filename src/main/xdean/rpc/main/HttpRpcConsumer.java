package xdean.rpc.main;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xdean.rpc.api.IRpcConsumer;
import xdean.rpc.base.Constants;
import xdean.rpc.util.AssertUtil;
import xdean.rpc.util.CommonUtil;
import xdean.rpc.util.HttpUtil;
import xdean.rpc.util.JSONUtil;
import xdean.rpc.util.ReflectUtil;

public class HttpRpcConsumer implements IRpcConsumer, Constants {

	private final String httpAddress;

	public HttpRpcConsumer(String httpAddress) {
		this.httpAddress = httpAddress;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getRemoteObject(final Class<T> interfaceClass, final String objectName) throws IOException {
		AssertUtil.assertTrue(interfaceClass.isInterface(),
				new IllegalArgumentException(String.format("Class %s is not an interface!", interfaceClass)));
		Map<KEY, Object> map = new HashMap<>();
		map.put(KEY.REQUEST_TYPE, REQUEST_TYPE.REQUEST_OBJECT.toString());
		map.put(KEY.OBJECT_CLASS, interfaceClass.getName());
		map.put(KEY.OBJECT_NAME, objectName);
		String data = JSONUtil.mapToString(map);
		try {
			// Map<String, List<String>> headers = new HashMap<>();
			Map<KEY, String> resultMap = CommonUtil
					.convertMapToEnumMap(HttpUtil.doPostReturnMap(httpAddress, data, null), KEY.class);
			if (resultMap.containsKey(KEY.THROWABLE)) {
				Throwable th = CommonUtil.deserialize(resultMap.get(KEY.THROWABLE));
				throw th;
			}
			// String body = HttpUtil.doPost(httpAddress, data, headers);
			// String response = headers.get(null).get(0);
			// if (response.contains("201")) {
			// Exception e = CommonUtil.deserialize(body);
			// throw e;
			// } else if (response.contains("200")) {
			return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass },
					new RpcHandler(interfaceClass, objectName));
			// } else {
			// throw new Error("SERVER RETURN AN UNRECOGNIZED RESPONSE!");
			// }
		} catch (IOException e) {
			throw e;
		} catch (Throwable e) {
			throw new IOException(e);
		}
	}

	private class RpcHandler implements InvocationHandler {

		private final String objectName;
		private final Class<?> clz;

		public RpcHandler(Class<?> clz, String objectName) {
			this.objectName = objectName;
			this.clz = clz;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Map<KEY, Object> map = new HashMap<>();
			map.put(KEY.REQUEST_TYPE, REQUEST_TYPE.INVOKE_METHOD.toString());
			map.put(KEY.OBJECT_CLASS, clz.getName());
			map.put(KEY.OBJECT_NAME, objectName);
			map.put(KEY.METHOD_SIGN, method.toString());
			{
				List<Map<KEY, Object>> list = new ArrayList<>();
				// Class<?>[] parameterClzs = method.getParameterTypes();
				if (args != null) {
					for (int i = 0; i < args.length; i++)
						list.add(getParamMap(args[i].getClass(), args[i]));
				}
				map.put(KEY.PARAMS, list);
			}
			String data = JSONUtil.mapToString(map);
			Map<KEY, String> resultMap = CommonUtil
					.convertMapToEnumMap(HttpUtil.doPostReturnMap(httpAddress, data, null), KEY.class);
			if (resultMap.containsKey(KEY.THROWABLE)) {
				Throwable th = CommonUtil.deserialize(resultMap.get(KEY.THROWABLE));
				throw th;
			}
			return ReflectUtil.parseObject(method.getReturnType().getName(), resultMap.get(KEY.OBJECT_VALUE));
		}

		private <T> Map<KEY, Object> getParamMap(Class<T> clz, Object value) {
			Map<KEY, Object> map = new HashMap<>();
			map.put(KEY.OBJECT_CLASS, clz.getName());
			map.put(KEY.OBJECT_VALUE, CommonUtil.serializeIfPossiable(value));
			return map;
		}
	}
}
