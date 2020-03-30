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

import root.annotation.Delegate;
import root.lang.Extractable;
import root.lang.Immutable;
import root.lang.Itemizer;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the set
 */
@Delegate
public final class SetExtractable<T extends Extractable> implements RootSet<T>, Extractable, Immutable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final SetHashed<T> set;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public SetExtractable() {
		this.set = new SetHashed<>();
	}

	public SetExtractable(final Collection<? extends T> collection) {
		this.set = new SetHashed<>(collection);
	}

	public SetExtractable(final int capacity) {
		this.set = new SetHashed<>(capacity);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final boolean add(final T t) {
		return this.set.add(t);
	}

	@Override
	public final boolean addAll(final Collection<? extends T> collection) {
		return this.set.addAll(collection);
	}

	@Override
	public final boolean addAll(final Iterable<? extends T> iterable) {
		return this.set.addAll(iterable);
	}

	@Override
	public final boolean addAll(final T[] array, final int offset, final int length) {
		return this.set.addAll(array, offset, length);
	}

	@Override
	public final void clear() {
		this.set.clear();
	}

	@Override
	public final boolean contains(final Object obj) {
		return this.set.contains(obj);
	}

	@Override
	public final boolean containsAll(final Collection<?> collection) {
		return this.set.containsAll(collection);
	}

	@Override
	public final boolean containsAll(final Iterable<? extends T> iterable) {
		return this.set.containsAll(iterable);
	}

	@Override
	public final boolean containsAny(final Iterable<? extends T> iterable) {
		return this.set.containsAny(iterable);
	}

	@Override
	public final SetExtractable<T> difference(final Iterable<? extends T> iterable) {
		final SetExtractable<T> diff = new SetExtractable<>(this.set.capacity);

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
			final java.util.Set<?> setParam = (java.util.Set<?>) param;

			return this.set.size == setParam.size() && this.set.containsAll(setParam);
		}

		return false;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append('[');

		if (this.set.size > 0) {
			int i = 0;

			for (final SetEntry<T> setEntry : this.set.table) {
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
	public final T get(final T t) {
		return this.set.get(t);
	}

	@Override
	public final int getSize() {
		return this.set.size;
	}

	@Override
	public final int hashCode() {
		SetEntry<T> e;
		int h = this.set.size;

		for (int i = 0, j = 0; j < this.set.size; i++) {
			for (e = this.set.table[i]; e != null; e = e.next) {
				h <<= 1;
				h ^= e.hash;
				j++;
			}
		}

		return h;
	}

	@Override
	public final SetExtractable<T> intersect(final Iterable<? extends T> iterable) {
		final SetExtractable<T> intersect = new SetExtractable<>(this.set.capacity);

		for (final T t : iterable) {
			if (this.contains(t)) {
				intersect.add(t);
			}
		}

		return intersect;
	}

	@Override
	public final boolean isEmpty() {
		return this.set.size == 0;
	}

	@Override
	public final Itemizer<T> iterator() {
		return this.set.iterator();
	}

	@Override
	public final boolean remove(final Object obj) {
		return this.set.remove(obj);
	}

	@Override
	public final boolean removeAll(final Collection<?> collection) {
		return this.set.removeAll(collection);
	}

	@Override
	public final boolean replace(final T oldObj, final T newObj) {
		return this.set.replace(oldObj, newObj);
	}

	@Override
	public final boolean retainAll(final Collection<?> collection) {
		return this.set.retainAll(collection);
	}

	@Override
	public final int size() {
		return this.set.size;
	}

	@Override
	public final Object[] toArray() {
		return this.set.toArray();
	}

	@Override
	public final <E> E[] toArray(final E[] arrayParam) {
		return this.set.toArray(arrayParam);
	}

	@Override
	public final SetImmutable<T> toImmutable() {
		return new SetImmutable<>(this.set);
	}

	@Override
	public final ListExtractable<T> toList() {
		final ListExtractable<T> list = new ListExtractable<>(this.set.size);
		SetEntry<T> n;

		for (int i = 0, j = 0; j < this.set.size; i++) {
			for (n = this.set.table[i]; n != null; n = n.next) {
				list.add(n.key);
			}
		}

		return list;
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(this.set.size << 4);
		this.extract(extractor);
		return extractor.toString();
	}

	@Override
	public final SetExtractable<T> union(final Iterable<? extends T> iterable) {
		final SetExtractable<T> union = new SetExtractable<>(this.set.capacity);

		union.addAll(this);
		union.addAll(iterable);

		return union;
	}

} // End SetExtractable
