package nl.hypothermic.htf.example;

import nl.hypothermic.htf.api.MethodTransformer;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

/*
 * This example project shows you how you can modify Integer.valueOf(String) to return the value '69' all the time.
 */
@MethodTransformer(
		targetClass = "java/lang/Integer",
		targetMethodName = "valueOf",
		targetMethodDescription = "(Ljava/lang/String;)I"
)
@SuppressWarnings("unused")
public class ExampleMethodTransformer extends MethodNode {

	public ExampleMethodTransformer(int access, String name, String desc, String signature, String[] exceptions, MethodVisitor mv) {
		super(Opcodes.ASM4, access, name, desc, signature, exceptions);
		this.mv = mv;
	}

	@Override
	public void visitCode() {
		visitLdcInsn(69);
		visitInsn(Opcodes.IRETURN);

		visitMaxs(0, 0);
		visitEnd();
	}
}
