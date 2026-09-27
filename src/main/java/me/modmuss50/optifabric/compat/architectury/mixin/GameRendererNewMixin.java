package me.modmuss50.optifabric.compat.architectury.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(GameRenderer.class)
@InterceptingMixin("dev/architectury/mixin/fabric/client/MixinGameRenderer")
abstract class GameRendererNewMixin {
	@Shim
	private native void renderScreenPre(float tickDelta, long startTime, boolean tick, CallbackInfo call, int mouseX, int mouseY, MatrixStack matrices);

	@PlacatingSurrogate
	private void renderScreenPre(float tickDelta, long startTime, boolean tick, CallbackInfo call, int mouseX, int mouseY, Window window) {
	}

	@Inject(method = "render(FJZ)V", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", ordinal = 0))
	private void renderScreenPre(float tickDelta, long startTime, boolean tick, CallbackInfo call, int mouseX, int mouseY, Window window, Matrix4f projection, MatrixStack modelView, MatrixStack matrices) {
		renderScreenPre(tickDelta, startTime, tick, call, mouseX, mouseY, matrices);
	}

	@Shim
	private native void renderScreenPost(float tickDelta, long startTime, boolean tick, CallbackInfo call, int mouseX, int mouseY, MatrixStack matrices);

	@PlacatingSurrogate
	private void renderScreenPost(float tickDelta, long startTime, boolean tick, CallbackInfo call, int mouseX, int mouseY, Window window) {
	}

	@Inject(method = "render(FJZ)V", locals = LocalCapture.CAPTURE_FAILHARD,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", shift = Shift.AFTER, ordinal = 0))
	private void renderScreenPost(float tickDelta, long startTime, boolean tick, CallbackInfo call, int mouseX, int mouseY, Window window, Matrix4f projection, MatrixStack modelView, MatrixStack matrices) {
		renderScreenPost(tickDelta, startTime, tick, call, mouseX, mouseY, matrices);
	}
}