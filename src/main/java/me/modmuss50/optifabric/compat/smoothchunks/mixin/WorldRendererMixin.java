package me.modmuss50.optifabric.compat.smoothchunks.mixin;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.LoudCoerce;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(WorldRenderer.class)
@InterceptingMixin("cc/flogi/dev/smoothchunks/mixin/WorldRendererMixin")
abstract class WorldRendererMixin {
	@PlacatingSurrogate
	private void renderLayerInject(RenderLayer layer, MatrixStack matrices, double x, double y, double z, CallbackInfo call, boolean isShaders, boolean smartAnimations,
			boolean isTranslucent, ObjectListIterator<?> chunks, @LoudCoerce("class_761$class_762") Object chunkInfo) {
	}

	@Inject(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/VertexBuffer;bind()V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void renderLayerInject(RenderLayer layer, MatrixStack matrices, double x, double y, double z, CallbackInfo call, boolean isShaders, boolean smartAnimations,
			boolean isTranslucent, ObjectListIterator<?> chunks, @Coerce Object chunkInfo, BuiltChunk chunk, VertexBuffer buffer) {
		renderLayerInject(layer, new MatrixStack() {
			@Override
			public void push() {
				GlStateManager.pushMatrix();
			}

			@Override
			public Entry peek() {
				throw new UnsupportedOperationException("Matrices are a lie");
			}

			@Override
			public void translate(double x, double y, double z) {
				GlStateManager.translated(x, y, z);
			}

			@Override
			public void scale(float x, float y, float z) {
				GlStateManager.scalef(x, y, z);
			}

			@Override
			public void multiply(Quaternion quaternion) {
				throw new UnsupportedOperationException("TODO");
			}

			@Override
			public boolean isEmpty() {
				return false; //The GL stack is probably not
			}

			@Override
			public void pop() {
				GlStateManager.popMatrix();
			}

			@Override
			public String toString() {
				return "GLMatrixStack"; //OptiFine adds this and using peek, let's be safe
			}
		}, x, y, z, call, isTranslucent, chunks, chunkInfo, chunk, buffer);
	}

	@Shim
	private native void renderLayerInject(RenderLayer layer, MatrixStack matrices, double x, double y, double z, CallbackInfo call, boolean isTranslucent, 
			ObjectListIterator<?> chunks, @LoudCoerce("class_761$class_762") Object chunkInfo, BuiltChunk chunk, VertexBuffer buffer);
}