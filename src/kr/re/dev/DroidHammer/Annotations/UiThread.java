package kr.re.dev.DroidHammer.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UiThread {
	int id() default -1;
	int delay() default 0;
	/**
	 * 기본 값은 false. false 일 경우 {@link UiThread} 어노테이션을 사용한 메소드를 UiThread 에서 호출 하였을 때, 작업 큐에 추가하지 않는다.
	 * 하지만, true 를 사용할경우 항상 작업큐의 마지막에 메소드 작업을 추가한다. 
	 * @return
	 */
	boolean allawaysAddLast() default false;
}
 