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
 * A map that stores both the {@code (key, value)} mapping as well as the {@code (value, key)} mapping. The {@link #getKey(Object)} method allows you
 * to look up the key using the value.
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
@Todo({ "To be entirely correct, the valueKeyMap should be a MapMultiValue since in a regular Map there is only one key but duplicate values are possible",
		"Or just don't allow duplicate values at all in a bidirectional map...which seems unneccessarily restrictive" })
public final class MapBidirectional<K, V> implements RootMap<K, V> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	final MapHashed<K, V> keyValueMap;
	final MapHashed<V, K> valueKeyMap;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor. Uses a default capacity of 8 for the map.
	 */
	public MapBidirectional() {
		this.keyValueMap = new MapHashed<>();
		this.valueKeyMap = new MapHashed<>();
	}

	/**
	 * A constructor that accepts a predetermined capacity. Calculates the capacity of the map using {@link Root#calculateHashTableCapacity(int)}
	 * which aligns the capacity of the map on multiples of 16.
	 *
	 * @param capacity
	 *            the predetermined capacity
	 */
	public MapBidirectional(int capacity) {
		capacity = Root.calculateHashTableCapacity(capacity);
		this.keyValueMap = new MapHashed<>(capacity);
		this.valueKeyMap = new MapHashed<>(capacity);
	}

	/**
	 * A constructor that adds all of the entries within the {@link Map} to this map upon creation.
	 *
	 * @param map
	 *            the {@link Map} to add to the map upon creation
	 */
	public MapBidirectional(final Map<? extends K, ? extends V> map) {
		final int capacity = Root.calculateHashTableCapacity(map.size());
		this.keyValueMap = new MapHashed<>(capacity);
		this.valueKeyMap = new MapHashed<>(capacity);

		for (final Entry<? extends K, ? extends V> entry : map.entrySet()) {
			this.keyValueMap.put(entry.getKey(), entry.getValue());
			this.valueKeyMap.put(entry.getValue(), entry.getKey());
		}
	}

	/**
	 * A constructor used by the {{@link #clone()} method.
	 *
	 * @param map
	 *            the {@link MapBidirectional} to clone
	 */
	private MapBidirectional(final MapBidirectional<K, V> map) {
		this.keyValueMap = map.keyValueMap.clone();
		this.valueKeyMap = map.valueKeyMap.clone();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Clears the map.
	 */
	@Override
	public final void clear() {
		this.keyValueMap.clear();
		this.valueKeyMap.clear();
	}

	/**
	 * Returns a shallow copy of this {@link MapBidirectional} instance. (The elements themselves are not copied.)
	 *
	 * @return a shallow copy of this {@link MapBidirectional} instance
	 */
	@Override
	public final MapBidirectional<K, V> clone() {
		return new MapBidirectional<>(this);
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
		return this.keyValueMap.containsEntry(key, value);
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
		return this.keyValueMap.containsKey(key);
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
		return this.valueKeyMap.containsKey(value);
	}

	/**
	 * Returns a {@link Set} of all the entries contained within this map.
	 *
	 * @return a {@link Set} of all the entries contained within this map
	 */
	@Override
	public final Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.keyValueMap.entrySet();
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

			if (this.keyValueMap.size == map.size()) {
				for (final Map.Entry<?, ?> entry : map.entrySet()) {
					if (!this.keyValueMap.containsEntry(entry.getKey(), entry.getValue())) {
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
		this.keyValueMap.extract(extractor);
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
		final V value = this.keyValueMap.get(key, clazz);

		if (!this.valueKeyMap.containsKey(value)) {
			this.valueKeyMap.put(value, key);
		}

		return value;
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
		return this.keyValueMap.get(key, defaultVal);
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
		return this.keyValueMap.get(key);
	}

	/**
	 * Returns the capacity of the map.
	 *
	 * @return the capacity of the map
	 */
	@Override
	public final int getCapacity() {
		return this.keyValueMap.getCapacity();
	}

	/**
	 * Returns the key associated with the {@code value}, or {@code null} if no mapping exists.
	 *
	 * @param value
	 *            the value of the entry
	 * @return the key associated with the {@code value}, or {@code null} if no mapping exists
	 */
	public final K getKey(final Object value) {
		return this.valueKeyMap.get(value);
	}

	/**
	 * Returns the size of the map, which is how many elements are actually in the map.
	 *
	 * @return the size of the map
	 */
	@Override
	public final int getSize() {
		return this.keyValueMap.size;
	}

	/**
	 * Returns the hash code of the map.
	 *
	 * @return the hash code of the map
	 */
	@Override
	public final int hashCode() {
		return this.keyValueMap.hashCode();
	}

	/**
	 * Returns {@code true} if the map is empty, which means its size is equal to zero.
	 *
	 * @return {@code true} if the map is empty
	 */
	@Override
	public final boolean isEmpty() {
		return this.keyValueMap.size == 0;
	}

	/**
	 * Returns an {@link Itemizer} for the map.
	 *
	 * @return an {@link Itemizer} for the map
	 */
	@Override
	public final Itemizer<MapEntry<K, V>> iterator() {
		return this.keyValueMap.iterator();
	}

	/**
	 * Returns a {@link Set} of all the keys contained within this map.
	 *
	 * @return a {@link Set} of all the keys contained within this map
	 */
	@Override
	public final Set<K> keySet() {
		return this.keyValueMap.keySet();
	}

	/**
	 * Puts the {@code (key, value)} mapping into the map. If a mapping for {@code key} already exists, the existing in the map is replaced with
	 * (@code value).
	 *
	 * @param key
	 *            the key of the entry
	 * @param value
	 *            the value of the entry
	 * @return {@code null} if a mapping for {@code key} <b>does not</b> exist, or the existing value in the map that was replaced.
	 */
	@Override
	public final V put(final K key, final V value) {
		final V oldValue = this.keyValueMap.put(key, value);

		if (oldValue != null) {
			this.valueKeyMap.remove(oldValue);
		}

		this.valueKeyMap.put(value, key);

		return oldValue;
	}

	/**
	 * Puts all of the entries in the {@link Map} into this map.
	 *
	 * @param map
	 *            the {@link Map} to put into this map
	 */
	@Override
	public final void putAll(final Map<? extends K, ? extends V> map) {
		for (final Map.Entry<? extends K, ? extends V> mapEntry : map.entrySet()) {
			this.keyValueMap.put(mapEntry.getKey(), mapEntry.getValue());
			this.valueKeyMap.put(mapEntry.getValue(), mapEntry.getKey());
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
		final V oldValue = this.keyValueMap.remove(key);

		if (oldValue != null) {
			this.valueKeyMap.remove(oldValue);
		}

		return oldValue;
	}

	/**
	 * Returns the size of the map, which is how many elements are actually in the map.
	 *
	 * @return the size of the map
	 */
	@Override
	public final int size() {
		return this.keyValueMap.size;
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
		final StringExtractor extractor = new StringExtractor(this.keyValueMap.size << 4);
		this.keyValueMap.extract(extractor);
		return extractor.toString();
	}

	/**
	 * Returns a {@link Collection} of all the values contained within this map.
	 *
	 * @return a {@link Collection} of all the values contained within this map
	 */
	@Override
	public final Collection<V> values() {
		return this.keyValueMap.values();
	}

} // End MapBidirectional
