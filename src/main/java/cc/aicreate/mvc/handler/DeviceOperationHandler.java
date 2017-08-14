package cc.aicreate.mvc.handler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ethercat device control class
 * 
 * @since 2017-07-12日 PM 8:04:23
 * @version $Id$
 * @author JiangJibo
 *
 */
@Component
public class DeviceOperationHandler {

	static {
		System.loadLibrary("shared2");
	}

	private static String info;

	final static Logger LOGGER = LoggerFactory.getLogger(DeviceOperationHandler.class);

	private static final long INVOKE_RESULT_METHOD_INTERVAL = 500;

	private static final BlockingQueue<Object> queue = new LinkedBlockingDeque<Object>();

	private final Timer timer = new Timer();
	String result;
	long usedTime;

	/**
	 * this method takes an instance of Bean as parameter and changes the value of its members
	 */
	public native void modifyMsg();

	/**
	 * 
	 */
	public native void doPreparing();

	/**
	 * 
	 */
	public native void home();

	/**
	 * @return
	 */
	public native void dataRecord();

	/**
	 * @param recordName
	 * @return
	 */
	public native void stopRecord(String recordName);

	/**
	 * @param recordName
	 * @return
	 */
	public native void reshow(String recordName);

	/**
	 * @return
	 */
	public native String getReqult();

	/**
	 * @param timeout
	 * @param clazz
	 * @return
	 * @throws InterruptedException
	 * @throws TimeoutException
	 */
	public <T> T getResult(long timeout, Class<T> clazz) throws TimeoutException, InterruptedException {
		usedTime = 0;
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				result = getReqult();
				System.out.println(result);
				if (checkResult(result) || usedTime >= timeout) {
					queue.add(result);
					timer.cancel();
				}
				usedTime = usedTime + INVOKE_RESULT_METHOD_INTERVAL;
			}

		};
		timer.schedule(task, 0, INVOKE_RESULT_METHOD_INTERVAL);
		return parseResult(queue.poll(timeout, TimeUnit.MILLISECONDS), clazz);
	}

	/**
	 * @param result
	 * @return
	 */
	private boolean checkResult(String result) {
		// TODO dai wanshan
		return true;
	}

	/**
	 * @param result
	 * @param clazz
	 * @return
	 * @throws TimeoutException
	 */
	@SuppressWarnings({ "unchecked" })
	private <T> T parseResult(Object result, Class<T> clazz) throws TimeoutException {
		if (result == null) {
			throw new TimeoutException("Time Out");
		}
		if (result.getClass().equals(clazz)) {
			return (T) result;
		} else {
			throw new IllegalArgumentException("Illegal Result Type");
		}
	}

	/**
	 * @param msg
	 */
	public static void info(String msg) {
		info = msg;
		System.out.println(info);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(msg);
		}
	}

	/**
	 * @param msg
	 */
	public static void debug(String msg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(msg);
		}
	}

	/**
	 * @param msg
	 */
	public static void error(String msg) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(msg);
		}
	}

}
