package kr.re.dev.DroidHammer;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import android.graphics.Path.Op;
import android.test.mock.MockApplication;

import kr.re.dev.DroidHammer.Annotations.Background;
import kr.re.dev.DroidHammer.Annotations.UiThread;


public class SyncLine {
	
	private WeakReference<Object> mWeakRefTarget;
	private BackgroundThreadRunner mBackgroundRunner;
	private UiThreadRunner mUiThreadRunner;
	
	public static SyncLine inject(Object target) {
		SyncLine syncLine =  new SyncLine(target);
		return syncLine;
	}
	
	private SyncLine(Object target) {
		mWeakRefTarget = new WeakReference<Object>(target);
		mappingMethodByAnnotation();
	}
	
	private void mappingMethodByAnnotation() {
		Object target = mWeakRefTarget.get();
		if(target == null) return;
		Method[] methods = target.getClass().getDeclaredMethods();
		for(final Method method : methods) {
			Annotation[] annotations = method.getAnnotations();
			// 어노테이션이 없을 경우 Continue
			if(annotations == null || annotations.length == 0) continue;
			for(Annotation annotation : annotations) {
				if(annotation instanceof Background) {
					// 백그라운드 쓰레드.
					mBackgroundRunner.putRunnerMethod(method, 0);
				} else if(annotation instanceof UiThread) {
					// UI쓰레드.
					int delay =  ((UiThread) annotation).id();
					mUiThreadRunner.putRunnerMethod(method, delay);
				}
			}
		}
	}
	
	public static class Option {
		private int mID = -1;
		private boolean mSync = true;
		private boolean mAllwaysAddLast = false;
		private boolean mDemone = false;
		private Object mResultObject;
		
		protected Option setID(int id) {
			mID = id;
			return this;
		}
		protected Option setAllwaysAddLast(boolean use) {
			mAllwaysAddLast = use;
			return this;
		}
		protected Option enableSync(boolean sync) {
			mSync = sync;
			return this;
		}
		protected Option enableDemon(boolean demone) {
			mDemone = demone;
			return this;
		}
		protected Option putResult(Object returnValue) {
			mResultObject = returnValue;
			return this;
		}
		
		public int getID() {
			return mID;
		}
		public boolean isSync() {
			return mSync;
		}
		public boolean isOnDemone() {
			return mDemone;
		}
		public Object getResultValue() {
			return mResultObject;
		}
	}
	
	/*public boolean runBackground(Object...args) {
		 return mBackgroundRunner.runBackground(args);
	}
	
	public boolean runUiThread(Object...args) {
		 return mUiThreadRunner.runUiThread(args);
	}*/

}
