package kr.re.dev.DroidHammer;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
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
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;



/**
 * 필드의 객체를 찾는다.
 * @author ice3x2
 */
public class FieldFinder {

	private WeakReference<Object> mWeakRefTarget;
	private WeakReference<Object> mWeakRefParent;
	private int mClassType;

	protected FieldFinder(WeakReference<Object> parent, WeakReference<Object> target, int targetType) {
		mWeakRefParent = parent;
		mWeakRefTarget = target;
		if(targetType == ClassType.TYPE_FRAGMENT) {
			 mWeakRefParent = new WeakReference<Object>(extractViewIfFragment(parent));
		}
		mClassType = targetType;
	}
	
	protected FieldFinder(WeakReference<Object> parent, int targetType) {
		mWeakRefTarget = parent;
		mWeakRefParent = mWeakRefTarget; 
		if(targetType == ClassType.TYPE_FRAGMENT) {
			 Object fragment = extractViewIfFragment(parent.get()); 
			 mWeakRefParent = new WeakReference<Object>(fragment);
		}
		mClassType = targetType;
	}
	
	private View extractViewIfFragment(Object object) {
		 try {
			Method getViewMethod = object.getClass().getMethod("getView");
			View view =  (View)getViewMethod.invoke(object);
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public Object getTarget() {		
		return mWeakRefTarget.get();
	}
	
	protected void clear() {
		mWeakRefParent.clear();
		mWeakRefTarget.clear();
	}

	
	public Context getContext(Object parent, int classType) {
		if(parent == null) return null;
		if(mClassType == ClassType.TYPE_FRAGMENT)		return ((View)parent).getContext();
		else if(mClassType == ClassType.TYPE_ACTIVTY)	return ((Activity)parent); 
		else if(mClassType == ClassType.TYPE_VIEW) 		return ((View)parent).getContext();
		else if(mClassType == ClassType.TYPE_DIALOG)		return ((Dialog)parent).getContext();
		else if(mClassType == ClassType.TYPE_CONTEXT)		return ((Context)parent);
		else if(mClassType == ClassType.TYPE_APPLICATION)		return ((Application)parent).getApplicationContext();
		return null;
	}
	
	
	public View findViewById(int id) {
		Object parent  = mWeakRefParent.get();
		if(parent == null) return null;
		if(mClassType == ClassType.TYPE_FRAGMENT) return ((View)parent).findViewById(id);
		else if(mClassType == ClassType.TYPE_ACTIVTY) return ((Activity)parent).findViewById(id); 
		else if(mClassType == ClassType.TYPE_VIEW)  return ((View)parent).findViewById(id);
		else if(mClassType == ClassType.TYPE_DIALOG)  return ((Dialog)parent).findViewById(id);
		return null;
	}
	
	public String getString(int id) {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		try {
			return context.getResources().getString(id); 
		} catch(NotFoundException e) {
			return null;
		}
			
	}
	
	public Drawable getDrawable(int id) {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		try {
			return context.getResources().getDrawable(id);
		} catch(NotFoundException e) {
			return null;
		}
	}
	
	public Animation getAnimation(int id) {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		try {
			return AnimationUtils.loadAnimation(context, id);
		} catch(NotFoundException e) {
			return null;
		}
		
	}
	
	public WindowManager getWindowManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (WindowManager) context.getSystemService("window");
	}
	public LayoutInflater getLayoutInflater() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (LayoutInflater) context.getSystemService("layout_inflater");
	}
	
	public TelephonyManager getTelephonyManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public  ActivityManager getActivityManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (ActivityManager) context.getSystemService("activity");
	}
	public PowerManager  getPowerManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (PowerManager) context.getSystemService("power");
	}
	public  AlarmManager  getAlarmManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (AlarmManager) context.getSystemService("alarm");
	}
	
	public  NotificationManager   getNotificationManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (NotificationManager) context.getSystemService("notification");
	}
	public KeyguardManager   getKeyguardManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (KeyguardManager) context.getSystemService("keyguard");
	}
	public LocationManager  getLocationManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (LocationManager) context.getSystemService("location");
	}
	public SearchManager  getSearchManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (SearchManager) context.getSystemService("search");
	}
	public Vibrator   getVibrator() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (Vibrator) context.getSystemService("vibrator");
	}
	public ConnectivityManager    getConnectivityManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	public WifiManager    getWifiManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}
	public  InputMethodManager     getInputMethodManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (InputMethodManager) context.getSystemService("input_method");
	}
	public  UiModeManager      getUiModeManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (UiModeManager) context.getSystemService("uimode");
	}
	public  DownloadManager       getDownloadManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (DownloadManager) context.getSystemService("download");
	}
	public  SensorManager       getSensorManager() {
		Object parent  = mWeakRefParent.get();
		Context context = getContext(parent, mClassType);
		if(parent == null || context == null) return null;
		return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}
	
	
	
}
