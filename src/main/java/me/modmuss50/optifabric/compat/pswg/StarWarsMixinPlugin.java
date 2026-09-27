package me.modmuss50.optifabric.compat.pswg;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import me.modmuss50.optifabric.compat.EmptyMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;

import net.fabricmc.tinyremapper.IMappingProvider.Member;

public class StarWarsMixinPlugin extends EmptyMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("FrameBufferMixin".equals(mixinInfo.getName())) {
			Member texImage2D = RemappingUtils.mapMethod("class_4493", "method_21954", "(IIIIIIIILjava/nio/IntBuffer;)V"); //GlStateManager, texImage2D
			String initFbo = RemappingUtils.getMethodName("class_276", "method_1231", "(IIZ)V"); //FrameBuffer, initFbo

			for (MethodNode method : targetClass.methods) {
				if (initFbo.equals(method.name)) {
					LabelNode skip = new LabelNode();

					InsnList extra = new InsnList();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					for (byte i = 0; i < 8; i++) extra.add(new InsnNode(Opcodes.ICONST_0));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new MethodInsnNode(Opcodes.INVOKESTATIC, texImage2D.owner, texImage2D.name, texImage2D.desc, false));
					extra.add(new InsnNode(Opcodes.POP));
					extra.add(skip);

					method.instructions.insertBefore(method.instructions.getFirst(), extra);
					break;
				}
			}
		}
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {	
	}
}