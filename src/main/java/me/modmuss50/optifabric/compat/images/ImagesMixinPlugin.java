package me.modmuss50.optifabric.compat.images;

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

public class ImagesMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if (mixinInfo.getName().endsWith("BuiltinModelItemRendererMixin")) {//BuiltinModelItemRenderer, render, (ItemStack, ModelTransformation$Mode, MatrixStack, VertexConsumerProvider)
			String render = RemappingUtils.getMethodName("class_756", "method_3166", "(Lnet/minecraft/class_1799;Lnet/minecraft/class_809$class_811;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;II)V");
			//BannerBlockEntityRenderer, renderCanvas, (MatrixStack, VertexConsumerProvider, ModelPart, SpriteIdentifier)
			Member renderCanvas = RemappingUtils.mapMethod("class_823", "method_23802", "(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;IILnet/minecraft/class_630;Lnet/minecraft/class_4730;ZLjava/util/List;Z)V");

			for (MethodNode method : targetClass.methods) {
				if (render.equals(method.name)) {
					LabelNode skip = new LabelNode();

					InsnList extra = new InsnList();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new InsnNode(Opcodes.ICONST_0));
					extra.add(new InsnNode(Opcodes.ICONST_0));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new InsnNode(Opcodes.ICONST_0));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new MethodInsnNode(Opcodes.INVOKESTATIC, renderCanvas.owner, renderCanvas.name, renderCanvas.desc, false));
					extra.add(skip);

					method.instructions.insertBefore(method.instructions.getLast(), extra);
				}
			}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}