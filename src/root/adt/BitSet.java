/*
 * Copyright 2006-2016 Edward Smith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package root.adt;

import root.lang.Constants;
import root.lang.Extractable;
import root.lang.StringExtractor;
import root.util.Root;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class BitSet implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int flags;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public BitSet() {
	}

	public BitSet(final int flags) {
		this.flags = flags;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Clears the {@link BitSet} flags.
	 */
	public final void clear() {
		this.flags = 0;
	}

	/**
	 * Clears the {@link BitSet} flag at the given <code>index</code>.
	 *
	 * @param index
	 *            the <code>index</code> of the flag to clear
	 */
	public final void clear(final int index) {
		this.flags &= ~(1 << index);
	}

	/**
	 * Returns <code>true</code> if this {@link BitSet} has all bits set in the <code>bitSet</code> parameter.
	 *
	 * @param bitSet
	 *            the {@link BitSet} to check if it is contained within this {@link BitSet}
	 * @return <code>true</code> if this {@link BitSet} has all bits set in the <code>bitSet</code> parameter, <code>false</code> otherwise
	 */
	public final boolean contains(final BitSet bitSet) {
		return (this.flags & bitSet.flags) == bitSet.flags;
	}

	/**
	 * Returns <code>true</code> if this {@link BitSet} has all bits set in the <code>flagsParam</code> parameter.
	 *
	 * @param flagsParam
	 *            the set of flags to check for in this {@link BitSet}
	 * @return <code>true</code> if this {@link BitSet} has all bits set in the <code>flagsParam</code> parameter, <code>false</code> otherwise
	 */
	public final boolean contains(final int flagsParam) {
		return (this.flags & flagsParam) == flagsParam;
	}

	/**
	 * Returns <code>true</code> if this {@link BitSet} has any bits set in the <code>bitSet</code> parameter
	 *
	 * @param bitSet
	 *            the {@link BitSet} to check if any of its flags are contained within this {@link BitSet}
	 * @return <code>true</code> if this {@link BitSet} has any bits set in the <code>bitSet</code> parameter, <code>false</code> otherwise
	 */
	public final boolean containsAny(final BitSet bitSet) {
		return (this.flags & bitSet.flags) != 0;
	}

	/**
	 * Returns <code>true</code> if this {@link BitSet} has any bits set in the <code>flagsParam</code> parameter
	 *
	 * @param flagsParam
	 *            the set of flags to check for in this {@link BitSet}
	 * @return <code>true</code> if this {@link BitSet} has any bits set in the <code>flagsParam</code> parameter, <code>false</code> otherwise
	 */
	public final boolean containsAny(final int flagsParam) {
		return (this.flags & flagsParam) != 0;
	}

	/**
	 * Returns <code>true</code> if this {@link BitSet} is identical to the <code>flagsParam</code> parameter
	 *
	 * @param flagsParam
	 *            the set of flags to check for equality in this {@link BitSet}
	 * @return <code>true</code> if this {@link BitSet} is identical to the <code>flagsParam</code> parameter, <code>false</code> otherwise
	 */
	public final boolean equals(final int flagsParam) {
		return this.flags == flagsParam;
	}

	/**
	 * Returns <code>true</code> if this {@link BitSet} is identical to the <code>obj</code> parameter
	 *
	 * @param obj
	 *            the {@link Object} to check for equality in this {@link BitSet}
	 * @return <code>true</code> if this {@link BitSet} is identical to the <code>obj</code> parameter, <code>false</code> otherwise
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (Root.equalToClass(obj, BitSet.class)) {
			return ((BitSet) obj).flags == this.flags;
		}

		return false;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		final char[] c = new char[] { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
				'0', '0', '0', '0', '0', '0', '0', '0', '0', '0' };
		int f = this.flags, i = 32;

		while (f > 0) {
			c[--i] = Constants.digits[f & 1];
			f >>>= 1;
		}

		extractor.append(c);
	}

	/**
	 * Returns <code>true</code> if the flag at <code>index</code> is set
	 *
	 * @param index
	 *            the flag to check whether it is set
	 * @return <code>true</code> if the flag at <code>index</code> is set
	 */
	public final boolean get(final int index) {
		return (this.flags & 1 << index) != 0;
	}

	/**
	 * Returns the internal flags representation of this {@link BitSet}.
	 *
	 * @return the internal flags representation of this {@link BitSet}.
	 */
	public final int getFlags() {
		return this.flags;
	}

	/**
	 * Returns the hash code of this {@link BitSet}
	 *
	 * @return the hash code of this {@link BitSet}
	 */
	@Override
	public final int hashCode() {
		return this.flags;
	}

	/**
	 * Returns a new {@link BitSet} with the intersection between this {@link BitSet} and the <code>bitSet</code> parameter.
	 *
	 * @param bitSet
	 *            the {@link BitSet} to perform the intersection with
	 * @return a new {@link BitSet} with the intersection between this {@link BitSet} and the <code>bitSet</code> parameter
	 */
	public final BitSet intersect(final BitSet bitSet) {
		return new BitSet(this.flags & bitSet.flags);
	}

	/**
	 * Returns <code>true</code> if this {@link BitSet} has no flags set.
	 *
	 * @return <code>true</code> if this {@link BitSet} has no flags set
	 */
	public final boolean isEmpty() {
		return this.flags == 0;
	}

	/**
	 * Sets the flag at the specified <code>index</code> to <code>true</code>.
	 *
	 * @param index
	 *            the index to set the flag at to <code>true</code>
	 */
	public final void set(final int index) {
		this.flags |= 1 << index;
	}

	/**
	 * Sets the flag at the specified <code>index</code> to <code>1</code> if <code>bool</code> is <code>true</code>, otherwise to <code>0</code> if
	 * <code>bool</code> is <code>false</code>.
	 *
	 * @param index
	 *            the index to set the flag at
	 * @param bool
	 *            determines whether to either set <code>0</code> or <code>1</code>
	 */
	public final void set(final int index, final boolean bool) {
		if (bool) {
			this.flags |= 1 << index;
		} else {
			this.flags &= ~(1 << index);
		}
	}

	/**
	 * Returns a {@link String} representation of this {@link BitSet}.
	 *
	 * @return a {@link String} representation of this {@link BitSet}
	 */
	@Override
	public final String toString() {
		final StringExtractor chars = new StringExtractor(32);
		this.extract(chars);
		return chars.toString();
	}

	/**
	 * Returns a new {@link BitSet} with the union between this {@link BitSet} and the <code>bitSet</code> parameter.
	 *
	 * @param bitSet
	 *            the {@link BitSet} to perform the union with
	 * @return a new {@link BitSet} with the union between this {@link BitSet} and the <code>bitSet</code> parameter
	 */
	public final BitSet union(final BitSet b) {
		return new BitSet(this.flags | b.flags);
	}

} // End BitSet
