package me.modmuss50.optifabric.compat.uglyscoreboardfix;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import me.modmuss50.optifabric.patcher.fixes.ClassFixer;
import me.modmuss50.optifabric.util.RemappingUtils;

public class InGameHudFix implements ClassFixer {
	//Recompiling causes OptiFine to move some (but not all) of the local variables around
	//It's not actually changed other than that though so we can just put the old method back

	@Override
	public void fix(ClassNode optifine, ClassNode minecraft) {
		String renderScoreboardSidebarDesc = "(Lnet/minecraft/class_4587;Lnet/minecraft/class_266;)V"; //(MatrixStack, ScoreboardObjective)
		String renderScoreboardSidebar = RemappingUtils.getMethodName("class_329", "method_1757", renderScoreboardSidebarDesc);
		renderScoreboardSidebarDesc = RemappingUtils.mapMethodDescriptor(renderScoreboardSidebarDesc); // ^ InGameHud, renderScoreboardSidebar

		for (ListIterator<MethodNode> it = optifine.methods.listIterator(); it.hasNext();) {
			MethodNode method = it.next();

			if (renderScoreboardSidebar.equals(method.name) && renderScoreboardSidebarDesc.equals(method.desc)) {
				for (MethodNode original : minecraft.methods) {
					if (renderScoreboardSidebar.equals(original.name) && renderScoreboardSidebarDesc.equals(original.desc)) {
						it.set(original);
						break;
					}
				}

				break;
			}			
		}
	}
}