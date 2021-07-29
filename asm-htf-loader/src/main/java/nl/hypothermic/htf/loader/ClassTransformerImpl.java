package nl.hypothermic.htf.loader;

import lombok.Getter;
import nl.hypothermic.htf.api.ClassTransformer;

public class ClassTransformerImpl {

	@Getter
	private final ClassTransformer annotation;

	@Getter
	private final Class<?> clazz;

	public ClassTransformerImpl(Class<?> clazz) {
		this.annotation = clazz.getAnnotation(ClassTransformer.class);
		this.clazz = clazz;
	}

}
