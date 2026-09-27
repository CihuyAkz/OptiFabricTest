package me.modmuss50.optifabric.compat.mubble;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.tinyremapper.IMappingProvider.Member;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;

public class MubbleMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		switch (mixinInfo.getName()) {
			case "ItemRendererMixin": {//ItemStack, isDamaged
				Member bobViewWhenHurt = RemappingUtils.mapMethod("class_1799", "method_7986", "()Z");
				String renderGuiItemOverlayDesc = "(Lnet/minecraft/class_327;Lnet/minecraft/class_1799;IILjava/lang/String;)V"; //(TextRenderer, ItemStack)
				String renderGuiItemOverlay = RemappingUtils.getMethodName("class_918", "method_4022", renderGuiItemOverlayDesc);
				renderGuiItemOverlayDesc = RemappingUtils.mapMethodDescriptor(renderGuiItemOverlayDesc);

				for (MethodNode method : targetClass.methods) {
					if (renderGuiItemOverlay.equals(method.name) && renderGuiItemOverlayDesc.equals(method.desc)) {
						LabelNode skip = new LabelNode();

						InsnList extra = new InsnList();
						extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
						extra.add(new InsnNode(Opcodes.ACONST_NULL));
						extra.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, bobViewWhenHurt.owner, bobViewWhenHurt.name, bobViewWhenHurt.desc, false));
						extra.add(new InsnNode(Opcodes.POP));
						extra.add(skip);

						method.instructions.insertBefore(method.instructions.getLast(), extra);
						break;
					}
				}
				break;
			}

			case "BackgroundRendererMixin": {
				//BackgroundRenderer, applyFog, (Camera, BackgroundRenderer$FogType)
				String applyFog = RemappingUtils.getMethodName("class_758", "method_3211", "(Lnet/minecraft/class_4184;Lnet/minecraft/class_758$class_4596;FZ)V");

				for (MethodNode method : targetClass.methods) {
					if (applyFog.equals(method.name)) {//LivingEntity, hasStatusEffect, (StatusEffect)
						Member hasStatusEffect = RemappingUtils.mapMethod("class_1309", "method_6059", "(Lnet/minecraft/class_1291;)Z");
						LabelNode skip = new LabelNode();

						InsnList extra = new InsnList();
						extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
						extra.add(new InsnNode(Opcodes.ACONST_NULL));
						extra.add(new InsnNode(Opcodes.ACONST_NULL));
						extra.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, hasStatusEffect.owner, hasStatusEffect.name, hasStatusEffect.desc, false));
						extra.add(new InsnNode(Opcodes.POP));
						extra.add(skip);

						method.instructions.insertBefore(method.instructions.getLast(), extra);
					}
				}
				break;
			}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}