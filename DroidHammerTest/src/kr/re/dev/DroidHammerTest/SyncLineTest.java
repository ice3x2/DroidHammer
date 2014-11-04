package kr.re.dev.DroidHammerTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import kr.re.dev.DroidHammer.Annotations.Background;
import kr.re.dev.DroidHammer.Annotations.UiThread;
import kr.re.dev.DroidHammer.Sync.SyncLine;

import org.junit.Before;

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

	/*@Test
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
	}*/
	
	public void testInnerClass() {
		AtomicInteger integer =  new AtomicInteger();
		//new E().exe(integer);
		/*assertEquals(integer.get(), 6000);
		new E().exe((int)new Integer(300),integer);*/
		//assertEquals(integer.get(), 10000);
		new E().exe(new Integer(300),integer);
		assertEquals(integer.get(), 20000);
		
		new E().exe((float)new Integer(300),integer);
		assertEquals(integer.get(), 10000);
		
		/*new E().exe(new Integer(300),integer);
		assertEquals(integer.get(), 20000);*/
		
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
	
	private class E extends D {
		@Background(sync = true)
		public void exe(Integer value,AtomicInteger ai) {
			if(SyncLine.run(this,value, ai)) return;
			ai.set(20000);
		}
		@Background(sync = true)
		public void exe(float value,AtomicInteger ai) {
			if(SyncLine.run(this,value, ai)) return;
			ai.set(10000);
		}
	}
	private class D extends C {
		@Background(sync = true)
		public void exe(AtomicInteger value) {
			if(SyncLine.run(this,value)) return;
			((AtomicInteger)value).set(6000);
		}
		@Background(sync = true)
		public void exe(Number value) {
			
			if(SyncLine.run(this,value)) return;
			
			
			
			((AtomicInteger)value).set(7000);
		}
		
	}
	private class C extends B {
		@Background(sync = true)
		public void exe(AtomicInteger value) {
			if(SyncLine.run(this,value)) return;
			value.set(40000);
		}
	}
	private class B extends A {
		
	}
	private class A {
		@Background(sync = true)
		public void exe(AtomicInteger value) {
			if(SyncLine.run(this,value)) return;
			value.set(10000);
		}
	}
	
	
}
