package me.modmuss50.optifabric.compat.indigo.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(BlockModelRenderer.class)
@InterceptingMixin("net/fabricmc/fabric/mixin/client/indigo/renderer/MixinBlockModelRenderer")
abstract class BlockModelRendererMixin {
	@Inject(at = @At("HEAD"),
			method = {"renderModel", "tesselateBlock"}, remap = false,
			cancellable = true)
	private void hookRender(BlockRenderView blockView, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer buffer, boolean checkSides, Random rand, long seed, int overlay, @Coerce Object modelData, CallbackInfoReturnable<Boolean> call) {
		hookRender(blockView, model, state, pos, matrix, buffer, checkSides, rand, seed, overlay, call);
	}

	@Shim
	private native void hookRender(BlockRenderView blockView, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer buffer, boolean checkSides, Random rand, long seed, int overlay, CallbackInfoReturnable<Boolean> call);

	/** renderQuad */
	public @Final void method_23073(BlockRenderView world, BlockState state, BlockPos pos, VertexConsumer vertexConsumer, Entry matrixEntry, BakedQuad quad, float brightness0, float brightness1, float brightness2, float brightness3, int light0, int light1, int light2, int light3, int overlay) {
		//If we get here a call has gone awry, OptiFine adds an argument to the end of this method so the body is naturally gone
		//The method is also normally private so quite possibly this isn't really a problem that gets seem
		//Either way, a nice GUI to explain what has happened would be nice, but we have no idea what state the game is in to show one
		throw new UnsupportedOperationException("You've found a bug - please report this crash here: https://github.com/Chocohead/OptiFabric/issues");
	}
}