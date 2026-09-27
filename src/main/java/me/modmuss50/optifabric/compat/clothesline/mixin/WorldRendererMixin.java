package me.modmuss50.optifabric.compat.clothesline.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(WorldRenderer.class)
@InterceptingMixin("com/jamieswhiteshirt/clothesline/mixin/client/render/WorldRendererMixin")
abstract class WorldRendererMixin {
	@PlacatingSurrogate
	private void renderClotheslines(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, 
			CallbackInfo ci, Profiler profiler, Vec3d cameraPos, double x, double y, double z, Matrix4f modelMatrix, boolean hasCapturedFrustum, Frustum frustum, boolean outlineSomething, float viewDistance) {
	}

	@Inject(
		at = @At(
			value = "INVOKE_STRING",
			target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
			args = "ldc=blockentities"
		),
		method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V",
		locals = LocalCapture.CAPTURE_FAILHARD
	)
    private void renderClotheslines(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix,
    		CallbackInfo ci, Profiler profiler, Vec3d cameraPos, double x, double y, double z, Matrix4f modelMatrix, boolean hasCapturedFrustum, Frustum frustum, boolean isShaders, float viewDistance, boolean thickFog,
    		int thirty, int maxFPS, long thirtyThreeMillion, long minimumFPS, long lastFrameTime, long targetFrameTime, long scaledTargetFrameTime, long clampedTargetFrameTime, boolean outlineSomething, Immediate immediate) {
		renderClotheslines(matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, lightmapTextureManager, projectionMatrix, ci, profiler, cameraPos, x, y, z, modelMatrix, hasCapturedFrustum, frustum, outlineSomething, immediate);
	}

	@Shim
	private native void renderClotheslines(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, 
			CallbackInfo ci, Profiler profiler, Vec3d cameraPos, double x, double y, double z, Matrix4f modelMatrix, boolean hasCapturedFrustum, Frustum frustum, boolean outlineSomething, Immediate immediate);

	@PlacatingSurrogate
	private void renderHighlight(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, 
			CallbackInfo ci, Profiler profiler, Vec3d cameraPos, double x, double y, double z, Matrix4f modelMatrix, boolean hasCapturedFrustum, Frustum frustum, boolean outlineSomething, float viewDistance) {
	}

	@Inject(
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/MinecraftClient;crosshairTarget:Lnet/minecraft/util/hit/HitResult;",
			shift = At.Shift.AFTER,
			ordinal = 1
		),
		method = "render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/util/math/Matrix4f;)V",
		locals = LocalCapture.CAPTURE_FAILHARD
	)
    private void renderHighlight(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix,
    		CallbackInfo ci, Profiler profiler, Vec3d cameraPos, double x, double y, double z, Matrix4f modelMatrix, boolean hasCapturedFrustum, Frustum frustum, boolean isShaders, float viewDistance, boolean thickFog,
    		int thirty, int maxFPS, long thirtyThreeMillion, long minimumFPS, long lastFrameTime, long targetFrameTime, long scaledTargetFrameTime, long clampedTargetFrameTime, boolean outlineSomething, Immediate immediate) {
		renderHighlight(matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, lightmapTextureManager, projectionMatrix, ci, profiler, cameraPos, x, y, z, modelMatrix, hasCapturedFrustum, frustum, outlineSomething, immediate);
	}

	@Shim
	private native void renderHighlight(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix,
			CallbackInfo ci, Profiler profiler, Vec3d cameraPos, double x, double y, double z, Matrix4f modelMatrix, boolean hasCapturedFrustum, Frustum frustum, boolean outlineSomething, Immediate immediate);

}