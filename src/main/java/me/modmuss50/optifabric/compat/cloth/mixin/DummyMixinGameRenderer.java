package me.modmuss50.optifabric.compat.cloth.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(GameRenderer.class)
@InterceptingMixin("me/shedaniel/cloth/mixin/client/events/MixinGameRenderer")
public class DummyMixinGameRenderer {
	// Taken from https://github.com/shedaniel/cloth-api/blob/3913836e59e1df9058ece3e791ee0f5993b8bc00/cloth-client-events-v0/src/main/java/me/shedaniel/cloth/mixin/client/events/MixinGameRenderer.java#L49
	@Inject(method = "render(FJZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", shift = At.Shift.AFTER, ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void optifabric_renderScreen(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window, MatrixStack matrices) {
		renderScreen(tickDelta, startTime, tick, ci, mouseX, mouseY, matrices);
	}

	@Shim
	public native void renderScreen(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, MatrixStack matrices);

	@PlacatingSurrogate
	private void renderScreen(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window) {
	}
}
