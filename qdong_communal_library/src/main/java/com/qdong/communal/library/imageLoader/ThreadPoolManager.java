package com.qdong.communal.library.imageLoader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
	// Sets the initial threadpool size to 4
	private static final int POOL_SIZE_PER_CPU = 4;

	// Sets the maximum threadpool size to 8
//	private static final int MAXIMUM_POOL_SIZE = 8;
	private static ThreadPoolExecutor normalThreadPool;

    /**
	 * http请求可能会堵塞，堵塞的任务可能会把线程池给占光，导致其他任务饿死?<br>
	 * 为了解决这个问题，我们把涉及到http请求的任务都放到这个线程池里运行
	 */
	private static ThreadPoolExecutor httpThreadPool;
    // Sets the amount of time an idle thread will wait for a task before
	// terminating
	private static int NUMBER_OF_CPU_CORES = Runtime.getRuntime()
			.availableProcessors();
	private static final int KEEP_ALIVE_TIME = 1;
	private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
	private static boolean started;
	
	public final static int DOWNLOADER_THREAD_SIZE = 3;
	static {
		init();
	}

	private static void init() {
		if (started)
			return;
        BlockingQueue<Runnable> normalWorkQueue = new LinkedBlockingQueue<Runnable>();
		/**
		 * 根据android官方的说明：http://developer.android.com/reference/java/util/concurrent/ThreadPoolExecutor.html <br>
		 * IllegalArgumentException if one of the following holds:<br>
		 * corePoolSize < 0<br>
		 * keepAliveTime < 0<br>
		 * maximumPoolSize <= 0<br>
		 * maximumPoolSize < corePoolSize<br>
		 * 为了避免这种情况，maximumPoolSize的计算必须是基于corePoolSize�?
		 * ***/
		normalThreadPool = new ThreadPoolExecutor(POOL_SIZE_PER_CPU
				* NUMBER_OF_CPU_CORES, (POOL_SIZE_PER_CPU+2)
				* NUMBER_OF_CPU_CORES, KEEP_ALIVE_TIME,
				KEEP_ALIVE_TIME_UNIT, normalWorkQueue);

        BlockingQueue<Runnable> httpWorkQueue = new LinkedBlockingQueue<Runnable>();
		httpThreadPool = new ThreadPoolExecutor(POOL_SIZE_PER_CPU
				* NUMBER_OF_CPU_CORES+DOWNLOADER_THREAD_SIZE, (POOL_SIZE_PER_CPU+2)
				* NUMBER_OF_CPU_CORES+DOWNLOADER_THREAD_SIZE, KEEP_ALIVE_TIME,
				KEEP_ALIVE_TIME_UNIT, httpWorkQueue);
		
		started = true;
	}

	/**
	 * �?��对cpu消�?不多的后台任务，比如查找和排序请求等，用这个方法.<br>
	 * 但是涉及到http请求的，请用executeHttpTask，以避免http请求超时导致整个运用的其它多线程请求被堵塞住
	 * 
	 * @param task
	 */
	public static void executeNormalTask(Runnable task) {
		normalThreadPool.execute(task);
	}
	
	/**
	 * http请求可能会堵塞，堵塞的任务可能会把线程池给占光，导致其他任务饿死<br>
	 * 为了解决这个问题，我们把涉及到http请求的任务都放到这个线程池里运行
	 */
	public static void executeHttpTask(Runnable task){
		httpThreadPool.execute(task);
	}

	/**
	 * 
	 * 获取icon时，新建线程比线程池要快
	 */
	public static void executeIconTask(Runnable task){
		new Thread(task).start();
	}
}
