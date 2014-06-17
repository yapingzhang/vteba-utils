package com.vteba.lang.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class OpenbossThreadPoolExecutor extends ThreadPoolExecutor {
	private ConcurrentMap<String, Runnable> allRunnableMap = new ConcurrentHashMap<String, Runnable>();
	
	public OpenbossThreadPoolExecutor() {
		super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	}

	public OpenbossThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}

	public OpenbossThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}

	public OpenbossThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	public OpenbossThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	/**
     * Method invoked prior to executing the given Runnable in the
     * given thread.  This method is invoked by thread <tt>t</tt> that
     * will execute task <tt>r</tt>, and may be used to re-initialize
     * ThreadLocals, or to perform logging.
     *
     * <p>This implementation does nothing, but may be customized in
     * subclasses. Note: To properly nest multiple overridings, subclasses
     * should generally invoke <tt>super.beforeExecute</tt> at the end of
     * this method.
     *
     * @param t the thread that will run task r.
     * @param r the task that will be executed.
     */
    public void beforeExecute(Thread t, Runnable r) {
    	
    	allRunnableMap.put(t.getName(), r);
    	super.beforeExecute(t, r);
    }

    /**
     * Method invoked upon completion of execution of the given Runnable.
     * This method is invoked by the thread that executed the task. If
     * non-null, the Throwable is the uncaught <tt>RuntimeException</tt>
     * or <tt>Error</tt> that caused execution to terminate abruptly.
     *
     * <p><b>Note:</b> When actions are enclosed in tasks (such as
     * {@link FutureTask}) either explicitly or via methods such as
     * <tt>submit</tt>, these task objects catch and maintain
     * computational exceptions, and so they do not cause abrupt
     * termination, and the internal exceptions are <em>not</em>
     * passed to this method.
     *
     * <p>This implementation does nothing, but may be customized in
     * subclasses. Note: To properly nest multiple overridings, subclasses
     * should generally invoke <tt>super.afterExecute</tt> at the
     * beginning of this method.
     *
     * @param r the runnable that has completed.
     * @param t the exception that caused termination, or null if
     * execution completed normally.
     */
    public void afterExecute(Runnable r, Throwable t) {
    	super.afterExecute(r, t);
    	
    	if (r instanceof OpenbossRunnable) {
    		((OpenbossRunnable) r).mainHandler();
    	}
    }

    /**
     * Method invoked when the Executor has terminated.  Default
     * implementation does nothing. Note: To properly nest multiple
     * overridings, subclasses should generally invoke
     * <tt>super.terminated</tt> within this method.
     */
    public void terminated() {
    	
    }
	
	/**
	 * ����̳߳ش�С�����̳߳ء�
	 * @param minPoolSize �̳߳���С�̸߳��� 0 <= minPoolSize <= 500
	 * @param maxPoolSize �̳߳�����̸߳��� 10 <= maxPoolSize <= 5000
	 * @return ExecutorService �̳߳�ThreadPoolExecutor
	 */
	public static ExecutorService newCachedThreadPool(int minPoolSize, int maxPoolSize) {
		minPoolSize = checkMinPoolSize(minPoolSize);
		maxPoolSize = checkMaxPoolSize(maxPoolSize);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(minPoolSize, maxPoolSize,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
        
        
        return executor;
    }
	
	public static ExecutorService newCachedThreadPool(int minPoolSize, int maxPoolSize, long keepAliveTime) {
		minPoolSize = checkMinPoolSize(minPoolSize);
		maxPoolSize = checkMaxPoolSize(maxPoolSize);
        return new ThreadPoolExecutor(minPoolSize, maxPoolSize,
                                      keepAliveTime, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }

	private static int checkMaxPoolSize(int maxPoolSize) {
		if (maxPoolSize < 10 ) {
			maxPoolSize = 10;
		} else if (maxPoolSize > 5000) {
			maxPoolSize = 5000;
		}
		return maxPoolSize;
	}

	private static int checkMinPoolSize(int minPoolSize) {
		if (minPoolSize < 0) {
			minPoolSize = 0;
		} else if (minPoolSize > 500) {
			minPoolSize = 500;
		}
		return minPoolSize;
	}
}
