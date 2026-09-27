package me.modmuss50.optifabric.compat.indigo.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.base.Suppliers;

import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk;
import net.minecraft.client.render.chunk.ChunkBuilder.ChunkData;
import net.minecraft.client.render.chunk.ChunkRendererRegion;

import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;

import me.modmuss50.optifabric.compat.indigo.ChunkRendererRegionAccess;

@Pseudo
@Mixin(targets = "net/optifine/override/ChunkCacheOF", remap = false)
abstract class ChunkCacheOFMixin implements ChunkRendererRegionAccess {
	@Shadow
	private @Final ChunkRendererRegion chunkCache;
	@Unique
	private Supplier<TerrainRenderContext> optifabric_renderContextFactory;

	@Override
	public void optifabric_setRenderer(TerrainRenderContext renderContext, BuiltChunk chunk, ChunkData data, BlockBufferBuilderStorage buffer) {
		optifabric_renderContextFactory = Suppliers.memoize(() -> renderContext.prepare(chunkCache, chunk, data, buffer));
	}

	@Override
	public TerrainRenderContext fabric_getRenderer() {
		return optifabric_renderContextFactory.get();
	}

	@Inject(method = "renderFinish()V", at = @At("RETURN"))
	private void cleanup(CallbackInfo call) {
		optifabric_renderContextFactory = null;
	}
}