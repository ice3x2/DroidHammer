package kr.re.dev.DroidHammer;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;

public class RunnableConveyer {
	
	private HandlerThread mHanderThread;
	private Handler mBackgroundWorkerHandler;
	private String mThreadName;
	
	protected static RunnableConveyer create(String name) {
		RunnableConveyer runnableConveyer = new RunnableConveyer(name);
		return runnableConveyer;
	}
	
	public RunnableConveyer(String name) {
		mThreadName = name;
		initHandler();
	}
	
	private void initHandler() {
		mHanderThread = new HandlerThread(mThreadName);
		mHanderThread.start();
		Looper looper = null;
		while(looper == null) {
			mHanderThread.getLooper();
			SystemClock.sleep(1);
		}
		mBackgroundWorkerHandler = new Handler(looper);
	}
	
	

	
}
