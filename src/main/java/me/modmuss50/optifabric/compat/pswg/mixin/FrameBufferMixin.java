package me.modmuss50.optifabric.compat.pswg.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gl.Framebuffer;

@Mixin(Framebuffer.class)
abstract class FrameBufferMixin {
	@Shadow(remap = false)
	private boolean stencilEnabled;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;resize(IIZ)V"))
	private void stencilUp(CallbackInfo call) {
		stencilEnabled = true;
	}

	@Redirect(method = "initFbo", at = @At(value = "INVOKE", target = "Lnet/optifine/reflect/ReflectorForge;getForgeUseCombinedDepthStencilAttachment()Z", remap = false))
	private boolean combined() {
		return true;
	}
}