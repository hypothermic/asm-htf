package nl.hypothermic.htf.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface MethodTransformer {

	String targetClass();

	String targetMethodName();

	String targetMethodDescription();

	Priority priority() default Priority.NORMAL;

}
