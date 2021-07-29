package nl.hypothermic.htf.api;

import java.util.function.Predicate;

public final class Predicates {

	public static <T> Predicate<T> not(Predicate<T> t) {
		return t.negate();
	}
}
