package me.modmuss50.optifabric.compat.apoli;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import net.fabricmc.tinyremapper.IMappingProvider.Member;

import me.modmuss50.optifabric.compat.origins.OriginsMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;

public class ApoliMixinPlugin extends OriginsMixinPlugin {
	@Override
	protected void addFocusedEntity(InsnList extra, Member getFocusedEntity) {
		extra.add(new InsnNode(Opcodes.ACONST_NULL));
		extra.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, getFocusedEntity.owner, getFocusedEntity.name, getFocusedEntity.desc, false));
		extra.add(new InsnNode(Opcodes.POP));
		extra.add(new InsnNode(Opcodes.ACONST_NULL));
		extra.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, getFocusedEntity.owner, getFocusedEntity.name, getFocusedEntity.desc, false));
	}

	@Override
	protected String fogStart() {
		return "setShaderFogStart";
	}

	@Override
	protected String fogEnd() {
		return "setShaderFogEnd";
	}

	@Override
	protected AbstractInsnNode getFogTarget() {
		return new LdcInsnNode(0.25F);
	}

	@Override
	protected Member getElytraMixinTarget() {//ItemStack, isOf, (Item)
		return RemappingUtils.mapMethod("class_1799", "method_31574", "(Lnet/minecraft/class_1792;)Z");
	}
}