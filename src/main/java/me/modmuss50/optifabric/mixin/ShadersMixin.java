package me.modmuss50.optifabric.mixin;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.fabricmc.loader.api.FabricLoader;

@Pseudo
@Mixin(targets = "net.optifine.shaders.Shaders", remap = false)
class ShadersMixin {
	@ModifyVariable(method = "loadShaderPack", at = @At(value = "FIELD", target = "Lnet/optifine/shaders/Shaders;shadersConfig:Ljava/util/Properties;", opcode = Opcodes.GETSTATIC), name = "shadersBlocked")
	private static boolean loadCarefully(boolean shadersBlocked) {
		if (FabricLoader.getInstance().isModLoaded("satin")) {
			SMCLog.callInfo("Shaders can not be loaded, the mod Satin is installed.");
			return true;
		} else {
			return shadersBlocked;
		}
	}
}