package xdean.rpc.util.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import xdean.rpc.base.Configs;

/**
 * Created by aa on 2016/4/2.
 */
public enum Logger {

	i {
		@Override
		public void print(String tag, String msg) {
			if (Configs.DEBUG)
				LoggerManager.INSTANCE.print(String.format("[INFO]  %s: %s", tag, msg));
		}
	},
	d {
		@Override
		public void print(String tag, String msg) {
			if (Configs.DEBUG)
				LoggerManager.INSTANCE.print(String.format("[DEBUG] %s: %s", tag, msg));
		}
	},
	e {
		@Override
		public void print(String tag, String msg) {
			// if (Configs.DEBUG)
			LoggerManager.INSTANCE.print(String.format("[ERROR] %s: %s", tag, msg));
		}
	};
	
	public abstract void print(String tag, String msg);

	public void print(Object o, String msg) {
		print(o.getClass().getSimpleName(), msg);
	}

	public void print(String tag, String msg, Throwable t) {
		print(tag, msg + '\n' + getStackTraceString(t));
	}

	public void print(Object o, String msg, Throwable t) {
		print(o.getClass().getSimpleName(), msg, t);
	}
	
	public void print(HttpServletRequest request, Object o, String msg) {
		print(request, o.getClass().getSimpleName(), msg);
	}

	public void print(HttpServletRequest request, String tag, String msg) {
		print(request.getRemoteAddr() + " " + tag, msg);
	}

	public void print(HttpServletRequest request, Object o, String msg, Throwable t) {
		print(request, o.getClass().getSimpleName(), msg + "\n" + getStackTraceString(t));
	}

	public void print(HttpServletRequest request, Object o, Throwable t) {
		print(request, o.getClass().getSimpleName(), getStackTraceString(t));
	}

	private static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}
		Throwable t = tr;
		while (t != null) {
			if (t instanceof UnknownHostException) {
				return "";
			}
			t = t.getCause();
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tr.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}
}
