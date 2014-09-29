package kr.re.dev.DroidHammer.Sync;

import java.lang.reflect.Method;

import kr.re.dev.DroidHammer.MethodHolder;
import kr.re.dev.DroidHammer.Annotations.Background;
import kr.re.dev.DroidHammer.Annotations.UiThread;


public class Worker {

	private static int sLastID = Integer.MIN_VALUE;
	private static final int DEFAULT_ID = 0;
	private int mID = DEFAULT_ID;
	private boolean mSync = true;
	private boolean mIsUiThread = false;
	private boolean mIsSingle = false; 
	private Object mResultObject;
	private int mDelay = 0;
	private MethodHolder mMethodHolder;
	private Runnable mWorkerRunnable;
	
	private static Worker sTop;
	private static int sPos = 0;
	private  Worker mPrev;

	protected static Worker factory(Method method, String... threadNames) {
		UiThread uiThread = method.getAnnotation(UiThread.class);
		Worker worker = null;
		if(uiThread != null) {
			worker = obtain(new MethodHolder(method), threadNames);
			worker.setSync(uiThread.sync()).setDelay(uiThread.delay()).setID(uiThread.id())
			.setSingle(uiThread.single()).setUIThread(true);
		}
		Background backgroundThread = method.getAnnotation(Background.class);
		if(backgroundThread != null) {
			worker = obtain(new MethodHolder(method), threadNames);
			worker.setSync(backgroundThread.sync()).setDelay(backgroundThread.delay()).setID(backgroundThread.id())
			.setSingle(backgroundThread.single()).setUIThread(false);
		}
		if(worker != null && worker.getID() == DEFAULT_ID) {
			worker.setID(++sLastID);
			if(sLastID > DEFAULT_ID) sLastID = Integer.MIN_VALUE;
		}
		return worker;
	}

	private static Worker obtain(MethodHolder methodHolder, String... threadNames) {
		Worker worker;
		if(sPos == 0) {
			worker = new Worker();
			worker.init(methodHolder);
		} else {
			worker = sTop;
			sTop = worker.mPrev;
			worker.init(methodHolder);
			sPos--;
		}
		return worker;
	}
	
	protected static Worker obtainCopy(Worker ogWorker) {
		Worker worker = obtain(ogWorker.mMethodHolder);
		worker.mID = ogWorker.mID;
		worker.mSync = ogWorker.mSync;
		worker.mResultObject = ogWorker.mResultObject;
		worker.mDelay = ogWorker.mDelay;
		return worker;
	}
	
	protected void recycle() {
		sPos++;
		this.mPrev = sTop;
		sTop = this;
	}


	private void init(MethodHolder methodHolder) {
		mMethodHolder = methodHolder;
		
	}

	private Worker() {
	}

	protected Worker setUIThread(boolean uiThread) {
		mIsUiThread = uiThread;
		return this;
	}
	
	protected boolean isUIThread() {
		return mIsUiThread;
	}
	


	protected Worker setSync(boolean sync) {
		mSync = sync;
		return this;
	}

	protected Worker setID(int id) {
		mID = id;
		return this;
	}
	protected Worker setDelay(int delay) {
		mDelay = delay;
		return this;
	}

	protected Worker enableSync(boolean sync) {
		mSync = sync;
		return this;
	}
	protected Worker putResult(Object returnValue) {
		mResultObject = returnValue;
		return this;
	}
	
	protected MethodHolder getMethodHolder() {
		return mMethodHolder;
	}

	protected Worker setWorkerRunnable(Runnable runnable) {
		mWorkerRunnable = runnable;
		return this;
	}
	public Worker setSingle(boolean use) {
		mIsSingle = use;
		return this;
	}

	public boolean isSingle() {
		return mIsSingle;
	}

	public Runnable getWorkerRunnable() {
		return mWorkerRunnable;
	}

	public int getID() {
		return mID;
	}
	public boolean isSync() {
		return mSync;
	}
	public Object getResultValue() {
		return mResultObject;
	}
	public int getDelay() {
		return mDelay;
	}
	
	

}
