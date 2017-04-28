package xdean.rpc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import xdean.rpc.base.Configs;

public enum HttpUtil {
	;

	// public static void setResponseStateCodeOnly(HttpServletResponse response,
	// STATE state) throws IOException {
	// response.setStatus(HttpServletResponse.SC_OK);
	// PrintWriter writer = response.getWriter();
	// writer.print(getSingleStateCodeJSON(state));
	// writer.flush();
	// }

//	public static void setResponseSuccess(HttpServletResponse response) {
//		response.setStatus(HttpServletResponse.SC_OK);
//	}
//
//	public static void setResponseByMap(HttpServletResponse response, Map<?, ?> map) throws IOException {
//		setResponseByString(response, JSONUtil.mapToString(map));
//	}
//	
//	public static void setResponseByString(HttpServletResponse response, String st) throws IOException{
//		response.setStatus(HttpServletResponse.SC_OK);
//		OutputStream output = response.getOutputStream();
//		output.write(st.getBytes(Configs.CHARSET));
//		output.flush();
//	}

//	public static void setResponseByException(HttpServletResponse response, Exception e) throws IOException {
//		response.setStatus(201);
//		PrintWriter writer = response.getWriter();
//		writer.println(CommonUtil.serialize(e));
//		writer.flush();
//	}
	
	// public static String getSingleStateCodeJSON(STATE state){
	// return JSONUtil.getSingleJson(Constants.KEY.RESPONSE_STATE.toString(),
	// state);
	// }

	public static String getStringFromStream(InputStream sourceInput) throws UnsupportedEncodingException {
		return new String(getByteArrayFromStream(sourceInput, Integer.MAX_VALUE), Configs.CHARSET);
	}

	private static byte[] getByteArrayFromStream(InputStream sourceInput, int maxLen) {
		if (sourceInput != null) {
			try {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				int hasRead = 0;
				int b;
				while ((b = sourceInput.read()) != -1) {
					output.write(b);
					hasRead++;
					if (hasRead == maxLen)
						break;
				}
				output.close();
				return output.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

//	public static Map<String, String> getMapFromRequestInJSON(HttpServletRequest request) throws IOException {
//		return JSONUtil.stringToMap(HttpUtil.getStringFromStream(request.getInputStream()));
//	}
//
//	public static <T extends Enum<T>> Map<T, String> getEnumMapFromJSON(HttpServletRequest request, Class<T> clz)
//			throws UnsupportedEncodingException, IOException {
//		return getEnumMapFromJSON(HttpUtil.getStringFromStream(request.getInputStream()), clz);
//	}

	public static <T extends Enum<T>> Map<T, String> getEnumMapFromJSON(String json, Class<T> clz) {
		return CommonUtil.convertMapToEnumMap(JSONUtil.stringToMap(json), clz);
	}
	
	public static String getFormDataFromMap(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			sb.append(key);
			sb.append('=');
			sb.append(map.get(key));
			sb.append('&');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static InputStream doPost(String httpAddress, byte[] data, Map<String, List<String>> headers)
			throws IOException {
		URL url = new URL(httpAddress);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		connection.setReadTimeout(Configs.HTTP_TIMEOUT_MILLIS);
		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(data);
		outputStream.flush();
		if (headers != null)
			headers.putAll(connection.getHeaderFields());
		return connection.getInputStream();
	}
	
	public static String doPost(String httpAddress, String data, Map<String, List<String>> headers)
			throws IOException {
		return getStringFromStream(doPost(httpAddress, data.getBytes(Configs.CHARSET), headers));
	}
	
	public static Map<String, String> doPostReturnMap(String httpAddress, byte[] data,
			Map<String, List<String>> headers) throws IOException {
		return JSONUtil.stringToMap(getStringFromStream(doPost(httpAddress, data, headers)));
	}
	
	public static Map<String, String> doPostReturnMap(String httpAddress, String data,
			Map<String, List<String>> headers) throws IOException {
		return doPostReturnMap(httpAddress, data.getBytes(Configs.CHARSET), headers);
	}
}
