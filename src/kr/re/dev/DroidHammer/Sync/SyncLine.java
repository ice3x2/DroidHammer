package kr.re.dev.DroidHammer.Sync;

import java.lang.ref.WeakReference;

import kr.re.dev.DroidHammer.MethodHolder;
import android.os.Looper;


/**
 * 백그라운드 쓰레드에서 돌아가는 비동기 메소드 호출을 원활하게 해준다.
 * 만약 
 * @author ice3x2
 *
 */
public class SyncLine {

	private static WorkerConveyer sWorkerConveyer = new WorkerConveyer(DefaultFlags.THREAD_NAME);
	private final static Object sSyncMonitor = new Object();


	private SyncLine(Object target) {

	}

	public static boolean run(Object target, Object... arguments) {
		Thread currentThread =  Thread.currentThread();
		StackTraceElement[] stackTraceElements = currentThread.getStackTrace();
		if(isRunInvoke(stackTraceElements)) return false; 
		TargetMap targetMap =  TargetMap.getInstance();
		boolean urgentRun = false;
		boolean isSync = false;
		WorkerGroup workerGroup = null;
		Worker worker = null;
		synchronized (sSyncMonitor) {
			workerGroup =  targetMap.getWorkerGroup(target);
			if(workerGroup == null) {
				throw new RuntimeException(new NullPointerException());
			}
			worker =  workerGroup.findWorker(stackTraceElements, target.getClass(), arguments);
			if(worker == null) {
				throw new RuntimeException(new NullPointerException("NotFind"));
			}
		}
		
		boolean currentUIThread = currentThread.equals(Looper.getMainLooper().getThread());
		boolean currentBackgroundThread = currentThread.getName().equals(DefaultFlags.THREAD_NAME);
		if(!currentBackgroundThread && !currentUIThread);
		// 각각 실행 되어야할 쓰레드 위에서 호출될때도 true 를 반홚나다.
		else if(currentUIThread && worker.isUIThread()) return false; 
		else if(currentBackgroundThread && !worker.isUIThread()) return false;
		else if(currentBackgroundThread && worker.isUIThread()) {
			urgentRun = true;
		} 		
		if(worker.isSync()) {
			isSync = true;
		} 
		
		WeakReference<Object> targetRef = targetMap.getTargetRef(target);
		addWorker(urgentRun,isSync, targetRef, worker, arguments);
		return true;
	}
	
	private static boolean isRunInvoke(StackTraceElement[] stackTraceElements) {
		for(int i = stackTraceElements.length - 1, n = stackTraceElements.length - 10; i > n; --i) {
			if(i < 0) return true;
			int checkIndex = stackTraceElements.length - 6;
			checkIndex = (checkIndex < 0)?0:checkIndex;
			StackTraceElement checkInvokeElement =  stackTraceElements[checkIndex];
			if(checkInvokeElement.getMethodName().indexOf("executeWorker") == 0 
					&& checkInvokeElement.getClassName().indexOf("kr.re.dev.DroidHammer.Sync.WorkerConveyer") == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param target
	 * @param worker
	 * @param arguments
	 */
	private static void addWorker(boolean urgent,boolean isSync,WeakReference<Object> target, Worker worker, Object ... arguments) {
		MethodHolder methodHolder =  worker.getMethodHolder();
		methodHolder.adaptArguments(arguments);
		Worker runWorker = Worker.obtainCopy(worker);
		ExecuteRunnable runnable = null;
		if(!isSync) runnable = ExecuteRunnable.newRunnable(target, methodHolder);
		else runnable = ExecuteRunnable.newSyncRunnable(target, methodHolder);
		runWorker.setWorkerRunnable(runnable);
		synchronized (sSyncMonitor) {
			sWorkerConveyer.addWorker(runWorker,urgent);
		}
		if(isSync) runnable.await();  
	}

}
