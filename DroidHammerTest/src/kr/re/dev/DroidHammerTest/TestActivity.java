package kr.re.dev.DroidHammerTest;


import kr.re.dev.DroidHammer.Annotations.Inject;
import kr.re.dev.DroidHammer.Annotations.Resource;
import kr.re.dev.DroidHammer.Annotations.ViewById;
import kr.re.dev.DroidHammer.LikeAA.LikeAA;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.ToggleButton;

public class TestActivity extends FragmentActivity {

	
	public @ViewById(R.id.button2) Button mButton1;
	public @ViewById(R.id.button2) Button mButton2;
	public @ViewById(R.id.button2) ListView mListViewNull1;
	public @ViewById(R.id.toggleButton1) ToggleButton mTommle1;
	public @ViewById(R.id.progressBar1) ProgressBar mPromress1;
	public @ViewById(R.id.radioButton1) RadioButton mRadioButton1;
	public @ViewById(R.id.ratingBar1) RatingBar mRadinmBar1;
	public @ViewById(R.id.listView1) ListView mListView1;
	
	private @Resource(R.string.app_name) String mAppName;
	public @Resource(R.anim.anim_test) Animation mTestAnimation;
	public @Resource(R.drawable.shot) Drawable mLauncherDrawable;
	public @Resource(R.string.app_name) Drawable mMissMatche;
	
	public @Inject WindowManager mSystemServiceWindowManager;
	public @Inject LayoutInflater  mSystemServiceLayoutInflater;
	public @Inject  ActivityManager  mSystemServiceActivityManager;
	public @Inject PowerManager   mSystemServicePowerManager;
	public @Inject  AlarmManager 	 mSystemServiceAlarmManager;
	public @Inject  NotificationManager  mSystemServiceNotificationManager;
	public @Inject KeyguardManager    mSystemServiceKeyguardManager;
	public @Inject LocationManager  mSystemServiceLocationManager;
	public @Inject SearchManager   mSystemServiceSearchManager;
	public @Inject Vibrator    mSystemServiceVibrator;
	public @Inject ConnectivityManager   mSystemServiceConnectivityManager;
	public @Inject WifiManager     mSystemServiceWifiManager;
	public @Inject  InputMethodManager   mSystemServiceInputMethodManager;
	public @Inject  UiModeManager     mSystemServiceUiModeManager;
	public @Inject  DownloadManager   mSystemServiceDownloadManager;
	public @Inject  TelephonyManager  mSystemServiceTelephonyManager;
	
	
	public LikeAA mMapper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_test);
		
		mMapper = LikeAA.inject(this);
		getSupportFragmentManager().beginTransaction().add(R.id.content, new TestFragment()).commit();
	}
}
