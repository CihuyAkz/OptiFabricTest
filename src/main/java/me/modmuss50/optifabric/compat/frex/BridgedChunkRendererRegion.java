package me.modmuss50.optifabric.compat.frex;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.WorldChunk.CreationType;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.level.ColorResolver;

import me.modmuss50.optifabric.compat.frex.mixin.ChunkCacheOF;

public class BridgedChunkRendererRegion extends ChunkRendererRegion {
	private final ChunkCacheOF wrapped;

	public BridgedChunkRendererRegion(ChunkCacheOF wrapped) {
		super(null, 0, 0, new WorldChunk[0][0], BlockPos.ORIGIN, BlockPos.ORIGIN);

		this.wrapped = wrapped;
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		return wrapped.getBlockState(pos);
	}

	@Override
	public FluidState getFluidState(BlockPos pos) {
		return wrapped.getFluidState(pos);
	}

	@Override
	public float getBrightness(Direction direction, boolean shaded) {
		return wrapped.getBrightness(direction, shaded);
	}

	@Override
	public LightingProvider getLightingProvider() {
		return wrapped.getLightingProvider();
	}

	@Override
	public BlockEntity getBlockEntity(BlockPos pos) {
		return wrapped.getBlockEntity(pos);
	}

	@Override
	public BlockEntity getBlockEntity(BlockPos pos, CreationType creation) {
		return wrapped.getBlockEntity(pos, creation);
	}

	@Override
	public int getColor(BlockPos pos, ColorResolver colorResolver) {
		return wrapped.getColor(pos, colorResolver);
	}
}