package me.modmuss50.optifabric.compat.fabricrenderingdata.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(ChunkRendererRegion.class)
@InterceptingMixin({"net/fabricmc/fabric/mixin/rendering/data/attachment/client/MixinChunkRendererRegion", "net/fabricmc/fabric/mixin/rendering/data/attachment/client/ChunkRendererRegionMixin"})
abstract class ChunkRendererRegionMixin {
	@PlacatingSurrogate
	private static void init(World world, BlockPos startPos, BlockPos endPos, int chunkRadius, CallbackInfoReturnable<ChunkRendererRegion> call) {
	}

	@Inject(at = @At("RETURN"), method = "generateCache", remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void initOF(World world, BlockPos startPos, BlockPos endPos, int chunkRadius, boolean checkEmpty, CallbackInfoReturnable<ChunkRendererRegion> call, int i, int j, int k, int l, WorldChunk[][] chunks) {
		init(world, startPos, endPos, chunkRadius, call, i, k, j, l, chunks);
	}

	@Shim
	private native static void init(World world, BlockPos startPos, BlockPos endPos, int chunkRadius, CallbackInfoReturnable<ChunkRendererRegion> call, int i, int j, int k, int l, WorldChunk[][] chunks);
}