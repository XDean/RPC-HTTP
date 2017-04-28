package xdean.rpc.api;

import java.io.IOException;

/**
 * NOTICE: Every implement of this interface must have a no parameter constructor.
 * 
 * @author XDean
 * @date 2016.9.21
 *
 */
public interface InnerSerializable {
	
	String serialize() throws IOException;
	
	Object deserialize(String st) throws IOException;
	
}
