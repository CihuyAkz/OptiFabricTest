package me.modmuss50.optifabric.compat.apoli.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.class_5636;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.render.Camera;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(BackgroundRenderer.class)
@InterceptingMixin("io/github/apace100/apoli/mixin/BackgroundRendererMixin")
abstract class BackgroundRendererNewerMixin {
	@ModifyVariable(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;", ordinal = 3), ordinal = 0)
	private static float optifabric_modifyD(float original, Camera camera) {
		return modifyD(original, camera);
	}

	@Shim
	private static native float modifyD(float original, Camera camera);

	@ModifyVariable(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;", ordinal = 0, remap = true), ordinal = 0, remap = false)
	private static class_5636 optifabric_modifyCameraSubmersionTypeFog(class_5636 original, Camera camera, FogType fogType, float viewDistance, boolean thickFog) {
		return modifyCameraSubmersionTypeFog(original, camera, fogType, viewDistance, thickFog);
	}

	@Shim
	private static native class_5636 modifyCameraSubmersionTypeFog(class_5636 original, Camera camera, FogType fogType, float viewDistance, boolean thickFog);

	@ModifyVariable(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"), ordinal = 2, remap = false)
    private static float optifabric_modifyFogEndForPhasingBlindness(float original, Camera camera, FogType fogType, float viewDistance, boolean thickFog) {
		return modifyFogEndForPhasingBlindness(original, camera, fogType, viewDistance, thickFog);
	}

    @Shim
    private static native float modifyFogEndForPhasingBlindness(float original, Camera camera, FogType fogType, float viewDistance, boolean thickFog);

	@Redirect(method = "setupFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogStart(F)V"), remap = false)
	private static void optifabric_redirectFogStart(float start, Camera camera, FogType fogType) {
		redirectFogStart(start, camera, fogType);
	}

	@Shim
	private static native void redirectFogStart(float start, Camera camera, FogType fogType);
}
