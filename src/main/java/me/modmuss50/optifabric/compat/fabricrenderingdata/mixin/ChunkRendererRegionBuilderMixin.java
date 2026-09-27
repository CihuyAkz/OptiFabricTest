package me.modmuss50.optifabric.compat.fabricrenderingdata.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.class_6850;
import net.minecraft.class_6850.class_6851;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(class_6850.class)
@InterceptingMixin({"net/fabricmc/fabric/mixin/rendering/data/attachment/client/MixinChunkRendererRegionBuilder", "net/fabricmc/fabric/mixin/rendering/data/attachment/client/ChunkRendererRegionBuilderMixin"})
abstract class ChunkRendererRegionBuilderMixin {
	@PlacatingSurrogate
	private void create(World world, BlockPos startPos, BlockPos endPos, int chunkRadius, CallbackInfoReturnable<ChunkRendererRegion> call) {
	}

	@Inject(at = @At("RETURN"), method = "createRegion", remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
	private void createOF(World world, BlockPos startPos, BlockPos endPos, int chunkRadius, boolean checkEmpty, CallbackInfoReturnable<ChunkRendererRegion> call, int i, int j, int k, int l, class_6851[][] chunks) {
		create(world, startPos, endPos, chunkRadius, call, i, k, j, l, chunks);
	}

	@Shim
	private native void create(World world, BlockPos startPos, BlockPos endPos, int chunkRadius, CallbackInfoReturnable<ChunkRendererRegion> call, int i, int j, int k, int l, class_6851[][] chunks);
}