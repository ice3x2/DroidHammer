package kr.re.dev.DroidHammer.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Background {
	/**
	 * ID of Method
	 * @return default 0
	 */
	int id() default 0;
	/**
	 * 딜레이 시간을 정한다. 단위 밀리세컨드. ms
	 * @return default 0
	 */
	int delay() default 0;
	
	boolean single() default false;
	/**
	 * 만약 sync 를 true 로 정할경우, @UIThread 어노테이션이 붙은 메소드나 메인쓰레드 위에서 Background 메소드를 호출할 때, 
	 * 메소드가 끝날때까지 대기하고 있는다. 가능하면 사용하지 않는 것을 권장. 
	 * @return default false
	 */
	boolean sync() default false;
}