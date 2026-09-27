package me.modmuss50.optifabric.mod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.commons.Remapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.fabricmc.tinyremapper.IMappingProvider;
import net.fabricmc.tinyremapper.IMappingProvider.MappingAcceptor;
import net.fabricmc.tinyremapper.TinyUtils;

import me.modmuss50.optifabric.mod.ContextualMappingContext.Member;
import me.modmuss50.optifabric.mod.ContextualMappingProvider.ContextTransformer;

public class IntermediaryContextTransformer implements ContextTransformer {
	private class MappingRemapper extends Remapper implements MappingAcceptor {
		private void noteMember(Map<String, Map<Member, Member>> backward, IMappingProvider.Member member, String name, Map<String, Map<Member, Member>> forward) {
			Map<Member, Member> memberMap = backward.get(member.owner);
			if (memberMap == null) return; //Don't care about any members in this class

			Member key = new Member(member);
			boolean hasKey = memberMap.containsKey(key);
			Member naiveKey = new Member(member.owner, member.name);
			boolean hasNaiveKey = memberMap.containsKey(naiveKey);
			if (!hasKey && !hasNaiveKey) return; //Don't about this particular class member

			Member mappedKey = new Member(map(member.owner), name, mapDesc(member.desc));
			forward.computeIfAbsent(mappedKey.owner, k -> new HashMap<>(4)).put(mappedKey, key);
			if (hasKey) memberMap.put(key, mappedKey);
			if (hasNaiveKey) memberMap.put(naiveKey, mappedKey);
		}

		@Override
		public void acceptClass(String srcName, String dstName) {
			classes.put(dstName, srcName);
		}

		@Override
		public String map(String internalName) {
			return classes.inverse().getOrDefault(internalName, internalName);
		}

		@Override
		public void acceptMethod(IMappingProvider.Member method, String dstName) {
			if (!backwardMethods.isEmpty()) noteMember(backwardMethods, method, dstName, forwardMethods);
		}

		@Override
		public void acceptMethodArg(IMappingProvider.Member method, int lvIndex, String dstName) {
		}

		@Override
		public void acceptMethodVar(IMappingProvider.Member method, int lvIndex, int startOpIdx, int asmIndex, String dstName) {
		}

		@Override
		public void acceptField(IMappingProvider.Member field, String dstName) {
			if (!backwardFields.isEmpty()) noteMember(backwardFields, field, dstName, forwardFields);
		}
	}
	private final BiMap<String, String> classes = HashBiMap.create(8192);
	private final Map<String, Map<Member, Member>> backwardMethods = new HashMap<>();
	private final Map<String, Map<Member, Member>> backwardFields = new HashMap<>();
	private final Map<String, Map<Member, Member>> forwardMethods = new HashMap<>();
	private final Map<String, Map<Member, Member>> forwardFields = new HashMap<>();

	public IntermediaryContextTransformer(String to, List<ContextualMapping> mappings) {
		for (ContextualMapping mapping : mappings) {
			for (Member method : mapping.getNeededMethods()) {
				backwardMethods.computeIfAbsent(method.owner, k -> new HashMap<>(4)).put(method, null);
			}
			for (Member field : mapping.getNeededFields()) {
				backwardFields.computeIfAbsent(field.owner, k -> new HashMap<>(4)).put(field, null);
			}
		}

		try (BufferedReader in = new BufferedReader(new InputStreamReader(OptifineSetup.class.getResourceAsStream("/mappings/mappings.tiny"), StandardCharsets.UTF_8))) {
			TinyUtils.createTinyMappingProvider(in, "intermediary", to).load(new MappingRemapper());
		} catch (IOException e) {
			throw new RuntimeException("Failed to read Intermediary -> " + to + " mappings", e);
		}
	}

	@Override
	public String transformClass(String name) {
		return classes.getOrDefault(name, name);
	}

	private void remapMemberForwards(Map<String, Map<Member, Member>> map, IMappingProvider.Member member) {
		Map<Member, Member> memberMap = map.get(member.owner);
		if (memberMap == null) return;

		Member remap = memberMap.get(new Member(member));
		//if (remap == null) remap = memberMap.get(new Member(member.owner, member.name));
		if (remap == null) return;

		member.owner = remap.owner;
		member.name = remap.name;
		member.desc = remap.desc;
	}

	@Override
	public IMappingProvider.Member transformMethod(IMappingProvider.Member method) {
		if (!forwardMethods.isEmpty()) remapMemberForwards(forwardMethods, method);
		return method;
	}

	@Override
	public IMappingProvider.Member transformField(IMappingProvider.Member field) {
		if (!forwardFields.isEmpty()) remapMemberForwards(forwardFields, field);
		return field;
	}

	@Override
	public String untransformClass(String name) {
		return classes.inverse().getOrDefault(name, name);
	}

	private Member remapMemberBackwards(Map<String, Map<Member, Member>> map, Member member) {
		Map<Member, Member> memberMap = map.get(member.owner);
		if (memberMap == null) return member;

		return memberMap.getOrDefault(member, member);
	}

	@Override
	public Member untransformMethod(Member method) {
		return remapMemberBackwards(backwardMethods, method);
	}

	@Override
	public Member untransformField(Member field) {
		return remapMemberBackwards(backwardFields, field);
	}
}