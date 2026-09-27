package me.modmuss50.optifabric.compat.carpet.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Group;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.render.WorldRenderer;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(WorldRenderer.class)
@InterceptingMixin("carpet/mixins/WorldRenderer_pausedShakeMixin")
abstract class WorldRendererExtraMixin {
	@Group(min = 2, max = 3)
	@ModifyVariable(method = "render", argsOnly = true, ordinal = 0,
					at = @At(value = "INVOKE", 
								target = "Lnet/minecraft/client/particle/ParticleManager;renderParticles(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/render/Frustum;)V",
								shift = At.Shift.BEFORE))
	private float doChangeTickPhaseBack(float previous) {
		return changeTickPhaseBack(previous);
	}

	@Group(min = 2, max = 3)
	@ModifyVariable(method = "render", argsOnly = true, ordinal = 0,
					at = @At(value = "INVOKE",
								target = "Lnet/minecraft/client/particle/ParticleManager;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/render/Frustum;)V",
								shift = At.Shift.BEFORE))
	private float doNewChangeTickPhaseBack(float previous) {
		return changeTickPhaseBack(previous);
	}

	@Shim
    private native float changeTickPhaseBack(float previous);
}