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

import java.util.Map;

import root.annotation.Builder;

/**
 * A {@link root.lang.Builder} implementation for a {@link MapHashed} instance.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param K
 *            The key type of the map
 * @param V
 *            The value type of the map
 */
@Builder
public final class MapBuilder<K, V> implements root.lang.Builder {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	final MapHashed<K, V> map;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor.
	 */
	public MapBuilder() {
		this.map = new MapHashed<>();
	}

	/**
	 * A constructor that accepts a predetermined capacity.
	 *
	 * @param capacity
	 *            the predetermined capacity
	 */
	public MapBuilder(final int capacity) {
		this.map = new MapHashed<>(capacity);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final MapHashed<K, V> build() {
		return this.map;
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final boolean equals(final Object param) {
		throw new UnsupportedOperationException("Cannot test for equals(Object) with a MapBuilder");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final int hashCode() {
		throw new UnsupportedOperationException("Cannot call hashCode() with a MapBuilder");
	}

	/**
	 * Puts the {@code (key, value)} mapping into the map builder.
	 *
	 * @param key
	 *            the key of the entry
	 * @param value
	 *            the value of the entry
	 * @return the {@link MapBuilder} instance
	 */
	public final MapBuilder<K, V> put(final K key, final V value) {
		this.map.put(key, value);

		return this;
	}

	/**
	 * Puts all of the entries in the {@link Map} into the map builder.
	 *
	 * @param map
	 *            the {@link Map} to put into the map builder
	 * @return the {@link MapBuilder} instance
	 */
	public final MapBuilder<K, V> putAll(final Map<? extends K, ? extends V> map) {
		this.map.putAll(map);

		return this;
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final String toString() {
		throw new UnsupportedOperationException("Cannot call toString() with a MapBuilder");
	}

} // End MapBuilder
