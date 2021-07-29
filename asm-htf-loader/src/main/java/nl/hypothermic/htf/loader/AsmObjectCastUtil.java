package nl.hypothermic.htf.loader;

/*
 * Casting primitives to Objects in bytecode is really fucking hard,
 * So here's a few simple static methods to do it.
 *
 * 1. Load primitive at the top of the stack
 * 2. INVOKESTATIC nl/hypothermic/notahack/loader/AsmObjectCastUtil.cast ({type})Ljava/lang/Object;
 * 3. Your new Object is at the top of the stack!
 */
public final class AsmObjectCastUtil {

	private AsmObjectCastUtil() {
		throw new UnsupportedOperationException();
	}

	public static Object toObject(int value) {
		return value;
	}

	public static int toInt(Object value) {
		return (int) value;
	}

	public static float toFloat(Object value) {
		return (float) value;
	}

	public static boolean toBool(Object value) {
		return (boolean) value;
	}

	public static int toInt(boolean value) {
		return (boolean) value ? 1 : 0;
	}
}
