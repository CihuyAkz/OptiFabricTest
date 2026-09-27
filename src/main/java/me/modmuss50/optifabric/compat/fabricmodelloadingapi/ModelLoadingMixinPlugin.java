package me.modmuss50.optifabric.compat.fabricmodelloadingapi;

import me.modmuss50.optifabric.compat.InterceptingMixinPlugin;
import me.modmuss50.optifabric.util.MixinUtils;
import me.modmuss50.optifabric.util.RemappingUtils;
import net.fabricmc.tinyremapper.IMappingProvider.Member;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

public class ModelLoadingMixinPlugin extends InterceptingMixinPlugin {
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if ("ModelLoaderBakerImplMixin".equals(mixinInfo.getName())) {
			// not doing this will cause Mixin to have a stroke while trying to find where to put the fabric injectors
			ClassInfo info = ClassInfo.forName(targetClassName);
			MixinUtils.completeClassInfo(info, targetClass.methods);
			String bakeDesc = "(Lnet/minecraft/class_2960;Lnet/minecraft/class_3665;)Lnet/minecraft/class_1087;";//(Identifier, ModelBakeSettings)BakedModel
			String bake = RemappingUtils.getMethodName("class_7775", "method_45873", bakeDesc);//BakerImpl, bake
			bakeDesc = RemappingUtils.mapMethodDescriptor(bakeDesc);

			for (MethodNode method : targetClass.methods) {
				if (bake.equals(method.name) && bakeDesc.equals(method.desc)) {
					//This is the @ModifyVariable
					//target = "Lnet/minecraft/client/render/model/ModelLoader$BakerImpl;getOrLoadModel(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/model/UnbakedModel;"
					Member mfMixinTarget = RemappingUtils.mapMethod("class_1088$class_7778", "method_45872", "(Lnet/minecraft/class_2960;)Lnet/minecraft/class_1100;");
					LabelNode fakeStart = new LabelNode();
					LabelNode skip = new LabelNode();
					InsnList extraFirst = new InsnList();

					extraFirst.add(new JumpInsnNode(Opcodes.GOTO, skip));
					extraFirst.add(fakeStart);
					extraFirst.add(new InsnNode(Opcodes.ACONST_NULL));
					extraFirst.add(new InsnNode(Opcodes.ACONST_NULL));
					extraFirst.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, mfMixinTarget.owner, mfMixinTarget.name, mfMixinTarget.desc));
					extraFirst.add(new VarInsnNode(Opcodes.ASTORE, 3));
					extraFirst.add(new InsnNode(Opcodes.NOP)); //Don't make the very next instruction after the store the end label for the local variable
					extraFirst.add(skip);

					method.localVariables.add(new LocalVariableNode("fakeUnbakedModel", 'L' + RemappingUtils.getClassName("class_1100") + ';', null, fakeStart, skip, 3));
					method.instructions.insertBefore(method.instructions.getFirst(), extraFirst);
					method.maxLocals += 1;

					//This part takes care of the two @Redirect
					//target = "Lnet/minecraft/client/render/model/UnbakedModel;bake(Lnet/minecraft/client/render/model/Baker;Ljava/util/function/Function;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/model/BakedModel;"
					Member redirectMixinTarget1 = RemappingUtils.mapMethod("class_1100", "method_4753", "(Lnet/minecraft/class_7775;Ljava/util/function/Function;Lnet/minecraft/class_3665;Lnet/minecraft/class_2960;)Lnet/minecraft/class_1087;");
					//target = "Lnet/minecraft/client/render/model/json/JsonUnbakedModel;bake(Lnet/minecraft/client/render/model/Baker;Lnet/minecraft/client/render/model/json/JsonUnbakedModel;Ljava/util/function/Function;Lnet/minecraft/client/render/model/ModelBakeSettings;Lnet/minecraft/util/Identifier;Z)Lnet/minecraft/client/render/model/BakedModel;"
					Member redirectMixinTarget2 = RemappingUtils.mapMethod("class_793", "method_3446", "(Lnet/minecraft/class_7775;Lnet/minecraft/class_793;Ljava/util/function/Function;Lnet/minecraft/class_3665;Lnet/minecraft/class_2960;Z)Lnet/minecraft/class_1087;");
					LabelNode skipLast = new LabelNode();
					InsnList extraLast = new InsnList();
					extraLast.add(new JumpInsnNode(Opcodes.GOTO, skipLast));
					extraLast.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, redirectMixinTarget1.owner, redirectMixinTarget1.name, redirectMixinTarget1.desc));
					extraLast.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, redirectMixinTarget2.owner, redirectMixinTarget2.name, redirectMixinTarget2.desc));
					extraLast.add(skipLast);
					method.instructions.insertBefore(method.instructions.getLast(), extraLast);
					break;
				}
			}
		}

		super.preApply(targetClassName, targetClass, mixinClassName, mixinInfo);
	}
}
