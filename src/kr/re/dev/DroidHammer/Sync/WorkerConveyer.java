package kr.re.dev.DroidHammer.Sync;

import java.util.HashMap;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class WorkerConveyer {

	private static HashMap<String, Looper> sLooperMap = new HashMap<String, Looper>(); 
	Handler mBackgroundWorkerHandler;
	Handler mMainHandler;
	private String mThreadName;

	protected static WorkerConveyer create(String name) {
		WorkerConveyer runnableConveyer = new WorkerConveyer(name);
		return runnableConveyer;
	}


	/**
	 * 작업 큐의 핸들러 초기화. 
	 * @param name 핸들러 쓰레드 이름.
	 */
	public WorkerConveyer(String name) {
		mThreadName = name;
		initHandler();
	}

	/**
	 * 핸들러 초기화.
	 */
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
		mBackgroundWorkerHandler = new Handler(looper, mCallBackBackground);
		mMainHandler = new Handler(Looper.getMainLooper(), mCallBackUI);
	}

	protected void quit() {
		Looper looper =  sLooperMap.get(mThreadName);
		if(looper != null && looper.getThread().isAlive() && !looper.getThread().isInterrupted())  {
			looper.quit();
		}
	}

	

	/**
	 * 워커를 대기큐에 마지막에 추가한다.
	 * @param worker
	 */
	protected void addWorker(Worker worker, boolean first) {
		Message msg = createWorkerMessgae(worker);
		int delay =  worker.getDelay();
		if(worker.isUIThread()) {
			if(worker.isSingle()) mMainHandler.removeMessages(worker.getID());
			if(delay > 0) {
				mMainHandler.sendMessageDelayed(msg,delay);
			} else if(first) {
				mMainHandler.sendMessageAtFrontOfQueue(msg);
			} else {
				mMainHandler.sendMessage(msg);
			}
		} else {
			if(worker.isSingle()) mBackgroundWorkerHandler.removeMessages(worker.getID());
			if(delay > 0) {
				mBackgroundWorkerHandler.sendMessageDelayed(msg, delay);
			} else if(first) {
				mBackgroundWorkerHandler.sendMessageAtFrontOfQueue(msg);
			} else {
				mBackgroundWorkerHandler.sendMessage(msg);
			}
			
		}
	}

	
	private Message createWorkerMessgae(Worker worker) {
		Message msg = Message.obtain();
		msg.what = worker.getID();
		msg.obj = worker;
		return msg;
	}
	
	
	private Handler.Callback mCallBackUI = new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			executeWorker(msg);
			return false;
		}
	};
	
	private Handler.Callback mCallBackBackground = new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			executeWorker(msg);
			return false;
		}
	};
	
	private void executeWorker(Message msg) {
		Worker worker =  ((Worker)msg.obj);
		ExecuteRunnable runnable = (ExecuteRunnable)worker.getWorkerRunnable();
		runnable.run();
		runnable.recycle();
	}
	
}
