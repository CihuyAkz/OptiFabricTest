package me.modmuss50.optifabric.compat.fabricscreenapi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class Events {
	/** Post the {@link ScreenEvents#AFTER_INIT} event for the given screen */
	public static void afterInit(MinecraftClient client, Screen screen, int width, int height) {
		ScreenEvents.AFTER_INIT.invoker().afterInit(client, screen, width, height);
	}
}