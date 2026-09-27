package me.modmuss50.optifabric.compat.fabriclifecycleevents;

public class LifecycleNewMixinPlugin extends LifecycleMixinPlugin {
	@Override
	protected String getLoadChunkFromPacketDesc() {
		return "(IILnet/minecraft/class_2540;Lnet/minecraft/class_2487;Ljava/util/function/Consumer;)Lnet/minecraft/class_2818;";
	}
}