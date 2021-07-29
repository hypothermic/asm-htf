package nl.hypothermic.htf.processor;

import nl.hypothermic.htf.api.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;

public enum AnnotationIndexFile {

	METHOD_TRANSFORMER_INDEX(MethodTransformer.class),
	CLASS_TRANSFORMER_INDEX(ClassTransformer.class),
	GATEWAY_INDEX(Gateway.class),

	;

	private static final String RELATIVE_INDEX_PATH = "META-INF/asm-htf-addons/";

	public static AnnotationIndexFile getIndexFor(Class<? extends Annotation> annotationType) {
		for (AnnotationIndexFile indexFile : values()) {
			if (annotationType.equals(indexFile.annotationType)) {
				return indexFile;
			}
		}
		throw new IllegalArgumentException("No index file for class " + annotationType.getName());
	}

	private final Class<? extends Annotation> annotationType;
	private Writer resourceWriter;

	AnnotationIndexFile(Class<? extends Annotation> annotationType) {
		this.annotationType = annotationType;
	}

	public void open(ProcessingEnvironment processingEnvironment) throws IOException {
		resourceWriter = processingEnvironment.getFiler().createResource(
				StandardLocation.CLASS_OUTPUT,
				"",
				RELATIVE_INDEX_PATH + annotationType.getCanonicalName()
			).openWriter();
	}

	public void close() throws IOException {
		resourceWriter.close();
	}

	public void addTargetClassName(String targetClassName) throws IOException {
		resourceWriter
				.append(targetClassName)
				.append(":");
	}
}
