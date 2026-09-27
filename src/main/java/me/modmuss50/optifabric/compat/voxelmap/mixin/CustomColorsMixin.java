package me.modmuss50.optifabric.compat.voxelmap.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

@Pseudo
@Mixin(targets = "net.optifine.CustomColors", remap = false)
class CustomColorsMixin {
	@ModifyVariable(method = {"getWorldFogColor", "getWorldSkyColor"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getX()D", remap = true), argsOnly = true)
	private static Entity focusEntity(Entity focusedEntity) {
		return focusedEntity != null ? focusedEntity : MinecraftClient.getInstance().player;
	}
}