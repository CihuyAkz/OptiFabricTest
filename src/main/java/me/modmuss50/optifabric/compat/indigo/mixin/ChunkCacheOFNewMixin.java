package me.modmuss50.optifabric.compat.indigo.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.render.chunk.ChunkRendererRegion;

import net.fabricmc.fabric.impl.client.indigo.renderer.accessor.AccessChunkRendererRegion;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;

@Pseudo
@Mixin(targets = "net/optifine/override/ChunkCacheOF", remap = false)
abstract class ChunkCacheOFNewMixin implements AccessChunkRendererRegion {
	@Shadow
	private @Final ChunkRendererRegion chunkCache;

	@Override
	public TerrainRenderContext fabric_getRenderer() {
		return ((AccessChunkRendererRegion) chunkCache).fabric_getRenderer();
	}

	@Override
	public void fabric_setRenderer(TerrainRenderContext renderer) {
		((AccessChunkRendererRegion) chunkCache).fabric_setRenderer(renderer);
	}
}