package me.modmuss50.optifabric.compat.sandwichable.mixin.old;

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
@InterceptingMixin("io/github/foundationgames/sandwichable/mixin/BuiltinModelItemRendererMixin")
abstract class BuiltinModelItemRendererMixin {
	@Shim
	private native void sandwich_render(ItemStack stack, Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo call);

	@Inject(method = "renderRaw", remap = false,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;getBlock()Lnet/minecraft/block/Block;", shift = At.Shift.AFTER, remap = true))
	private void sandwich_render(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo call) {
		sandwich_render(stack, Mode.GROUND, matrices, vertexConsumers, light, overlay, call);
	}
}