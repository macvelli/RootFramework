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
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import root.annotation.Todo;
import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.util.Root;

/**
 * Modern take on the classic {@link HashMap}.
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
@Todo("Check out http://www.ibm.com/developerworks/library/j-pg04149/ and see what I can do here")
public final class MapHashed<K, V> implements RootMap<K, V> {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	/**
	 * An {@link Itemizer} for the {@code MapHashed}.
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private final class Iterator implements Itemizer<MapEntry<K, V>> {

		private int i, j;
		private MapEntry<K, V> mapEntry;

		@Override
		public final int getIndex() {
			return this.j - 1;
		}

		@Override
		public final int getSize() {
			return MapHashed.this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.j < MapHashed.this.size;
		}

		@Override
		public final Itemizer<MapEntry<K, V>> iterator() {
			return this;
		}

		@Override
		public final MapEntry<K, V> next() {
			if (this.j == MapHashed.this.size) {
				throw new NoSuchElementException();
			}

			this.j++;

			if (this.mapEntry != null) {
				this.mapEntry = this.mapEntry.next;
			}

			while (this.mapEntry == null) {
				this.mapEntry = MapHashed.this.table[this.i++];
			}

			return this.mapEntry;
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public final void reset() {
			this.i = 0;
			this.j = 0;
			this.mapEntry = null;
		}

	} // End Iterator

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	int size;
	int capacity;
	MapEntry<K, V>[] table;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor. Uses a default capacity of 8 for the map.
	 */
	@SuppressWarnings("unchecked")
	public MapHashed() {
		this.capacity = 8;
		this.table = new MapEntry[Root.calculateHashTableSize(8)];
	}

	/**
	 * A constructor that accepts a predetermined capacity. Calculates the capacity of the map using {@link Root#calculateHashTableCapacity(int)}
	 * which aligns the capacity of the map on multiples of 16.
	 *
	 * @param capacity
	 *            the predetermined capacity
	 */
	@SuppressWarnings("unchecked")
	public MapHashed(final int capacity) {
		this.capacity = Root.calculateHashTableCapacity(capacity);
		this.table = new MapEntry[Root.calculateHashTableSize(this.capacity)];
	}

	/**
	 * A constructor that adds all of the entries within the {@link Map} to this map upon creation.
	 *
	 * @param map
	 *            the {@link Map} to add to the map upon creation
	 */
	@SuppressWarnings("unchecked")
	public MapHashed(final Map<? extends K, ? extends V> map) {
		this.capacity = Root.calculateHashTableCapacity(map.size());
		this.table = new MapEntry[Root.calculateHashTableSize(this.capacity)];

		for (final Entry<? extends K, ? extends V> entry : map.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Clears the map.
	 */
	@Override
	public final void clear() {
		MapEntry<K, V> entry, next;

		for (int i = 0; i < this.table.length; i++) {
			for (entry = this.table[i]; entry != null; entry = next) {
				next = entry.next;
				entry.value = null;
				entry.next = null;
			}

			this.table[i] = null;
		}

		this.size = 0;
	}

	/**
	 * Returns a shallow copy of this {@link MapHashed} instance. (The elements themselves are not copied.)
	 *
	 * @return a shallow copy of this {@link MapHashed} instance
	 */
	@Override
	public final MapHashed<K, V> clone() {
		final MapHashed<K, V> map = new MapHashed<>(this.capacity);

		for (final MapEntry<K, V> entry : this) {
			map.put(entry.key, entry.value);
		}

		return map;
	}

	/**
	 * Returns {@code true} if the map contains the {@code (key, value)} pair, {@code false} otherwise.
	 *
	 * @param key
	 *            the key of the entry
	 * @param value
	 *            the value of the entry
	 * @return {@code true} if the map contains the {@code (key, value)} pair
	 */
	@Override
	public final boolean containsEntry(final Object key, final Object value) {
		for (MapEntry<K, V> e = this.table[Root.hashCode(key) % this.table.length]; e != null; e = e.next) {
			if (Root.equals(e.key, key)) {
				return Root.equals(e.value, value);
			}
		}

		return false;
	}

	/**
	 * Returns {@code true} if the map contains the {@code key}, {@code false} otherwise.
	 *
	 * @param key
	 *            the key of the entry
	 * @return {@code true} if the map contains the {@code key}
	 */
	@Override
	public final boolean containsKey(final Object key) {
		for (MapEntry<K, V> e = this.table[Root.hashCode(key) % this.table.length]; e != null; e = e.next) {
			if (Root.equals(e.key, key)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns {@code true} if the map contains the {@code value}, {@code false} otherwise.
	 *
	 * @param value
	 *            the value of the entry
	 * @return {@code true} if the map contains the {@code value}
	 */
	@Override
	public final boolean containsValue(final Object value) {
		MapEntry<K, V> entry;

		for (final MapEntry<K, V> element : this.table) {
			for (entry = element; entry != null; entry = entry.next) {
				if (Root.equals(entry.value, value)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns a {@link Set} of all the entries contained within this map.
	 *
	 * @return a {@link Set} of all the entries contained within this map
	 */
	@Override
	public final Set<java.util.Map.Entry<K, V>> entrySet() {
		return new MapEntrySet<K, V>(this);
	}

	/**
	 * Returns {@code true} if the specified {@link Object} is equal to {@code this} object. The specified {@link Object} is equal to {@code this}
	 * object if:
	 * <ul>
	 * <li>The {@link Class} of the specified {@link Object} is an instance of {@link Map}</li>
	 * <li>The {@code size} of the specified {@link Map} and {@code this} object are equal</li>
	 * <li>All entries in both the specified {@link Map} and {@code this} object are equal to each other</li>
	 * </ul>
	 *
	 * @param param
	 *            the specified {@link Object} to compare for equality to {@code this} object
	 * @return {@code true} if the specified {@link Object} is equal to {@code this} object, false otherwise
	 */
	@Override
	public final boolean equals(final Object param) {
		if (param != null && param instanceof Map) {
			final Map<?, ?> map = (Map<?, ?>) param;

			if (this.size == map.size()) {
				for (final Map.Entry<?, ?> entry : map.entrySet()) {
					if (!this.containsEntry(entry.getKey(), entry.getValue())) {
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}

	/**
	 * Extracts a {@link String} representation of the list.
	 *
	 * @param extractor
	 *            the {@link StringExtractor} to populate
	 */
	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append('{');

		if (this.size > 0) {
			int j = 0;

			for (MapEntry<K, V> entry : this.table) {
				for (; entry != null; entry = entry.next) {
					if (j++ > 0) {
						extractor.addSeparator();
					}

					extractor.append(entry.key).append('=').append(entry.value);
				}
			}
		}

		extractor.append('}');
	}

	/**
	 * Returns the value associated with the {@code key}. If there is no mapping for the {@code key}:
	 * <ul>
	 * <li>A new instance of {@link Class} is created using its default constructor</li>
	 * <li>The mapping of {@code key} and the new {@link Class} instance is added to the map</li>
	 * <li>The newly created value instance is returned from the method</li>
	 * </ul>
	 *
	 * @param key
	 *            the key of the entry
	 * @param clazz
	 *            the {@link Class} of the value of the map
	 * @return the value associated with the {@code key}, or a new instance of {@link Class}
	 */
	@Override
	public final V get(final K key, final Class<? extends V> clazz) {
		final int h = Root.hashCode(key);
		int i = h % this.table.length;

		for (MapEntry<K, V> e = this.table[i]; e != null; e = e.next) {
			if (Root.equals(e.key, key)) {
				return e.value;
			}
		}

		if (this.size++ == this.capacity) {
			i = this.resize(h);
		}

		this.table[i] = new MapEntry<K, V>(key, Root.newInstance(clazz), h, this.table[i]);

		return this.table[i].value;
	}

	/**
	 * Returns the value associated with the {@code key}. If there is no mapping for the {@code key}, then the {@code defaultVal} is returned from the
	 * method.
	 *
	 * @param key
	 *            the key of the entry
	 * @param defaultVal
	 *            the default value to return if no mapping exists
	 * @return the value associated with the {@code key}, or the {@code defaultVal}
	 */
	@Override
	public final V get(final K key, final V defaultVal) {
		for (MapEntry<K, V> e = this.table[Root.hashCode(key) % this.table.length]; e != null; e = e.next) {
			if (Root.equals(e.key, key)) {
				return e.value;
			}
		}

		return defaultVal;
	}

	/**
	 * Returns the value associated with the {@code key}, or {@code null} if no mapping exists.
	 *
	 * @param key
	 *            the key of the entry
	 * @return the value associated with the {@code key}, or {@code null} if no mapping exists
	 */
	@Override
	public final V get(final Object key) {
		for (MapEntry<K, V> e = this.table[Root.hashCode(key) % this.table.length]; e != null; e = e.next) {
			if (Root.equals(e.key, key)) {
				return e.value;
			}
		}

		return null;
	}

	/**
	 * Returns the capacity of the map.
	 *
	 * @return the capacity of the map
	 */
	@Override
	public final int getCapacity() {
		return this.capacity;
	}

	/**
	 * Returns the size of the map, which is how many elements are actually in the map.
	 *
	 * @return the size of the map
	 */
	@Override
	public final int getSize() {
		return this.size;
	}

	/**
	 * Returns the hash code of the map.
	 *
	 * @return the hash code of the map
	 */
	@Override
	public final int hashCode() {
		int h = this.size;

		for (MapEntry<K, V> entry : this.table) {
			for (; entry != null; entry = entry.next) {
				h <<= 1;

				h ^= entry.hash;
			}
		}

		return h;
	}

	/**
	 * Returns {@code true} if the map is empty, which means its size is equal to zero.
	 *
	 * @return {@code true} if the map is empty
	 */
	@Override
	public final boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Returns an {@link Itemizer} for the map.
	 *
	 * @return an {@link Itemizer} for the map
	 */
	@Override
	public final Itemizer<MapEntry<K, V>> iterator() {
		return new Iterator();
	}

	/**
	 * Returns a {@link Set} of all the keys contained within this map.
	 *
	 * @return a {@link Set} of all the keys contained within this map
	 */
	@Override
	public final Set<K> keySet() {
		return new MapKeySet<>(this);
	}

	/**
	 * Puts the {@code (key, value)} mapping into the map. If a mapping for {@code key} already exists, the existing value in the map is replaced with
	 * (@code value).
	 *
	 * @param key
	 *            the key of the entry
	 * @param value
	 *            the value of the entry
	 * @return {@code null} if a mapping for {@code key} <b>does not</b> exist, or the existing value in the map that was replaced
	 */
	@Override
	public final V put(final K key, final V value) {
		final int h = Root.hashCode(key);
		int i = h % this.table.length;

		for (MapEntry<K, V> e = this.table[i]; e != null; e = e.next) {
			if (Root.equals(e.key, key)) {
				final V v = e.value;
				e.value = value;
				return v;
			}
		}

		if (this.size++ == this.capacity) {
			i = this.resize(h);
		}

		this.table[i] = new MapEntry<K, V>(key, value, h, this.table[i]);

		return null;
	}

	/**
	 * Puts all of the entries in the {@link Map} into the map.
	 *
	 * @param map
	 *            the {@link Map} to put into the map
	 */
	@Override
	public final void putAll(final Map<? extends K, ? extends V> map) {
		for (final Map.Entry<? extends K, ? extends V> mapEntry : map.entrySet()) {
			this.put(mapEntry.getKey(), mapEntry.getValue());
		}
	}

	/**
	 * Removes the {@code (key, value)} mapping from the map, if one exists. Returns the value associated with the mapping if present, or {@code null}
	 * if no mapping exists.
	 *
	 * @param key
	 *            the key of the entry to remove
	 * @return the value associated with the mapping if present, or {@code null} if no mapping exists
	 */
	@Override
	public final V remove(final Object key) {
		final int i = Root.hashCode(key) % this.table.length;

		for (MapEntry<K, V> e = this.table[i], prev = null; e != null; prev = e, e = e.next) {
			if (Root.equals(e.key, key)) {
				if (prev == null) {
					this.table[i] = e.next;
				} else {
					prev.next = e.next;
				}

				final V v = e.value;
				e.value = null;
				e.next = null;
				this.size--;
				return v;
			}
		}

		return null;
	}

	/**
	 * Returns the size of the map, which is how many elements are actually in the map.
	 *
	 * @return the size of the map
	 */
	@Override
	public final int size() {
		return this.size;
	}

	/**
	 * Returns an immutable version of the map.
	 *
	 * @return an immutable version of the map
	 */
	@Override
	public final MapImmutable<K, V> toImmutable() {
		return new MapImmutable<>(this);
	}

	/**
	 * Returns a {@link String} representation of the map.
	 *
	 * @return a {@link String} representation of the map
	 */
	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(this.size << 4);
		this.extract(extractor);
		return extractor.toString();
	}

	/**
	 * Returns a {@link Collection} of all the values contained within this map.
	 *
	 * @return a {@link Collection} of all the values contained within this map
	 */
	@Override
	public final Collection<V> values() {
		return new MapValueCollection<>(this);
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	@SuppressWarnings("unchecked")
	private int resize(final int h) {
		final MapEntry<K, V>[] oldTable = this.table;
		this.capacity <<= 1;
		this.table = new MapEntry[Root.calculateHashTableSize(this.capacity)];

		MapEntry<K, V> entry, next;
		int index;
		for (int i = 0; i < oldTable.length; i++) {
			for (entry = oldTable[i]; entry != null; entry = next) {
				next = entry.next;
				index = entry.hash % this.table.length;
				entry.next = this.table[index];
				this.table[index] = entry;
			}

			oldTable[i] = null;
		}

		return h % this.table.length;
	}

} // End MapHashed
