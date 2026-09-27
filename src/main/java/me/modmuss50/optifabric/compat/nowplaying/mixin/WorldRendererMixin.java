package me.modmuss50.optifabric.compat.nowplaying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.text.Text;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(WorldRenderer.class)
@InterceptingMixin("com/github/scotsguy/nowplaying/mixin/WorldRendererMixin")
abstract class WorldRendererMixin {
	@Shim
	private native void modifyRecordPlayingOverlay(InGameHud hud, Text text);

	@Redirect(method = "playRecord",
				at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;setRecordPlayingOverlay(Lnet/minecraft/text/Text;)V", remap = true),
				remap = false)
    private void doModifyRecordPlayingOverlay(InGameHud hud, Text text) {
		modifyRecordPlayingOverlay(hud, text);
	}
}