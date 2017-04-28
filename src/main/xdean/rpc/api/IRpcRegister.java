package xdean.rpc.api;

public interface IRpcRegister {
	
	public <T> boolean registerRemoteObject(String objectName, T t); 
	
	public <T> boolean unregisterRemoteObject(String objectName, T t);
}
