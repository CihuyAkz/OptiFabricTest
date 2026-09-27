package me.modmuss50.optifabric.mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import net.fabricmc.tinyremapper.IMappingProvider.MappingAcceptor;
import net.fabricmc.tinyremapper.IMappingProvider.Member;

import me.modmuss50.optifabric.mod.ContextualMapping.Builder;

public class ContextualMappingProvider implements MappingAcceptor {
	public interface ContextTransformer {
		String transformClass(String name);

		String untransformClass(String name);

		Member transformMethod(Member method);

		ContextualMappingContext.Member untransformMethod(ContextualMappingContext.Member method);

		Member transformField(Member field);

		ContextualMappingContext.Member untransformField(ContextualMappingContext.Member field);
	}
	private final MappingAcceptor parent;
	private final List<ContextualMapping> extraMappings = new ArrayList<>();
	private ContextTransformer transformer = new ContextTransformer() {
		@Override
		public String transformClass(String name) {
			return name;
		}

		@Override
		public String untransformClass(String name) {
			return name;
		}

		@Override
		public Member transformMethod(Member method) {
			return method;
		}

		@Override
		public ContextualMappingContext.Member untransformMethod(ContextualMappingContext.Member method) {
			return method;
		}

		@Override
		public Member transformField(Member field) {
			return field;
		}

		@Override
		public ContextualMappingContext.Member untransformField(ContextualMappingContext.Member field) {
			return field;
		}
	};

	public ContextualMappingProvider(MappingAcceptor parent) {
		this.parent = parent;
	}

	public ContextualMappingProvider add(Builder mapping) {
		extraMappings.add(mapping.build(this));
		return this;
	}

	public void setContextTransformer(Function<List<ContextualMapping>, ContextTransformer> factory) {
		transformer = Objects.requireNonNull(factory.apply(Collections.unmodifiableList(extraMappings)), "Transformer must not be null");
	}

	public ContextTransformer getContextTransformer() {
		return transformer;
	}

	@Override
	public void acceptClass(String srcName, String dstName) {
		parent.acceptClass(srcName, dstName);
		srcName = transformer.transformClass(srcName);
		for (ContextualMapping mapping : extraMappings) mapping.addClassMapping(srcName, dstName);
	}

	@Override
	public void acceptMethod(Member method, String dstName) {
		parent.acceptMethod(method, dstName);
		method = transformer.transformMethod(method);
		for (ContextualMapping mapping : extraMappings) mapping.addMethodMapping(method, dstName);
	}

	@Override
	public void acceptMethodArg(Member method, int lvIndex, String dstName) {
		parent.acceptMethodArg(method, lvIndex, dstName);
	}

	@Override
	public void acceptMethodVar(Member method, int lvIndex, int startOpIdx, int asmIndex, String dstName) {
		parent.acceptMethodVar(method, lvIndex, startOpIdx, asmIndex, dstName);
	}

	@Override
	public void acceptField(Member field, String dstName) {
		parent.acceptField(field, dstName);
		field = transformer.transformField(field);
		for (ContextualMapping mapping : extraMappings) mapping.addFieldMapping(field, dstName);
	}
}