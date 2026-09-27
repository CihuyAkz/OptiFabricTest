package me.modmuss50.optifabric.compat.frex;

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
import me.modmuss50.optifabric.compat.LoudCoerce.CoercionApplicator;
import me.modmuss50.optifabric.util.RemappingUtils;

public class FrexMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		switch (mixinInfo.getName()) {
		case "RebuildTaskMixin": {
			CoercionApplicator.preApply(mixinInfo, targetClass);
		}
		case "old.RebuildTaskMixin": {//BlockPos, iterate, (BlockPos, BlockPos)
			Member iterate = RemappingUtils.mapMethod("class_2338", "method_10097", "(Lnet/minecraft/class_2338;Lnet/minecraft/class_2338;)Ljava/lang/Iterable;");
			String renderHandDesc = "(FFFLnet/minecraft/class_846$class_849;Lnet/minecraft/class_750;)Ljava/util/Set;"; //(ChunkBuilder$ChunkData, BlockBufferBuilderStorage)
			String renderHand = RemappingUtils.getMethodName("class_846$class_851$class_4578", "method_22785", renderHandDesc);
			renderHandDesc = RemappingUtils.mapMethodDescriptor(renderHandDesc); //^ ChunkBuilder$BuiltChunk$RebuildTask, render

			for (MethodNode method : targetClass.methods) {
				if (renderHand.equals(method.name) && renderHandDesc.equals(method.desc)) {
					LabelNode skip = new LabelNode();

					InsnList extra = new InsnList();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new MethodInsnNode(Opcodes.INVOKESTATIC, iterate.owner, iterate.name, iterate.desc, false));
					extra.add(new InsnNode(Opcodes.POP));
					extra.add(skip);

					method.instructions.insertBefore(method.instructions.getLast(), extra);
					break;
				}
			}
			break;
		}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("RebuildTaskMixin".equals(mixinInfo.getName())) {
			CoercionApplicator.postApply(targetClass);
		}

		super.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}