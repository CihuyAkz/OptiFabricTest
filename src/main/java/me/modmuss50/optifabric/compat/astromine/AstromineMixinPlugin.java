package me.modmuss50.optifabric.compat.astromine;

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

public class AstromineMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("GameRendererMixin".equals(mixinInfo.getName())) {//GameRenderer, bobViewWhenHurt, (MatrixStack)
			Member bobViewWhenHurt = RemappingUtils.mapMethod("class_757", "method_3198", "(Lnet/minecraft/class_4587;F)V");
			String renderHandDesc = "(Lnet/minecraft/class_4587;Lnet/minecraft/class_4184;F)V"; //(MatrixStack, Camera)
			String renderHand = RemappingUtils.getMethodName("class_757", "method_3172", renderHandDesc);
			renderHandDesc = RemappingUtils.mapMethodDescriptor(renderHandDesc);

			for (MethodNode method : targetClass.methods) {
				if (renderHand.equals(method.name) && renderHandDesc.equals(method.desc)) {
					LabelNode skip = new LabelNode();

					InsnList extra = new InsnList();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new InsnNode(Opcodes.FCONST_0));
					extra.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, bobViewWhenHurt.owner, bobViewWhenHurt.name, bobViewWhenHurt.desc, false));
					extra.add(new InsnNode(Opcodes.POP));
					extra.add(skip);

					method.instructions.insertBefore(method.instructions.getLast(), extra);
					break;
				}
			}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}