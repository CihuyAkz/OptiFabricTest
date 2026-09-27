package me.modmuss50.optifabric.compat.fabricrendererapi.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

@Mixin(BlockRenderManager.class)
abstract class BlockRenderManagerMixin {
	@Inject(method = {"renderModel", "renderBatched"}, remap = false, locals = LocalCapture.CAPTURE_FAILSOFT,
			at = {@At(value = "INVOKE", shift = Shift.AFTER, remap = true,
					target = "Lnet/minecraft/util/crash/CrashReportSection;addBlockInfo(Lnet/minecraft/util/crash/CrashReportSection;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"),
					@At(value = "INVOKE", shift = Shift.AFTER, remap = true,
						target = "Lnet/minecraft/util/crash/CrashReportSection;addBlockInfo(Lnet/minecraft/util/crash/CrashReportSection;"
								+ "Lnet/minecraft/world/HeightLimitView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V")
			})
	private void addInfo(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull, Random random,
							@Coerce Object modelData, CallbackInfoReturnable<Boolean> call, Throwable t, CrashReport crash, CrashReportSection blockInfo) {
		blockInfo.add("Block render type", state.getRenderType());
	}
}