package me.modmuss50.optifabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;

@Pseudo
@Mixin(targets = "net.optifine.Config", remap = false)
public interface Config {
	@Invoker
	public static void callShowGuiMessage(String firstLine, String secondLine) {
		throw new AssertionError("Mixin failed to apply");
	}
}