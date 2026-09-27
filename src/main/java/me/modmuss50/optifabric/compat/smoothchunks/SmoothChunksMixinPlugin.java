package me.modmuss50.optifabric.compat.smoothchunks;

import java.util.OptionalInt;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.RemappingUtils;

public class SmoothChunksMixinPlugin extends InterceptingMixinPlugin {
	private OptionalInt access = OptionalInt.empty();

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("WorldRendererMixin".equals(mixinInfo.getName())) { //(RenderLayer, MatrixStack)
			String renderLayerDesc = "(Lnet/minecraft/class_1921;Lnet/minecraft/class_4587;DDD)V";
			String renderLayer = RemappingUtils.getMethodName("class_761", "method_3251", renderLayerDesc);
			renderLayerDesc = RemappingUtils.mapMethodDescriptor(renderLayerDesc); // ^ WorldRenderer, renderLayer 

			for (MethodNode method : targetClass.methods) {
				if (renderLayer.equals(method.name) && renderLayerDesc.equals(method.desc)) {
					access = OptionalInt.of(method.access);
					method.access = (method.access & (~0x7)) | Opcodes.ACC_PRIVATE;
					break;
				}
			}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if (access.isPresent() && "WorldRendererMixin".equals(mixinInfo.getName())) {
			String renderLayerDesc = "(Lnet/minecraft/class_1921;Lnet/minecraft/class_4587;DDD)V"; //See above
			String renderLayer = RemappingUtils.getMethodName("class_761", "method_3251", renderLayerDesc);
			renderLayerDesc = RemappingUtils.mapMethodDescriptor(renderLayerDesc);

			for (MethodNode method : targetClass.methods) {
				if (renderLayer.equals(method.name) && renderLayerDesc.equals(method.desc)) {
					method.access = access.getAsInt();
					break;
				}
			}

			access = OptionalInt.empty();
		}

		super.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}