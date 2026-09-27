package me.modmuss50.optifabric.compat.zoomify;

import java.io.IOException;
import java.util.function.Consumer;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.ASMUtils;
import me.modmuss50.optifabric.util.RemappingUtils;

import net.fabricmc.loader.launch.common.FabricLauncherBase;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class ZoomifyMixinPlugin extends InterceptingMixinPlugin {
    private final String className = RemappingUtils.getClassName("class_757"); // net.minecraft.client.render.GameRenderer
    private final String methodName = RemappingUtils.getMethodName("class_757", "method_3196", "(Lnet/minecraft/class_4184;FZ)D"); // void getFOV(Camera, float, boolean)
    private final String methodDesc = RemappingUtils.mapMethodDescriptor("(Lnet/minecraft/class_4184;FZ)D");

    private int access = 0;

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);

        // https://github.com/Chocohead/OptiFabric/pull/630#issuecomment-1076728336
        // I'm not sure if it is a good idea, but it works
        // recover the access, then mixin, then restore the access
        this.withMethodNode(targetClass, method -> {
            this.access = method.access;
            try {
                this.withMethodNode(ASMUtils.readClass(FabricLauncherBase.getLauncher().getClassByteArray(targetClassName, true)), m -> method.access = m.access);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        super.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);

        this.withMethodNode(targetClass, method -> method.access = this.access);
    }

    private void withMethodNode(ClassNode targetClass, Consumer<MethodNode> consumer) {
        if (this.className.equals(targetClass.name)) {
            targetClass.methods.stream()
                .filter(method -> this.methodName.equals(method.name) && this.methodDesc.equals(method.desc))
                .findFirst()
                .ifPresent(consumer);
        }
    }
}
