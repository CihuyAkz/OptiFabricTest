package me.modmuss50.optifabric.compat.enhancedcelestials.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3f;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(LightmapTextureManager.class)
@InterceptingMixin("corgitaco/enhancedcelestials/mixin/client/MixinLightMapTexture")
abstract class LightmapTextureManagerNewMixin {
	@PlacatingSurrogate
	private void doOurLightMap(float partialTicks, CallbackInfo call, ClientWorld world, float skyLight, float lightingTint, float underwaterEffect, float extraShift) {
	}

	@Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/Vector3f;lerp(Lnet/minecraft/util/math/Vec3f;F)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private void doOurLightMap(float partialTicks, CallbackInfo call, ClientWorld world, float skyLight, float lightingTint, float underwaterEffect, float extraShift, Vec3f skyVector) {
		doOurLightMap(partialTicks, call, world, skyLight, lightingTint, extraShift, skyVector);
	}

	@Shim
	private native void doOurLightMap(float partialTicks, CallbackInfo call, ClientWorld world, float skyLight, float lightingTint, float extraShift, Vec3f skyVector);
}