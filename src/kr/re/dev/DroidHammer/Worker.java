package kr.re.dev.DroidHammer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import kr.re.dev.DroidHammer.Annotations.Background;
import kr.re.dev.DroidHammer.Annotations.UiThread;


public class Worker {
	
		private int mID = -1;
		private boolean mSync = true;
		private boolean mIsUiThread = false;
		private AddMode mAddMode = AddMode.Default;
		private Runnable mWorkerRunnable;
		private Object mResultObject;
		private int mDelay = 0;
		private MethodHolder mMethodHolder;
		private String[] mThreadNames;
		
		
		private static Worker sTop;
		private static int sPos = 0;
		private  Worker mPrev;
		
		protected static Worker factory(Method method, String[] threadNames) {
			 UiThread uiThread = method.getAnnotation(UiThread.class);
			 Worker worker = null;
			 if(uiThread != null) {
				 worker = obtain(new MethodHolder(method), threadNames);
				 worker.setSync(uiThread.sync()).setDelay(uiThread.delay()).setID(uiThread.id())
				 .setAddMode(uiThread.addMode()).setUiThread(true);
			 }
			 Background backgroundThread = method.getAnnotation(Background.class);
			 if(backgroundThread != null) {
				 worker = obtain(new MethodHolder(method), threadNames);
				 worker.setSync(backgroundThread.sync()).setDelay(backgroundThread.delay()).setID(backgroundThread.id())
				 .setAddMode(backgroundThread.addMode()).setUiThread(false);
			 }
			 
			 
			 
			 
			 
			
		}
		
		protected static Worker obtain(MethodHolder methodHolder, String[] threadNames) {
			Worker worker;
			if(sPos == 0) {
				worker = new Worker();
				worker.init(methodHolder, threadNames);
			} else {
				worker = sTop;
				sTop = worker.mPrev;
				worker.init(methodHolder, threadNames);
				sPos--;
			}
			return worker;
		}
		protected Worker obtainCopy() {
			Worker worker = obtain(mMethodHolder, mThreadNames);
			worker.mID = mID;
			worker.mSync = mSync;
			worker.mAddMode = mAddMode;
			worker.mWorkerRunnable = mWorkerRunnable;
			worker.mResultObject = mResultObject;
			worker.mDelay = mDelay;
			return worker;
		}
		protected void recycle() {
			sPos++;
			this.mPrev = sTop;
			sTop = this;
		}
		
		
		private void init(MethodHolder methodHolder, String[] threadNames) {
			mMethodHolder = methodHolder;
			mThreadNames = threadNames;
		}
		
		private Worker() {
		}
		
		protected Worker setUiThread(boolean uiThread) {
			mIsUiThread = uiThread;
			return this;
		}
		
		protected Worker setAddMode(AddMode addMode) {
			mAddMode = addMode;
			return this;
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
		
		protected Worker setWorkerRunnable(Runnable runnable) {
			mWorkerRunnable = runnable;
			return this;
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
		public AddMode getAddMode() {
			return mAddMode;
		}
		
	}
