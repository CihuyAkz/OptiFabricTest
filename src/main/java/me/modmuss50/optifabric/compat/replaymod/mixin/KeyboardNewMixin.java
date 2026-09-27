package me.modmuss50.optifabric.compat.replaymod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(Keyboard.class)
@InterceptingMixin("com/replaymod/lib/de/johni0702/minecraft/gui/versions/mixin/MixinKeyboardListener")
abstract class KeyboardNewMixin {
	@Shim
	private native void keyPressed(int action, Screen screen, boolean[] handled, int keyCode, int scanCode, int modifiers, CallbackInfo call);

	@PlacatingSurrogate
	private void keyPressed(int action, boolean[] handled, int keyCode, int scanCode, int modifiers, Screen screen, CallbackInfo call) {
		keyPressed(action, screen, handled, keyCode, scanCode, modifiers, call);
	}

	@Shim
	private native void keyReleased(int action, Screen screen, boolean[] handled, int keyCode, int scanCode, int modifiers, CallbackInfo call);

	@PlacatingSurrogate
	private void keyReleased(int action, boolean[] handled, int keyCode, int scanCode, int modifiers, Screen screen, CallbackInfo call) {
		keyReleased(action, screen, handled, keyCode, scanCode, modifiers, call);
	}

	@Shim
	private static native void charTyped(Element element, int keyChar, int modifiers, CallbackInfo call);

	@PlacatingSurrogate
	private static void charTyped(Keyboard self, int keyChar, int modifiers, Element element, CallbackInfo call) {
		charTyped(element, keyChar, modifiers, call);
	}

	@Shim
	private static native void charTyped(Element element, char keyChar, int modifiers, CallbackInfo call);

	@PlacatingSurrogate
	private static void charTyped(Keyboard self, char keyChar, int modifiers, Element element, CallbackInfo call) {
		charTyped(element, keyChar, modifiers, call);
	}
}