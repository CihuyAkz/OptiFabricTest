package me.modmuss50.optifabric.compat.fabricrenderingdata;

import org.objectweb.asm.tree.ClassNode;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.MixinUtils;

public class RenderingDataExtraMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("ChunkRendererRegionMixin".equals(mixinInfo.getName())) {
			ClassInfo info = ClassInfo.forName(targetClassName);
			MixinUtils.completeClassInfo(info, targetClass.methods);
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}