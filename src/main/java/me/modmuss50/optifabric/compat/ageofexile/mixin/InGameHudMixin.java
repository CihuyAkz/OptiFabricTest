package me.modmuss50.optifabric.compat.ageofexile.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;

@Mixin(InGameHud.class)
abstract class InGameHudMixin {
	@Inject(method = "getHeartCount", at = @At("HEAD"), cancellable = true)
	public void on$getMaxHealth(LivingEntity entity, CallbackInfoReturnable<Integer> call) {
		//Follow what https://github.com/RobertSkalko/Age-of-Exile/blob/master/src/main/java/com/robertx22/age_of_exile/mixins/InGameHudMixin.java#L43 does
		call.setReturnValue(entity == null || !entity.isLiving() ? 0 : 10);
	}
}