package nl.hypothermic.htf.loader;

import lombok.SneakyThrows;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class DynamicClassTransformer implements ClassFileTransformer {

	private final ClassTransformerImpl transformer;

	public DynamicClassTransformer(ClassTransformerImpl transformer) {
		this.transformer = transformer;
		L.i("Dyn class transformer %s", transformer.getClazz().getSimpleName());
	}

	@SneakyThrows
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
		if (className.equals(transformer.getAnnotation().targetClass())) {
			ClassReader classReader = new ClassReader(classfileBuffer);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
			ClassVisitor classVisitor = (ClassVisitor) transformer.getClazz().getConstructor(ClassVisitor.class).newInstance(classWriter);
			classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

			return classWriter.toByteArray();
		}

		return classfileBuffer;
	}
}
