package me.modmuss50.optifabric.compat.fabricrenderingdata.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;

import me.modmuss50.optifabric.compat.frex.BridgedChunkRendererRegion;
import me.modmuss50.optifabric.compat.frex.mixin.ChunkCacheOF;

@Mixin(value = BridgedChunkRendererRegion.class, remap = false)
abstract class BridgedChunkRendererRegionMixin implements RenderAttachedBlockView {
	@Shadow
	private @Final ChunkCacheOF wrapped;

	@Override
	public Object getBlockEntityRenderAttachment(BlockPos pos) {
		return ((RenderAttachedBlockView) wrapped).getBlockEntityRenderAttachment(pos);
	}
}