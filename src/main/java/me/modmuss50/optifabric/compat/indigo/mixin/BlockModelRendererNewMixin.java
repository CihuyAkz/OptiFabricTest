package me.modmuss50.optifabric.compat.indigo.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.class_5819;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(BlockModelRenderer.class)
@InterceptingMixin({"net/fabricmc/fabric/mixin/client/indigo/renderer/MixinBlockModelRenderer", "net/fabricmc/fabric/mixin/client/indigo/renderer/BlockModelRendererMixin"})
abstract class BlockModelRendererNewMixin {
	@Group(min = 1, max = 1)
	@Inject(at = @At("HEAD"),
			method = {"renderModel", "tesselateBlock"}, remap = false,
			cancellable = true)
	private void hookRender(BlockRenderView blockView, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer buffer, boolean checkSides, class_5819 rand, long seed, int overlay, @Coerce Object modelData, CallbackInfo call) {
		hookRender(blockView, model, state, pos, matrix, buffer, checkSides, rand, seed, overlay, call);
	}

	@Group(min = 1, max = 1)
	@Inject(method = "tesselateBlock", remap = false, at = @At("HEAD"), cancellable = true)
	private void hookRender(BlockRenderView blockView, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer buffer, boolean checkSides, class_5819 rand, long seed, int overlay, @Coerce Object modelData, RenderLayer layer, CallbackInfo call) {
		hookRender(blockView, model, state, pos, matrix, buffer, checkSides, rand, seed, overlay, call);
	}

	@Shim
	private native void hookRender(BlockRenderView blockView, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer buffer, boolean checkSides, class_5819 rand, long seed, int overlay, CallbackInfo call);
}