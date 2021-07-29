package nl.hypothermic.htf.loader;

import java.util.Comparator;

public class MethodTransformerPriorityComparator implements Comparator<MethodTransformerImpl> {

	@Override
	public int compare(MethodTransformerImpl o1, MethodTransformerImpl o2) {
		return sort(o1, o2);
	}

	public static int sort(MethodTransformerImpl o1, MethodTransformerImpl o2) {
		return -o1.getAnnotation().priority().compareTo(o2.getAnnotation().priority());
	}
}
