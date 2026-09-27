package me.modmuss50.optifabric.compat.multiconnect.mixin;

import java.util.Iterator;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.Block;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(ModelLoader.class)
@InterceptingMixin("net/earthcomputer/multiconnect/mixin/bridge/MixinModelLoader")
abstract class ModelLoaderMixin {
	@Redirect(method = "processLoading", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DefaultedRegistry;getIds()Ljava/util/Set;", remap = true), remap = false)
    private Set<Identifier> redirectGetIDs(DefaultedRegistry<?> registry) {
        return redirectGetIds(registry);
    }

	@Shim
    private native Set<Identifier> redirectGetIds(DefaultedRegistry<?> registry);

    @Redirect(method = "processLoading", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DefaultedRegistry;iterator()Ljava/util/Iterator;", remap = true), remap = false)
    private Iterator<Block> doRedirectBlockRegistryIterator(DefaultedRegistry<Block> registry) {
        return redirectBlockRegistryIterator(registry);
    }

    @Shim
    private native Iterator<Block> redirectBlockRegistryIterator(DefaultedRegistry<Block> registry);
}