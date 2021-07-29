package nl.hypothermic.htf.api;

import java.lang.reflect.Method;

public final class Gateways {

	private static final Method getterMethod;

	private Gateways() {
		throw new UnsupportedOperationException("You cannot create new objects of this class");
	}

	public static Object get(long callbackId, Object data) {
		// try-catch paradise
		try {
			return getterMethod.invoke(null, callbackId, data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static {
		try {
			getterMethod = Class.forName("nl.hypothermic.htf.loader.GatewayManager")
					.getMethod("get", long.class, Object.class);
		} catch (Exception e) {
			throw new RuntimeException("You didn't add asm-htf-loader to your classpath! TODO handle this error properly", e);
		}
	}
}
