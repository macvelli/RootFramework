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

import root.util.Root;

/**
 * The map entry class used internally for classes such as {@link MapHashed}. This implements the {@link Map.Entry} interface so that it may be used
 * in places where objects implementing this interface are exposed to the outside world.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <K>
 *            the {@link MapEntry} key type
 * @param <V>
 *            the {@link MapEntry} value type
 */
public final class MapEntry<K, V> implements java.util.Map.Entry<K, V> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	final K key;
	V value;
	final int hash;
	MapEntry<K, V> next;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public MapEntry(final K key, final V value) {
		this.key = key;
		this.value = value;
		this.hash = key.hashCode();
	}

	MapEntry(final K key, final V value, final int h, final MapEntry<K, V> next) {
		this.key = key;
		this.value = value;
		this.hash = h;
		this.next = next;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final boolean equals(final Object obj) {
		if (Root.equalToClass(obj, MapEntry.class)) {
			final MapEntry<?, ?> entry = (MapEntry<?, ?>) obj;

			return this.key.equals(entry.key) && this.value.equals(entry.value);
		} else if (Root.equalToClass(obj, Map.Entry.class)) {
			final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;

			return this.key.equals(entry.getKey()) && this.value.equals(entry.getValue());
		}

		return false;
	}

	@Override
	public final K getKey() {
		return this.key;
	}

	@Override
	public final V getValue() {
		return this.value;
	}

	@Override
	public final int hashCode() {
		return this.hash;
	}

	@Override
	public final V setValue(final V value) {
		final V oldValue = this.value;
		this.value = value;
		return oldValue;
	}

} // End MapEntry
