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
import root.lang.Extractable;
import root.lang.Itemizer;
import root.lang.StringExtractor;

/**
 * Turns any {@link RootMap} into an {@link Extractable} map, meaning it will accept {@link Extractable} objects of type {@code T} and utilizes the
 * efficiencies of the {@link Extractable} interface on {@link #extract(StringExtractor)} and {@link #toString()}.
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
public final class MapExtractable<K extends Extractable, V extends Extractable> implements RootMap<K, V> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final RootMap<K, V> map;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor. Uses a default capacity of 8 for the map.
	 */
	public MapExtractable() {
		this.map = new MapHashed<>();
	}

	/**
	 * A constructor that accepts a predetermined capacity.
	 *
	 * @param capacity
	 *            the predetermined capacity
	 */
	public MapExtractable(final int capacity) {
		this.map = new MapHashed<>(capacity);
	}

	/**
	 * A constructor that adds all of the entries within the {@link Map} to this map upon creation.
	 *
	 * @param map
	 *            the {@link Map} to add to the map upon creation
	 */
	public MapExtractable(final Map<? extends K, ? extends V> map) {
		this.map = new MapHashed<>(map);
	}

	/**
	 * A constructor that uses the {@link RootMap} parameter to initialize this map.
	 *
	 * @param rootMap
	 *            the {@link RootMap} to use
	 */
	public MapExtractable(final RootMap<K, V> rootMap) {
		this.map = rootMap;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Clears the map.
	 */
	@Override
	public final void clear() {
		this.map.clear();
	}

	/**
	 * Returns a shallow copy of this {@link MapExtractable} instance. (The elements themselves are not copied.)
	 *
	 * @return a shallow copy of this {@link MapExtractable} instance
	 */
	@Override
	public final MapExtractable<K, V> clone() {
		return new MapExtractable<>(this.map.clone());
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
		return this.map.containsEntry(key, value);
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
	 * Returns {@code true} if the map contains the {@code value}, {@code false} otherwise.
	 *
	 * @param value
	 *            the value of the entry
	 * @return {@code true} if the map contains the {@code value}
	 */
	@Override
	public final boolean containsValue(final Object value) {
		return this.map.containsValue(value);
	}

	/**
	 * Returns a {@link Set} of all the entries contained within this map.
	 *
	 * @return a {@link Set} of all the entries contained within this map
	 */
	@Override
	public final Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.map.entrySet();
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

			if (this.map.size() == map.size()) {
				for (final Map.Entry<?, ?> entry : map.entrySet()) {
					if (!this.map.containsEntry(entry.getKey(), entry.getValue())) {
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

		if (this.map.size() > 0) {
			int i = 0;

			for (final MapEntry<K, V> entry : this.map) {
				if (i++ > 0) {
					extractor.addSeparator();
				}

				extractor.append(entry.key).append('=').append(entry.value);
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
		return this.map.get(key, clazz);
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
		return this.map.get(key, defaultVal);
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
		return this.map.get(key);
	}

	/**
	 * Returns the capacity of the map.
	 *
	 * @return the capacity of the map
	 */
	@Override
	public final int getCapacity() {
		return this.map.getCapacity();
	}

	/**
	 * Returns the size of the map, which is how many elements are actually in the map.
	 *
	 * @return the size of the map
	 */
	@Override
	public final int getSize() {
		return this.map.getSize();
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
		return this.map.isEmpty();
	}

	/**
	 * Returns an {@link Itemizer} for the map.
	 *
	 * @return an {@link Itemizer} for the map
	 */
	@Override
	public final Itemizer<MapEntry<K, V>> iterator() {
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
		return this.map.put(key, value);
	}

	/**
	 * Puts all of the entries in the {@link Map} into this map.
	 *
	 * @param map
	 *            the {@link Map} to put into this map
	 */
	@Override
	public final void putAll(final Map<? extends K, ? extends V> map) {
		this.map.putAll(map);
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
		return this.map.remove(key);
	}

	/**
	 * Returns the size of the map, which is how many elements are actually in the map.
	 *
	 * @return the size of the map
	 */
	@Override
	public final int size() {
		return this.map.size();
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
		final StringExtractor extractor = new StringExtractor(this.map.size() << 4);
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
		return this.map.values();
	}

} // End MapExtractable
