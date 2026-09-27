package me.modmuss50.optifabric.compat.indigo;

import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk;
import net.minecraft.client.render.chunk.ChunkBuilder.ChunkData;

import net.fabricmc.fabric.impl.client.indigo.renderer.accessor.AccessChunkRendererRegion;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;

public interface ChunkRendererRegionAccess extends AccessChunkRendererRegion {
	void optifabric_setRenderer(TerrainRenderContext renderContext, BuiltChunk chunk, ChunkData data, BlockBufferBuilderStorage buffer);

	@Override
	default void fabric_setRenderer(TerrainRenderContext renderer) {
		throw new UnsupportedOperationException("Received unexpected render context: " + renderer);
	}
}