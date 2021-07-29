package nl.hypothermic.htf.loader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class DynamicMethodTransformer implements ClassFileTransformer {

	private final MethodTransformerImpl transformer;

	public DynamicMethodTransformer(MethodTransformerImpl transformer) {
		this.transformer = transformer;
		L.i("Dyn method transformer %s", transformer.getClazz().getSimpleName());
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
		if (className.equals(transformer.getAnnotation().targetClass())) {
			ClassReader classReader = new ClassReader(classfileBuffer);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = new TransformerClassVisitor(className, classWriter, transformer);
			classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

			return classWriter.toByteArray();
		}

		return classfileBuffer;
	}
}
