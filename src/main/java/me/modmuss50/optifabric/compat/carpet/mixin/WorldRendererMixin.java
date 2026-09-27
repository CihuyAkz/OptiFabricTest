package me.modmuss50.optifabric.compat.carpet.mixin;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.render.WorldRenderer;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(WorldRenderer.class)
@InterceptingMixin("carpet/mixins/WorldRenderer_pausedShakeMixin")
abstract class WorldRendererMixin {
	@ModifyVariable(method = "render", argsOnly = true, ordinal = 0,
					at = @At(value = "FIELD", remap = false, target = "renderInfosEntities:Ljava/util/List;", opcode = Opcodes.GETFIELD))
	private float doChangeTickPhase(float previous) {
		return changeTickPhase(previous);
	}

	@Shim
	private native float changeTickPhase(float previous);
}