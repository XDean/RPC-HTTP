package xdean.rpc.test.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import xdean.rpc.main.HttpRpcRegister;
import xdean.rpc.test.model.TestImpl;

@WebListener
public class ContextListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		HttpRpcRegister.getInstance("test").registerRemoteObject("testString", new String("H E L L O"));
		HttpRpcRegister.getInstance("test").registerRemoteObject("testObject", new TestImpl());
	}

}
