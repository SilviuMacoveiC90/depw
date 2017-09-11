package com.pulse.hawkeye.depw.core.common;

import com.pulse.hawkeye.depw.utils.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TasksPoolExecutor extends ThreadPoolExecutor
{
	public static boolean DEBUG = Log.DEBUG; 
	public static final String TAG = TasksPoolExecutor.class.getSimpleName();	
	
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	   private boolean isPaused;
	   
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	   private ReentrantLock pauseLock = new MyReentrantLock();
	   
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	   private Condition unpaused = pauseLock.newCondition();

	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	   private static TasksPoolExecutor instance = null;
	   
	   
	   private BlockingQueue<Task> workQueue;
	   
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	   public static TasksPoolExecutor getInstance()
	   {
			if (null == instance)
			{
				 int corePoolSize = 8;//Runtime.getRuntime().availableProcessors();
				 int maximumPoolSize = corePoolSize*16;
				 
				 long keepAliveTime = 1;	// amount of time an idle thread waits before terminating
				 TimeUnit unit = TimeUnit.MINUTES; // Time Unit to seconds
				 BlockingQueue<Task> workQueue = new LinkedBlockingQueue<Task>();	
				 RejectedExecutionHandler handler = new RejectedExecutionHandler() {
					
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						
						if (r instanceof Task)
						{
							Task worker = (Task) r;
							Log.e (TAG,"rejectedExecution: " + worker);
						}
						
					}
				};
				 
				instance = new TasksPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
				
			}
			return instance;
	   }
	   
	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */		   
	public TasksPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue workersQueue, RejectedExecutionHandler handler)
	{		   
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workersQueue);
	}

	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
   protected void beforeExecute(Thread t, Runnable r) {
	   
	 super.beforeExecute(t, r);
     pauseLock.lock();
     try 
     {
       while (isPaused) unpaused.await();
     } 
     catch (InterruptedException ie) {
       t.interrupt();
     } finally {
       pauseLock.unlock();
     }
   }

	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	   public void pause() {
	     pauseLock.lock();
	     try {
	       isPaused = true;
	     } finally {
	       pauseLock.unlock();
	     }
	   }

	/**
	 * 
	 *
	 * @param  
	 * @return      
	 * @see         
	 */	
	   public void resume() {
	     pauseLock.lock();
	     try {
	       isPaused = false;
	       unpaused.signalAll();
	     } finally {
	       pauseLock.unlock();
	     }
	   }

}
