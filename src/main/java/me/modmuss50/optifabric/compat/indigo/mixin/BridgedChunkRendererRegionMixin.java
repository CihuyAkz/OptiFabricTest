package me.modmuss50.optifabric.compat.indigo.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk;
import net.minecraft.client.render.chunk.ChunkBuilder.ChunkData;

import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;

import me.modmuss50.optifabric.compat.frex.BridgedChunkRendererRegion;
import me.modmuss50.optifabric.compat.frex.mixin.ChunkCacheOF;
import me.modmuss50.optifabric.compat.indigo.ChunkRendererRegionAccess;

@Mixin(value = BridgedChunkRendererRegion.class, remap = false)
abstract class BridgedChunkRendererRegionMixin implements ChunkRendererRegionAccess {
	@Shadow
	private @Final ChunkCacheOF wrapped;

	@Override
	public TerrainRenderContext fabric_getRenderer() {
		return ((ChunkRendererRegionAccess) wrapped).fabric_getRenderer();
	}

	@Override
	public void optifabric_setRenderer(TerrainRenderContext renderContext, BuiltChunk chunk, ChunkData data, BlockBufferBuilderStorage buffer) {
		((ChunkRendererRegionAccess) wrapped).optifabric_setRenderer(renderContext, chunk, data, buffer);
	}
}