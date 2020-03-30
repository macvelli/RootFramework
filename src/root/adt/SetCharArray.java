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

import java.util.Collection;
import java.util.NoSuchElementException;

import root.lang.Extractable;
import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.util.Root;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class SetCharArray implements RootSet<char[]>, Cloneable, Extractable {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	private final class Itr implements Itemizer<char[]> {

		private int i, j;
		private SetEntry<char[]> e;

		@Override
		public final int getIndex() {
			return this.j - 1;
		}

		@Override
		public final int getSize() {
			return SetCharArray.this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.j < SetCharArray.this.size;
		}

		@Override
		public final Itemizer<char[]> iterator() {
			return this;
		}

		@Override
		public final char[] next() {
			if (this.j == SetCharArray.this.size) {
				throw new NoSuchElementException();
			}

			this.j++;

			if (this.e != null) {
				this.e = this.e.next;
			}

			while (this.e == null) {
				this.e = SetCharArray.this.table[this.i++];
			}

			return this.e.key;
		}

		@Override
		public final void remove() {
			SetCharArray.this.remove(this.e.key);
		}

		@Override
		public final void reset() {
			this.i = 0;
			this.j = 0;
			this.e = null;
		}

	} // End Itr

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	int size;
	int capacity;
	SetEntry<char[]>[] table;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public SetCharArray() {
		this.capacity = 8;
		this.table = SetEntry.newArray(8);
	}

	public SetCharArray(final Collection<char[]> collection) {
		this.capacity = Root.calculateHashTableCapacity(collection.size());
		this.table = SetEntry.newArray(this.capacity);

		for (final char[] charArray : collection) {
			this.add(charArray);
		}
	}

	public SetCharArray(final int capacity) {
		this.capacity = Root.calculateHashTableCapacity(capacity);
		this.table = SetEntry.newArray(this.capacity);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final boolean add(final char[] charArray) {
		final int h = Root.hashCode(charArray);
		int i = h % this.table.length;

		for (SetEntry<char[]> n = this.table[i]; n != null; n = n.next) {
			if (Root.equals(n.key, charArray)) {
				return false;
			}
		}

		if (this.size++ == this.capacity) {
			i = this.resize(h);
		}

		this.table[i] = new SetEntry<char[]>(charArray, h, this.table[i]);

		return true;
	}

	@Override
	public final boolean addAll(final char[][] array, final int offset, final int length) {
		final int origSize = this.size;
		final int newSize = origSize + length;

		if (newSize > this.table.length) {
			this.resize(newSize + (newSize >> 1));
		}

		char[] charArray;
		int tableIndex, h;
		final int endLoop = offset + length;
		array: for (int i = offset; i < endLoop; i++) {
			charArray = array[i];
			h = Root.hashCode(charArray);
			tableIndex = h % this.table.length;

			for (SetEntry<char[]> n = this.table[tableIndex]; n != null; n = n.next) {
				if (Root.equals(n.key, charArray)) {
					continue array;
				}
			}

			this.table[tableIndex] = new SetEntry<char[]>(charArray, h, this.table[tableIndex]);
			this.size++;
		}

		return origSize != this.size;
	}

	@Override
	public final boolean addAll(final Collection<? extends char[]> collection) {
		final int origSize = this.size;

		for (final char[] charArray : collection) {
			this.add(charArray);
		}

		return origSize != this.size;
	}

	@Override
	public final boolean addAll(final Iterable<? extends char[]> iterable) {
		final int origSize = this.size;

		for (final char[] charArray : iterable) {
			this.add(charArray);
		}

		return origSize != this.size;
	}

	@Override
	public final void clear() {
		SetEntry<char[]> n, next;

		for (int i = 0, j = 0; j < this.size; i++) {
			for (n = this.table[i]; n != null; n = next) {
				next = n.next;
				n.next = null;
				j++;
			}
			this.table[i] = null;
		}

		this.size = 0;
	}

	@Override
	public final SetCharArray clone() {
		return new SetCharArray(this);
	}

	public final boolean contains(final char[] charArray) {
		for (SetEntry<char[]> n = this.table[Root.hashCode(charArray) % this.table.length]; n != null; n = n.next) {
			if (Root.equals(n.key, charArray)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public final boolean contains(final Object obj) {
		if (Root.equalToClass(obj, char[].class)) {
			final char[] charArray = (char[]) obj;

			for (SetEntry<char[]> n = this.table[Root.hashCode(charArray) % this.table.length]; n != null; n = n.next) {
				if (Root.equals(n.key, charArray)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public final boolean containsAll(final Collection<?> collection) {
		SetEntry<char[]> n;
		char[] charArray;

		items: for (final Object obj : collection) {
			if (Root.equalToClass(obj, char[].class)) {
				charArray = (char[]) obj;

				for (n = this.table[Root.hashCode(charArray) % this.table.length]; n != null; n = n.next) {
					if (Root.equals(n.key, charArray)) {
						continue items;
					}
				}
			}

			return false;
		}

		return true;
	}

	@Override
	public final boolean containsAll(final Iterable<? extends char[]> iterable) {
		SetEntry<char[]> n;

		items: for (final char[] charArray : iterable) {
			for (n = this.table[Root.hashCode(charArray) % this.table.length]; n != null; n = n.next) {
				if (Root.equals(n.key, charArray)) {
					continue items;
				}
			}

			return false;
		}

		return true;
	}

	public final boolean containsAll(final SetCharArray collection) {
		SetEntry<char[]> n;

		items: for (final char[] charArray : collection) {
			for (n = this.table[Root.hashCode(charArray) % this.table.length]; n != null; n = n.next) {
				if (Root.equals(n.key, charArray)) {
					continue items;
				}
			}

			return false;
		}

		return true;
	}

	@Override
	public final boolean containsAny(final Iterable<? extends char[]> iterable) {
		SetEntry<char[]> n;

		for (final char[] charArray : iterable) {
			for (n = this.table[Root.hashCode(charArray) % this.table.length]; n != null; n = n.next) {
				if (Root.equals(n.key, charArray)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public final SetCharArray difference(final Iterable<? extends char[]> iterable) {
		final SetCharArray diff = new SetCharArray(this.capacity);

		for (final char[] charArray : iterable) {
			if (!this.contains(charArray)) {
				diff.add(charArray);
			}
		}

		return diff;
	}

	@Override
	public final boolean equals(final Object param) {
		if (param != null && Root.equalToClass(param, SetCharArray.class)) {
			final SetCharArray set = (SetCharArray) param;

			return this.size == set.size && this.containsAll(set);
		}

		return false;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append('[');

		if (this.size > 0) {
			int i = 0;

			for (final SetEntry<char[]> setEntry : this.table) {
				for (SetEntry<char[]> e = setEntry; e != null; e = e.next) {
					if (i++ > 0) {
						extractor.addSeparator();
					}

					extractor.append(e.key);
				}
			}
		}

		extractor.append(']');
	}

	@Override
	public final char[] get(final char[] charArray) {
		final int i = Root.hashCode(charArray) % this.table.length;

		for (SetEntry<char[]> n = this.table[i]; n != null; n = n.next) {
			if (Root.equals(n.key, charArray)) {
				return n.key;
			}
		}

		return null;
	}

	@Override
	public final int getSize() {
		return this.size;
	}

	@Override
	public final int hashCode() {
		SetEntry<char[]> e;
		int h = this.size;

		for (int i = 0, j = 0; j < this.size; i++) {
			for (e = this.table[i]; e != null; e = e.next) {
				h <<= 1;
				h ^= e.hash;
				j++;
			}
		}

		return h;
	}

	@Override
	public final SetCharArray intersect(final Iterable<? extends char[]> iterable) {
		final SetCharArray intersect = new SetCharArray(this.size);

		for (final char[] charArray : iterable) {
			if (this.contains(charArray)) {
				intersect.add(charArray);
			}
		}

		return intersect;
	}

	@Override
	public final boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public final Itemizer<char[]> iterator() {
		return new Itr();
	}

	@Override
	public final boolean remove(final Object obj) {
		if (Root.equalToClass(obj, char[].class)) {
			final char[] charArray = (char[]) obj;

			final int i = Root.hashCode(charArray) % this.table.length;
			SetEntry<char[]> n, prev = null;

			for (n = this.table[i]; n != null; prev = n, n = n.next) {
				if (Root.equals(n.key, charArray)) {
					if (prev == null) {
						this.table[i] = n.next;
					} else {
						prev.next = n.next;
					}

					n.next = null;
					this.size--;
					return true;
				}
			}
		}

		return false;
	}

	/**
	 *
	 * @param c
	 * @return
	 */
	@Override
	public final boolean removeAll(final Collection<?> collection) {
		final int origSize = this.size;

		for (final Object o : collection) {
			this.remove(o);
		}

		return origSize != this.size;
	}

	@Override
	public final boolean replace(final char[] oldObj, final char[] newObj) {
		return this.remove(oldObj) && this.add(newObj);
	}

	@Override
	public final boolean retainAll(final Collection<?> collection) {
		final SetCharArray retainedSet = new SetCharArray(this.size);
		final int origSize = this.size;
		char[] charArray;

		for (final Object obj : collection) {
			if (Root.equalToClass(obj, char[].class)) {
				charArray = (char[]) obj;

				if (this.contains((char[]) obj)) {
					retainedSet.add(charArray);
				}
			}
		}

		this.size = retainedSet.size;
		this.capacity = retainedSet.capacity;
		this.table = retainedSet.table;

		return origSize != this.size;
	}

	@Override
	public final int size() {
		return this.size;
	}

	@Override
	public final char[][] toArray() {
		final char[][] array = new char[this.size][];
		SetEntry<char[]> n;

		for (int i = 0, j = 0; j < this.size; i++) {
			for (n = this.table[i]; n != null; n = n.next) {
				array[j++] = n.key;
			}
		}

		return array;
	}

	@Override
	public final <E> E[] toArray(final E[] arrayParam) {
		final E[] array = Root.newArray(arrayParam, this.size);
		SetEntry<char[]> n;

		for (int i = 0, j = 0; j < this.size; i++) {
			for (n = this.table[i]; n != null; n = n.next) {
				array[j++] = Root.cast(n.key);
			}
		}

		return array;
	}

	@Override
	public final ListArray<char[]> toList() {
		final ListArray<char[]> list = new ListArray<>(this.size);
		SetEntry<char[]> n;

		for (int i = 0, j = 0; j < this.size; i++) {
			for (n = this.table[i]; n != null; n = n.next) {
				list.values[j++] = n.key;
			}
		}

		return list;
	}

	@Override
	public String toString() {
		final StringExtractor extractor = new StringExtractor(this.size << 4);
		this.extract(extractor);
		return extractor.toString();
	}

	@Override
	public final SetCharArray union(final Iterable<? extends char[]> iterable) {
		final SetCharArray set = new SetCharArray(this.capacity);

		set.addAll(this);
		set.addAll(iterable);

		return set;
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private int resize(final int h) {
		final SetEntry<char[]>[] oldTable = this.table;
		this.capacity = (this.capacity << 1) - (this.capacity >> 2);
		this.table = SetEntry.newArray(this.capacity);

		SetEntry<char[]> n, next;
		for (int i = 0, j = 0; i < oldTable.length; i++) {
			for (n = oldTable[i]; n != null; n = next) {
				next = n.next;
				j = n.hash % this.table.length;
				n.next = this.table[j];
				this.table[j] = n;
			}
			oldTable[i] = null;
		}

		return h % this.table.length;
	}

} // End SetCharArray
