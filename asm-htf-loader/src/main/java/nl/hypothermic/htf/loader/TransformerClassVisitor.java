package nl.hypothermic.htf.loader;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TransformerClassVisitor extends ClassVisitor {

	private final String className;
	private final MethodTransformerImpl transformer;

	public TransformerClassVisitor(String className, ClassVisitor classVisitor, MethodTransformerImpl transformer) {
		super(Opcodes.ASM4, classVisitor);
		this.className = className;
		this.transformer = transformer;
	}

	@Override
	public MethodVisitor visitMethod(int methodAccess, String methodName, String methodDesc, String signature, String[] exceptions) {
		MethodVisitor methodVisitor = cv.visitMethod(methodAccess, methodName, methodDesc, signature, exceptions);

		if (methodName.equals(transformer.getAnnotation().targetMethodName())
				&& methodDesc.equals(transformer.getAnnotation().targetMethodDescription())) {
			L.i("%s is transforming %s:%s:%s", transformer.getClazz().getSimpleName(), className, methodName, methodDesc);
			try {
				return (MethodVisitor) transformer.getClazz()
						.getConstructor(int.class, String.class, String.class, String.class, String[].class, MethodVisitor.class)
						.newInstance(methodAccess, methodName, methodDesc, signature, exceptions, methodVisitor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return methodVisitor;
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		if (className.equals("net/minecraft/client/Minecraft") && name.equals("a")) {
			return cv.visitField(Opcodes.ACC_STATIC + Opcodes.ACC_PUBLIC, name, desc, signature, value);
		}

		return cv.visitField(access, name, desc, signature, value);
	}
}
