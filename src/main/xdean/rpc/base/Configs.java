package xdean.rpc.base;

import xdean.rpc.util.log.LoggerManager.LogPolicy;

public enum Configs {
	;

	public static final boolean DEBUG = false;
	
	public static final String CHARSET = "UTF-8";
	
	public static final int HTTP_TIMEOUT_MILLIS = 5000;
	
	public static final LogPolicy LOG_POLICY = LogPolicy.POLICY_EVERY_DAY;

	public static final int LOG_MAX_SIZE_BYTE = 10 * 1024 * 1024;
}
