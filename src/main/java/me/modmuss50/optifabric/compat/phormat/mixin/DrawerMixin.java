package me.modmuss50.optifabric.compat.phormat.mixin;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.Glyph;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(targets = "net/minecraft/client/font/TextRenderer$Drawer")
@InterceptingMixin("user11681/phormat/asm/mixin/TextRendererDrawerMixin")
abstract class DrawerMixin {
	@PlacatingSurrogate
	public void formatCustom(int charIndex, Style style, int codePoint, CallbackInfoReturnable<Boolean> call, FontStorage storage, Glyph glyph, GlyphRenderer glyphRenderer,
								boolean isBold, float alpha, TextColor colour, float red, float green, float blue) {
	}

	@Inject(method = "accept(ILnet/minecraft/text/Style;I)Z", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void formatCustom(int charIndex, Style style, int codePoint, CallbackInfoReturnable<Boolean> call, FontStorage storage, Glyph glyph, GlyphRenderer glyphRenderer,
								boolean isBold, float alpha, TextColor colour, float red, float green, float blue, float advance) {
		formatCustom(charIndex, style, codePoint, call, storage, glyph, glyphRenderer, isBold, red, green, blue, alpha, advance);
	}

	@Shim
	public native void formatCustom(int charIndex, Style style, int codePoint, CallbackInfoReturnable<Boolean> call, FontStorage storage, Glyph glyph,
									GlyphRenderer glyphRenderer, boolean isBold, float red, float green, float blue, float alpha, float advance);
}