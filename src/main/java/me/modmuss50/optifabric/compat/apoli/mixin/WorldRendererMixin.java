package me.modmuss50.optifabric.compat.apoli.mixin;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldRenderer.class)
@InterceptingMixin("io/github/apace100/apoli/mixin/WorldRendererMixin")
abstract class WorldRendererMixin {
    @Shim
    private native void getEntity(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci,
                                  Profiler profiler, boolean hasNoChunkUpdaters, Vec3d pos, double x, double y, double z, Matrix4f positionMatrix, boolean hasCapturedFrustum, Frustum capturedFrustum, boolean shouldThickenFog, VertexConsumerProvider.Immediate entityVertexConsumers, Iterator entities, Entity entity);

    @PlacatingSurrogate
    private void getEntity(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci,
                           Profiler profiler, boolean isLightUpdateQueueEmpty, Vec3d position, double x, double y, double z, Matrix4f pose, boolean hasCapturedFrustum, Frustum capturedFrustum, boolean isShaders, float renderDistance, boolean shouldCreateWorldFog, boolean shouldShowEntityOutlines) {
    }

    @Inject(method = "render", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/Entity;getTeamColorValue()I"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void optifabric_getEntity(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci,
                             Profiler profilerFiller, boolean isLightUpdateQueueEmpty, Vec3d position, double x, double y, double z, Matrix4f pose, boolean hasCapturedFrustum, Frustum capturedFrustum, boolean isShaders, float renderDistance, boolean shouldCreateWorldFog, boolean shouldShowEntityOutlines, VertexConsumerProvider.Immediate bufferSource, int minWorldY, int maxWorldY, Collection entityLists, Iterator entityListsItr, List entityList, Iterator entities, Entity entity) {
        this.getEntity(matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, lightmapTextureManager, matrix4f, ci, profilerFiller, isLightUpdateQueueEmpty, position, x, y, z, pose, hasCapturedFrustum, capturedFrustum, shouldCreateWorldFog, bufferSource, entities, entity);
    }
}
