package kr.re.dev.DroidHammer;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;

import kr.re.dev.DroidHammer.Annotations.Click;
import kr.re.dev.DroidHammer.Annotations.ItemClick;
import kr.re.dev.DroidHammer.Annotations.LongClick;
import kr.re.dev.DroidHammer.Annotations.SeekBarProgressChange;
import kr.re.dev.DroidHammer.Annotations.SeekBarTouchStart;
import kr.re.dev.DroidHammer.Annotations.SeekBarTouchStop;
import kr.re.dev.DroidHammer.Annotations.Touch;
import android.annotation.SuppressLint;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;

/**
 * 리스너를 주입.
 * @author ice3x2
 */
public class ListenerMapper {
	
	private FieldFinder mFinder;
	
	public ListenerMapper(FieldFinder finder) {
		mFinder = finder;
	}
	
	protected boolean callInjectionListenerMethod(Annotation annotation, Method method) {
		try {
			int[] viewIDs = getViewIDByAnnotation(annotation);
			if(viewIDs == null || viewIDs.length == 0) return false;
			for(int id: viewIDs) {
				View targetView =  mFinder.findViewById(id);
				if(targetView != null) {
					if(annotation instanceof Click) setClickListenerForMapping(method, targetView);
					if(annotation instanceof LongClick) setLongClickListenerForMapping(method, targetView);
					else if(annotation instanceof Touch) setTouchListenerForMapping(method, targetView);
					else if(annotation instanceof ItemClick) setItemClickListenerForMapping(method, targetView);
					else if(annotation instanceof SeekBarTouchStart) setSeekbarTouchStartForMapping(method, targetView);
					else if(annotation instanceof SeekBarTouchStop) setSeekbarTouchStopForMapping(method, targetView);
					else if(annotation instanceof SeekBarProgressChange) setSeekbarProgressChangedForMapping(method, targetView);
				}
			}
		} catch (Exception e1) {
			return false;
		}
		return true;
	}
	
	/**
	 * 모든 사용이 끝났을 때, 한 번 호출해서 불필요한 자원을 해지한다.
	 */
	protected void recycle() {
		SeekBarChangeListener.recycle();
	}
	
	
	private int[] getViewIDByAnnotation(Annotation annotation) {
		int[] viewIDs = null;
		if(annotation instanceof Click) viewIDs = ((Click)annotation).value();
		if(annotation instanceof LongClick) viewIDs = ((LongClick)annotation).value();  
		else if(annotation instanceof Touch) viewIDs = ((Touch)annotation).value();
		else if(annotation instanceof ItemClick) viewIDs = ((ItemClick)annotation).value();
		else if(annotation instanceof SeekBarTouchStart) viewIDs = ((SeekBarTouchStart)annotation).value();
		else if(annotation instanceof SeekBarTouchStop) viewIDs = ((SeekBarTouchStop)annotation).value();
		else if(annotation instanceof SeekBarProgressChange) viewIDs = ((SeekBarProgressChange)annotation).value();
		return viewIDs;
	}
	
	
	private void setTouchListenerForMapping(final Method method,final View view) {
		final MethodHolder methodHolder = new MethodHolder(method);
		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {
				try {
					Object target = mFinder.getTarget();
					if(target != null) {
						Object[] args = new Object[]{v, event};
						methodHolder.adaptArguments(args);
						Object obj = methodHolder.invokeGetObject(target);
						methodHolder.clearArgs();
						if(obj instanceof Boolean ) {
							return (Boolean)obj;
						}
					}
				} catch (Exception e) {
					uncaughtException(e);
				}	
				return false;
			}
		});
	}
	
	
	private void setClickListenerForMapping(final Method method,final View view) {
		final MethodHolder methodHolder = new MethodHolder(method);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Object target = mFinder.getTarget();
					if(target != null) {
						Object[] args = new Object[]{v};
						methodHolder.adaptArguments(args);
						methodHolder.invoke(target);
						methodHolder.clearArgs();
					}
				} catch (Exception e) {
					uncaughtException(e);
				}
			}
		});
	}
	
	private void setLongClickListenerForMapping(final Method method,final View view) {
		final MethodHolder methodHolder = new MethodHolder(method);
		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				try {
					Object target = mFinder.getTarget();
					if(target != null) {
						Object[] args = new Object[]{v};
						methodHolder.adaptArguments(args);
						Object object = methodHolder.invokeGetObject(target);
						methodHolder.clearArgs();
						if(object != null && object instanceof Boolean) {
							return(boolean) object;
						}						
					}
				} catch (Exception e) {
					uncaughtException(e);
				}
				return false; 
			}

		});
	}
	
	private void setItemClickListenerForMapping(final Method method,final View view) {
		if(!(view instanceof ListView)) return;
		ListView listView = (ListView)view;
		final MethodHolder methodHolder = new MethodHolder(method);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				try {
					Object target = mFinder.getTarget();
					if(target != null) {
						Object[] args = new Object[]{adapter, view, position, id, adapter.getItemAtPosition(position)};
						methodHolder.adaptArguments(args);
						methodHolder.invoke(target);
						methodHolder.clearArgs();
					}
				} catch (Exception e) {
					uncaughtException(e);
				}
			}
		});
	}
	
	
	private void setSeekbarTouchStartForMapping(final Method method, final View view) {
		if(!(view instanceof SeekBar)) return;
		SeekBar seekBar = (SeekBar) view;
		final MethodHolder methodHolder = new MethodHolder(method);
		SeekBarChangeListener seekBarListener =  SeekBarChangeListener.obtain(mFinder.getTarget(), seekBar);
		seekBarListener.setStartTouchMethodHolder(methodHolder);
	}
	
	private void setSeekbarTouchStopForMapping(final Method method, final View view) {
		if(!(view instanceof SeekBar)) return;
		SeekBar seekBar = (SeekBar) view;
		final MethodHolder methodHolder = new MethodHolder(method);
		SeekBarChangeListener seekBarListener =  SeekBarChangeListener.obtain(mFinder.getTarget(), seekBar);
		seekBarListener.setStopTouchMethodHolder(methodHolder);
	}
	
	private void setSeekbarProgressChangedForMapping(final Method method, final View view) {
		if(!(view instanceof SeekBar)) return;
		SeekBar seekBar = (SeekBar) view;
		final MethodHolder methodHolder = new MethodHolder(method);
		SeekBarChangeListener seekBarListener =  SeekBarChangeListener.obtain(mFinder.getTarget(), seekBar);
		seekBarListener.setProgressChangedMethodHolder(methodHolder);
	}
	
	
	private void uncaughtException(Exception e) {
		Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Looper.getMainLooper().getThread(), e);
	}
	

	
	
	/**
	 * OnSeekBarChangeListener 내부에는 총 세개의 이벤트를 받아올 수 있는 메소드가 각각 존재한다.
	 * 이 이벤트들을 따로따로 받기 하기 위해서 존재하는 리스너를 상속받은 클래스.
	 * @author ice3x2
	 *
	 */
	@SuppressLint("UseSparseArrays")
	public static class SeekBarChangeListener  implements SeekBar.OnSeekBarChangeListener {
		private MethodHolder mProgressChangedMethod;
		private MethodHolder mStartTouchMethod;
		private MethodHolder mStopTouchMethod;
		private WeakReference<Object> mTargetRef;
		private static HashMap<Integer, SeekBarChangeListener> mMapSeekBar = new HashMap<Integer, ListenerMapper.SeekBarChangeListener>();
		public static SeekBarChangeListener obtain(Object target,SeekBar seekBar) {
			SeekBarChangeListener seekBarChangeListener = mMapSeekBar.get((Integer)seekBar.getId()); 
			if(seekBarChangeListener == null) {
				seekBarChangeListener = new SeekBarChangeListener(target);
				seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
				mMapSeekBar.put((Integer)seekBar.getId(), seekBarChangeListener);
				return seekBarChangeListener;
			} else {
				return seekBarChangeListener;
			}
		}
		public static void recycle() {
			mMapSeekBar.clear();
		}
		
		private SeekBarChangeListener(Object target) {
			mTargetRef= new WeakReference<Object>(target);
		}
		
		public void setProgressChangedMethodHolder(MethodHolder methodHolder) {
			mProgressChangedMethod = methodHolder;
		}
		public void setStartTouchMethodHolder(MethodHolder methodHolder) {
			mStartTouchMethod = methodHolder;
		}
		public void setStopTouchMethodHolder(MethodHolder methodHolder) {
			mStopTouchMethod = methodHolder;
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			Object target = mTargetRef.get();
			if(target != null && mProgressChangedMethod != null) {
				Object[] args = new Object[]{seekBar};
				mProgressChangedMethod.adaptArguments(args);
				try {
					mProgressChangedMethod.invoke(target);
				} catch(Exception e) {
					Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Looper.getMainLooper().getThread(), e);
				}
				mProgressChangedMethod.clearArgs();
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			Object target = mTargetRef.get();
			if(target != null && mStartTouchMethod != null) {
				Object[] args = new Object[]{seekBar};
				mStartTouchMethod.adaptArguments(args);
				try {
					mStartTouchMethod.invoke(target);
				} catch(Exception e) {
					Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Looper.getMainLooper().getThread(), e);
				}
				mStartTouchMethod.clearArgs();
			}
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			Object target = mTargetRef.get();
			if(target != null && mStopTouchMethod != null) {
				Object[] args = new Object[]{seekBar};
				mStopTouchMethod.adaptArguments(args);
				try {
					mStopTouchMethod.invoke(target);
				} catch(Exception e) {
					Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Looper.getMainLooper().getThread(), e);
				}
				mStopTouchMethod.clearArgs();
			}
		}
	}
}
