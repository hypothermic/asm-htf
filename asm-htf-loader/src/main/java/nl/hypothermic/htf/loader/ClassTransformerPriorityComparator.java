package nl.hypothermic.htf.loader;

import java.util.Comparator;

public class ClassTransformerPriorityComparator implements Comparator<ClassTransformerImpl> {

	@Override
	public int compare(ClassTransformerImpl o1, ClassTransformerImpl o2) {
		return sort(o1, o2);
	}

	public static int sort(ClassTransformerImpl o1, ClassTransformerImpl o2) {
		return -o1.getAnnotation().priority().compareTo(o2.getAnnotation().priority());
	}
}
