package me.modmuss50.optifabric.compat.apoli.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.render.Camera;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(BackgroundRenderer.class)
@InterceptingMixin("io/github/apace100/apoli/mixin/BackgroundRendererMixin")
abstract class BackgroundRendererMixin {
	@ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;", ordinal = 5), ordinal = 0)
    private static double optifabric_modifyD(double original, Camera camera) {
		return modifyD(original, camera);
	}

	@Shim
	private static native double modifyD(double original, Camera camera);

	@Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V", remap = false), remap = false)
	private static void optifabric_redirectFogStart(float start, Camera camera, FogType fogType) {
		redirectFogStart(start, camera, fogType);
	}

	@Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V", remap = false), remap = false)
	private static void optifabric_redirectFogEnd(float end, Camera camera, FogType fogType) {
		redirectFogEnd(end, camera, fogType);
	}

	@Shim
	private static native void redirectFogStart(float start, Camera camera, FogType fogType);

	@Shim
	private static native void redirectFogEnd(float end, Camera camera, FogType fogType);

	@ModifyConstant(method = "setupFog", constant = @Constant(floatValue = 0.25F, ordinal = 1), remap = false)
	private static float optifabric_modifyLavaVisibilitySNoPotion(float original, Camera camera) {
		return modifyLavaVisibilitySNoPotion(original, camera);
	}

	@ModifyConstant(method = "setupFog", constant = @Constant(floatValue = 1F, ordinal = 0), remap = false)
	private static float optifabric_modifyLavaVisibilityVNoPotion(float original, Camera camera) {
		return modifyLavaVisibilityVNoPotion(original, camera);
	}

	@ModifyConstant(method = "setupFog", constant = @Constant(floatValue = 0F, ordinal = 0), remap = false)
	private static float optifabric_modifyLavaVisibilitySWithPotion(float original, Camera camera) {
		return modifyLavaVisibilitySWithPotion(original, camera);
	}

	@ModifyConstant(method = "setupFog", constant = @Constant(floatValue = 3F, ordinal = 0), remap = false)
	private static float optifabric_modifyLavaVisibilityVWithPotion(float original, Camera camera) {
		return modifyLavaVisibilityVWithPotion(original, camera);
	}

	@Shim
	private static native float modifyLavaVisibilitySNoPotion(float original, Camera camera);

	@Shim
	private static native float modifyLavaVisibilityVNoPotion(float original, Camera camera);

	@Shim
	private static native float modifyLavaVisibilitySWithPotion(float original, Camera camera);

	@Shim
	private static native float modifyLavaVisibilityVWithPotion(float original, Camera camera);
}