package me.modmuss50.optifabric.compat.ae2.mixin;

import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;

import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;

@Pseudo
@Mixin(targets = "appeng/client/render/tesr/ChestTileEntityRenderer$FaceRotatingModel", remap = false)
abstract class FaceRotatingModelMixin extends ForwardingBakedModel {
	@Unique
	private BakedQuad activeQuad;

	@Inject(method = "getQuads(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;Ljava/util/Random;)Ljava/util/List;",
			at = @At(value = "NEW", target = "net/minecraft/client/render/model/BakedQuad"), locals = LocalCapture.CAPTURE_FAILEXCEPTION,
			remap = true)
	private void grabQuad(BlockState state, Direction side, Random rand, CallbackInfoReturnable<List<BakedQuad>> call, List<BakedQuad> quads, int i, BakedQuad quad) {
		activeQuad = quad;
	}

	@ModifyArg(method = "getQuads(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;Ljava/util/Random;)Ljava/util/List;",
				at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/BakedQuad;<init>([IILnet/minecraft/util/math/Direction;Lnet/minecraft/client/texture/Sprite;Z)V"),
				remap = true)
	private Sprite notSoNull(Sprite actuallyNull) {
		try {
			return actuallyNull != null ? actuallyNull : ((BakedQuadAccess) activeQuad).getSprite();
		} finally {
			activeQuad = null;
		}
	}
}