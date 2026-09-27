package me.modmuss50.optifabric.compat.frex.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.chunk.WorldChunk.CreationType;

@Pseudo
@Mixin(targets = "net/optifine/override/ChunkCacheOF", remap = false)
public interface ChunkCacheOF extends BlockRenderView {
	@Invoker(value = "getTileEntity")
	BlockEntity getBlockEntity(BlockPos pos, CreationType creation);
}