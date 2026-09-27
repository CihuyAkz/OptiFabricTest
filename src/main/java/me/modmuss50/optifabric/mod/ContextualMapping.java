package me.modmuss50.optifabric.mod;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import net.fabricmc.tinyremapper.IMappingProvider;
import net.fabricmc.tinyremapper.IMappingProvider.MappingAcceptor;

public class ContextualMapping implements ContextualMappingContext {
	public static class Builder {
		private final BiConsumer<MappingAcceptor, ContextualMappingContext> factory;
		private final Set<String> classes = new HashSet<>();
		private final Set<Member> methods = new HashSet<>();
		private final Set<Member> fields = new HashSet<>();

		private Builder(BiConsumer<MappingAcceptor, ContextualMappingContext> factory) {
			this.factory = factory;
		}

		public Builder usingClass(String name) {
			classes.add(name);
			return this;
		}

		public Builder usingClasses(String... names) {
			Collections.addAll(classes, names);
			return this;
		}

		public Builder usingMethod(Member method) {
			methods.add(method);
			return this;
		}

		public Builder usingMethods(Member... methods) {
			Collections.addAll(this.methods, methods);
			return this;
		}

		public Builder usingField(Member field) {
			fields.add(field);
			return this;
		}

		public Builder usingFields(Member... fields) {
			Collections.addAll(this.fields, fields);
			return this;
		}

		public ContextualMapping build(ContextualMappingProvider provider) {
			if (classes.isEmpty() && methods.isEmpty() && fields.isEmpty()) throw new IllegalStateException("Tried to make contextual mapping without context");
			return new ContextualMapping(provider, factory, classes, methods, fields);
		}
	}

	public static Builder forMethod(BiConsumer<MappingAcceptor, ContextualMappingContext> factory) {
		return new Builder(factory);
	}

	public static Builder forField(BiConsumer<MappingAcceptor, ContextualMappingContext> factory) {
		return new Builder(factory);
	}

	private final ContextualMappingProvider provider;
	private final BiConsumer<MappingAcceptor, ContextualMappingContext> factory;
	private final Map<String, String> classes;
	private final Map<Member, String> methods;
	private final Map<Member, String> fields;
	private int contextNeeded;

	private static <T> Map<T, String> convert(Set<T> list) {
		if (list.isEmpty()) return Collections.emptyMap();
		Map<T, String> out = new Object2ObjectArrayMap<>(list.size());
		for (T object : list) {
			out.put(object, null);
		}
		return out;
	}

	private ContextualMapping(ContextualMappingProvider provider, BiConsumer<MappingAcceptor, ContextualMappingContext> factory, Set<String> classes, Set<Member> methods, Set<Member> fields) {
		this.provider = provider;
		this.factory = factory;
		this.classes = convert(classes);
		this.methods = convert(methods);
		this.fields = convert(fields);
		contextNeeded = classes.size() + methods.size() + fields.size();
	}

	public Set<String> getNeededClasses() {
		return Collections.unmodifiableSet(classes.keySet());
	}

	public Set<Member> getNeededMethods() {
		return Collections.unmodifiableSet(methods.keySet());
	}

	public Set<Member> getNeededFields() {
		return Collections.unmodifiableSet(fields.keySet());
	}

	private <T> void addMapping(Map<T, String> type, T from, String to) {
		if (type.containsKey(from)) {
			type.put(from, to);
			if (--contextNeeded < 1) this.factory.accept(provider, this);
		}
	}

	void addClassMapping(String from, String to) {
		if (contextNeeded >= 1 && !classes.isEmpty()) addMapping(classes, from, to);
	}

	@Override
	public String unmapClass(String name) {
		return provider.getContextTransformer().untransformClass(name);
	}

	@Override
	public String mapClass(String name) {
		return classes.get(name);
	}

	void addMethodMapping(IMappingProvider.Member member, String name) {
		if (contextNeeded < 1 || methods.isEmpty()) return;
		addMapping(methods, new Member(member), name);
		addMapping(methods, new Member(member.owner, member.name), name);
	}

	@Override
	public Member unmapMethod(Member method) {
		return provider.getContextTransformer().untransformMethod(method);
	}

	@Override
	public String mapMethod(Member method) {
		return methods.get(method);
	}

	void addFieldMapping(IMappingProvider.Member member, String name) {
		if (contextNeeded < 1 || fields.isEmpty()) return;
		addMapping(fields, new Member(member), name);
		addMapping(fields, new Member(member.owner, member.name), name);
	}

	@Override
	public Member unmapField(Member field) {
		return provider.getContextTransformer().untransformField(field);
	}

	@Override
	public String mapField(Member field) {
		return fields.get(field);
	}
}