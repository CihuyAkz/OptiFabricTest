package me.modmuss50.optifabric.compat.indigo;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.util.Bytecode;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;

public class IndigoOldMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		super.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);

		if ("ChunkCacheOFMixin".equals(mixinInfo.getName())) {
			out: for (MethodNode method : targetClass.methods) {
				if (Bytecode.hasFlag(method, Opcodes.ACC_SYNTHETIC)) {//(ChunkRendererRegion, ChunkBuilder$BuiltChunk, ChunkBuilder$ChunkData, BlockBufferBuilderStorage)
					String desc = RemappingUtils.mapMethodDescriptor("(Lnet/minecraft/class_853;Lnet/minecraft/class_846$class_851;Lnet/minecraft/class_846$class_849;Lnet/minecraft/class_750;)Lnet/fabricmc/fabric/impl/client/indigo/renderer/render/TerrainRenderContext;");

					for (AbstractInsnNode insn : method.instructions) {
						if (insn.getType() == AbstractInsnNode.METHOD_INSN && insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
							MethodInsnNode minsn = (MethodInsnNode) insn;
							if ("net/fabricmc/fabric/impl/client/indigo/renderer/render/TerrainRenderContext".equals(minsn.owner) && "prepare".equals(minsn.name) && desc.equals(minsn.desc)) {
								minsn.desc = Bytecode.changeDescriptorReturnType(desc, "V");
								method.instructions.insert(insn, new VarInsnNode(Opcodes.ALOAD, 1)); //This return is gone, need to load the context back
								break out;
							}
						}
					}
				}
			}
		}
	}
}