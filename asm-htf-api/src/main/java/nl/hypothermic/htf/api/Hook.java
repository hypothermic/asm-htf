package nl.hypothermic.htf.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Hook {

	String targetClass();

	String targetMethodName();

	String targetMethodDescription();

	InsertionPoint insertionPoint();

	Priority priority() default Priority.NORMAL;

}