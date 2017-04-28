package xdean.rpc.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import xdean.rpc.base.Configs;
import xdean.rpc.util.CommonUtil;

/**
 * A actually private class, never use it. To use {@link Logger}.
 * <p>
 * And you can set its parameter in {@link Configs}
 * 
 * @author aa
 *
 */
public enum LoggerManager {

	INSTANCE;

	public enum LogPolicy {
		POLICY_EVERY_DAY {
			final DateFormat format = new SimpleDateFormat("yy-MM-dd");
			private long lastMillis;

			@Override
			File getFile() {
				lastMillis = System.currentTimeMillis();
				Date date = new Date(lastMillis);
				String dateString = format.format(date);
				return new File(LOG_ROOT_PATH, dateString + ".log");
			}

			@Override
			boolean checkChangeFile(File logFile) {
				return !CommonUtil.isSameDayOfMillis(lastMillis, System.currentTimeMillis());
			}
		},
		POLICY_EVERY_TIME {
			final DateFormat format = new SimpleDateFormat("yy-MM-dd HHmmssSSS");

			@Override
			File getFile() {
				String date = format.format(new Date());
				return new File(LOG_ROOT_PATH, date + ".log");
			}

			@Override
			boolean checkChangeFile(File logFile) {
				return logFile.length() > Configs.LOG_MAX_SIZE_BYTE;
			}
		};

		abstract File getFile();

		abstract boolean checkChangeFile(File logFile);

		Writer getFileWriter(File file) throws IOException {
			if (file.exists())
				return new BufferedWriter(new FileWriter(file, true));
			else {
				file.getParentFile().mkdirs();
				file.createNewFile();
				return new BufferedWriter(new FileWriter(file));
			}
		}
	}

	private static final long FLUSH_INTERVAL_MILLIS = 1000;

	private static final String DATE_FORMAT = "yy-MM-dd HH:mm:ss.SSS";

	private static final String LOG_ROOT_PATH = new File(
			LoggerManager.class.getClassLoader().getResource("/").getPath()).getParentFile().getParent() + "\\log\\";;

	static {
		File file = new File(LOG_ROOT_PATH);
		if (!file.exists())
			file.mkdirs();
	}

	private DateFormat dateFormat;
	private final LogPolicy policy = Configs.LOG_POLICY;

	private Thread thread;

	private LinkedBlockingQueue<String> msgQueue;

	private LoggerManager() {
		dateFormat = new SimpleDateFormat(DATE_FORMAT);
		msgQueue = new LinkedBlockingQueue<>();
		thread = new Thread(new LoggerTask());
		thread.start();
	}

	private class LoggerTask implements Runnable {
		@Override
		public void run() {
			try {
				while (!Thread.interrupted()) {
					File logFile = policy.getFile();
					Writer writer = null;
					StopWatch watch = new StopWatch(s -> {
					});
					watch.start();
					try {
						while (true) {
							if (watch.stop() > FLUSH_INTERVAL_MILLIS) {
								writer = policy.getFileWriter(logFile);
								while (true) {
									String msg = msgQueue.poll();
									if (msg != null)
										writer.write(String.format("%s %s\n", getDateTime(), msg));
									else
										break;
								}
								writer.flush();
								writer.close();
								watch.start();
								if (policy.checkChangeFile(logFile))
									break;
							}
							Thread.sleep(Math.max(FLUSH_INTERVAL_MILLIS - watch.stop(), 50));
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} finally {
						if (writer != null) {
							try {
								writer.flush();
								writer.close();
							} catch (Exception e) {

							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void print(String msg) {
		msgQueue.add(msg);
	}

	private String getDateTime() {
		return dateFormat.format(new Date());
	}
}
