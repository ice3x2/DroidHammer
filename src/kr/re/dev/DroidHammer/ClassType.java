package kr.re.dev.DroidHammer;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.view.View;


/**
 * 클래스 타입을 분류하고 갖고 있는다.
 * @author ice3x2
 */
public class ClassType {
	/**
	 * 액티비티 타입
	 */
	public static final int TYPE_ACTIVTY = 0;
	/**
	 * 뷰 타입 
	 */
	public static final int TYPE_VIEW = 1;
	/**
	 * 다이얼로그 타입 
	 */
	public static final int TYPE_DIALOG = 2;
	/**
	 *  프래그먼트 타입 
	 */
	public static final int TYPE_FRAGMENT = 3;
	/**
	 *  컨텍스트 타입 
	 */
	public static final int TYPE_CONTEXT = 4;
	/**
	 *  애플리케이션 타입 
	 */
	public static final int TYPE_APPLICATION = 5;
	/**
	 * 기타 일반 오브젝트 타입 
	 */
	public static final int TYPE_OBJECT = 10;
	
	/**
	 * 프래그먼트의 디폴드 메소드 이름 리스트. 
	 * android 3.0 미만에서 프래그먼트를 찾기 위해 사용한다.
	 */
	protected static String[] mFragmentPublicMethodList = {"getActivity", "getTargetFragment", "onCreateView", "onActivityCreated"};
	
	
	/**
	 * 클래스 타입을 찾아낸다.
	 * @param object 클래스 타입을 찾을 대상 객체.
	 * @return {@link #TYPE_ACTIVTY} , {@link #TYPE_APPLICATION}, {@link #TYPE_CONTEXT}, {@link #TYPE_DIALOG} 
	 * {@link #TYPE_FRAGMENT} , {@link #TYPE_OBJECT} {@link #TYPE_VIEW}
	 */
	public static int searchClassType(Object object) {
		if(object instanceof Activity) {
			return TYPE_ACTIVTY;
		} else if(object instanceof View) {
			return TYPE_VIEW;
 		}  else if(object instanceof Dialog) {
			return TYPE_DIALOG;
		}  else if(object instanceof Application) {
			return TYPE_APPLICATION;
		}  else if(object instanceof Context) {
			return TYPE_CONTEXT;
		} else if(isFragmentType(object)) {
			return TYPE_FRAGMENT;
		} else {
			return TYPE_OBJECT;
		}
	}
	
	/**
	 * Fragment type 이라면 true 를 반환하고 그렇지 않으면  false 를 반환. 
	 * @param Fragment 의심 클래스 타입.
	 * @return 만약 true 라면 프래그먼트. 
	 */
	private static boolean isFragmentType(Object obj) {
		// 프래그 먼트를 지원하는 Level11 이상 (허니콤) 에서는 바로 검사.
		if(android.os.Build.VERSION.SDK_INT >= 11) {
			if(obj instanceof Fragment) return true;
		}
		Class<?> type = obj.getClass();
		int matchCount = 0;
		Method[] methods = type.getMethods();
		for(Method method : methods) {
			for(String compareMethod : mFragmentPublicMethodList) {
				if(compareMethod.equals(method.getName())) {
					matchCount++;
					break;
				}
			}
		}
		if(matchCount == mFragmentPublicMethodList.length) return true;
		else return false;
	}
	
	
	
	
}
