package me.modmuss50.optifabric.compat.full_slabs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(BlockRenderManager.class)
@InterceptingMixin("dev/micalobia/full_slabs/mixin/client/render/block/BlockRenderManagerMixin")
abstract class BlockRenderManagerMixin {
	@Inject(method = "renderBlockDamage", remap = false, cancellable = true,
			at = @At(value = "INVOKE", remap = true,
						target = "Lnet/minecraft/client/render/block/BlockModels;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;"))
	private void renderSlabDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertices, @Coerce Object modelData, CallbackInfo call) {
		renderSlabDamage(state, pos, world, matrices, vertices, call);
	}

	@Shim
	public abstract void renderSlabDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertices, CallbackInfo call);
}