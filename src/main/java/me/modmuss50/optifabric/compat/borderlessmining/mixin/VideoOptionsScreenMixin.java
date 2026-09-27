package me.modmuss50.optifabric.compat.borderlessmining.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.client.gui.screen.option.VideoOptionsScreen;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;
import me.modmuss50.optifabric.compat.borderlessmining.MagicMixinBridge;

@Mixin(VideoOptionsScreen.class)
@InterceptingMixin("link/infra/borderlessmining/mixin/FullScreenOptionMixin")
abstract class VideoOptionsScreenMixin implements MagicMixinBridge {
	@Shim
	private native void modifyOption(Args args);

	@Override
	public void optifabric£modifyFullscreenButton(Args args) {
		modifyOption(args);
	}
}