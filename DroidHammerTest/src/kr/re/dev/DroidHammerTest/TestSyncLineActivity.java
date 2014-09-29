package kr.re.dev.DroidHammerTest;

import kr.re.dev.DroidHammer.Annotations.Background;
import kr.re.dev.DroidHammer.Annotations.UiThread;
import kr.re.dev.DroidHammer.Sync.SyncLine;
import android.app.Activity;
import android.os.Bundle;

public class TestSyncLineActivity  extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}
	
	
	public int sum = 0;
	@UiThread
	public  void runThread() {
		if(SyncLine.run(this)) return;
		sum = 100;
	}
	@Background(sync = true)
	public  void runThread(int sum) {
		if(SyncLine.run(this,sum)) return;
		this.sum = sum;
	}
}
