package me.modmuss50.optifabric.compat.indigo.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk;
import net.minecraft.client.render.chunk.ChunkBuilder.ChunkData;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;

import me.modmuss50.optifabric.compat.indigo.ChunkRendererRegionAccess;

@Mixin(targets = "net.minecraft.client.render.chunk.ChunkBuilder$BuiltChunk$RebuildTask")
abstract class RebuildTaskMixin {
	@Shadow
	protected BuiltChunk field_20839;

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;render(FFFLnet/minecraft/client/render/chunk/ChunkBuilder$ChunkData;Lnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Ljava/util/Set;",
			at = @At(value = "INVOKE", target = "Lnet/optifine/override/ChunkCacheOF;renderStart()V", shift = Shift.AFTER, remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
	private void hookChunkBuild(float cameraX, float cameraY, float cameraZ, ChunkData data, BlockBufferBuilderStorage buffer, CallbackInfoReturnable<Set<BlockEntity>> call,
								int one, BlockPos origin, BlockPos oppositeOrigin, ChunkOcclusionDataBuilder occlusionBuilder, Set<BlockEntity> bes, MatrixStack matrices, @Coerce Object chunkCache) {
		((ChunkRendererRegionAccess) chunkCache).optifabric_setRenderer(TerrainRenderContext.POOL.get(), field_20839, data, buffer);
	}
}