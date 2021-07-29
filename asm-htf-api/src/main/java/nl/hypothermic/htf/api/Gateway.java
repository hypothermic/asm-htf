package nl.hypothermic.htf.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Gateway {

	long id();

	Class<?> inputType() default Void.class;

	Class<?> outputType() default Void.class;

	Priority priority() default Priority.NORMAL;

}
