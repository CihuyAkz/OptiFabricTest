package me.modmuss50.optifabric.compat.cullparticles;

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

import me.modmuss50.optifabric.compat.EmptyMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;

public class CullParticlesMixinPlugin extends EmptyMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("ParticleManagerMixin".equals(mixinInfo.getName())) {//(MatrixStack, VertexConsumerProvider$Immediate, LightmapTextureManager, Camera)
			String renderParticlesDesc = "(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597$class_4598;Lnet/minecraft/class_765;Lnet/minecraft/class_4184;F)V";
			String renderParticles = RemappingUtils.getMethodName("class_702", "method_3049", renderParticlesDesc); //ParticleManager, renderParticles
			renderParticlesDesc = RemappingUtils.mapMethodDescriptor(renderParticlesDesc);

			for (MethodNode method : targetClass.methods) {
				if (renderParticles.equals(method.name) && renderParticlesDesc.equals(method.desc)) {//Particle, buildGeometry, (VertexConsumer, Camera)
					Member buildGeometry = RemappingUtils.mapMethod("class_703", "method_3074", "(Lnet/minecraft/class_4588;Lnet/minecraft/class_4184;F)V");
					LabelNode skip = new LabelNode();

					InsnList extra = new InsnList();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new InsnNode(Opcodes.FCONST_0));
					extra.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, buildGeometry.owner, buildGeometry.name, buildGeometry.desc, true));
					extra.add(skip);

					method.instructions.insertBefore(method.instructions.getLast(), extra);
					break;
				}
			}
		}
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}