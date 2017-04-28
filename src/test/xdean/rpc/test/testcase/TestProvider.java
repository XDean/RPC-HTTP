package xdean.rpc.test.testcase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import xdean.rpc.base.Constants.KEY;
import xdean.rpc.base.Constants.REQUEST_TYPE;
import xdean.rpc.test.util.TestHttpUtil;
import xdean.rpc.util.JSONUtil;

public class TestProvider {

	@Before
	public void setUp() throws Exception {
	}

	@SuppressWarnings("unused")
	@Test
	public void testManual() throws IOException, NoSuchMethodException, SecurityException {
		String path = "http://127.0.0.1:8080/RPC-HTTP/RPC";
		Map<KEY, String> map = new HashMap<>();
		map.put(KEY.REQUEST_TYPE, REQUEST_TYPE.INVOKE_METHOD.toString());
		map.put(KEY.OBJECT_NAME, "testString");
		map.put(KEY.OBJECT_CLASS, String.class.getName());
		map.put(KEY.METHOD_SIGN, String.class.getMethod("split", String.class).toString());
		map.put(KEY.PARAMS, "[{\"OBJECT_CLASS\":\"java.lang.String\",\"OBJECT_VALUE\":\" \"}]");
		Map<String, String> response = TestHttpUtil.doSimplePostReturnMap(path, JSONUtil.mapToString(map), null);
	}
}
