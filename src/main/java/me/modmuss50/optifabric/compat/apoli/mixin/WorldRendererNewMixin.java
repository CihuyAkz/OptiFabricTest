package me.modmuss50.optifabric.compat.apoli.mixin;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(WorldRenderer.class)
@InterceptingMixin("io/github/apace100/apoli/mixin/WorldRendererMixin")
abstract class WorldRendererNewMixin {
	@Shim
	private native void getEntity(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer renderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix,
								  CallbackInfo call, Profiler profiler, boolean hasNoChunkUpdaters, Vec3d pos, double x, double y, double z, Matrix4f positionMatrix, boolean hasCapturedFrustum, Frustum capturedFrustum,
								  float viewDistance, boolean useThickFog, boolean shouldShowEntityOutlines, Immediate entityVertexConsumers, Iterator<Entity> entities, Entity entity);

	@PlacatingSurrogate
	private void getEntity(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer renderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix,
			  			   CallbackInfo call, Profiler profiler, boolean hasNoChunkUpdaters, Vec3d pos, double x, double y, double z, Matrix4f positionMatrix, boolean hasCapturedFrustum, Frustum capturedFrustum,
			  			   boolean isShaders, float viewDistance, boolean useThickFog, boolean renderSky, boolean shouldShowEntityOutlines, Immediate entityVertexConsumers) {
	}

	@Inject(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/Entity;getTeamColorValue()I"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void optifabric_getEntity(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer renderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix,
									  CallbackInfo call, Profiler profiler, boolean hasNoChunkUpdaters, Vec3d pos, double x, double y, double z, Matrix4f positionMatrix, boolean hasCapturedFrustum, Frustum capturedFrustum,
									  boolean isShaders, float viewDistance, boolean useThickFog, boolean renderSky, boolean shouldShowEntityOutlines, Immediate entityVertexConsumers, int minWorldY, int maxWorldY,
									  Collection<List<Entity>> entityLists, Iterator<List<Entity>> entityListsItr, List<Entity> entityList, Iterator<Entity> entities, Entity entity) {
		getEntity(matrices, tickDelta, limitTime, renderBlockOutline, camera, renderer, lightmapTextureManager, matrix, call, profiler, hasNoChunkUpdaters, pos, x, y, z, positionMatrix, hasCapturedFrustum, capturedFrustum,
				  viewDistance, useThickFog, shouldShowEntityOutlines, entityVertexConsumers, entities, entity);
	}
}
