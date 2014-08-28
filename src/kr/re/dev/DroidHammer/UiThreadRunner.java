package kr.re.dev.DroidHammer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import android.os.Handler;
import android.os.Looper;

public class UiThreadRunner {
	private ArrayList<MethodHolder> mListMethodHolderIndex = new ArrayList<MethodHolder>();
	private FieldFinder mFinder;
	private Handler mHandler = new Handler(Looper.getMainLooper());
	private static ExecutorService mExecutor;
	public UiThreadRunner(FieldFinder finder) {
		mFinder = finder;
		if(mExecutor == null) mExecutor = Executors.newSingleThreadExecutor();
	}
	
	protected void putRunnerMethod(Method method, int callDelayed) {
		mListMethodHolderIndex.add(new MethodHolder(method, callDelayed));
	}
	
	protected boolean runUiThread(final Object...args) {
		 Object target = mFinder.getTarget();
		 if(target == null) return true;
		 Thread currentThread = Thread.currentThread();
		 if(Looper.getMainLooper().getThread() != currentThread) {
			 final String methodName = findMethodName(target.getClass().getName(), currentThread.getStackTrace());
			 if(methodName == null) return false; 
			 runUiThread(target, methodName.toString(), args);
			 return true;
		 } else {
			 StackTraceElement[] stackTrace = currentThread.getStackTrace();
			 String className =  target.getClass().getName();
			 String methodName = findMethodName(className, stackTrace);
			 final String  invokForMethodName = methodName;
			 int methodIndex = indexOfStackTrace(className, methodName, stackTrace) + 1;
			 if(stackTrace.length > methodIndex) {
				 StackTraceElement trace =  stackTrace[methodIndex];
				 methodName = trace.getMethodName();
				 className = trace.getClassName();
			 }
			 if(methodName.indexOf("invoke") != 0 && className.indexOf("java.lang.reflect.Method") != 0) {
				 mExecutor.execute(new Runnable() {
					@Override
					public void run() {
						Object target = mFinder.getTarget();
						 if(target != null) {
							 runUiThread(target, invokForMethodName, args);
						 }
					}
				});
				return true;
			 }
		 }
		 return false;
	}
	
	
	private synchronized boolean runUiThread(final Object target, String methodName,final Object...args) { 
		ArrayList<Class<?>> listParams = new ArrayList<Class<?>>();
		 for(Object param : args) {
			 listParams.add(param.getClass());
		 }
		 final Class<?>[] paramArray = new Class[args.length];
		 listParams.toArray(paramArray);
		 final MethodHolder realMethodHolder = findMethodHolder(target, methodName, paramArray);
		 if(realMethodHolder == null) return true;
		 int delayed = realMethodHolder.getDelayed();
		 
		 
		 Runnable runnable = new Runnable() {
			@Override
			public void run() {
				 invoke(target, realMethodHolder,args);				
			}
		 };	
		 if(delayed == 0) mHandler.post(runnable);
		 else if(delayed < 0) mHandler.postAtFrontOfQueue(runnable);
		 else mHandler.postDelayed(runnable, delayed);
		 return true;
	}
	
	private String findMethodName(String targetClassName, StackTraceElement[] stackTrace) {
		for(StackTraceElement trace :stackTrace) {
			try {
				if(trace.getClassName().equals(targetClassName)) {
					return trace.getMethodName();
				}	
			}catch(Exception e) {
				continue;
			}
		}
		return null;
	}
	
	private int indexOfStackTrace(String targetClassName, String methodName, StackTraceElement[] stackTrace) {
		
		for(int i = 0, n = stackTrace.length; i < n; ++i) {
			try {
				StackTraceElement trace = stackTrace[i];
				if(trace.getClassName().equals(targetClassName) && trace.getMethodName().equals(methodName)) {
					return i;
				}	
			}catch(Exception e) {
				continue;
			}
		}
		return -1;
	}
	
	
	private MethodHolder findMethodHolder(Object target, String name,  Class<?>[] paramsType) {
		MethodHolder methodHolder = new MethodHolder(name, paramsType); 
		int findIndex =  mListMethodHolderIndex.indexOf(methodHolder);
		if(findIndex < 0) {
			Method[] methods =  target.getClass().getDeclaredMethods();
			for(Method method : methods) {
				MethodHolder targetMethodHolder = new MethodHolder(method, 0);
				if(targetMethodHolder.equalsIncludExcludesParams(methodHolder)) {
					mListMethodHolderIndex.add(targetMethodHolder);
					return targetMethodHolder;
				}
			}
			return null;
		}
		return mListMethodHolderIndex.get(findIndex);
	}
	
	
	
	protected boolean invoke(Object target, MethodHolder methodHodler, Object[] args) {
		try {
			methodHodler.getMethod().setAccessible(true);
			if(args.length > 0) methodHodler.getMethod().invoke(target, args);
			else methodHodler.getMethod().invoke(target);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false; 
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false; 
		} catch (InvocationTargetException e) {
			Looper.getMainLooper().getThread()
				.getUncaughtExceptionHandler()
				.uncaughtException(Looper.getMainLooper().getThread(), e.getCause());
			return false; 
		}
		return true;
	}
	
	
}
