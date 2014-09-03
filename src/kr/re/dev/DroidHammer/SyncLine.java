package kr.re.dev.DroidHammer;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.graphics.Path.Op;
import android.test.mock.MockApplication;

import kr.re.dev.DroidHammer.Annotations.Background;
import kr.re.dev.DroidHammer.Annotations.UiThread;


public class SyncLine {
	
	public static final String DEFAULT_THREAD_NAME = "2014_SYNCLINE";

	private static WorkerConveyer mWorkerConveyer;
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
	
}
