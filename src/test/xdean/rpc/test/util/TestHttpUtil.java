package xdean.rpc.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import xdean.rpc.base.Configs;
import xdean.rpc.util.JSONUtil;

public enum TestHttpUtil {
	;

	public static int timeout = 100000;

	public static InputStream doSimplePost(String httpAddress, byte[] data, Map<String, List<String>> headers)
			throws IOException {
		URL url = new URL(httpAddress);
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		connection.setReadTimeout(timeout);
		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(data);
		outputStream.flush();
		if (headers != null)
			headers.putAll(connection.getHeaderFields());
		return connection.getInputStream();
	}

	public static Map<String, String> doSimplePostReturnMap(String httpAddress, byte[] data,
			Map<String, List<String>> headers) throws IOException {
		return JSONUtil.stringToMap(getStringFromInputStream(doSimplePost(httpAddress, data, headers)));
	}

	public static Map<String, String> doSimplePostReturnMap(String httpAddress, String data,
			Map<String, List<String>> headers) throws IOException {
		return doSimplePostReturnMap(httpAddress, data.getBytes(), headers);
	}
	
	public static void setResponseSuccess(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_OK);
	}

	public static void setResponseByMap(HttpServletResponse response, Map<?, ?> map) throws IOException {
		setResponseByString(response, JSONUtil.mapToString(map));
	}
	
	public static void setResponseByString(HttpServletResponse response, String st) throws IOException{
		response.setStatus(HttpServletResponse.SC_OK);
		OutputStream output = response.getOutputStream();
		output.write(st.getBytes(Configs.CHARSET));
		output.flush();
	}

	public static String getStringFromInputStream(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(in);
		int i;
		while ((i = reader.read()) != -1) {
			sb.append((char) i);
		}
		return sb.toString();
	}
}
