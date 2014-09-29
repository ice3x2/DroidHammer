package kr.re.dev.DroidHammer.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UiThread {
	int id() default 0;
	int delay() default 0;
	boolean single() default false;
	boolean sync() default true;
}
 