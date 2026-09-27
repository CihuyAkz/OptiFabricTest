package me.modmuss50.optifabric.patcher.fixes;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import net.fabricmc.tinyremapper.IMappingProvider.Member;

import me.modmuss50.optifabric.util.RemappingUtils;

public class ParticleManagerFix implements ClassFixer {
	@Override
	public void fix(ClassNode optifine, ClassNode minecraft) {
		String factories = RemappingUtils.mapFieldName("class_702", "field_3835", "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;");

		for (FieldNode field : optifine.fields) {
			if (factories.equals(field.name)) {//Fix the field type from OptiFine changing it to a map
				field.desc = "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;";
				break;
			}
		}

		Member[] methods = {
				RemappingUtils.mapMethod("class_702", "<init>", "(Lnet/minecraft/class_638;Lnet/minecraft/class_1060;)V"),
				RemappingUtils.mapMethod("class_702", "method_3055", "(Lnet/minecraft/class_2394;DDDDDD)Lnet/minecraft/class_703;"), //createParticle
				RemappingUtils.mapMethod("class_702", "method_3043", "(Lnet/minecraft/class_2396;Lnet/minecraft/class_707;)V"), //registerFactory
				RemappingUtils.mapMethod("class_702", "method_18834", "(Lnet/minecraft/class_2396;Lnet/minecraft/class_702$class_4091;)V") //registerFactory too
		};

		for (ListIterator<MethodNode> it = optifine.methods.listIterator(); it.hasNext();) {
			MethodNode method = it.next();

			for (Member undo : methods) {
				if (undo.name.equals(method.name) && undo.desc.equals(method.desc)) {
					it.set(find(minecraft.methods, undo));
					break;
				}
			}
		}
	}

	private static MethodNode find(List<MethodNode> methods, Member target) {
		for (MethodNode method : methods) {
			if (target.name.equals(method.name) && target.desc.equals(method.desc)) {
				return method;
			}
		}

		throw new NoSuchElementException("Cannot find " + target + " in given methods");
	}
}