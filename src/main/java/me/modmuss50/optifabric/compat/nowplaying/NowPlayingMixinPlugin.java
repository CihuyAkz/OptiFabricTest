package me.modmuss50.optifabric.compat.nowplaying;

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

public class NowPlayingMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("WorldRendererMixin".equals(mixinInfo.getName())) {//InGameHud, setRecordPlayingOverlayDesc, (Text)
			Member setRecordPlayingOverlay = RemappingUtils.mapMethod("class_329", "method_1732", "(Lnet/minecraft/class_2561;)V");
			//WorldRenderer, playSong, (SoundEvent, BlockPos)
			String playSong = RemappingUtils.getMethodName("class_761", "method_8562", "(Lnet/minecraft/class_3414;Lnet/minecraft/class_2338;)V");

			for (MethodNode method : targetClass.methods) {
				if (playSong.equals(method.name)) {//Now Playing does this to all methods called playSong
					LabelNode skip = new LabelNode();

					InsnList extra = new InsnList();
					extra.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extra.add(new InsnNode(Opcodes.ACONST_NULL));
					extra.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, setRecordPlayingOverlay.owner, setRecordPlayingOverlay.name, setRecordPlayingOverlay.desc, false));
					extra.add(new InsnNode(Opcodes.POP));
					extra.add(skip);

					method.instructions.insertBefore(method.instructions.getLast(), extra);
				}
			}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}