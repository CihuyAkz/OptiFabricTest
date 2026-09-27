package me.modmuss50.optifabric.compat.amecs.mixin;

import java.io.PrintWriter;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.LoudCoerce;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(GameOptions.class)
@InterceptingMixin("de/siphalor/amecs/impl/mixin/MixinGameOptions")
abstract class GameOptionsMixin {
	@PlacatingSurrogate
	public void onKeyBindingWritten(CallbackInfo callbackInfo, PrintWriter printWriter, @LoudCoerce(value = "null", remap = false) Object closeException,
										KeyBinding[] keyBindings, int keyBindingsCount, int index) {
	}

	@Inject(method = "write",
			at = @At(value = "INVOKE", target = "Ljava/io/PrintWriter;println(Ljava/lang/String;)V", ordinal = 1, remap = false),
			slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;keysAll:[Lnet/minecraft/client/option/KeyBinding;")),
			locals = LocalCapture.CAPTURE_FAILSOFT)
	private void onKeyBindingWritten(CallbackInfo callbackInfo, PrintWriter printWriter, @Coerce Object closeException,
										KeyBinding[] keyBindings, int keyBindingsCount, int index, KeyBinding keyBinding) {
		onKeyBindingWritten(callbackInfo, printWriter, keyBindings, keyBindingsCount, index, keyBinding);
	}

	@Shim
	public abstract void onKeyBindingWritten(CallbackInfo callbackInfo, PrintWriter printWriter, KeyBinding[] keyBindings, int keyBindingsCount, int index, KeyBinding keyBinding);
}