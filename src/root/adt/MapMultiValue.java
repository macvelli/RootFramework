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
import java.util.Set;

import root.annotation.Delegate;
import root.annotation.Todo;
import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.util.Root;

/**
 * Not quite a faithful implementation of {@link RootMap} but gets the job done for a Multimap.
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
@Todo("Compare to Guava")
@Delegate
public class MapMultiValue<K, V> implements RootMap<K, ListArray<V>> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int numValues;
	private final MapHashed<K, ListArray<V>> map;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor. Uses a default capacity of 8 for the map.
	 */
	public MapMultiValue() {
		this.map = new MapHashed<>();
	}

	/**
	 * A constructor that accepts a predetermined capacity.
	 *
	 * @param capacity
	 *            the predetermined capacity
	 */
	public MapMultiValue(final int capacity) {
		this.map = new MapHashed<>(capacity);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Clears the map.
	 */
	@Override
	public final void clear() {
		MapEntry<K, ListArray<V>> next;

		for (int i = 0; i < this.map.table.length; i++) {
			for (MapEntry<K, ListArray<V>> entry = this.map.table[i]; entry != null; entry = next) {
				next = entry.next;
				entry.value.clear();
				entry.value = null;
				entry.next = null;
			}

			this.map.table[i] = null;
		}

		this.map.size = 0;
		this.numValues = 0;
	}

	/**
	 * Returns a shallow copy of this {@link MapMultiValue} instance. (The elements themselves are not copied.)
	 *
	 * @return a shallow copy of this {@link MapMultiValue} instance
	 */
	@Override
	public final MapMultiValue<K, V> clone() {
		final MapMultiValue<K, V> map = new MapMultiValue<>(this.map.capacity);

		for (MapEntry<K, ListArray<V>> entry : this.map.table) {
			for (; entry != null; entry = entry.next) {
				map.put(entry.key, entry.value.clone());
			}
		}

		return map;
	}

	/**
	 * Returns {@code true} if the map contains the {@code key} and the {@code value} is contained within the {@link ListArray}, {@code false}
	 * otherwise.
	 *
	 * @param key
	 *            the key of the entry
	 * @param value
	 *            the value of the entry
	 * @return {@code true} if the map contains the {@code key} and the {@code value}
	 */
	@Override
	public final boolean containsEntry(final Object key, final Object value) {
		final ListArray<V> valueList = this.map.get(key);

		return valueList != null && valueList.contains(value);
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
		return this.map.containsKey(key);
	}

	/**
	 * Returns {@code true} if the map contains the {@code value} associated with the {@code key}, {@code false} otherwise.
	 *
	 * @param key
	 *            the key of the entry
	 * @param value
	 *            the value of the entry
	 * @return {@code true} if the map contains the {@code value} associated with the {@code key}
	 */
	public final boolean containsValue(final K key, final V value) {
		for (MapEntry<K, ListArray<V>> entry = this.map.table[Root.hashCode(key) % this.map.table.length]; entry != null; entry = entry.next) {
			if (Root.equals(entry.key, key)) {
				return entry.value.contains(value);
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
		for (MapEntry<K, ListArray<V>> entry : this.map.table) {
			for (; entry != null; entry = entry.next) {
				if (entry.value.contains(value)) {
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
	public final Set<java.util.Map.Entry<K, ListArray<V>>> entrySet() {
		return new MapEntrySet<>(this);
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

			if (this.map.size == map.size()) {
				Object mapValue;

				for (MapEntry<K, ListArray<V>> entry : this.map.table) {
					for (; entry != null; entry = entry.next) {
						mapValue = map.get(entry.key);

						if (mapValue == null) {
							return false;
						}

						if (!entry.value.equals(mapValue)) {
							return false;
						}
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
		this.map.extract(extractor);
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final ListArray<V> get(final K key, final Class<? extends ListArray<V>> clazz) {
		throw new UnsupportedOperationException(
				"This method is pointless for a MapMultiValue, and because of type erasure I cannot include a get(K, Class<V>) method...oh and good luck trying to call this method anyway");
	}

	/**
	 * Returns the value associated with the {@code key} at the specified index, or {@code null} if no mapping exists.
	 *
	 * @param key
	 *            the key of the entry
	 * @param index
	 *            the index of the value
	 * @return the value associated with the {@code key} at the specified index, or {@code null} if no mapping exists
	 */
	public final V get(final K key, final int index) {
		final ListArray<V> list = this.map.get(key);

		return list == null ? null : list.get(index);
	}

	/**
	 * Returns the list of values associated with the {@code key}. If there is no mapping for the {@code key}, then the {@code defaultVal} is returned
	 * from the method.
	 *
	 * @param key
	 *            the key of the entry
	 * @param defaultVal
	 *            the default list of values to return if no mapping exists
	 * @return the list of values associated with the {@code key}, or the {@code defaultVal}
	 */
	@Override
	public final ListArray<V> get(final K key, final ListArray<V> defaultVal) {
		return this.map.get(key, defaultVal);
	}

	/**
	 * Returns the list of values associated with the {@code key}, or {@code null} if no mapping exists.
	 *
	 * @param key
	 *            the key of the entry
	 * @return the list of values associated with the {@code key}, or {@code null} if no mapping exists
	 */
	@Override
	public final ListArray<V> get(final Object key) {
		return this.map.get(key);
	}

	/**
	 * Returns the capacity of the map, which is how many keys it can accept before performing a resize.
	 *
	 * @return the capacity of the map
	 */
	@Override
	public final int getCapacity() {
		return this.map.getCapacity();
	}

	/**
	 * Returns the number of values in the map.
	 *
	 * @return the number of values in the map
	 */
	public final int getNumValues() {
		return this.numValues;
	}

	/**
	 * Returns the size of the map, which is how many keys are in the map.
	 *
	 * @return the size of the map
	 */
	@Override
	public final int getSize() {
		return this.map.size;
	}

	/**
	 * Returns the hash code of the map.
	 *
	 * @return the hash code of the map
	 */
	@Override
	public final int hashCode() {
		return this.map.hashCode();
	}

	/**
	 * Returns {@code true} if the map is empty, which means its size is equal to zero.
	 *
	 * @return {@code true} if the map is empty
	 */
	@Override
	public final boolean isEmpty() {
		return this.map.size == 0;
	}

	/**
	 * Returns an {@link Itemizer} for the map.
	 *
	 * @return an {@link Itemizer} for the map
	 */
	@Override
	public final Itemizer<MapEntry<K, ListArray<V>>> iterator() {
		return this.map.iterator();
	}

	/**
	 * Returns a {@link Set} of all the keys contained within this map.
	 *
	 * @return a {@link Set} of all the keys contained within this map
	 */
	@Override
	public final Set<K> keySet() {
		return this.map.keySet();
	}

	/**
	 * Puts the list of values into the map under the {@code key}. If a mapping for {@code key} already exists, the existing list of values in the map
	 * is replaced with (@code valueList).
	 *
	 * @param key
	 *            the key of the entry
	 * @param valueList
	 *            the list of values of the entry
	 * @return {@code null} if a mapping for {@code key} <b>does not</b> exist, or the existing list of values in the map that was replaced
	 */
	@Override
	public final ListArray<V> put(final K key, final ListArray<V> valueList) {
		final ListArray<V> list = this.map.put(key, valueList);

		if (list != null) {
			this.numValues -= list.size;
		}

		this.numValues += valueList.size;

		return list;
	}

	/**
	 * Puts all of the values in the {@link Collection} into the map under the {@code key}.
	 *
	 * @param key
	 *            the key of the entry
	 * @param collection
	 *            the {@link Collection} of values to put into the map under the {@code key}
	 */
	public final void putAll(final K key, final Collection<? extends V> collection) {
		ListArray<V> list = this.map.get(key);

		if (list == null) {
			list = new ListArray<>();
			this.map.put(key, list);
		}

		list.addAll(collection);
		this.numValues += collection.size();
	}

	/**
	 * Puts all of the entries in the {@link Map} into the map.
	 *
	 * @param map
	 *            the {@link Map} to put into the map
	 */
	@Override
	public final void putAll(final Map<? extends K, ? extends ListArray<V>> map) {
		for (final Map.Entry<? extends K, ? extends ListArray<V>> entry : map.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Removes the {@code (key, value)} mapping from the map, if one exists.
	 *
	 * @param key
	 *            the key of the entry to remove
	 * @param value
	 *            the value of the entry to remove
	 * @return {@code true} if the value was removed from the map, {@code false} otherwise
	 */
	public final boolean remove(final K key, final V value) {
		final ListArray<V> list = this.map.get(key);

		if (list != null && list.remove(value)) {
			if (list.isEmpty()) {
				this.map.remove(key);
			}

			this.numValues--;

			return true;
		}

		return false;
	}

	/**
	 * Removes the list of values mapped under {@code key}, if one exists.
	 *
	 * @param key
	 *            the key of the entry to remove
	 * @return the list of values associated with the mapping if present, or {@code null} if no mapping exists
	 */
	@Override
	public final ListArray<V> remove(final Object key) {
		final ListArray<V> list = this.map.remove(key);

		if (list != null) {
			this.numValues -= list.size;
		}

		return list;
	}

	/**
	 * Returns the size of the map, which is how many keys are in the map.
	 *
	 * @return the size of the map
	 */
	@Override
	public final int size() {
		return this.map.size;
	}

	/**
	 * Returns an immutable version of the map.
	 *
	 * @return an immutable version of the map
	 */
	@Override
	public final MapImmutable<K, ListArray<V>> toImmutable() {
		return new MapImmutable<>(this);
	}

	/**
	 * Returns a {@link String} representation of the map.
	 *
	 * @return a {@link String} representation of the map
	 */
	@Override
	public String toString() {
		final StringExtractor extractor = new StringExtractor(this.map.size << 4);
		this.extract(extractor);
		return extractor.toString();
	}

	/**
	 * Returns a {@link Collection} of all the values contained within this map.
	 *
	 * @return a {@link Collection} of all the values contained within this map
	 */
	@Override
	public final Collection<ListArray<V>> values() {
		return new MapValueCollection<>(this.map);
	}

} // End MapMultiValue
