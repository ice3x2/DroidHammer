package kr.re.dev.DroidHammer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import kr.re.dev.DroidHammer.Annotations.Inject;
import kr.re.dev.DroidHammer.Annotations.Resource;
import kr.re.dev.DroidHammer.Annotations.ViewById;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;



/**
 * 필드의 인스턴스를 대상 오브젝트에 주입하는 클래스.
 * @author ice3x2
 */
public class FieldInjector {
	
	private FieldFinder mFinder;
	
	public FieldInjector(FieldFinder finder) {
		mFinder = finder;
	}
	
	public void callInjectionField() {
		Object target = mFinder.getTarget();
		Class<?> type =  target.getClass();
		injectContentView(target, type);
		if(target == null) return;
		Field[] fields = type.getDeclaredFields();
		Annotation[] annotations = null;
		for(Field field :fields) {
			 annotations =  field.getAnnotations();
			if(annotations == null || annotations.length == 0) continue;
			for(Annotation annotation : annotations) {
				if(annotation instanceof ViewById &&  injectFieldByViewAnnotation(target, field, (ViewById) annotation)) continue;
				else if(annotation instanceof Inject && injectSystemService(target, field)) continue;
				else if(annotation instanceof Resource && injectFieldByResAnnotation(target, field,(Resource)annotation )) continue;	
			}
		}	
	}
	
	private void injectContentView(Object target, Class<?> type) {
		Annotation[] annotations =  type.getDeclaredAnnotations();
		for(Annotation annotation : annotations) {
			if(annotation instanceof kr.re.dev.DroidHammer.Annotations.Actvity && target instanceof Activity) {
				 int layout =  ((kr.re.dev.DroidHammer.Annotations.Actvity) annotation).value();
				 ((Activity)target).setContentView(layout);
			} else if(annotation instanceof kr.re.dev.DroidHammer.Annotations.Fragment && ClassType.isFragmentType(target)){
				int layout =  ((kr.re.dev.DroidHammer.Annotations.Fragment)annotation).value();
				LayoutInflater inflater = mFinder.getLayoutInflater();
				View view = inflater.inflate(layout, null);
				
				Field field;
				try {
					field = type.getDeclaredField("mView");
					field.setAccessible(true);
					injectField(target, view,field);
				} catch (NoSuchFieldException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	
 	/**
	 * InjectRes 어노테이션이 붙은 필드에 리소스 인스턴스를 찾아서 주입한다.
	 * @param target
	 * @param field
	 * @return
	 */
	private boolean injectFieldByResAnnotation(Object target, Field field, Resource injectRes) {
		if(injectRes == null) return false;
		int identifier = injectRes.value();
		if(target == null || identifier == 0) return false;
		Class<?> type = field.getType();
		Object value = null;
		if(type.isAssignableFrom(String.class)) {
			value = mFinder.getString(identifier);
		} else if(Drawable.class.isAssignableFrom(type)) {
			value = mFinder.getDrawable(identifier);
		} else if(type.isAssignableFrom(Animation.class)) {
			value = mFinder.getAnimation(identifier);
		}
		return injectField(target, value, field);
	
	}
	
	
	/**
	 * 시스템 서비스를 찾아서 주입한다.
	 * @param target 주입 대상 필드가 있는 클래스의 객체. 
	 * @param field 주입 대상 필드 필드.
	 * @return 성공/실패.
	 */
	@SuppressLint("NewApi")
	private boolean injectSystemService(Object target, Field field) {
		Object value = null;
		if(field.getType().isAssignableFrom(WindowManager.class)) value = mFinder.getWindowManager();
		else if(field.getType().isAssignableFrom(LayoutInflater.class)) value = mFinder.getLayoutInflater();
		else if(field.getType().isAssignableFrom(TelephonyManager.class)) value = mFinder.getTelephonyManager();
		else if(field.getType().isAssignableFrom(ActivityManager.class)) value = mFinder.getActivityManager();
		else if(field.getType().isAssignableFrom(PowerManager.class)) value = mFinder.getPowerManager();
		else if(field.getType().isAssignableFrom(AlarmManager.class)) value = mFinder.getAlarmManager();
		else if(field.getType().isAssignableFrom(NotificationManager.class)) value = mFinder.getNotificationManager();
		else if(field.getType().isAssignableFrom(KeyguardManager.class)) value = mFinder.getKeyguardManager();
		else if(field.getType().isAssignableFrom(LocationManager.class)) value = mFinder.getLocationManager();
		else if(field.getType().isAssignableFrom(SearchManager.class)) value = mFinder.getSearchManager();
		else if(field.getType().isAssignableFrom(Vibrator.class)) value = mFinder.getVibrator();
		else if(field.getType().isAssignableFrom(ConnectivityManager.class)) value = mFinder.getConnectivityManager();
		else if(field.getType().isAssignableFrom(WifiManager.class)) value = mFinder.getWifiManager();
		else if(field.getType().isAssignableFrom(InputMethodManager.class)) value = mFinder.getInputMethodManager();
		else if(field.getType().isAssignableFrom(UiModeManager.class)) value = mFinder.getUiModeManager();
		else if(field.getType().isAssignableFrom(DownloadManager.class)) value = mFinder.getDownloadManager();
		else if(field.getType().isAssignableFrom(SensorManager.class)) value = mFinder.getSensorManager();
		
		return injectField(target, value, field);
}
	
	
	private boolean injectFieldByViewAnnotation(Object target, Field field, ViewById injectView) {
			if(injectView == null) return false;
			int identifier = injectView.value();
			View view = mFinder.findViewById(identifier);
			injectField(target, view, field);
			return true;
	}
	
	
	private boolean injectField(Object target, Object value, Field field) {
		if(target == null || field == null) return false;
			try {
				boolean accessible =  field.isAccessible();
				if(!accessible) field.setAccessible(true);
				if(value != null && field.getType().isAssignableFrom(value.getClass())) { 
					field.set(target,  value);
				} else {
					field.set(target,  null);
				}
				if(!accessible) field.setAccessible(false);
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return false;
			}
		
		return true;
	}
	
		
}
