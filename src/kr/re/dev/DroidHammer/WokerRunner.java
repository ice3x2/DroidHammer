package kr.re.dev.DroidHammer;

import java.lang.ref.WeakReference;

public class WokerRunner  {

	private WeakReference<Object> mTargetRef;
	private WorkerConveyer mWorkerConveyer;
	
	protected void pushTarget(Object target) {
		mTargetRef = new WeakReference<Object>(target);
	}
	
	public void run(Object ... objs) {
		
		
	}
	public Object result() {
		return null;
	}
}
