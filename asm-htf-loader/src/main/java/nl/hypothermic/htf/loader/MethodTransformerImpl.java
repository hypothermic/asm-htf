package nl.hypothermic.htf.loader;

import lombok.Getter;
import nl.hypothermic.htf.api.MethodTransformer;

public class MethodTransformerImpl {

	@Getter
	private final MethodTransformer annotation;

	@Getter
	private final Class<?> clazz;

	public MethodTransformerImpl(Class<?> clazz) {
		this.annotation = clazz.getAnnotation(MethodTransformer.class);
		this.clazz = clazz;
	}

}
