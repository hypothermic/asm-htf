package nl.hypothermic.htf.processor;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

@SupportedAnnotationTypes("nl.hypothermic.htf.api.*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
@SuppressWarnings("unused")
public class MethodTransformerAnnotationProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		annotations.stream()
				.map(MethodTransformerAnnotationProcessor::getAnnotationClassOrNull)
				.filter(Objects::nonNull)
				.forEach(annotationClass -> {
					AnnotationIndexFile indexFile = AnnotationIndexFile.getIndexFor(annotationClass);
					Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotationClass);

					try {
						indexFile.open(processingEnv);

						elements.forEach(element -> {
							String binaryName = getBinaryName(element);

							processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing class " + binaryName);
							try {
								indexFile.addTargetClassName(binaryName);
							} catch (IOException e) {
								processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
							}
						});

						indexFile.close();
					} catch (IOException e) {
						processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
						e.printStackTrace();
					}
				});

		return false;
	}

	private static String getBinaryName(Element element) {
		return getBinaryNameImpl(element, element.getSimpleName().toString());
	}

	@SuppressWarnings("UnstableApiUsage") // google-auto is stable enough
	private static String getBinaryNameImpl(Element element, String className) {
		Element enclosingElement = element.getEnclosingElement();

		if (enclosingElement instanceof PackageElement) {
			PackageElement pkg = MoreElements.asPackage(enclosingElement);

			if (pkg.isUnnamed()) {
				return className;
			}
			return pkg.getQualifiedName() + "." + className;
		}

		TypeElement typeElement = MoreElements.asType(enclosingElement);
		return getBinaryNameImpl(typeElement, typeElement.getSimpleName() + "$" + className);
	}

	private static Class<? extends Annotation> getAnnotationClassOrNull(Element element) {
		try {
			// Class loading isn't generic, so....
			//noinspection unchecked
			return (Class<? extends Annotation>) Class.forName(element.toString());
		} catch (ClassNotFoundException classNotFoundException) {
			classNotFoundException.printStackTrace();
			return null;
		}
	}
}
