package me.modmuss50.optifabric.mod;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Registries {
	public static Identifier getID(Block block) {
		return Registry.BLOCK.getId(block);
	}
}