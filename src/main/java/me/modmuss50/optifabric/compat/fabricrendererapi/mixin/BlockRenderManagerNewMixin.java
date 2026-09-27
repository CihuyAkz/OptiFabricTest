package me.modmuss50.optifabric.compat.fabricrendererapi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.class_5819;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

@Mixin(BlockRenderManager.class)
abstract class BlockRenderManagerNewMixin {
	@Group(min = 1, max = 1)
	@Inject(method = "renderBatched", remap = false, locals = LocalCapture.CAPTURE_FAILSOFT,
			at = @At(value = "INVOKE", shift = Shift.AFTER, remap = true,
					target = "Lnet/minecraft/util/crash/CrashReportSection;addBlockInfo(Lnet/minecraft/util/crash/CrashReportSection;"
							+ "Lnet/minecraft/world/HeightLimitView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
			)
	private void addInfo(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull,
						class_5819 random, @Coerce Object modelData, CallbackInfo call, Throwable t, CrashReport crash, CrashReportSection blockInfo) {
		blockInfo.add("Block render type", state.getRenderType());
	}

	@Group(min = 1, max = 1)
	@Inject(method = "renderBatched", remap = false, locals = LocalCapture.CAPTURE_FAILSOFT,
			at = @At(value = "INVOKE", shift = Shift.AFTER, remap = true,
					target = "Lnet/minecraft/util/crash/CrashReportSection;addBlockInfo(Lnet/minecraft/util/crash/CrashReportSection;"
							+ "Lnet/minecraft/world/HeightLimitView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
			)
	private void addInfo(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull, class_5819 random,
						@Coerce Object modelData, RenderLayer layer, CallbackInfo call, Throwable t, CrashReport crash, CrashReportSection blockInfo) {
		addInfo(state, pos, world, matrix, vertexConsumer, cull, random, modelData, call, t, crash, blockInfo);
	}

	@Group(min = 1, max = 1)
	@Inject(method = "renderBatched{1, 2}", remap = false, locals = LocalCapture.CAPTURE_FAILSOFT,
			at = @At(value = "INVOKE", shift = Shift.AFTER, remap = true,
					target = "Lnet/minecraft/util/crash/CrashReportSection;addBlockInfo(Lnet/minecraft/util/crash/CrashReportSection;"
							+ "Lnet/minecraft/world/HeightLimitView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
			)
	private void addInfo(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull, class_5819 random,
						@Coerce Object modelData, RenderLayer layer, boolean queryModelSpecificData, CallbackInfo call, Throwable t, CrashReport crash, CrashReportSection blockInfo) {
		addInfo(state, pos, world, matrix, vertexConsumer, cull, random, modelData, call, t, crash, blockInfo);
	}
}