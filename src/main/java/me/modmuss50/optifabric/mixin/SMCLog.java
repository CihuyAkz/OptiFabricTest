package me.modmuss50.optifabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;

@Pseudo
@Mixin(targets = "net.optifine.shaders.SMCLog", remap = false)
public interface SMCLog {
	@Invoker
	public static void callInfo(String message) {
		throw new AssertionError("Mixin failed to apply");
	}
}