package kr.re.dev.DroidHammer;

import java.lang.reflect.Method;

import kr.re.dev.DroidHammer.SyncLine.Option;

public class RunnableMethodHolder extends MethodHolder implements Runnable {
	
	private String mThreadName = "";	
	private Option mOption;
		
	protected RunnableMethodHolder(Method method) {
		super(method);
		
	}
		
	
	@Override
	public void run() {
		
	}

}
