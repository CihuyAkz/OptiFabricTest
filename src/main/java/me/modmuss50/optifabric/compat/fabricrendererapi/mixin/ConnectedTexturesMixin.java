package me.modmuss50.optifabric.compat.fabricrendererapi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;

@Pseudo
@Mixin(targets = "net.optifine.ConnectedTextures", remap = false)
abstract class ConnectedTexturesMixin {
	@Inject(method = "getNeighbourIcon",
			at = @At(value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/client/render/block/BlockModels;getModel(Lnet/minecraft/block/BlockState;)Lnet/minecraft/client/render/model/BakedModel;",
					remap = true),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION, //Avoid crashing completely when it can be mitigated turning off connected textures
			cancellable = true)
	private static void skipNeighbourIcon(BlockView world, BlockState state, BlockPos pos, BlockState neighbourState, int side, CallbackInfoReturnable<Sprite> call, BakedModel model) {
		if (model instanceof FabricBakedModel && !((FabricBakedModel) model).isVanillaAdapter()) {
			call.setReturnValue(null); //Could attempt to handle properly via FabricBakedModel#emitBlockQuads...
		}
	}
}