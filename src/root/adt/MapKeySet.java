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
import java.util.Set;

import root.annotation.Delegate;
import root.lang.Itemizer;
import root.util.Root;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <K>
 *            The key type of the map
 * @param <V>
 *            The value type of the map
 */
@Delegate
class MapKeySet<K, V> implements Set<K> {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	/**
	 * A {@link java.util.Iterator} for the {@code MapKeySet}. It is pointless to implement {@link Itemizer} here since we are implementing
	 * {@link java.util.Set}.
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private final class Iterator implements java.util.Iterator<K> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		final Itemizer<MapEntry<K, V>> itemizer = MapKeySet.this.map.iterator();

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final boolean hasNext() {
			return this.itemizer.hasNext();
		}

		@Override
		public final K next() {
			if (!this.itemizer.hasNext()) {
				throw new NoSuchElementException();
			}

			return this.itemizer.next().key;
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

	} // End Iterator

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final RootMap<K, V> map;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	MapKeySet(final RootMap<K, V> map) {
		this.map = map;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final boolean add(final K e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final boolean addAll(final Collection<? extends K> collection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void clear() {
		this.map.clear();
	}

	@Override
	public final boolean contains(final Object obj) {
		return this.map.containsKey(obj);
	}

	@Override
	public final boolean containsAll(final Collection<?> collection) {
		if (collection != null && collection.size() > 0) {
			for (final Object obj : collection) {
				if (!this.map.containsKey(obj)) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public final boolean isEmpty() {
		return this.map.isEmpty();
	}

	@Override
	public final java.util.Iterator<K> iterator() {
		return new Iterator();
	}

	@Override
	public final boolean remove(final Object obj) {
		return this.map.remove(obj) != null;
	}

	@Override
	public final boolean removeAll(final Collection<?> collection) {
		if (collection != null && collection.size() > 0) {
			final int originalSize = this.map.getSize();

			for (final Object obj : collection) {
				this.map.remove(obj);
			}

			return originalSize != this.map.size();
		}

		return false;
	}

	@Override
	public final boolean retainAll(final Collection<?> collection) {
		if (collection != null && collection.size() > 0) {
			final int originalSize = this.map.getSize();
			MapEntry<K, V> mapEntry;

			for (final Itemizer<MapEntry<K, V>> itemizer = this.map.iterator(); itemizer.hasNext();) {
				mapEntry = itemizer.next();

				if (!collection.contains(mapEntry.key)) {
					itemizer.remove();
				}
			}

			return originalSize != this.map.size();
		}

		return false;
	}

	@Override
	public final int size() {
		return this.map.size();
	}

	@Override
	public final Object[] toArray() {
		final Object[] array = new Object[this.map.size()];
		int i = 0;

		for (final java.util.Map.Entry<K, V> mapEntry : this.map) {
			array[i++] = mapEntry.getKey();
		}

		return array;
	}

	@Override
	public final <T> T[] toArray(final T[] arrayVar) {
		final T[] array = Root.newArray(arrayVar, this.map.size());
		int i = 0;

		for (final java.util.Map.Entry<K, ?> mapEntry : this.map) {
			array[i++] = Root.cast(mapEntry.getKey());
		}

		return array;
	}

} // End MapKeySet
