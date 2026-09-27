package me.modmuss50.optifabric.compat.architectury.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(GameRenderer.class)
@InterceptingMixin("dev/architectury/mixin/fabric/client/MixinGameRenderer")
abstract class GameRendererNewerererMixin {
    @Shim
    private native void renderScreenPre(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window, Matrix4f matrix, MatrixStack matrices, MatrixStack matrices2);

    @PlacatingSurrogate
    private void renderScreenPre(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window, float guiFarPlane, Matrix4f matrix, MatrixStack matrices) {
    }

    @Inject(method = "render(FJZ)V", locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", ordinal = 0))
    private void renderScreenPre(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window, float guiFarPlane, Matrix4f matrix, MatrixStack matrices, MatrixStack matrices2) {
        renderScreenPre(tickDelta, startTime, tick, ci, mouseX, mouseY, window, matrix, matrices, matrices2);
    }

    @Shim
    private native void renderScreenPost(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window, Matrix4f matrix, MatrixStack matrices, MatrixStack matrices2);

    @PlacatingSurrogate
    private void renderScreenPost(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window, float guiFarPlane, Matrix4f matrix, MatrixStack matrices) {
    }

    @Inject(method = "render(FJZ)V", locals = LocalCapture.CAPTURE_FAILHARD,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderWithTooltip(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", shift = Shift.AFTER, ordinal = 0))
    private void renderScreenPost(float tickDelta, long startTime, boolean tick, CallbackInfo ci, int mouseX, int mouseY, Window window, float guiFarPlane, Matrix4f matrix, MatrixStack matrices, MatrixStack matrices2) {
        renderScreenPost(tickDelta, startTime, tick, ci, mouseX, mouseY, window, matrix, matrices, matrices2);
    }
}