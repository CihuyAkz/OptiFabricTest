package me.modmuss50.optifabric.compat.aether.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.render.Camera;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(BackgroundRenderer.class)
@InterceptingMixin("com/aether/mixin/client/BackgroundRendererMixin")
abstract class BackgroundRendererMixin {
	@Inject(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;fogDensity(F)V"), cancellable = true, remap = false)
	private static void dontChangeFogDensity(Camera camera, FogType fogType, float viewDistance, boolean thickFog, float partialTicks, CallbackInfo call) {
		dontChangeFogDensity(camera, fogType, viewDistance, thickFog, call);
	}

	@Shim
	private static native void dontChangeFogDensity(Camera camera, FogType fogType, float viewDistance, boolean thickFog, CallbackInfo call);
}