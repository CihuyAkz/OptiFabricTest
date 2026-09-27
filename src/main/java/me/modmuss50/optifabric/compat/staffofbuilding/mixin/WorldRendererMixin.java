package me.modmuss50.optifabric.compat.staffofbuilding.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(WorldRenderer.class)
@InterceptingMixin("draylar/staffofbuilding/mixin/WorldRendererMixin")
abstract class WorldRendererMixin {
	@PlacatingSurrogate
	private void renderWandHighlight(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, 
			CallbackInfo call, Profiler profiler, Vec3d cameraPos, double x, double y, double z, Matrix4f modelMatrix, boolean hasCapturedFrustum, Frustum frustum, boolean outlineSomething, float viewDistance) {
	}

	@Inject(method = "render", 
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"), 
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void renderWandHighlight(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix,
			CallbackInfo call, Profiler profiler, Vec3d cameraPos, double x, double y, double z, Matrix4f modelMatrix, boolean hasCapturedFrustum, Frustum frustum, boolean isShaders, float viewDistance, boolean thickFog,
			int thirty, int maxFPS, long thirtyThreeMillion, long minimumFPS, long lastFrameTime, long targetFrameTime, long scaledTargetFrameTime, long clampedTargetFrameTime, boolean outlineSomething, Immediate immediate) {
		renderWandHighlight(matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, lightmapTextureManager, projectionMatrix, call, profiler, cameraPos, x, y, z, modelMatrix, hasCapturedFrustum, frustum, outlineSomething, immediate);
	}

	@Shim
	private native void renderWandHighlight(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, 
			CallbackInfo call, Profiler profiler, Vec3d cameraPos, double x, double y, double z, Matrix4f modelMatrix, boolean hasCapturedFrustum, Frustum frustum, boolean outlineSomething, Immediate immediate);
}