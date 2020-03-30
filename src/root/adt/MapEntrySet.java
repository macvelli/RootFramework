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
import java.util.Map;
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
 *            the {@link Map.Entry} key type
 * @param <V>
 *            the {@link Map.Entry} value type
 */
@Delegate
class MapEntrySet<K, V> implements Set<java.util.Map.Entry<K, V>> {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	/**
	 * A {@link java.util.Iterator} for the {@code MapEntrySet}. It is pointless to implement {@link Itemizer} here since we are implementing
	 * {@link java.util.Set}.
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private final class Iterator implements java.util.Iterator<java.util.Map.Entry<K, V>> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		final Itemizer<MapEntry<K, V>> itemizer = MapEntrySet.this.map.iterator();

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final boolean hasNext() {
			return this.itemizer.hasNext();
		}

		@Override
		public final MapEntry<K, V> next() {
			if (!this.itemizer.hasNext()) {
				throw new NoSuchElementException();
			}

			return this.itemizer.next();
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

	} // End Iterator

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	final RootMap<K, V> map;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	MapEntrySet(final RootMap<K, V> map) {
		this.map = map;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final boolean add(final java.util.Map.Entry<K, V> mapEntry) {
		this.map.put(mapEntry.getKey(), mapEntry.getValue());

		return true;
	}

	@Override
	public final boolean addAll(final Collection<? extends java.util.Map.Entry<K, V>> collection) {
		for (final Map.Entry<K, V> mapEntry : collection) {
			this.map.put(mapEntry.getKey(), mapEntry.getValue());
		}

		return true;
	}

	@Override
	public final void clear() {
		this.map.clear();
	}

	@Override
	public final boolean contains(final Object obj) {
		if (obj instanceof Map.Entry) {
			final Map.Entry<?, ?> mapEntry = (Map.Entry<?, ?>) obj;

			return this.map.containsEntry(mapEntry.getKey(), mapEntry.getValue());
		}

		return false;
	}

	@Override
	public final boolean containsAll(final Collection<?> collection) {
		if (collection != null && collection.size() > 0) {
			final java.util.Iterator<?> itr = collection.iterator();
			final Object obj;
			Map.Entry<?, ?> mapEntry = null;

			obj = itr.next();

			if (obj instanceof Map.Entry) {
				mapEntry = (Map.Entry<?, ?>) obj;

				if (this.map.containsEntry(mapEntry.getKey(), mapEntry.getValue())) {
					while (itr.hasNext()) {
						mapEntry = (Map.Entry<?, ?>) itr.next();

						if (!this.map.containsEntry(mapEntry.getKey(), mapEntry.getValue())) {
							return false;
						}
					}

					return true;
				}
			}
		}

		return false;
	}

	@Override
	public final boolean isEmpty() {
		return this.map.isEmpty();
	}

	@Override
	public final java.util.Iterator<java.util.Map.Entry<K, V>> iterator() {
		return new Iterator();
	}

	@Override
	public final boolean remove(final Object obj) {
		if (obj instanceof Map.Entry) {
			final Map.Entry<?, ?> mapEntry = (Map.Entry<?, ?>) obj;

			return this.map.remove(mapEntry.getKey()) != null;
		}

		return false;
	}

	@Override
	public final boolean removeAll(final Collection<?> collection) {
		if (collection != null && collection.size() > 0) {
			final java.util.Iterator<?> itr = collection.iterator();
			final Object obj;
			Map.Entry<?, ?> mapEntry = null;

			obj = itr.next();

			if (obj instanceof Map.Entry) {
				mapEntry = (Map.Entry<?, ?>) obj;

				this.map.remove(mapEntry.getKey());

				while (itr.hasNext()) {
					mapEntry = (Map.Entry<?, ?>) itr.next();

					this.map.remove(mapEntry.getKey());
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public final boolean retainAll(final Collection<?> collection) {
		if (collection != null && collection.size() > 0) {
			final int originalSize = this.map.size();

			for (final Itemizer<MapEntry<K, V>> itemizer = this.map.iterator(); itemizer.hasNext();) {
				if (!collection.contains(itemizer.next())) {
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
			array[i++] = mapEntry;
		}

		return array;
	}

	@Override
	public final <T> T[] toArray(final T[] arrayVar) {
		final T[] array = Root.newArray(arrayVar, this.map.size());
		int i = 0;

		for (final java.util.Map.Entry<K, V> mapEntry : this.map) {
			array[i++] = Root.cast(mapEntry);
		}

		return array;
	}

} // End MapEntrySet
