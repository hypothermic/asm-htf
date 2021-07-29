package nl.hypothermic.htf.loader;

import java.io.File;
import java.time.LocalDateTime;
import java.util.logging.Level;

/**
 * Really simple logging class cuz slf4j and logback and logger all sux
 */
public final class L {

	public static void i(String message, Object... args) {
		write("INFO", message, args);
	}

	public static void e(Exception e, String message, Object... args) {
		write("ERROR", message, args);
		e.printStackTrace();
	}

	public static void m(Level level, String message, Object... args) {
		write(level.getName(), message, args);
	}

	private static void write(String prefix, String message, Object... args) {
		String a = String.format(message, args);
		String f = String.format("[%s][%12s] %s", LocalDateTime.now(), prefix, a);
		System.err.println(f);
		System.err.flush();
	}

	public static String getMostDescriptivePath(File file) {
		try {
			return file.getCanonicalPath();
		} catch (Exception e) {
			return file.getPath();
		}
	}
}
