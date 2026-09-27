package me.modmuss50.optifabric.mixin;

import java.io.IOException;
import java.io.InputStream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.resource.DefaultResourcePack;

import me.modmuss50.optifabric.mod.OptifineResources;

@Mixin(value = DefaultResourcePack.class, priority = 400)
abstract class DefaultResourcePackMixin {
	@Redirect(method = "getResourceOF", remap = false,
				at = @At(value = "INVOKE", target = "Lnet/optifine/reflect/ReflectorForge;getOptiFineResourceStream(Ljava/lang/String;)Ljava/io/InputStream;"))
	private InputStream onFindInputStream(String path) throws IOException {
		return OptifineResources.INSTANCE.getResource(path);
	}
}