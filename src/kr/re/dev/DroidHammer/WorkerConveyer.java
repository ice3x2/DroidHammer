package kr.re.dev.DroidHammer;

import java.util.HashMap;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class WorkerConveyer {
	
	private static HashMap<String, Looper> sLooperMap = new HashMap<String, Looper>(); 
	Handler mBackgroundWorkerHandler;
	private String mThreadName;
	
	protected static WorkerConveyer create(String name) {
		WorkerConveyer runnableConveyer = new WorkerConveyer(name);
		return runnableConveyer;
	}
	
	public WorkerConveyer(String name) {
		mThreadName = name;
		initHandler();
	}
	
	private void initHandler() {
		Looper looper = null;
		if(sLooperMap.containsKey(mThreadName)) {
			looper = sLooperMap.get(mThreadName);
		} else if(looper == null || !looper.getThread().isAlive() || looper.getThread().isInterrupted())  {
			HandlerThread handlerThread = new HandlerThread(mThreadName);
			handlerThread.start();
			looper = handlerThread.getLooper();
			sLooperMap.remove(mThreadName);
			sLooperMap.put(mThreadName, looper);
		}
		mBackgroundWorkerHandler = new Handler(looper, mCallBack);
	}
	
	protected void quit() {
		Looper looper =  sLooperMap.get(mThreadName);
		if(looper != null && looper.getThread().isAlive() && !looper.getThread().isInterrupted())  {
			looper.quit();
		}
	}
	
	protected void removeWorkers(int id) {
		mBackgroundWorkerHandler.removeMessages(id);
	}
	
	protected void addWorker(Worker worker) {
		Message msg = Message.obtain();
		msg.what = worker.getID();
		msg.obj = worker;
		mBackgroundWorkerHandler.sendMessageAtFrontOfQueue(msg);
	}
	
	protected void addWorkerFirst(Worker worker) {
		Message msg = Message.obtain();
		msg.what = worker.getID();
		msg.obj = worker;
		mBackgroundWorkerHandler.sendMessageAtFrontOfQueue(msg);
	}
	
	private Handler.Callback mCallBack = new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			 ((Worker)msg.obj).getWorkerRunnable().run(); 
			return false;
		}
	};
		
}
