package me.modmuss50.optifabric.compat.indigo.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.chunk.ChunkRendererRegion;

@Pseudo
@Mixin(targets = "net/optifine/override/ChunkCacheOF", remap = false)
public interface ChunkCacheOFAccess {
	@Accessor
	ChunkRendererRegion getChunkCache();
}