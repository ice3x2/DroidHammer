/**
 * 
 */
package kr.re.dev.DroidHammerTest;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import kr.re.dev.DroidHammer.Annotations.AfterViews;
import kr.re.dev.DroidHammer.Annotations.ViewById;
import kr.re.dev.DroidHammer.LikeAA;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.ToggleButton;

/**
 * @author ice3x2
 * LikeAA 테스트.
 * 홍성범 
 * 
 */


/**
 * 테스트 A. @ViewById 테스트. 
 *   - Activity 와 Fragment, View 내부에서 @ViewById 를 이용하여 View 객체들이 잘 주입되는지 테스트 한다. 
 *   
 * @author ice3x2
 *
 */

public class LikeAATest extends ActivityInstrumentationTestCase2<TestActivity> {

	TestActivity mActivity;
	Instrumentation mInstrumentation;
	TestLooper mTestLooper = new TestLooper();
	TestFragment mFragment = new TestFragment();
	
	private @ViewById(R.id.button2) Button gButton1;
	private @ViewById(R.id.button2) Button gButton2;
	private @ViewById(R.id.button2) ListView gListViewNull1;
	private @ViewById(R.id.toggleButton1) ToggleButton gToggle1;
	private @ViewById(R.id.progressBar1) ProgressBar gProgress1;
	private @ViewById(R.id.radioButton1) RadioButton gRadioButton1;
	private @ViewById(R.id.ratingBar1) RatingBar gRadingBar1;
	private @ViewById(R.id.listView1) ListView gListView1;

	
	

	private LikeAA gLikeAA;
	 
	
	public LikeAATest() {
		super(TestActivity.class);
	}
	
	public void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		mInstrumentation = getInstrumentation();
	}
	
	
	@SuppressLint("NewApi")
	@SmallTest 
	public void testInjectRes() {
		gLikeAA = LikeAA.inject(mActivity);
		assertEquals(getObject(mActivity, "mAppName"), mActivity.getString(R.string.app_name));
		assertNotNull(mActivity.mLauncherDrawable);
		assertEquals(((BitmapDrawable)mActivity.mLauncherDrawable).getBitmap(), ((BitmapDrawable)mActivity.getResources().getDrawable(R.drawable.shot)).getBitmap());
		assertNotNull(mActivity.mTestAnimation);
		assertNull(mActivity.mMissMatche);
		
		assertNotNull(mActivity.mSystemServiceActivityManager);
		assertNotNull(mActivity.mSystemServiceAlarmManager);
		assertNotNull(mActivity.mSystemServiceConnectivityManager);
		assertNotNull(mActivity.mSystemServiceDownloadManager);
		assertNotNull(mActivity.mSystemServiceInputMethodManager);
		assertNotNull(mActivity.mSystemServiceKeyguardManager);
		assertNotNull(mActivity.mSystemServiceLayoutInflater);
		assertNotNull(mActivity.mSystemServiceLocationManager);
		assertNotNull(mActivity.mSystemServiceNotificationManager);
		assertNotNull(mActivity.mSystemServicePowerManager);
		assertNotNull(mActivity.mSystemServiceSearchManager);
		assertNotNull(mActivity.mSystemServiceUiModeManager);
		assertNotNull(mActivity.mSystemServiceVibrator);
		assertNotNull(mActivity.mSystemServiceWifiManager);
		assertNotNull(mActivity.mSystemServiceWindowManager);
		
		assertEquals(mActivity.mSystemServiceActivityManager.getClass(), ActivityManager.class );
		assertEquals(mActivity.mSystemServiceAlarmManager.getClass(), AlarmManager.class);
		assertEquals(mActivity.mSystemServiceConnectivityManager.getClass(), ConnectivityManager.class);
		assertEquals(mActivity.mSystemServiceDownloadManager.getClass(), DownloadManager.class);
		assertEquals(mActivity.mSystemServiceInputMethodManager.getClass(), InputMethodManager.class);
		assertEquals(mActivity.mSystemServiceKeyguardManager.getClass(), KeyguardManager.class);
		assertEquals(mActivity.mSystemServiceLayoutInflater, mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		assertEquals(mActivity.mSystemServiceLocationManager.getClass(), LocationManager.class);
		assertEquals(mActivity.mSystemServiceNotificationManager.getClass(), NotificationManager.class);
		assertEquals(mActivity.mSystemServicePowerManager.getClass(), PowerManager.class);
		assertEquals(mActivity.mSystemServiceSearchManager.getClass(), SearchManager.class);
		assertEquals(mActivity.mSystemServiceUiModeManager.getClass(),UiModeManager.class );
		assertNotNull(mActivity.mSystemServiceVibrator.getClass().isAssignableFrom(Vibrator.class));
		assertEquals(mActivity.mSystemServiceWifiManager.getClass(), WifiManager.class);
		assertEquals(mActivity.mSystemServiceTelephonyManager.getClass(), TelephonyManager.class);
		assertNotNull(mActivity.mSystemServiceWindowManager.getClass().isAssignableFrom(WindowManager.class));
		
		System.out.println(A.B.C.class.getDeclaredMethods()[0].toString());
		A a = new A();
		a.b.c.run();
		
		
	}
	
	public static class A {
		B b = new B();
		public static class B {
			C c = new C();
			public static class C{
				public void run() {
					StackTraceElement[] ste = Thread.currentThread().getStackTrace();
					for(StackTraceElement el : ste) {
						System.out.println(el.toString());
					}
					
				}
			}
		}
		
	}
	
	
	
	
	
	
	@SmallTest
	public void testPreconditions() {
		assertNotNull(mActivity);
		assertNotNull(mInstrumentation);
	}
	
	@SmallTest
	public void testMapping() {
		gLikeAA = LikeAA.inject(mActivity.getWindow().getDecorView(), this);
		assertNotNull(gLikeAA);
		mTestLooper.loop();
	}
	
	/**
	** @ViewById 가 잘 동작하는지 테스트 한다. 
	*/
	@AfterViews 
	public void afterViews(Object obj, int g) {
		mTestLooper.pushTest(new Runnable() {
			@Override
			public void run() {
				assertEquals(gButton1, mActivity.findViewById(R.id.button2));
				assertEquals(gButton1, mActivity.mButton1);
				assertEquals(gButton2, mActivity.findViewById(R.id.button2));
				assertEquals(gButton2, mActivity.mButton2);
				assertEquals(gListView1, mActivity.findViewById(R.id.listView1));
				assertEquals(gListView1, mActivity.mListView1);
				assertEquals(gListViewNull1, mActivity.mListViewNull1);
				assertEquals(gProgress1, mActivity.mPromress1);
				assertEquals(gRadingBar1, mActivity.mRadinmBar1);
				assertEquals(gRadioButton1, mActivity.mRadioButton1);
				assertEquals(gToggle1, mActivity.mTommle1);
				
			}
		});
		mTestLooper.unLoop();
		
	}
	
	@SmallTest
	public void testInjectViewOnFragment() {
		mActivity.getSupportFragmentManager().beginTransaction().add(R.id.content, mFragment, "TestFragment").commitAllowingStateLoss();
		mActivity.getWindow().getDecorView().postDelayed(new Runnable() {
			public void run() {
				mTestLooper.pushTest(new Runnable() {
					@Override
					public void run() {
						assertNotNull(mFragment.mseekBar1);
						assertEquals(mFragment.mSeekBar50, mFragment.getView().findViewById(R.id.seekBar1));
					}
				});
				mTestLooper.unLoop();
			};
		}, 200);
		mTestLooper.loop();
	}
	
	@SmallTest
	public void testInjectViewOnDialog() {
		final TestDialog dlg = new TestDialog(mActivity);
		dlg.show();
		mActivity.getWindow().getDecorView().postDelayed(new Runnable() {
			@Override
			public void run() {
				mTestLooper.pushTest(new Runnable() {
					@Override
					public void run() {
						assertNotNull(dlg.mButton);
						assertEquals(dlg.mButton, dlg.findViewById(R.id.button2));
					}
				});
				mTestLooper.unLoop();
				
			}
		}, 1);
		mTestLooper.loop();
	}
	
	
	public Object getObject(Object target, String name) {
		try {
			Field field = target.getClass().getDeclaredField(name);
			boolean accessible = field.isAccessible();
			if(!accessible) field.setAccessible(true);
			Object obj = field.get(target);
			if(!accessible) field.setAccessible(false);
			return obj;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

	 /**(non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 **/
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public static class TestLooper {
		private AtomicBoolean isLoop = new AtomicBoolean(false);
		private AtomicBoolean isFlush = new AtomicBoolean(false);
		private LinkedList<Runnable> mRunnableList = new LinkedList<Runnable>();
		
		public void loop() {
			isLoop.set(true);
			while(isLoop.get()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(isFlush.get()) {
				while(!mRunnableList.isEmpty()) mRunnableList.removeFirst().run();
				isFlush.set(false);
				mRunnableList.clear();
			}
		}
		public void pushTest(Runnable run) {
			mRunnableList.add(run);
		}
		
		public void unLoop() {
			isLoop.set(false);
			isFlush.set(true);
		}
	}

	

}
