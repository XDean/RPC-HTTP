package xdean.rpc.test.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import xdean.rpc.main.HttpRpcRegister;
import xdean.rpc.test.util.TestHttpUtil;
import xdean.rpc.util.HttpUtil;

@WebServlet("/RPC")
public class HTTPRemoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private HttpRpcRegister httpRpcRegister;

	public HTTPRemoteServlet() {
		super();
		httpRpcRegister = (HttpRpcRegister) HttpRpcRegister.getInstance("test");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String responseString = httpRpcRegister.handleHttp(HttpUtil.getStringFromStream(request.getInputStream()));
		TestHttpUtil.setResponseByString(response, responseString);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
