package kr.re.dev.DroidHammer.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Background {
	int id() default -1;
	int delay() default 0;
	/**
	 * {@link UiThread} 와 싱크를 맞춘다. 만약  {@link Background} 어노테이션을 사용한 메소드에서 {@link UiThread} 어노테이션을 사용한 메소드를 호출한다면
	 * {@link Background} 어노테이션의 메소드가 끝난 다음 호출한 {@link UiThread} 의 메소드가 호출된다.
	 * @return default 값 true
	 */
	boolean sync() default true;
	boolean demon() default false;
	boolean await() default false;
	boolean allawaysAddLast() default false;
}