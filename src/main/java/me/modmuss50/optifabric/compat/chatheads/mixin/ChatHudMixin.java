package me.modmuss50.optifabric.compat.chatheads.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(ChatHud.class)
@InterceptingMixin("dzwdz/chat_heads/mixin/ChatHudMixin")
abstract class ChatHudMixin {
	@PlacatingSurrogate
	private void render(MatrixStack matrices, int tickDelta, CallbackInfo call, int chatWidth, int visibleLines, int visibleMessages, boolean chatFocused, 
						double scale, int scaledWidth, double chatOpacity, double backgroundOpacity, double backgroundHeight, double chatHeight, int drawnLines, 
						int i, ChatHudLine<OrderedText> line, int lineAge, double lineOpacity, int lineAlpha, int backgroundAlpha) {		
	}

	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;I)V",
			at = {@At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I",
					ordinal = 0
			), @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I",
					ordinal = 0
			)},
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void render(MatrixStack matrices, int tickDelta, CallbackInfo call, int chatWidth, int visibleLines, int visibleMessages, boolean chatFocused, double scale,
						int scaledWidth, double chatOpacity, double backgroundOpacity, double backgroundHeight, double chatHeight, int drawnLines, int i, 
						ChatHudLine<OrderedText> line, int lineAge, double lineOpacity, int lineAlpha, int backgroundAlpha, int zero, double lineHeight) {
		render(matrices, tickDelta, call, visibleLines, visibleMessages, chatFocused, scale, scaledWidth, chatOpacity, backgroundOpacity, backgroundHeight, chatHeight,
				drawnLines, i, line, lineOpacity, lineAlpha, backgroundAlpha, zero, lineHeight);
	}

	@Shim
	public native void render(MatrixStack matrices, int tickDelta, CallbackInfo call, int visibleLines, int visibleMessages, boolean chatFocused, double scale,
								int scaledWidth, double chatOpacity, double backgroundOpacity, double backgroundHeight, double chatHeight, int drawnLines, int i, 
								ChatHudLine<OrderedText> line, double lineOpacity, int lineAlpha, int backgroundAlpha, int zero, double lineHeight);
}