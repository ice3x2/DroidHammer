package kr.re.dev.DroidHammerTest;

import java.util.concurrent.CountDownLatch;

import kr.re.dev.DroidHammer.Annotations.Background;
import kr.re.dev.DroidHammer.Annotations.UiThread;
import kr.re.dev.DroidHammer.Sync.SyncLine;

import org.junit.Before;
import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

public class SyncLineTest extends ActivityInstrumentationTestCase2<TestSyncLineActivity> {

	public int sum = 0;
	CountDownLatch latch = null;
	TestSyncLineActivity mActivity;
	public SyncLineTest() {
		super(TestSyncLineActivity.class);
	}

	@Before
	public void setUp() throws Exception {
		mActivity = getActivity();
	}

	@Test
	public void test() {
		runThread(100);
		assertEquals(sum, 200);
	}
	
	public void testDelay() {
		final long ms = System.currentTimeMillis();
		runThreadDelay(new Runnable() {
			@Override
			public void run() {
				long delta = System.currentTimeMillis() - ms;
				assertTrue(delta > 3000 && delta < 3300);
			}
		});
	}
	
	@UiThread(sync = true,delay = 3000)
	public  void runThreadDelay(Runnable run) {
		if(SyncLine.run(this,run)) return;
		run.run();
	}
	
	@UiThread(sync = true)
	public  void runThread(int sumA, int sumB) {
		if(SyncLine.run(this,sumA, sumB)) return;
		sum = sumA + sumB;
	}
	@Background(sync = true)
	public  void runThread(Integer sum) {
		if(SyncLine.run(this,sum)) return;
		runThread(sum, sum);
	}
	
	
}
