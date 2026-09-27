package me.modmuss50.optifabric.compat.fabricrenderingdata.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.math.BlockPos;
import net.minecraft.client.render.chunk.ChunkRendererRegion;

import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;

@Pseudo
@Mixin(targets = "net/optifine/override/ChunkCacheOF", remap = false)
abstract class ChunkCacheOFMixin implements RenderAttachedBlockView {
	@Shadow
	private @Final ChunkRendererRegion chunkCache;

	@Override
	public Object getBlockEntityRenderAttachment(BlockPos pos) {
		return ((RenderAttachedBlockView) chunkCache).getBlockEntityRenderAttachment(pos);
	}
}