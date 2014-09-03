package kr.re.dev.DroidHammer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


import android.os.Looper;

public class BackgroundThreadRunner {
	
	private static final String RUN_THREAD_NAME = "R_LIKE_AA_RUNBACGROUND";
	private static final String START_THREAD_NAME = "S_LIKE_AA_RUNBACGROUND";
	
	private ArrayList<MethodHolder> mListMethodHolderIndex = new ArrayList<MethodHolder>();
	private FieldFinder mFinder;
	
	
	protected void putRunnerMethod(Method method) {
		mListMethodHolderIndex.add(new MethodHolder(method));
	}
	
	public BackgroundThreadRunner(FieldFinder finder) {		
		mFinder = finder;
	}
	
	
	public boolean runBackground(final Object...args) {
		 Object target =  mFinder.getTarget();
		 if(target == null) return true;
		 Thread currentThread = Thread.currentThread();
		 if(Looper.getMainLooper().getThread() == currentThread || currentThread.getName().indexOf(RUN_THREAD_NAME) == 0) {
			 StackTraceElement[] stackTrace =  currentThread.getStackTrace();
			 String strMethodName = findMethodName(target.getClass().getName(), stackTrace);
			 if(strMethodName == null) return true;
			 final StringBuilder methodName = new StringBuilder(strMethodName);
			 new Thread() { 
				 
				public void run() {
					 ArrayList<Class<?>> listParams = new ArrayList<Class<?>>();
					 for(Object param : args) listParams.add(param.getClass());
					 Class<?>[] paramArray = new Class[args.length];
					 listParams.toArray(paramArray);
					 Object target =  mFinder.getTarget();
					 if(target != null){ 
						 invoke(target, methodName.toString(), paramArray, args);
						 setName(START_THREAD_NAME + getName());
					 }
				 };
			 }.start();
			 return true;
		 } else if(currentThread.getName().indexOf(START_THREAD_NAME) == 0) {
			 currentThread.setName(RUN_THREAD_NAME + currentThread.getName().replace(START_THREAD_NAME, ""));
			 return false; 
		 }
		return false;
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
	
	
	
	
	private MethodHolder findMethodHolder(Object target, String name,  Class<?>[] paramsType) {
		MethodHolder methodHolder = new MethodHolder(name, paramsType); 
		int findIndex =  mListMethodHolderIndex.indexOf(methodHolder);
		if(findIndex < 0) {
			Method[] methods =  target.getClass().getDeclaredMethods();
			for(Method method : methods) {
				MethodHolder targetMethodHolder = new MethodHolder(method);
				if(targetMethodHolder.equalsIncludExcludesParams(methodHolder)) {
					mListMethodHolderIndex.add(targetMethodHolder);
					return targetMethodHolder;
				}
			}
			return null;
		}
		return mListMethodHolderIndex.get(findIndex);
	}
	
	
	protected boolean invoke(Object target, String name,  Class<?>[] paramsType, Object[] args) {
		MethodHolder realMethodHolder =findMethodHolder(target, name, paramsType);
		if(realMethodHolder != null) {
			try {
				realMethodHolder.getMethod().setAccessible(true);
				if(args.length > 0) realMethodHolder.getMethod().invoke(target, args);
				else realMethodHolder.getMethod().invoke(target);
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
		return false;
	}
	
	
}
