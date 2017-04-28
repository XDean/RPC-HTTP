package xdean.rpc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public enum JSONUtil {
	;
	
	public static <T> T getObjectFromJSON(String json, Class<T> clz) {
		return JSON.toJavaObject((JSON)JSON.parse(json), clz);
	}

	public static String getSingleJson(String key, Object value) {
		JSONObject jo = new JSONObject();
		jo.put(key, value);
		return jo.toString();
	}

	public static Map<String, String> stringToMap(String s) {
		if (s == null)
			return new HashMap<String, String>();
		return jsonToMap(JSON.parseObject(s));
	}

	static Map<String, String> jsonToMap(JSONObject o) {
		Map<String, String> map = new HashMap<>();
		if (o.size() == 0)
			return map;
		o.forEach((key, value) -> map.put(key, value.toString()));
		// for (String key : names)
		// map.put(key, o.get(key).toString());
		return map;
	}

	public static String mapToString(Map<?, ?> map) {
		return JSON.toJSONString(map);
	}

	public static List<String> stringToList(String s) {
		return jsonToList(JSON.parseArray(s));
	}

	static List<String> jsonToList(JSONArray array) {
		List<String> list = new ArrayList<>();
//		for (int i = 0; i < array.size(); i++)
//			list.add(array.get(i).toString());
		array.forEach(e->list.add(e.toString()));
		return list;
	}

	public static String listToString(List<?> list) {
		return JSON.toJSONString(list);
	}
}
