package kr.re.dev.DroidHammer.Sync;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import kr.re.dev.DroidHammer.MethodHolder;

public class ExecuteRunnable implements Runnable {
	private static int MAX_RECYCLED = 128;
	private static LinkedList<ExecuteRunnable> sRecycledResourceList = new LinkedList<ExecuteRunnable>();
	private Object mResult;
	private WeakReference<Object> mTarget;
	private MethodHolder mMethodHolder;
	private CountDownLatch mLatch;
	
	public static ExecuteRunnable newRunnable(WeakReference<Object> target, MethodHolder methodHolder) {
		ExecuteRunnable runnable = new ExecuteRunnable();
		if(sRecycledResourceList.isEmpty()) {
			runnable = new ExecuteRunnable();
		} else {
			runnable = sRecycledResourceList.removeLast();
		}
		runnable.mMethodHolder = methodHolder;
		runnable.mTarget = target;
		runnable.mLatch = null;
		return runnable;
	}
	
	public static ExecuteRunnable newSyncRunnable(WeakReference<Object> target, MethodHolder methodHolder) {
		ExecuteRunnable runnable = newRunnable(target, methodHolder);
		runnable.mLatch = new CountDownLatch(1);
		return runnable;
	}
	
	public void await() {
		try {
			mLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public void recycle() {
		mTarget = null;
		mMethodHolder.clearArgs();
		mResult = null;
		if(sRecycledResourceList.size() < MAX_RECYCLED) {
			sRecycledResourceList.add(this);
		}
	}
	
	public Object getResult() {
		return mResult;
	}
	
	
	private ExecuteRunnable() {}
	
	@Override
	public void run() {
		Object target =  mTarget.get();
		if(target != null) {
			try {
				mResult = mMethodHolder.invokeGetObject(target);
			} catch (Exception e) {
				//Throwable[] throwables = e.get
				 
				throw new RuntimeException(e);
			}
		}
		mMethodHolder.clearArgs();
		if(mLatch != null) mLatch.countDown();
	} 
}