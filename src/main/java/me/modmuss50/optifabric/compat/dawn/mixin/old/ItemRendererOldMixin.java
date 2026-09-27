package me.modmuss50.optifabric.compat.dawn.mixin.old;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(ItemRenderer.class)
@InterceptingMixin("com/hugman/dawn/mod/mixin/client/ItemRendererMixin")
abstract class ItemRendererMixin {
	@Redirect(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
				at = @At(value = "INVOKE", target = "Lnet/optifine/reflect/ReflectorForge;isItemDamaged(Lnet/minecraft/item/ItemStack;)Z"))
	private boolean optifabric_appearsDamaged(ItemStack stack) {
		return dawn_appearsDamaged(stack); //No real need to fallback on the presumably missing Forge hook
	}

	@Shim
	public native boolean dawn_appearsDamaged(ItemStack stack);
}