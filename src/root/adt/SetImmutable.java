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
import root.lang.ImmutableItemizer;
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
public final class SetImmutable<T> implements RootSet<T>, Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final SetHashed<T> set;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	SetImmutable(final SetHashed<T> set) {
		this.set = set.clone();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final boolean add(final T t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean addAll(final Collection<? extends T> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean addAll(final Iterable<? extends T> iterable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean addAll(final T[] array, final int offset, final int length) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void clear() {
		throw new UnsupportedOperationException();
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
	public final SetHashed<T> difference(final Iterable<? extends T> iterable) {
		return this.set.difference(iterable);
	}

	@Override
	public final boolean equals(final Object obj) {
		return this.set.equals(obj);
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		this.set.extract(extractor);
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
		return this.set.hashCode();
	}

	@Override
	public final SetHashed<T> intersect(final Iterable<? extends T> iterable) {
		return this.set.intersect(iterable);
	}

	@Override
	public final boolean isEmpty() {
		return this.set.isEmpty();
	}

	@Override
	public final Itemizer<T> iterator() {
		return new ImmutableItemizer<>(this.set.iterator());
	}

	@Override
	public final boolean remove(final Object obj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean removeAll(final Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean replace(final T oldObj, final T newObj) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean retainAll(final Collection<?> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final int size() {
		return this.set.size;
	}

	@Override
	public final T[] toArray() {
		return this.set.toArray();
	}

	@Override
	public final <E> E[] toArray(final E[] array) {
		return this.set.toArray(array);
	}

	@Override
	public final ListArray<T> toList() {
		return this.set.toList();
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(this.set.size << 4);
		this.set.extract(extractor);
		return extractor.toString();
	}

	@Override
	public final SetHashed<T> union(final Iterable<? extends T> iterable) {
		return this.set.union(iterable);
	}

} // End SetImmutable
