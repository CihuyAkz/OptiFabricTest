package me.modmuss50.optifabric.compat.charm.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(BlockEntityRenderDispatcher.class)
@InterceptingMixin("svenhjol/charm/mixin/storage_labels/CallStorageLabelsRenderMixin")
abstract class BlockEntityRenderDispatcherMixin {
	@PlacatingSurrogate
	private static void hookExtraRender(BlockEntityRenderer<BlockEntity> renderer, BlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo call, World world) {
	}

	@Inject(method = "render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V",
			at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void hookExtraRender(BlockEntityRenderer<BlockEntity> renderer, BlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo call, World world, int light) {
		hookExtraRender(renderer, blockEntity, tickDelta, matrices, vertexConsumers, call, light);
	}

	@Shim
	private static native <T extends BlockEntity> void hookExtraRender(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo call, int light);
}