package nl.hypothermic.htf.loader;

import lombok.Getter;
import nl.hypothermic.htf.api.Gateway;

import java.lang.reflect.Method;

public class GatewayMethod {

	@Getter
	private final String clazz, methodName;

	private Method method;
	private Gateway annotation;

	public GatewayMethod(String mergedString) {
		String[] splitString = mergedString.split("\\$");
		this.clazz = splitString[0];
		this.methodName = splitString[1];
	}

	public boolean isValid(ClassLoader classLoader) {
		try {
			Class<?> clazz = Class.forName(this.clazz, false, classLoader);
			method = clazz.getMethod(this.methodName, Object.class);
			annotation = method.getAnnotation(Gateway.class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Gateway getAnnotation() {
		return annotation;
	}

	public Method getMethod() {
		return method;
	}
}
