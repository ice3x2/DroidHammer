
package kr.re.dev.DroidHammer.LikeAA;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.re.dev.DroidHammer.ClassType;
import kr.re.dev.DroidHammer.Annotations.AfterViews;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

/**
 * salmonPie <br/>
 * http://ice3x2.github.io/salmonPie/   <br/>
 * 
 * salmonPie.java
 * <b>License </b><br/>
 * 
 * Copyright (c) 2013 ice3x2@gmail.com Beom
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE. <br/>
 *  
 * v0.9.3 <br/> 
 * 사용법 : https://github.com/ice3x2/salmonPie/wiki/salmonPie
 * 
 * @author ice3x2
 *
 */
public class LikeAA {

	private FieldFinder mFinder;
	private ListenerMapper mListenerMapper;
	private ArrayList<Method> mAfterViewsMethods = new ArrayList<Method>();

	private static ExecutorService sExcutorMapping;

	private LikeAA() {}

	public static LikeAA inject(Activity activtiy) {
		LikeAA salmonPie = LikeAA.injectObject(activtiy);
		return salmonPie;
	}
	public static LikeAA inject(View view) {
		LikeAA salmonPie = LikeAA.injectObject(view);
		return salmonPie;
	}
	public static LikeAA inject(Dialog dialog) {
		LikeAA salmonPie = LikeAA.injectObject(dialog);
		return salmonPie;
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static LikeAA inject(Fragment fragment) {
		LikeAA salmonPie = LikeAA.injectObject(fragment);
		return salmonPie;
	}
	
	public static LikeAA inject(Service service) {
		LikeAA salmonPie = LikeAA.inject(service.getApplicationContext(), service);
		return salmonPie;
	}
	
	public static LikeAA inject(Context context, Object target) {
		LikeAA salmonPie = new LikeAA();
		salmonPie.inject(salmonPie.createFieldFinder(context, target, ClassType.TYPE_CONTEXT), target);
		return salmonPie;
	}
	public static LikeAA injectFragment(Object fragment) {
		return injectObject(fragment);
	}
	
	public static LikeAA injectObject(Object androidObject) {
		LikeAA salmonPie = new LikeAA();
		int classType = ClassType.searchClassType(androidObject);
		salmonPie.inject(salmonPie.createFieldFinder(androidObject, androidObject, classType), androidObject);
		return salmonPie;
	}
	
	/**
	 * 
	 * @param androidObject Andoird 의 Object. {@link Activity} {@link View},{@link Fragment},{@link Context},{@link Dialog} 등을 입력할 수 있다.
	 * @param target 대상 객체. 
	 * @return
	 */
	public static LikeAA injectObject(Object androidObject, Object target) {
		LikeAA salmonPie = new LikeAA();
		int classType = ClassType.searchClassType(androidObject);
		salmonPie.inject(salmonPie.createFieldFinder(androidObject,target, classType), target);
		return salmonPie;
	}
	

	public static void run(Object...args) {
	}


	private void inject(FieldFinder finder, Object target) {
		mAfterViewsMethods.addAll(getAfterViewMethods(target));
		mFinder = finder;
		initMapper();
		// 만약 타킷 클래스에 @AfterViews 어노테이션을 갖고 있는 메소드가 있다면 맵핑을 백그라운드에서 실행한다.   
		if(mAfterViewsMethods.isEmpty()) 
			mRunnableMapping.run();
		else {
			initMappingExecutor();
			sExcutorMapping.execute(mRunnableMapping);
		}
	}

	private ArrayList<Method> getAfterViewMethods(Object target) {
		Method[] methods =  target.getClass().getDeclaredMethods();
		ArrayList<Method> afterViewMethodList = new ArrayList<Method>();
		for(Method method : methods) {
			if(method.getAnnotation(AfterViews.class) != null) {
				afterViewMethodList.add(method);
			}
		}
		return afterViewMethodList;
	}

	private Runnable mRunnableMapping = new Runnable() {
		@Override
		public void run() {
			mappinFieldByAnnotation();
			callAfterViews();
			mappingListenerByAnnotation();
		}
	};


	@SuppressLint("HandlerLeak")
	private void callAfterViews() {
		for(Method method :  mAfterViewsMethods) {
			if(!method.isAccessible()) method.setAccessible(true);
			final WeakReference<Handler> waekHandler = new WeakReference<Handler>(new Handler(Looper.getMainLooper()) {
				public void dispatchMessage(Message msg) {
					Object target = mFinder.getTarget();
					if(target == null) return;
					Method method = (Method) msg.obj;
					try {
						method.invoke(target, makeEmptyArgs(method.getParameterTypes()));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (Exception e) {
						Thread.getDefaultUncaughtExceptionHandler()
						.uncaughtException(Looper.getMainLooper().getThread(), 
								e);
					}

				};
			});
			Message msg =  waekHandler.get().obtainMessage();
			msg.obj = method;
			waekHandler.get().sendMessage(msg);
			waekHandler.clear();
		}
	}


	private FieldFinder createFieldFinder(Object target, Object androidObject, int targetType) {
		final WeakReference<Object> weakRefAndroidObject = new WeakReference<Object>(androidObject);
		if(target == androidObject) {
			return new FieldFinder(weakRefAndroidObject, targetType);
		} else {
			final WeakReference<Object> weakRefParent = new WeakReference<Object>(target);
			return new FieldFinder(weakRefParent,weakRefAndroidObject, targetType);
		}
	}



	private void initMappingExecutor() {
		if(sExcutorMapping == null || sExcutorMapping.isShutdown()) {
			sExcutorMapping = Executors.newSingleThreadExecutor();
		}
	}

	private void initMapper() {
		mListenerMapper = new ListenerMapper(mFinder);
	}

	private void mappinFieldByAnnotation() {
		Object target =  mFinder.getTarget();
		// target 이 상했을 경우에는 맵핑을 진행하지 않고 종료한다.
		if(target == null) return;

		FieldInjector mapper = new FieldInjector(mFinder);
		// TimeCheck
		long startTime = System.nanoTime();
		mapper.callInjectionField();
		long endTime = System.nanoTime();
		System.out.println("callInjectionField() : " + ((endTime - startTime) / 1000.0f / 1000.0f) + "ms");


	}


	private void mappingListenerByAnnotation() {
		Object target = mFinder.getTarget();
		if(target == null) return;
		Method[] methods = target.getClass().getDeclaredMethods();

		for(final Method method : methods) {
			Annotation[] annotations = method.getAnnotations();
			// 어노테이션이 없을 경우 Continue
			if(annotations == null || annotations.length == 0) continue;
			for(Annotation annotation : annotations) {
				mListenerMapper.callInjectionListenerMethod(annotation, method);
			}
		}
		mListenerMapper.recycle();
	}

	private Object[] makeEmptyArgs(Class<?>[] params) {
		Object[] args = new Object[params.length];
		for(int i = 0, n = params.length; i < n; ++i) {
			if(params[i].isAssignableFrom(byte.class)) args[i] = Byte.valueOf((byte) 0);
			else if(params[i].isAssignableFrom(boolean.class)) args[i] = Boolean.valueOf(false);
			else if(params[i].isAssignableFrom(char.class)) args[i] = Character.valueOf('\0');
			else if(params[i].isAssignableFrom(int.class)) args[i] = Integer.valueOf(0);
			else if(params[i].isAssignableFrom(float.class)) args[i] = Float.valueOf(0.0f);
			else if(params[i].isAssignableFrom(long.class)) args[i] = Long.valueOf(0);
			else if(params[i].isAssignableFrom(double.class)) args[i] = Double.valueOf(0);
			else args[i] = null;
		}
		return args;
	}

	public void close() {
		mFinder.clear();
	}


}