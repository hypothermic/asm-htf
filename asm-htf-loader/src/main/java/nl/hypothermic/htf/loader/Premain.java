package nl.hypothermic.htf.loader;

import java.lang.instrument.Instrumentation;

public final class Premain {

	public static void premain(String args, Instrumentation instrumentation) {
		L.i("NotAHack Loader v%s", Premain.class.getPackage().getImplementationVersion());

		AddonLoader.getInstance().load(args, instrumentation);
	}

	private Premain() {
		throw new RuntimeException();
	}

}
