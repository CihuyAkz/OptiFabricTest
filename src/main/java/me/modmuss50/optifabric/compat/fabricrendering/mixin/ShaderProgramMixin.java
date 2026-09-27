package me.modmuss50.optifabric.compat.fabricrendering.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.class_5944;
import net.minecraft.util.Identifier;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(class_5944.class)
@InterceptingMixin("net/fabricmc/fabric/mixin/client/rendering/shader/ShaderProgramMixin")
abstract class ShaderProgramMixin {
	@Shim
	private native String modifyProgramId(String id);

	@ModifyVariable(method = "<init>(Lnet/minecraft/class_5912;Lnet/minecraft/util/Identifier;Lnet/minecraft/client/render/VertexFormat;)V",
					at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Identifier;getNamespace()Ljava/lang/String;", ordinal = 0), argsOnly = true, allow = 1)
	private Identifier modifyProgramID(Identifier id) {
		String in = id.toString();
		String out = modifyProgramId(in);
		return in != out ? new Identifier(out) : id;
	}
}