package kr.re.dev.DroidHammerTest;

import kr.re.dev.DroidHammer.Annotations.Click;
import kr.re.dev.DroidHammer.Annotations.ViewById;
import kr.re.dev.DroidHammer.LikeAA;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class TestFragment extends Fragment{
	
	@ViewById(R.id.seekBar1)
	public SeekBar mseekBar1;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar2;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar3;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar4;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar5;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar6;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar7;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar8;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar9;
	@ViewById(R.id.seekBar1)
	public SeekBar mseekBar10;
	@ViewById(R.id.seekBar1)
	public SeekBar mseekBar11;
	@ViewById(R.id.seekBar1)
	public SeekBar mseekBar12;
	@ViewById(R.id.seekBar1)
	public SeekBar mseekBar13;
	@ViewById(R.id.seekBar1)
	public SeekBar mseekBar14;
	@ViewById(R.id.seekBar1)
	public SeekBar mseekBar15;
	@ViewById(R.id.seekBar1)
	public SeekBar mseekBar16;
	@ViewById(R.id.seekBar1)
	public SeekBar mseekBar17;
	@ViewById(R.id.seekBar1)
	public SeekBar mseekBar18;
	@ViewById(R.id.seekBar1)
	public SeekBar mseekBar19;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar20;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar21;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar22;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar23;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar24;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar25;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar26;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar27;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar28;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar29;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar30;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar31;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar32;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar33;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar34;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar35;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar37;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar38;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar39;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar40;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar41;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar42;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar43;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar44;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar45;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar46;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar47;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar48;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar49;
	@ViewById(R.id.seekBar1)
	public SeekBar mSeekBar50;
	
	private LikeAA mMapper;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_test, null);
	}
	long startTime = 0, endTime = 0;
	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getActivity().findViewById(R.id.seekBar1);
		View view = getView();
		SeekBar SeekBarView = null;
		startTime = System.nanoTime();
		for(int i = 0; i < 50; ++i) {
			SeekBarView = (SeekBar) view.findViewById(R.id.seekBar1);
			SeekBarView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
		}
		endTime = System.nanoTime();
		System.out.println("Use findViewById Method : " + ((endTime - startTime) / 1000.0f / 1000.0f) + "ms");
		startTime = System.nanoTime();
		mMapper = LikeAA.inject(this);
		endTime = System.nanoTime();
		System.out.println("Use LikeAA : " + ((endTime - startTime) / 1000.0f / 1000.0f) + "ms");
		
		startTime = System.nanoTime();
		mMapper = LikeAA.inject(this);
		endTime = System.nanoTime();
		System.out.println("use2 LikeAA : " + ((endTime - startTime) / 1000.0f / 1000.0f) + "ms");
		
		super.onActivityCreated(savedInstanceState);
	}
	
	
	@Click(R.id.seekBar1)
	public void click11() {
		
	}
	@Click(R.id.seekBar1)
	public void click121() {
		
	}
	@Click(R.id.seekBar1)
	public void click311() {
		
	}
	@Click(R.id.seekBar1)
	public void click141() {
		
	}
	@Click(R.id.seekBar1)
	public void click151() {
		
	}
	@Click(R.id.seekBar1)
	public void click161() {
		
	}
	@Click(R.id.seekBar1)
	public void click1111() {
		
	}
	@Click(R.id.seekBar1)
	public void click1111117() {
		
	}
	@Click(R.id.seekBar1)
	public void click111117() {
		
	}
	@Click(R.id.seekBar1)
	public void click11111117() {
		
	}
	@Click(R.id.seekBar1)
	public void click1111111177() {
		
	}
	@Click(R.id.seekBar1)
	public void click111111111777() {
		
	}
	@Click(R.id.seekBar1)
	public void click11111111117777() {
		
	}
	@Click(R.id.seekBar1)
	public void click11111111111888() {
		
	}
	@Click(R.id.seekBar1)
	public void click122222227() {
		
	}
	@Click(R.id.seekBar1)
	public void click122227() {
		
	}
	@Click(R.id.seekBar1)
	public void click12227() {
		
	}
	@Click(R.id.seekBar1)
	public void click1227() {
		
	}
	@Click(R.id.seekBar1)
	public void click1337() {
		
	}
	
	@Click(R.id.seekBar1)
	public void click13337() {
		
	}
	@Click(R.id.seekBar1)
	public void click133337() {
		
	}
	@Click(R.id.seekBar1)
	public void click13333377() {
		
	}
	@Click(R.id.seekBar1)
	public void click1333333777() {
		
	}
	@Click(R.id.seekBar1)
	public void click13333333777() {
		
	}
	@Click(R.id.seekBar1)
	public void click1333333337777() {
		
	}
	@Click(R.id.seekBar1)
	public void click1() {
		
	}
	@Click(R.id.seekBar1)
	public void click12() {
		
	}
	@Click(R.id.seekBar1)
	public void click31() {
		
	}
	@Click(R.id.seekBar1)
	public void click14() {
		
	}
	@Click(R.id.seekBar1)
	public void click15() {
		
	}
	@Click(R.id.seekBar1)
	public void click16() {
		
	}
	@Click(R.id.seekBar1)
	public void click111() {
		
	}
	@Click(R.id.seekBar1)
	public void click11111() {
		
	}
	@Click(R.id.seekBar1)
	public void click111111() {
		
	}
	@Click(R.id.seekBar1)
	public void click1111111() {
		
	}
	@Click(R.id.seekBar1)
	public void click11111111() {
		
	}
	@Click(R.id.seekBar1)
	public void click111111111() {
		
	}
	@Click(R.id.seekBar1)
	public void click1111111111() {
		
	}
	@Click(R.id.seekBar1)
	public void click11111111111() {
		
	}
	@Click(R.id.seekBar1)
	public void click12222222() {
		
	}
	@Click(R.id.seekBar1)
	public void click12222() {
		
	}
	@Click(R.id.seekBar1)
	public void click1222() {
		
	}
	@Click(R.id.seekBar1)
	public void click122() {
		
	}
	@Click(R.id.seekBar1)
	public void click133() {
		
	}
	
	@Click(R.id.seekBar1)
	public void click1333() {
		
	}
	@Click(R.id.seekBar1)
	public void click13333() {
		
	}
	@Click(R.id.seekBar1)
	public void click133333() {
		
	}
	@Click(R.id.seekBar1)
	public void click1333333() {
		
	}
	@Click(R.id.seekBar1)
	public void click13333333() {
		
	}
	@Click(R.id.seekBar1)
	public void click133333333() {
		
	}
	

	
}
