package me.modmuss50.optifabric.compat.bannerpp.mixin;

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
@InterceptingMixin("io/github/fablabsmc/fablabs/mixin/bannerpattern/client/BuiltinModelItemRendererMixin")
abstract class BuiltinModelItemRendererMixin {
	@Inject(method = "renderRaw", remap = false,
			at = @At(value = "INVOKE", remap = true, 
						target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLjava/util/List;Z)V"))
	private void setBppLoomPatterns(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo call) {
		setBppLoomPatterns(stack, /* Hopefully not used */ null, matrices, vertexConsumers, light, overlay, call);
	}

	@Shim
	private native void setBppLoomPatterns(ItemStack stack, Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo call);
}