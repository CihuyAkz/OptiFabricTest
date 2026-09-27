package me.modmuss50.optifabric.mod;

import java.util.Objects;

import net.fabricmc.tinyremapper.IMappingProvider;

public interface ContextualMappingContext {
	final class Member {
		public final String owner, name, desc;

		public Member(String owner, String name) {
			this.owner = Objects.requireNonNull(owner);
			this.name = Objects.requireNonNull(name);
			this.desc = null;
		}

		public Member(IMappingProvider.Member member) {
			this(member.owner, member.name, member.desc);
		}

		public Member(String owner, String name, String desc) {
			this.owner = Objects.requireNonNull(owner);
			this.name = Objects.requireNonNull(name);
			this.desc = Objects.requireNonNull(desc);
		}

		public boolean isNaive() {
			return desc == null;
		}

		public IMappingProvider.Member toTR() {
			return new IMappingProvider.Member(owner, name, desc);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + owner.hashCode();
			result = prime * result + name.hashCode();
			result = prime * result + Objects.hashCode(desc);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || getClass() != obj.getClass()) return false;
			Member that = (Member) obj;
			if (!owner.equals(that.owner)) return false;
			if (!name.equals(that.name)) return false;
			return isNaive() ? that.isNaive() : desc.equals(that.desc);
		}
	}

	String unmapClass(String name);

	String mapClass(String name);

	Member unmapMethod(Member method);

	String mapMethod(Member method);

	Member unmapField(Member field);

	String mapField(Member field);
}