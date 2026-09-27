package me.modmuss50.optifabric.compat.astromine.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(GameRenderer.class)
@InterceptingMixin("com/github/chainmailstudios/astromine/mixin/GameRendererMixin")
abstract class GameRendererMixin {
	@Shim
	private native void onRenderWorld(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo call);

	@Inject(method = "renderHand", remap = false,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", remap = true))
	private void onRenderWorld(MatrixStack matrices, Camera camera, float tickDelta, boolean renderItem, boolean renderOverlay, boolean renderTranslucent, CallbackInfo call) {
		onRenderWorld(matrices, camera, tickDelta, call);
	}
}