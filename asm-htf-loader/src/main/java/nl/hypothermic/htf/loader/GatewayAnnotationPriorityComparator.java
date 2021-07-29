package nl.hypothermic.htf.loader;


import java.util.Comparator;

public class GatewayAnnotationPriorityComparator implements Comparator<GatewayMethod> {

	@Override
	public int compare(GatewayMethod o1, GatewayMethod o2) {
		return sort(o1, o2);
	}

	public static int sort(GatewayMethod o1, GatewayMethod o2) {
		return -o1.getAnnotation().priority().compareTo(o2.getAnnotation().priority());
	}
}
