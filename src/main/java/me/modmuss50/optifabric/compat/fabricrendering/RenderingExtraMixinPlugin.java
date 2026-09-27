package me.modmuss50.optifabric.compat.fabricrendering;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;

public class RenderingExtraMixinPlugin extends InterceptingMixinPlugin {
	private static final String FAKE_IDENTIFIER = "me/modmuss50/optifabric/fake/Identifier";

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("ShaderProgramMixin".equals(mixinInfo.getName())) {//(ResourceFactory, VertexFormat)
			String desc = RemappingUtils.mapMethodDescriptor("(Lnet/minecraft/class_5912;Ljava/lang/String;Lnet/minecraft/class_293;)V");

			for (MethodNode method : targetClass.methods) {
				if ("<init>".equals(method.name) && desc.equals(method.desc)) {
					String identifier = RemappingUtils.getClassName("class_2960");

					for (AbstractInsnNode insn : method.instructions) {
						if (insn.getType() == AbstractInsnNode.METHOD_INSN && insn.getOpcode() == Opcodes.INVOKESPECIAL) {
							MethodInsnNode minsn = (MethodInsnNode) insn;

							if (identifier.equals(minsn.owner) && "<init>".equals(minsn.name) && "(Ljava/lang/String;)V".equals(minsn.desc)) {
								minsn.owner = FAKE_IDENTIFIER;
							}
						}
					}

					InsnList extra = new InsnList();
					LabelNode skip = new LabelNode();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, identifier, "<init>", "(Ljava/lang/String;)V", false));
					extra.add(skip);
					method.instructions.insertBefore(method.instructions.getLast(), extra);
					break;
				}
			}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("ShaderProgramMixin".equals(mixinInfo.getName())) {//(ResourceFactory, VertexFormat)
			String desc = RemappingUtils.mapMethodDescriptor("(Lnet/minecraft/class_5912;Ljava/lang/String;Lnet/minecraft/class_293;)V");

			for (MethodNode method : targetClass.methods) {
				if ("<init>".equals(method.name) && desc.equals(method.desc)) {
					String identifier = RemappingUtils.getClassName("class_2960");

					for (AbstractInsnNode insn : method.instructions) {
						if (insn.getType() == AbstractInsnNode.METHOD_INSN && FAKE_IDENTIFIER.equals(((MethodInsnNode) insn).owner)) {
							((MethodInsnNode) insn).owner = identifier;
						}
					}
					break;
				}
			}			
		}

		super.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}