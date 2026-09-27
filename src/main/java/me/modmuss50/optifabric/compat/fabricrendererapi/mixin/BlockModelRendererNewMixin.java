package me.modmuss50.optifabric.compat.fabricrendererapi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.class_5819;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockRenderView;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;

@Mixin(BlockModelRenderer.class)
abstract class BlockModelRendererNewMixin {
	@Group(min = 1, max = 1)
	@Inject(method = {"renderModel", "tesselateBlock"}, remap = false, locals = LocalCapture.CAPTURE_FAILSOFT,
			at = @At(value = "INVOKE_ASSIGN", remap = true,
					target = "Lnet/minecraft/util/crash/CrashReportSection;add(Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraft/util/crash/CrashReportSection;"))
	private void addInfo(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull,
						class_5819 random, long seed, int overlay, @Coerce Object modelData, CallbackInfo call, boolean useAO, Vec3d modelOffset, Throwable t,
						CrashReport crash, CrashReportSection modelInfo) {
		boolean isFabricModel = model instanceof FabricBakedModel;

		modelInfo.add("Model class", model != null ? model.getClass().getName() : model);
		modelInfo.add("Is Fabric model", isFabricModel);
		if (isFabricModel) modelInfo.add("Is adapted vanilla model", ((FabricBakedModel) model).isVanillaAdapter());
	}

	@Group(min = 1, max = 1)
	@Inject(method = "tesselateBlock", remap = false, locals = LocalCapture.CAPTURE_FAILSOFT,
			at = @At(value = "INVOKE_ASSIGN", remap = true,
					target = "Lnet/minecraft/util/crash/CrashReportSection;add(Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraft/util/crash/CrashReportSection;"))
	private void addInfo(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull,
						class_5819 random, long seed, int overlay, @Coerce Object modelData, RenderLayer layer, CallbackInfo call, boolean useAO, Vec3d modelOffset,
						Throwable t, CrashReport crash, CrashReportSection modelInfo) {
		addInfo(world, model, state, pos, matrix, vertexConsumer, cull, random, seed, overlay, modelData, call, useAO, modelOffset, t, crash, modelInfo);
	}

	@Group(min = 1, max = 1)
	@Inject(method = "tesselateBlock{1, 2}", remap = false, locals = LocalCapture.CAPTURE_FAILSOFT,
			at = @At(value = "INVOKE_ASSIGN", remap = true,
					target = "Lnet/minecraft/util/crash/CrashReportSection;add(Ljava/lang/String;Ljava/lang/Object;)Lnet/minecraft/util/crash/CrashReportSection;"))
	private void addInfo(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrix, VertexConsumer vertexConsumer, boolean cull,
						class_5819 random, long seed, int overlay, @Coerce Object modelData, RenderLayer layer, boolean queryModelSpecificData, CallbackInfo call,
						boolean useAO, Vec3d modelOffset, Throwable t, CrashReport crash, CrashReportSection modelInfo) {
		addInfo(world, model, state, pos, matrix, vertexConsumer, cull, random, seed, overlay, modelData, call, useAO, modelOffset, t, crash, modelInfo);
	}
}