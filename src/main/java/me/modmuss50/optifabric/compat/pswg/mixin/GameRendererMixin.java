package me.modmuss50.optifabric.compat.pswg.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(GameRenderer.class)
@InterceptingMixin("com/parzivail/pswg/mixin/GameRendererMixin")
abstract class GameRendererMixin {
	@PlacatingSurrogate
	private void applyCameraTransformations(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo call, boolean isShaders, boolean shouldRenderBlockOutline) {
	}

	@Inject(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", locals = LocalCapture.CAPTURE_FAILHARD,
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;"+
								"Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V"))
	private void applyCameraTransformations(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo call, boolean isShaders, boolean shouldRenderBlockOutline, Camera camera) {
		applyCameraTransformations(tickDelta, limitTime, matrices, call, shouldRenderBlockOutline, camera);
	}

	@Shim
	abstract void applyCameraTransformations(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo call, boolean shouldRenderBlockOutline, Camera camera);

	@Redirect(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V",
				at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V", ordinal = 4))
	private void noopCameraPitch(MatrixStack stack, Quaternion shift) {//Fortunately the ordinal 2 call is never hit anyway
	}
}