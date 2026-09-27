package me.modmuss50.optifabric.compat.images.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(BuiltinModelItemRenderer.class)
@InterceptingMixin("net/sorenon/images/mixin/client/BuiltinModelItemRendererMixin")
abstract class BuiltinModelItemRendererMixin {
	@Shim
	private native void inject_renderShield(ItemStack stack, Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo call);

	@Inject(method = "renderRaw", remap = false,
			at = @At(value = "INVOKE", 
						target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V",
						remap = true))
	private void inject_renderShield(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo call) {
		inject_renderShield(stack, /* Hopefully not used */ null, matrices, vertexConsumers, light, overlay, call);
	}
}