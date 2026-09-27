package me.modmuss50.optifabric.mod;

import javax.lang.model.SourceVersion;

import net.fabricmc.loader.api.FabricLoader;

import net.fabricmc.tinyremapper.ClassInstance;
import net.fabricmc.tinyremapper.InputTag;
import net.fabricmc.tinyremapper.MemberInstance;
import net.fabricmc.tinyremapper.api.TrMember.MemberType;

public class Preloader {
	public static void preloadTinyRemapper() {
		ClassInstance.class.getClass();
		InputTag.class.getClass();
		MemberInstance.class.getClass();
		MemberType.class.getClass();
		SourceVersion.class.getClass(); //Classes in javax don't get the special treatment java ones do

		ClassLoader classLoader = Preloader.class.getClassLoader();
		for (String type : new String[] {
				"net.fabricmc.tinyremapper.AsmClassRemapper$AsmAnnotationRemapper",
				"net.fabricmc.tinyremapper.AsmClassRemapper$AsmAnnotationRemapper$AsmArrayAttributeAnnotationRemapper",
				"net.fabricmc.tinyremapper.AsmClassRemapper$AsmRecordComponentRemapper",
				"net.fabricmc.tinyremapper.AsmClassRemapper$AsmFieldRemapper",
				"net.fabricmc.tinyremapper.AsmClassRemapper$AsmMethodRemapper",
				"net.fabricmc.tinyremapper.Propagator",
				"net.fabricmc.tinyremapper.TinyRemapper$2",
				"net.fabricmc.tinyremapper.TinyRemapper$Direction",
				"net.fabricmc.tinyremapper.VisitTrackingClassRemapper$VisitKind",
		}) {
			try {
				Class.forName(type, false, classLoader);
			} catch (ClassNotFoundException e) {
				System.err.println("Failed to preload " + type);
				if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
					e.printStackTrace();
				}
			}
		}
	}
}