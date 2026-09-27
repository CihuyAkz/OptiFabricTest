package me.modmuss50.optifabric.compat.cloth.mixin;

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
@InterceptingMixin("me/shedaniel/cloth/mixin/client/events/MixinGameRenderer")
abstract class GameRendererNewMixin {
	@Inject(method = "render(FJZ)V", locals = LocalCapture.CAPTURE_FAILEXCEPTION,
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", shift = Shift.AFTER, ordinal = 0))
	private void optifabric_renderScreen(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window, Matrix4f projection, MatrixStack modelView, MatrixStack matrices) {
		renderScreen(tickDelta, startTime, tick, ci, mouseX, mouseY, matrices);
	}

	@Shim
	public abstract void renderScreen(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, MatrixStack matrices);

	@PlacatingSurrogate
	private void renderScreen(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window) {
	}
}