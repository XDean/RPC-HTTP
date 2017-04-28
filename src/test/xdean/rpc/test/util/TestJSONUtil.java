//package xdean.rpc.test.util;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.alibaba.fastjson.JSON;
//
//public enum TestJSONUtil {
//	;
//
//	public static Map<String, String> getMapFromJSON(String json) {
//		return TestJSONUtil.stringToMap(json);
//	}
//
//	public static List<String> getListFromJSON(String json) {
//		return TestJSONUtil.stringToList(json);
//	}
//
//	public static <T> T getObjectFromJSON(String json, Class<T> clz) {
//		return JSON.toJavaObject(JSON.parseObject(json), clz);
//	}
//
//	static String getSingleJson(String key, Object value) {
//		JSONObject jo = new JSONObject();
//		jo.put(key, value);
//		return jo.toString();
//	}
//
//	static Map<String, String> stringToMap(String s) {
//		if (s == null)
//			return new HashMap<String, String>();
//		return jsonToMap(new JSONObject(s));
//	}
//
//	static Map<String, String> jsonToMap(JSONObject o) {
//		Map<String, String> map = new HashMap<>();
//		if (o.length() == 0)
//			return map;
//		String[] names = JSONObject.getNames(o);
//		for (String key : names)
//			map.put(key, o.get(key).toString());
//		return map;
//	}
//
//	public static String mapToString(Map<?, ?> map) {
//		return JSONObject.valueToString(map);
//	}
//
//	static List<String> stringToList(String s) {
//		return jsonToList(new JSONArray(s));
//	}
//
//	static List<String> jsonToList(JSONArray array) {
//		List<String> list = new ArrayList<>();
//		for (int i = 0; i < array.length(); i++)
//			list.add(array.get(i).toString());
//		return list;
//	}
//}
