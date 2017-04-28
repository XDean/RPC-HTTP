package xdean.rpc.base;

public interface Constants {

	public enum KEY {
		REQUEST_TYPE, OBJECT_NAME, OBJECT_CLASS, METHOD_SIGN, PARAMS, OBJECT_VALUE, THROWABLE
//		, RESPONSE_STATE
		;
		
	}

	public enum REQUEST_TYPE {
		REQUEST_OBJECT, INVOKE_METHOD;
	}
	
//	public enum STATE{
//		SUCCESS,
//		FAIL;
//	}
}
