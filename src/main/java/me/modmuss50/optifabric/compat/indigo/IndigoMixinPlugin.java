package me.modmuss50.optifabric.compat.indigo;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
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

public class IndigoMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		switch (mixinInfo.getName()) {
			case "RebuildTaskNewMixin":
			case "RebuildTaskNewerMixin": {
				String renderDesc = "(FFFLnet/minecraft/class_750;)Lnet/minecraft/class_846$class_851$class_4578$class_7435;";
				String render = RemappingUtils.getMethodName("class_846$class_851$class_4578", "method_22785", renderDesc); //(BlockBufferBuilderStorage)RenderData
				renderDesc = RemappingUtils.mapMethodDescriptor(renderDesc);
				String getAllInBoxMutableDesc = RemappingUtils.mapMethodDescriptor("(Lnet/minecraft/class_2338;Lnet/minecraft/class_2338;)Ljava/lang/Iterable;");

				for (MethodNode method : targetClass.methods) {
					if (render.equals(method.name) && renderDesc.equals(method.desc)) {
						for (AbstractInsnNode insn : method.instructions) {
							if (insn.getType() == AbstractInsnNode.METHOD_INSN && insn.getOpcode() == Opcodes.INVOKESTATIC) {
								MethodInsnNode call = (MethodInsnNode) insn;
								if (!"net/optifine/BlockPosM".equals(call.owner) || !"getAllInBoxMutable".equals(call.name) || !getAllInBoxMutableDesc.equals(call.desc)) continue;
								LabelNode skip = new LabelNode();

								InsnList extra = new InsnList();
								extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
								Member iterate = RemappingUtils.mapMethod("class_2338", "method_10097", "(Lnet/minecraft/class_2338;Lnet/minecraft/class_2338;)Ljava/lang/Iterable;");
								extra.add(new MethodInsnNode(Opcodes.INVOKESTATIC, iterate.owner, iterate.name, iterate.desc, false)); //BlockPos#iterate(BlockPos, BlockPos) ^
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
}