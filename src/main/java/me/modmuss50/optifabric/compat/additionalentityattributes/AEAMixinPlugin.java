package me.modmuss50.optifabric.compat.additionalentityattributes;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;

public class AEAMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("BackgroundRendererMixin".equals(mixinInfo.getName())) {
			String applyFogDesc = "(Lnet/minecraft/class_4184;Lnet/minecraft/class_758$class_4596;FZ)V";//(Camera, BackgroundRenderer$FogType)
			String applyFog = RemappingUtils.getMethodName("class_758", "method_3211", applyFogDesc);//BackgroundRenderer, applyFog
			applyFogDesc = RemappingUtils.mapMethodDescriptor(applyFogDesc);

			for (MethodNode method : targetClass.methods) {
				if (applyFog.equals(method.name) && applyFogDesc.equals(method.desc)) {
					InsnList extra = new InsnList();
					LabelNode skip = new LabelNode();

					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new InsnNode(Opcodes.FCONST_0));
					extra.add(new LdcInsnNode(0.25F));
					extra.add(new InsnNode(Opcodes.FCONST_1));
					extra.add(new LdcInsnNode(3F));
					extra.add(new LdcInsnNode(96F));
					extra.add(skip);

					method.instructions.insertBefore(method.instructions.getFirst(), extra);
					break;
				}
			}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}