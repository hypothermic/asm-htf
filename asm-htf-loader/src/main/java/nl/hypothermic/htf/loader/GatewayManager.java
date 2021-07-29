package nl.hypothermic.htf.loader;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public final class GatewayManager {

	protected static final HashMap<Long, Method> GATEWAYS = new HashMap<>();

	public static Object get(long callbackId, Object data) {
		return GATEWAYS.entrySet()
				.stream()
				.filter(entry -> entry.getKey() == callbackId)
				.findFirst()
				.map(entry -> {
					try {
						return entry.getValue().invoke(null, data);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				})
				.orElse(null);
	}

	public static InsnList create(long callbackId) {
		return create(callbackId, new InsnNode(Opcodes.ACONST_NULL));
	}

	public static InsnList create(long callbackId, Object variableLoadInstruction) {
		InsnList insnList = new InsnList();

		insnList.add(new LdcInsnNode(callbackId));
		if (variableLoadInstruction instanceof InsnList) {
			insnList.add((InsnList) variableLoadInstruction);
		} else {
			insnList.add((AbstractInsnNode) variableLoadInstruction);
		}

		// the "itf" argument isn't available in asm4
		//noinspection deprecation
		insnList.add(new MethodInsnNode(
				Opcodes.INVOKESTATIC,
				"nl/hypothermic/htf/api/Gateways",
				"get",
				"(JLjava/lang/Object;)Ljava/lang/Object;"
		));

		return insnList;
	}
}
