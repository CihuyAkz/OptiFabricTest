package me.modmuss50.optifabric.compat.apoli.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(ElytraFeatureRenderer.class)
@InterceptingMixin("io/github/apace100/apoli/mixin/ElytraFeatureRendererMixin")
abstract class ElytraFeatureRendererMixin {
	@Shim
    private native boolean modifyEquippedStackToElytra(ItemStack stack, Item item, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, LivingEntity entity,
    		float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch);

	@Inject(method = "shouldRender", at = @At("HEAD"), remap = false, cancellable = true)
	public void shouldRender(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Boolean> call) {
		if (modifyEquippedStackToElytra(stack, Items.ELYTRA, null, null, 0, entity, 0, 0, 0, 0, 0, 0)) {
			call.setReturnValue(true); //Let's take the positive approach that those parameters are never needed
		}
	}
}