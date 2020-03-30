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

import root.annotation.Todo;
import root.lang.Extractable;
import root.lang.Immutable;
import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.util.Root;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the set
 */
@Todo("Look at http://en.wikipedia.org/wiki/Set_(abstract_data_type) for any ideas")
public final class SetHashed<T> implements RootSet<T>, Cloneable, Extractable, Immutable {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	private final class Itr implements Itemizer<T> {

		private int i, j;
		private SetEntry<T> e;

		@Override
		public final int getIndex() {
			return this.j - 1;
		}

		@Override
		public final int getSize() {
			return SetHashed.this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.j < SetHashed.this.size;
		}

		@Override
		public final Itemizer<T> iterator() {
			return this;
		}

		@Override
		public final T next() {
			if (this.j == SetHashed.this.size) {
				throw new NoSuchElementException();
			}

			this.j++;

			if (this.e != null) {
				this.e = this.e.next;
			}

			while (this.e == null) {
				this.e = SetHashed.this.table[this.i++];
			}

			return this.e.key;
		}

		@Override
		public final void remove() {
			SetHashed.this.remove(this.e.key);
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
	SetEntry<T>[] table;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public SetHashed() {
		this.capacity = 8;
		this.table = SetEntry.newArray(8);
	}

	public SetHashed(final Collection<? extends T> collection) {
		this.capacity = Root.calculateHashTableCapacity(collection.size());
		this.table = SetEntry.newArray(this.capacity);

		for (final T t : collection) {
			this.add(t);
		}
	}

	public SetHashed(final int capacity) {
		this.capacity = Root.calculateHashTableCapacity(capacity);
		this.table = SetEntry.newArray(this.capacity);
	}

	/**
	 * A constructor that takes an array and uses it to initialize the set upon creation.
	 *
	 * @param array
	 *            the array to initialize the set with upon creation
	 */
	@SafeVarargs
	public SetHashed(final T... array) {
		this.capacity = Root.calculateHashTableCapacity(array.length);
		this.table = SetEntry.newArray(this.capacity);

		for (final T t : array) {
			this.add(t);
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final boolean add(final T t) {
		final int h = Root.hashCode(t);
		int i = h % this.table.length;

		for (SetEntry<T> n = this.table[i]; n != null; n = n.next) {
			if (Root.equals(n.key, t)) {
				return false;
			}
		}

		if (this.size++ == this.capacity) {
			i = this.resize(h);
		}

		this.table[i] = new SetEntry<T>(t, h, this.table[i]);

		return true;
	}

	@Override
	public final boolean addAll(final Collection<? extends T> collection) {
		final int origSize = this.size;

		for (final T t : collection) {
			this.add(t);
		}

		return origSize != this.size;
	}

	@Override
	public final boolean addAll(final Iterable<? extends T> iterable) {
		final int origSize = this.size;

		for (final T t : iterable) {
			this.add(t);
		}

		return origSize != this.size;
	}

	@Override
	public final boolean addAll(final T[] array, final int offset, final int length) {
		final int origSize = this.size;
		final int newSize = origSize + length;

		if (newSize > this.table.length) {
			this.resize(newSize + (newSize >> 1));
		}

		T t;
		int tableIndex, h;
		final int endLoop = offset + length;
		array: for (int i = offset; i < endLoop; i++) {
			t = array[i];
			h = Root.hashCode(t);
			tableIndex = h % this.table.length;

			for (SetEntry<T> n = this.table[tableIndex]; n != null; n = n.next) {
				if (Root.equals(n.key, t)) {
					continue array;
				}
			}

			this.table[tableIndex] = new SetEntry<T>(t, h, this.table[tableIndex]);
			this.size++;
		}

		return origSize != this.size;
	}

	@Override
	public final void clear() {
		SetEntry<T> n, next;

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
	public final SetHashed<T> clone() {
		return new SetHashed<>(this);
	}

	@Override
	public final boolean contains(final Object obj) {
		for (SetEntry<T> n = this.table[Root.hashCode(obj) % this.table.length]; n != null; n = n.next) {
			if (Root.equals(n.key, obj)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public final boolean containsAll(final Collection<?> collection) {
		SetEntry<T> n;

		items: for (final Object obj : collection) {
			for (n = this.table[Root.hashCode(obj) % this.table.length]; n != null; n = n.next) {
				if (Root.equals(n.key, obj)) {
					continue items;
				}
			}

			return false;
		}

		return true;
	}

	@Override
	public final boolean containsAll(final Iterable<? extends T> iterable) {
		SetEntry<T> n;

		items: for (final T t : iterable) {
			for (n = this.table[Root.hashCode(t) % this.table.length]; n != null; n = n.next) {
				if (Root.equals(n.key, t)) {
					continue items;
				}
			}

			return false;
		}

		return true;
	}

	@Override
	public final boolean containsAny(final Iterable<? extends T> iterable) {
		SetEntry<T> n;

		for (final T t : iterable) {
			for (n = this.table[Root.hashCode(t) % this.table.length]; n != null; n = n.next) {
				if (Root.equals(n.key, t)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public final SetHashed<T> difference(final Iterable<? extends T> iterable) {
		final SetHashed<T> diff = new SetHashed<>(this.capacity);

		for (final T t : iterable) {
			if (!this.contains(t)) {
				diff.add(t);
			}
		}

		return diff;
	}

	@Override
	public final boolean equals(final Object param) {
		if (param != null && param instanceof java.util.Set) {
			final java.util.Set<?> set = (java.util.Set<?>) param;

			return this.size == set.size() && this.containsAll(set);
		}

		return false;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append('[');

		if (this.size > 0) {
			int i = 0;

			for (final SetEntry<T> setEntry : this.table) {
				for (SetEntry<T> e = setEntry; e != null; e = e.next) {
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
	public final T get(final T e) {
		final int i = Root.hashCode(e) % this.table.length;

		for (SetEntry<T> n = this.table[i]; n != null; n = n.next) {
			if (Root.equals(n.key, e)) {
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
		SetEntry<T> e;
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
	public final SetHashed<T> intersect(final Iterable<? extends T> iterable) {
		final SetHashed<T> intersect = new SetHashed<>(this.size);

		for (final T t : iterable) {
			if (this.contains(t)) {
				intersect.add(t);
			}
		}

		return intersect;
	}

	@Override
	public final boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public final Itemizer<T> iterator() {
		return new Itr();
	}

	@Override
	public final boolean remove(final Object obj) {
		final int i = Root.hashCode(obj) % this.table.length;
		SetEntry<T> n, prev = null;

		for (n = this.table[i]; n != null; prev = n, n = n.next) {
			if (Root.equals(n.key, obj)) {
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
	public final boolean replace(final T oldObj, final T newObj) {
		return this.remove(oldObj) && this.add(newObj);
	}

	@Override
	public final boolean retainAll(final Collection<?> collection) {
		SetEntry<T> n, prev = null;
		final int origSize = this.size;

		for (int i = 0, j = 0; j < this.size; i++) {
			for (n = this.table[i]; n != null; prev = n, n = n.next) {
				if (!collection.contains(n.key)) {
					if (prev == null) {
						this.table[i] = n.next;
					} else {
						prev.next = n.next;
					}

					n.next = null;
					this.size--;
				}

				j++;
			}
		}

		return origSize != this.size;
	}

	@Override
	public final int size() {
		return this.size;
	}

	@Override
	public final T[] toArray() {
		final T[] array = Root.newArray(this.size);
		SetEntry<T> n;

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
		SetEntry<T> n;

		for (int i = 0, j = 0; j < this.size; i++) {
			for (n = this.table[i]; n != null; n = n.next) {
				array[j++] = Root.cast(n.key);
			}
		}

		return array;
	}

	@Override
	public final SetImmutable<T> toImmutable() {
		return new SetImmutable<>(this);
	}

	@Override
	public final ListArray<T> toList() {
		final ListArray<T> list = new ListArray<>(this.size);
		SetEntry<T> n;

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
	public final SetHashed<T> union(final Iterable<? extends T> iterable) {
		final SetHashed<T> set = new SetHashed<>(this.capacity);

		set.addAll(this);
		set.addAll(iterable);

		return set;
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private int resize(final int h) {
		final SetEntry<T>[] oldTable = this.table;
		this.capacity = (this.capacity << 1) - (this.capacity >> 2);
		this.table = SetEntry.newArray(this.capacity);

		SetEntry<T> n, next;
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

} // End SetHashed
