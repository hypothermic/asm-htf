package nl.hypothermic.htf.utils;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.function.Consumer;

public class IfStatementBuilder {

	private Consumer<MethodVisitor> onLoad, onTrue, onFalse;
	private int opcode;

	public static IfStatementBuilder newBuilder() {
		return new IfStatementBuilder();
	}

	private IfStatementBuilder() {

	}

	public void onLoad(Consumer<MethodVisitor> onLoad) {
		this.onLoad = onLoad;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public void onTrue(Consumer<MethodVisitor> onTrue) {
		this.onTrue = onTrue;
	}

	public void onFalse(Consumer<MethodVisitor> onFalse) {
		this.onFalse = onFalse;
	}

	public void insert(MethodVisitor methodVisitor) {
		Label trueLabel = new Label(),
			  afterFalseLabel = new Label();

		// --- if

		// load vars for jump
		onLoad.accept(methodVisitor);

		// do the jump with the specified opcode
		methodVisitor.visitJumpInsn(opcode, trueLabel);

		// --- true

		onTrue.accept(methodVisitor);

		methodVisitor.visitJumpInsn(Opcodes.GOTO, afterFalseLabel);
		methodVisitor.visitLabel(trueLabel);

		// --- false

		onFalse.accept(methodVisitor);

		methodVisitor.visitLabel(afterFalseLabel);

		// --- merge
	}
}
