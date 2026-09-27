package me.modmuss50.optifabric.compat.stacc;

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

public class StaccMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("PacketByteBufMixin".equals(mixinInfo.getName())) {
			String writeItemStackDesc = "(Lnet/minecraft/class_1799;)Lnet/minecraft/class_2540;"; //(ItemStack)PacketByteBuf
			String writeItemStack = RemappingUtils.getMethodName("class_2540", "method_10793", writeItemStackDesc); //PacketByteBuf, writeItemStack
			writeItemStackDesc = RemappingUtils.mapMethodDescriptor(writeItemStackDesc);

			for (MethodNode method : targetClass.methods) {
				if (writeItemStack.equals(method.name) && writeItemStackDesc.equals(method.desc)) {
					String writeCompoundTagDesc = "(Lnet/minecraft/class_2487;)Lnet/minecraft/class_2540;"; //(CompoundTag)PacketByteBuf
					String writeCompoundTag = RemappingUtils.getMethodName("class_2540", "method_10794", writeCompoundTagDesc);
					writeCompoundTagDesc = RemappingUtils.mapMethodDescriptor(writeCompoundTagDesc); // ^ PacketByteBuf, writeCompoundTag
					LabelNode skip = new LabelNode();

					InsnList extra = new InsnList();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, targetClass.name, writeCompoundTag, writeCompoundTagDesc, false));
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