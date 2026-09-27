package me.modmuss50.optifabric.compat.stacc.mixin.old;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(PacketByteBuf.class)
@InterceptingMixin("net/devtech/stacc/mixin/optifabric/OptifabricDesyncFixin")
abstract class PacketByteBufMixin {
	@Inject(method = "writeItemStack(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/network/PacketByteBuf;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;writeCompoundTag(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/network/PacketByteBuf;"))
	private void write(ItemStack stack, boolean limitedTag, CallbackInfoReturnable<PacketByteBuf> call) {
		write(stack, call);
		write(stack, call); //Stacc double injects this Mixin from a bug
	}

	@Shim
	private native void write(ItemStack stack, CallbackInfoReturnable<PacketByteBuf> call);
}