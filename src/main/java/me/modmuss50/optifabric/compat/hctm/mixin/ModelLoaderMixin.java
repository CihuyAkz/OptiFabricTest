package me.modmuss50.optifabric.compat.hctm.mixin;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelVariantMap;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import me.modmuss50.optifabric.compat.InterceptingMixin;
import me.modmuss50.optifabric.compat.LoudCoerce;
import me.modmuss50.optifabric.compat.PlacatingSurrogate;
import me.modmuss50.optifabric.compat.Shim;

@Mixin(ModelLoader.class)
@InterceptingMixin("net/dblsaiko/hctm/mixin/ModelLoaderMixin")
abstract class ModelLoaderMixin {
	@PlacatingSurrogate
	private void loadModel(Identifier id, CallbackInfo call, ModelIdentifier castID, Identifier idAgain, StateManager<Block, BlockState> stateManager,
							List<Property<?>> colourProperties, ImmutableList<BlockState> states, Map<ModelIdentifier, BlockState> modelIDToState,
							Map<BlockState, Pair<UnbakedModel, Supplier<?>>> stateToModelMaker, Identifier blockstateDefinition, UnbakedModel missingModel,
							@LoudCoerce("class_1088$class_4455") Object missingModelDefinition, Pair<UnbakedModel, Supplier<?>> missingModelMaker,
							List<Pair<String, ModelVariantMap>> definedBlockstates, Iterator<Pair<String, ModelVariantMap>> it, Pair<String, ModelVariantMap> definedBlockstate,
							ModelVariantMap variantMap) {//This is a long method definition
	}

	@Inject(method = "loadModel(Lnet/minecraft/util/Identifier;)V",
			at = @At(value = "INVOKE", target = "Ljava/util/Map;putAll(Ljava/util/Map;)V", remap = false),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void loadModel(Identifier id, CallbackInfo call, ModelIdentifier castID, Identifier idAgain, StateManager<Block, BlockState> stateManager, List<Property<?>> colourProperties,
							ImmutableList<BlockState> states, Map<ModelIdentifier, BlockState> modelIDToState, Map<BlockState, Pair<UnbakedModel, Supplier<?>>> stateToModelMaker,
							Identifier blockstateDefinition, UnbakedModel missingModel, @Coerce Object missingModelDefinition, Pair<UnbakedModel, Supplier<?>> missingModelMaker,
							List<Pair<String, ModelVariantMap>> definedBlockstates, Iterator<Pair<String, ModelVariantMap>> it, Pair<String, ModelVariantMap> definedBlockstate,
							ModelVariantMap variantMap, Map<BlockState, Pair<UnbakedModel, Supplier<?>>> definedStateToModelMakers) {
		loadModel(id, call, idAgain, stateManager, colourProperties, states, modelIDToState, stateToModelMaker, blockstateDefinition, missingModel, missingModelDefinition,
					missingModelMaker, definedBlockstates, it, definedBlockstate, variantMap, definedStateToModelMakers);
	}

	@Shim
	private native void loadModel(Identifier id, CallbackInfo call, Identifier idAgain, StateManager<Block, BlockState> stateManager, List<Property<?>> colourProperties,
									ImmutableList<BlockState> states, Map<ModelIdentifier, BlockState> modelIDToState, Map<BlockState, Pair<UnbakedModel, Supplier<?>>> stateToModelMaker,
									Identifier blockstateDefinition, UnbakedModel missingModel, @Coerce Object missingModelDefinition, Pair<UnbakedModel, Supplier<?>> missingModelMaker,
									List<Pair<String, ModelVariantMap>> definedBlockstates, Iterator<Pair<String, ModelVariantMap>> it, Pair<String, ModelVariantMap> definedBlockstate,
									ModelVariantMap variantMap, Map<BlockState, Pair<UnbakedModel, Supplier<?>>> definedStateToModelMakers);
}