package me.modmuss50.optifabric.compat.fabricmodelloadingapi.mixin;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.Shim;
import net.minecraft.class_7775;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(targets = "net/minecraft/class_1088$class_7778")
@InterceptingMixin("net/fabricmc/fabric/mixin/client/model/loading/ModelLoaderBakerImplMixin")
abstract class ModelLoaderBakerImplMixin {
    @Shim private native UnbakedModel invokeModifyBeforeBake(UnbakedModel model, Identifier id, ModelBakeSettings settings);

    @ModifyVariable(method = "bake", remap = false,
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/class_1088$class_7778;method_45872(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/model/UnbakedModel;", remap = true))
    private UnbakedModel invokeModifyBeforeBake(UnbakedModel model, Identifier id, ModelBakeSettings settings, Function<?, ?> sprites) {
        return invokeModifyBeforeBake(model, id, settings);
    }

    @Shim
    private native BakedModel invokeModifyAfterBake(UnbakedModel unbakedModel, class_7775 baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier id);

    @Redirect(method = "bake", remap = false,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/UnbakedModel;method_4753(Lnet/minecraft/class_7775;Ljava/util/function/Function;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/model/BakedModel;", remap = true))
    private BakedModel optifabric_invokeModifyAfterBake(UnbakedModel unbakedModel, class_7775 baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier id) {
        return invokeModifyAfterBake(unbakedModel, baker, textureGetter, settings, id);
    }


    @Shim private native BakedModel invokeModifyAfterBake(JsonUnbakedModel unbakedModel, class_7775 baker, JsonUnbakedModel parent, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier id, boolean hasDepth);

    @Redirect(method = "bake", remap = false,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel;method_3446(Lnet/minecraft/class_7775;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/function/Function;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/model/BakedModel;", remap = true))
    private BakedModel optifabric_invokeModifyAfterBake(JsonUnbakedModel unbakedModel, class_7775 baker, JsonUnbakedModel parent, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings, Identifier id, boolean hasDepth) {
        return invokeModifyAfterBake(unbakedModel, baker, parent, textureGetter, settings, id, hasDepth);
    }
}
