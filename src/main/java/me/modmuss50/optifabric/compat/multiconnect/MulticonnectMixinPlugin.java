package me.modmuss50.optifabric.compat.multiconnect;

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

public class MulticonnectMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("ModelLoaderMixin".equals(mixinInfo.getName())) {//ResourceManager, BlockColors, Profiler
			//String constructorDesc = RemappingUtils.mapMethodDescriptor("(Lnet/minecraft/class_3300;Lnet/minecraft/class_324;Lnet/minecraft/class_3695;I)V");
			String defaultedRegistry = RemappingUtils.getClassName("class_2348");
			String getIds = RemappingUtils.getMethodName("class_2378", "method_10235", "()Ljava/util/Set;");

			for (MethodNode method : targetClass.methods) {
				if ("<init>".equals(method.name)) {//Multiconnect does this to all constructors...
					LabelNode skip = new LabelNode();

					InsnList extra = new InsnList();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, defaultedRegistry, getIds, "()Ljava/util/Set;", false));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, defaultedRegistry, "iterator", "()Ljava/util/Iterator;", false));
					extra.add(new InsnNode(Opcodes.POP));
					extra.add(skip);

					method.instructions.insertBefore(method.instructions.getLast(), extra);
				}
			}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}