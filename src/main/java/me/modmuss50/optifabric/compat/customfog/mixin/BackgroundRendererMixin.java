package me.modmuss50.optifabric.compat.customfog.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(BackgroundRenderer.class)
@InterceptingMixin("setadokalo/customfog/mixin/RendererMixin")
abstract class BackgroundRendererMixin {
	@PlacatingSurrogate
	private static void setFogFalloff(Camera camera, FogType fogType, float viewDistance, boolean thickFog, CallbackInfo call) {
	}

	@Inject(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setupNvFogDistance()V"), remap = false)
	private static void setFogFalloff(Camera camera, FogType fogType, float viewDistance, boolean thickFog, float partialTicks, CallbackInfo call) {
		setFogFalloff(camera, fogType, viewDistance, thickFog, call, camera.getSubmergedFluidState(), camera.getFocusedEntity());
	}

	@Shim
	private static native void setFogFalloff(Camera camera, FogType fogType, float viewDistance, boolean thickFog, CallbackInfo call, FluidState state, Entity entity);
}