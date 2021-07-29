package nl.hypothermic.htf.utils;

import nl.hypothermic.htf.api.Gateway;
import nl.hypothermic.htf.api.Gateways;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.function.Function;

import static org.objectweb.asm.Opcodes.*;

public final class GatewayCreator {

	public static final String CAST_UTIL_CLASS = Type.getInternalName(AsmObjectCastUtil.class),
							   GATEWAYS_CLASS = Type.getInternalName(Gateways.class);

	public static InsnList create(Gateway gateway) {
		if (gateway.inputType() != Void.class) {
			throw new IllegalArgumentException("A parameter must be pushed onto the stack if you don't have Void as input type!");
		}
		return create(gateway, null);
	}

	public static InsnList create(Gateway gateway, InsnList loadInsns) {
		InsnList insnList = new InsnList();

		// The gateway "get" method takes 2 vars which we load onto the stack: the gatewayId and the Object arg
		insnList.add(new LdcInsnNode(gateway.id()));
		if (loadInsns != null && gateway.inputType() != Void.class) {
			insnList.add(loadInsns);
		}

		if (gateway.inputType().equals(Void.class)) {
			insnList.add(new InsnNode(ACONST_NULL));
		} else if (gateway.inputType().isPrimitive()) {
			if (gateway.inputType().equals(int.class)) {
				insnList.add(new MethodInsnNode(
						INVOKESTATIC,
						CAST_UTIL_CLASS,
						"toObject",
						"(I)Ljava/lang/Object;"
				));
			} else {
				throw new RuntimeException("Don't know how to handle type " + gateway.outputType());
			}
		}

		insnList.add(new MethodInsnNode(
				INVOKESTATIC,
				GATEWAYS_CLASS,
				"get",
				"(JLjava/lang/Object;)Ljava/lang/Object;"
		));

		if (gateway.outputType().equals(Void.class)) {
			insnList.add(new InsnNode(POP));
		} else if (gateway.outputType().isPrimitive()) {
			if (gateway.outputType().equals(boolean.class)) {
				insnList.add(new MethodInsnNode(
						INVOKESTATIC,
						CAST_UTIL_CLASS,
						"toBool",
						"(Ljava/lang/Object;)Z"
				));
			} else if (gateway.outputType().equals(float.class)) {
				insnList.add(new MethodInsnNode(
						INVOKESTATIC,
						CAST_UTIL_CLASS,
						"toFloat",
						"(Ljava/lang/Object;)F"
				));
			} else if (gateway.outputType().equals(int.class)) {
				insnList.add(new MethodInsnNode(
						INVOKESTATIC,
						CAST_UTIL_CLASS,
						"toInt",
						"(Ljava/lang/Object;)I"
				));
			} else {
				throw new RuntimeException("Don't know how to handle type " + gateway.outputType());
			}
		}
		return insnList;
	}

	public static void create(MethodVisitor methodVisitor, Gateway gateway) {
		if (gateway.inputType() != Void.class) {
			throw new IllegalArgumentException("A parameter must be pushed onto the stack if you don't have Void as input type!");
		}

		create(methodVisitor, gateway, childMethodVisitor -> {
			//childMethodVisitor.visitInsn(ACONST_NULL);
			return null;
		});
	}

	public static void create(MethodVisitor methodVisitor, Gateway gateway, Function<MethodVisitor, Void> argLoadSequence) {
		// The gateway "get" method takes 2 vars which we load onto the stack: the gatewayId and the Object arg
		methodVisitor.visitLdcInsn(gateway.id());
		argLoadSequence.apply(methodVisitor);

		if (gateway.inputType().equals(Void.class)) {
			methodVisitor.visitInsn(ACONST_NULL);
		} else if (gateway.inputType().isPrimitive()) {
			if (gateway.inputType().equals(int.class)) {
				methodVisitor.visitMethodInsn(
						INVOKESTATIC,
						CAST_UTIL_CLASS,
						"toObject",
						"(I)Ljava/lang/Object;"
				);
			} else {
				throw new RuntimeException("Don't know how to handle type " + gateway.outputType());
			}
		}

		methodVisitor.visitMethodInsn(
				INVOKESTATIC,
				GATEWAYS_CLASS,
				"get",
				"(JLjava/lang/Object;)Ljava/lang/Object;"
		);

		if (gateway.outputType().equals(Void.class)) {
			methodVisitor.visitInsn(POP);
		} else if (gateway.outputType().isPrimitive()) {
			if (gateway.outputType().equals(boolean.class)) {
				methodVisitor.visitMethodInsn(
						INVOKESTATIC,
						CAST_UTIL_CLASS,
						"toBool",
						"(Ljava/lang/Object;)Z"
				);
			} else if (gateway.outputType().equals(float.class)) {
				methodVisitor.visitMethodInsn(
						INVOKESTATIC,
						CAST_UTIL_CLASS,
						"toFloat",
						"(Ljava/lang/Object;)F"
				);
			} else if (gateway.outputType().equals(int.class)) {
				methodVisitor.visitMethodInsn(
						INVOKESTATIC,
						CAST_UTIL_CLASS,
						"toInt",
						"(Ljava/lang/Object;)I"
				);
			} else {
				throw new RuntimeException("Don't know how to handle type " + gateway.outputType());
			}
		}
	}

	public static Gateway ref(Class<?> clazz, String method) {
		try {
			return clazz.getMethod(method, Object.class).getAnnotation(Gateway.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
