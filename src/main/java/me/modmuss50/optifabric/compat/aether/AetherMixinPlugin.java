package me.modmuss50.optifabric.compat.aether;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;

public class AetherMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("BackgroundRendererMixin".equals(mixinInfo.getName())) {
			//BackgroundRenderer, applyFog, (Camera, BackgroundRenderer$FogType)
			String applyFog = RemappingUtils.getMethodName("class_758", "method_3211", "(Lnet/minecraft/class_4184;Lnet/minecraft/class_758$class_4596;FZ)V");

			for (MethodNode method : targetClass.methods) {
				if (applyFog.equals(method.name)) {
					LabelNode skip = new LabelNode();

					InsnList extra = new InsnList();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new InsnNode(Opcodes.FCONST_0));
					extra.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mojang/blaze3d/systems/RenderSystem", "fogDensity", "(F)V", false));
					extra.add(skip);

					method.instructions.insertBefore(method.instructions.getLast(), extra);
				}
			}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}