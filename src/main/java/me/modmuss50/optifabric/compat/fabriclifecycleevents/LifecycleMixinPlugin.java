package me.modmuss50.optifabric.compat.fabriclifecycleevents;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;

public class LifecycleMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		switch (mixinInfo.getName()) {
		case "ClientChunkManagerMixin":
		case "ClientChunkManagerNewMixin": {
			String loadChunkFromPacketDesc = getLoadChunkFromPacketDesc();
			String loadChunkFromPacket = RemappingUtils.getMethodName("class_631", "method_16020", loadChunkFromPacketDesc); //(BiomeArray, PacketByteBuf, NbtCompound)WorldChunk
			loadChunkFromPacketDesc = RemappingUtils.mapMethodDescriptor(loadChunkFromPacketDesc);

			for (MethodNode method : targetClass.methods) {
				if (loadChunkFromPacket.equals(method.name) && loadChunkFromPacketDesc.equals(method.desc)) {
					for (AbstractInsnNode insn : method.instructions) {
						if (insn.getType() == AbstractInsnNode.TYPE_INSN && insn.getOpcode() == Opcodes.NEW && "net/optifine/ChunkOF".equals(((TypeInsnNode) insn).desc)) {
							LabelNode skip = new LabelNode();

							InsnList extra = new InsnList();
							extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
							extra.add(new TypeInsnNode(Opcodes.NEW, RemappingUtils.getClassName("class_2818"))); //WorldChunk
							extra.add(new InsnNode(Opcodes.POP));
							extra.add(skip);

							method.instructions.insertBefore(insn, extra);
							break;
						}
					}

					break;
				}
			}
			break;
		}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}

	protected String getLoadChunkFromPacketDesc() {
		return "(IILnet/minecraft/class_4548;Lnet/minecraft/class_2540;Lnet/minecraft/class_2487;Ljava/util/BitSet;)Lnet/minecraft/class_2818;";
	}
}