package nl.hypothermic.htf.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ClassTransformer {

	String targetClass();

	Priority priority() default Priority.NORMAL;
}
