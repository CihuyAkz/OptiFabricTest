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

import net.minecraft.class_5819;
import net.minecraft.class_846.class_851.class_4578.class_7435;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.util.math.BlockPos;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.LoudCoerce;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(targets = "net.minecraft.client.render.chunk.ChunkBuilder$BuiltChunk$RebuildTask")
@InterceptingMixin("net/fabricmc/fabric/mixin/client/indigo/renderer/MixinChunkRebuildTask")
abstract class RebuildTaskNewMixin {
	@Shadow
	protected BuiltChunk field_20839;

	@Inject(method = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask;render(FFFLnet/minecraft/client/render/chunk/BlockBufferBuilderStorage;)Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk$RebuildTask$RenderData;",
			at = @At(value = "INVOKE", target = "Lnet/optifine/BlockPosM;getAllInBoxMutable(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Ljava/lang/Iterable;", shift = Shift.AFTER),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void hookChunkBuild(float cameraX, float cameraY, float cameraZ, BlockBufferBuilderStorage buffer, CallbackInfoReturnable<?> call, class_7435 renderData,
								int one, BlockPos origin, BlockPos oppositeOrigin, ChunkOcclusionDataBuilder occlusionBuilder, MatrixStack matrices, @Coerce ChunkCacheOFAccess chunkCache,
								RenderLayer[] singleLayer, boolean shaders, boolean shadersMidBlock, Set<RenderLayer> initializedLayers, class_5819 random, BlockRenderManager renderManager) {
		hookChunkBuild(cameraX, cameraY, cameraZ, buffer, call, renderData, one, origin, oppositeOrigin, occlusionBuilder, chunkCache.getChunkCache(), matrices, initializedLayers, random, renderManager);
	}

	@Shim
	private native void hookChunkBuild(float cameraX, float cameraY, float cameraZ, BlockBufferBuilderStorage builder, CallbackInfoReturnable<?> call, class_7435 renderData,
										int one, BlockPos blockPos, BlockPos blockPos2, ChunkOcclusionDataBuilder chunkOcclusionDataBuilder, ChunkRendererRegion region,
										MatrixStack matrixStack, Set<RenderLayer> initializedLayers, class_5819 abstractRandom, BlockRenderManager blockRenderManager);

	@PlacatingSurrogate
	private void hookChunkBuild(float cameraX, float cameraY, float cameraZ, BlockBufferBuilderStorage buffer, CallbackInfoReturnable<?> call, class_7435 renderData,
								int one, BlockPos origin, BlockPos oppositeOrigin, ChunkOcclusionDataBuilder occlusionBuilder, MatrixStack matrices,
								@LoudCoerce(value="net/optifine/override/ChunkCacheOF", remap=false) Object chunkCache, RenderLayer[] singleLayer, boolean shaders, boolean shadersMidBlock) {
	}
}