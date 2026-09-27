package me.modmuss50.optifabric.compat.fabriclifecycleevents.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(ClientChunkManager.class)
@InterceptingMixin("net/fabricmc/fabric/mixin/event/lifecycle/client/ClientChunkManagerMixin")
abstract class ClientChunkManagerNewMixin {
	@Inject(method = {"loadChunkFromPacket", "method_16020(IILnet/minecraft/class_2540;Lnet/minecraft/class_2487;Ljava/util/function/Consumer;)Lnet/minecraft/class_2818;"},
			at = @At(value = "NEW", target = "net/optifine/ChunkOF", remap = false, shift = Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void onOFChunkUnload(int x, int z, PacketByteBuf buf, NbtCompound tag, Consumer<?> beVisitor, CallbackInfoReturnable<WorldChunk> call, int index, WorldChunk chunk, ChunkPos pos) {
		onChunkUnload(x, z, buf, tag, beVisitor, call, index, chunk, pos);
	}

	@Shim
	private native void onChunkUnload(int x, int z, PacketByteBuf buf, NbtCompound tag, Consumer<?> beVisitor, CallbackInfoReturnable<WorldChunk> call, int index, WorldChunk chunk, ChunkPos pos);
}