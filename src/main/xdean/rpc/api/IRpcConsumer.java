package xdean.rpc.api;

import java.io.IOException;

public interface IRpcConsumer {
	
	public <T> T getRemoteObject(Class<T> interfaceClass, String objectName) throws IOException;
	
}
