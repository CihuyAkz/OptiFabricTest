package me.modmuss50.optifabric.mod;

import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class Text {
	public static MutableText literal(String text) {
		return new LiteralText(text);
	}

	public static MutableText literal(String text, Formatting style) {
		return literal(text).formatted(style);
	}
}