package kr.re.dev.DroidHammer.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kr.re.dev.DroidHammer.AddMode;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UiThread {
	int id() default -1;
	int delay() default 0;
	AddMode addMode() default AddMode.Default;
	boolean sync() default true;
}
 