package me.modmuss50.optifabric.compat.additionalentityattributes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(BackgroundRenderer.class)
@InterceptingMixin("de/dafuqs/additionalentityattributes/mixin/client/BackgroundRendererMixin")
abstract class BackgroundRendererMixin {
	@ModifyConstant(method = "setupFog", constant = @Constant(floatValue = 0.25F, ordinal = 0), remap = false)
	private static float optifabric_modifyLavaVisibilityMinWithoutFireResistance(float original, Camera camera) {
		return modifyLavaVisibilityMinWithoutFireResistance(original, camera);
	}

	@Shim
	private static native float modifyLavaVisibilityMinWithoutFireResistance(float original, Camera camera);

	@ModifyConstant(method = "setupFog", constant = @Constant(floatValue = 1F, ordinal = 0), remap = false)
	private static float optifabric_modifyLavaVisibilityMaxWithoutFireResistance(float original, Camera camera) {
		return modifyLavaVisibilityMaxWithoutFireResistance(original, camera);
	}

	@Shim
	private static native float modifyLavaVisibilityMaxWithoutFireResistance(float original, Camera camera);

	@ModifyConstant(method = "setupFog", constant = @Constant(floatValue = 0F, ordinal = 0), remap = false)
	private static float optifabric_modifyLavaVisibilityMinFireResistance(float original, Camera camera) {
		return modifyLavaVisibilityMinFireResistance(original, camera);
	}

	@Shim
	private static native float modifyLavaVisibilityMinFireResistance(float original, Camera camera);

	@ModifyConstant(method = "setupFog", constant = @Constant(floatValue = 3F, ordinal = 0), remap = false)
	private static float optifabric_modifyLavaVisibilityMaxWithFireResistance(float original, Camera camera) {
		return modifyLavaVisibilityMaxWithFireResistance(original, camera);
	}

	@Shim
	private static native float modifyLavaVisibilityMaxWithFireResistance(float original, Camera camera);

	@ModifyConstant(method = "setupFog", constant = @Constant(floatValue = 96F, ordinal = 0), remap = false)
	private static float optifabric_modifyWaterVisibility(float original, Camera camera) {
		return modifyWaterVisibility(original, camera);
	}

	@Shim
	private static native float modifyWaterVisibility(float original, Camera camera);
}