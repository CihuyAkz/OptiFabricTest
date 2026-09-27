package me.modmuss50.optifabric.compat.sandwichable;

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

public class SandwichableMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		switch (mixinInfo.getName()) {
		case "old.BuiltinModelItemRendererMixin":
		case "BuiltinModelItemRendererMixin":
		case "BuiltinModelItemRendererNewMixin": {//ItemBlock, getBlock, ()Block
			Member getBlock = RemappingUtils.mapMethod("class_1747", "method_7711", "()Lnet/minecraft/class_2248;");
			//BuiltinModelItemRenderer, render, (ItemStack, ModelTransformation$Mode, MatrixStack, VertexConsumerProvider)
			String render = RemappingUtils.getMethodName("class_756", "method_3166",
					"(Lnet/minecraft/class_1799;Lnet/minecraft/class_809$class_811;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;II)V");

			for (MethodNode method : targetClass.methods) {
				if (render.equals(method.name)) {//Sandwichable does this to all methods called render
					LabelNode skip = new LabelNode();

					InsnList extra = new InsnList();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, getBlock.owner, getBlock.name, getBlock.desc, false));
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