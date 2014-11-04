package kr.re.dev.DroidHammerTest;


import kr.re.dev.DroidHammer.Annotations.ViewById;
import kr.re.dev.DroidHammer.LikeAA.LikeAA;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

public class TestDialog extends Dialog{
	
	@ViewById(R.id.button2)
	public Button mButton;
	public LikeAA mMapper;
	public TestDialog(Context context) {
		super(context);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_test);
		mMapper = LikeAA.inject(this);
	}
	

}
