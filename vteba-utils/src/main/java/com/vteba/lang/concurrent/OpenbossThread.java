package com.vteba.lang.concurrent;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class OpenbossThread extends Thread implements OpenbossRunnable {
	
	private OpenbossRunnable parentRunnable;
	private volatile AtomicBoolean callParent;
	private volatile AtomicBoolean callChild;
	private OpenbossThreadPoolExecutor threadPoolExecutor;
	
	public OpenbossThread (OpenbossRunnable parentRunnable, Runnable runnable, String threadName) {
		super(runnable, threadName);
		this.parentRunnable = parentRunnable;
		callParent = new AtomicBoolean();
		callChild = new AtomicBoolean();
		threadPoolExecutor = new OpenbossThreadPoolExecutor();
	}
	
	public OpenbossThread (Runnable runnable, String threadName) {
		
	}
	
	/**
	 * ִ���̣߳��޷��ؽ��
	 */
	public void execute(Runnable command) {
		threadPoolExecutor.execute(command);
	}
	
	/**
	 * ����future
	 */
	public void submit() {
		
	}
	
	public void test() {
		Thread thread = new Thread("ThreadName");
		thread.getId();
		thread.getName();
		thread.getState();//�߳�״̬
		thread.isInterrupted();//�����߳��Ƿ��жϣ���Ӱ���߳��ж�״̬
		Thread.yield();//��ǰ���߳��ò����������߳�ִ��
		try {
			Thread.sleep(1000);//��ǰ�߳�����1000����
			TimeUnit.MILLISECONDS.sleep(2);//�������ֵ���
		} catch (InterruptedException e) {
			
		}
		try {
			thread.join();//�ȴ��߳�����
		} catch (InterruptedException e) {
			
		}
		thread.isAlive();//�߳��Ƿ���
		try {
			thread.wait();//��ǰ�̵߳ȴ�ֱ�������̵߳���java.lang.Object.notify() or java.lang.Object.notifyAll()
		} catch (InterruptedException e) {
			
		}
		Thread.activeCount();//��ǰ�߳����ڵ��߳��飬��Ծ���߳���
		Thread.interrupted();//���Ե�ǰ�߳��Ƿ��жϣ����̵߳��ж�״̬��Ϊ�ж�
		
		Thread.currentThread();//���ص�ǰ�߳�
	}

	public OpenbossRunnable getParentRunnable() {
		return parentRunnable;
	}

	public void setParentRunnable(OpenbossRunnable parentRunnable) {
		this.parentRunnable = parentRunnable;
	}

	public AtomicBoolean getCallParent() {
		return callParent;
	}

	public void setCallParent(AtomicBoolean callParent) {
		this.callParent = callParent;
	}

	public AtomicBoolean getCallChild() {
		return callChild;
	}

	public void setCallChild(AtomicBoolean callChild) {
		this.callChild = callChild;
	}

	@Override
	public void mainHandler() {
		
	}

	@Override
	public void subHandler() {
		
	}

	/**
	 * ����߳����ȡ�ȴ��������
	 * @param threadName �߳�����
	 * @return ���������
	 */
	public OpenbossThread getThreadByThreadName(String threadName) {
		OpenbossThread thread = null;
		
		return thread;
	}
	
	/**
	 * ����ȥִ�����ύ�����˳����һ������Ĺرգ�����ִ�е����񽫱�ִ���꣬<br>
	 * ���ǲ���������������Ѿ��رգ������û���������á�<br>
	 */
	public void shutdown() {
		threadPoolExecutor.shutdown();
	}

	/**
	 * ����ֹͣ���еĻִ��������ͣ�ȴ�����Ĵ��?�����صȴ�ִ�е������б?<br>
	 * �ڴӴ˷������ص�����������ſգ��Ƴ���Щ���� 
	 * ������֤�ܹ�ֹͣ���ڴ���Ļִ�����񣬵��ǻᾡ�����ԡ�<br>
	 * ��ʵ��ͨ�� Thread.interrupt() ȡ�����������޷���Ӧ�жϵ����������Զ�޷���ֹ��
	 * @return ��δ��ʼִ�е�������б?
	 */
	public List<Runnable> shutdownNow() {
		return threadPoolExecutor.shutdownNow();
	}

	/**
	 * ����ִ�����ѹرգ��򷵻� true������false��
	 * @return �Ƿ�ر�
	 */
	public boolean isShutdown() {
		return threadPoolExecutor.isShutdown();
	}

	/**
	 * ����ִ�г������� shutdown �� shutdownNow ֮��������ֹ����δ��ȫ��ֹ�Ĺ���У��򷵻� true��<br>
	 * �˷������ܶԵ��Ժ����á��ر�֮��ܳ�һ��ʱ��ű��淵�ص� true������ܱ�ʾ�ύ�������Ѿ�<br>
	 * �����Ի�ȡ���жϣ����´�ִ�г����޷���ȷ��ֹ��
	 * @return ���������ֹ����δ��ɣ��򷵻� true
	 */
	public boolean isTerminating() {
		return threadPoolExecutor.isTerminating();
	}

	/**
	 * ���رպ�������������ɣ��򷵻� true��ע�⣬������ȵ��� shutdown �� shutdownNow��<br>
	 * ���� isTerminated ����Ϊ true��
	 * @return ���رպ�������������ɣ��򷵻� true
	 */
	public boolean isTerminated() {
		return threadPoolExecutor.isTerminated();
	}

	/**
	 * ���غ����߳���
	 * @return �����߳���
	 */
	public int getCorePoolSize() {
		return threadPoolExecutor.getCorePoolSize();
	}

	/**
	 * �������������߳���
	 * @return ���������߳���
	 */
	public int getMaximumPoolSize() {
		return threadPoolExecutor.getMaximumPoolSize();
	}

	/**
	 * �����̱߳��ֻ��ʱ�䣬��ʱ����ǳ�����ĳش�С���߳̿�������ֹǰ���ֿ��е�ʱ��ֵ��
	 * @param unit ʱ�䵥λ
	 * @return
	 */
	public long getKeepAliveTime(TimeUnit unit) {
		return threadPoolExecutor.getKeepAliveTime(unit);
	}

	/**
	 * ���ش�ִ�г���ʹ�õ�������С���������еķ�����Ҫ���ڵ��Ժͼ�ء��˶��п������ڻʹ��״̬�С�<br>
	 * ��ȡ������в������Ѽ�����е������ִ�С�
	 * @return �ȴ�����������
	 */
	public BlockingQueue<Runnable> getQueue() {
		return threadPoolExecutor.getQueue();
	}

	
	/**
	 * ��ִ�г�����ڲ��������Ƴ�����������ڣ����Ӷ������δ��ʼ�����䲻�����С� <br>
	 * �˷���������ȡ����һ���֡�������޷��Ƴ��ڷ��õ��ڲ�����֮ǰ�Ѿ�ת��Ϊ����<br>
	 * ��ʽ���������磬ʹ�� submit �����������ܱ�ת��Ϊά�� Future ״̬����ʽ�����ǣ�<br>
	 * �ڴ�����£�purge() �����������Ƴ���Щ�ѱ�ȡ��� Future��
	 * @param task Ҫ�Ƴ������
	 * @return
	 */
	public boolean remove(Runnable task) {
		return threadPoolExecutor.remove(task);
	}

	/**
	 * ���Դӹ��������Ƴ�������ȡ��� Future ���񡣴˷����������洢���ղ�������Թ���û���κ�Ӱ�졣<br>
	 * ȡ������񲻻��ٴ�ִ�У��������ǿ����ڹ����������ۻ�ֱ�� worker �߳����������Ƴ�<Br>
	 * ���ô˷�������ͼ�����Ƴ����ǡ����ǣ������������̵߳ĸ�Ԥ����ô�˷����Ƴ�����ʧ�ܡ�
	 */
	public void purge() {
		threadPoolExecutor.purge();
	}

	/**
	 * ���س��еĵ�ǰ�߳���
	 * @return �߳���
	 */
	public int getPoolSize() {
		return threadPoolExecutor.getPoolSize();
	}

	/**
	 * ��������ִ������Ľ����߳���
	 * @return �߳���
	 */
	public int getActiveCount() {
		return threadPoolExecutor.getActiveCount();
	}

	/**
	 * ������ͬʱλ�ڳ��е�����߳���
	 * @return �߳���
	 */
	public int getLargestPoolSize() {
		return threadPoolExecutor.getLargestPoolSize();
	}
	
	/**
	 * ������ƻ�ִ�еĽ�����������
	 * @return ������
	 */
	public long getTaskCount() {
		return threadPoolExecutor.getTaskCount();
	}

	/**
	 * ���������ִ�еĽ�����������
	 * @return ������
	 */
	public long getCompletedTaskCount() {
		return threadPoolExecutor.getCompletedTaskCount();
	}
	
}
