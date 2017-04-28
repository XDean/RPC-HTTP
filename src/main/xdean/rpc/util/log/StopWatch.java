package xdean.rpc.util.log;

import java.util.function.Consumer;

public class StopWatch {

	private long startTime;
	private long lastTime;

	private Consumer<String> out;
	private String message;

	public StopWatch() {
		out = System.out::println;
		message = "Used %d ms";
	}

	public StopWatch(Consumer<String> output) {
		this();
		out = output;
	}

	/**
	 * @param st
	 *            String with a "%d"
	 */
	public void setMessage(String st) {
		message = st;
	}

	public void start() {
		startTime = System.nanoTime();
		lastTime = startTime;
	}

	/**
	 * 
	 * @return Milliseconds since last down or stop
	 */
	public long down() {
		long t = System.nanoTime();
		long interval = (t - lastTime) / 1000000;
		out.accept(String.format(message, interval));
		lastTime = t;
		return interval;
	}

	/**
	 * 
	 * @return Milliseconds since start
	 */
	public long stop() {
		long t = System.nanoTime();
		long interval = (t - startTime) / 1000000;
		out.accept("Total: " + String.format(message, interval));
		lastTime = t;
		return interval;
	}
}
