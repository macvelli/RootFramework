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
package root.cache;

import root.util.Root;

/**
 * Package class used to manage a single cache entry within a cache implementation.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <K>
 *            The key type of the cache entry
 * @param <V>
 *            The value type of the cache entry
 */
final class CacheEntry<K, V> {

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	@SuppressWarnings("unchecked")
	static final <K, V> CacheEntry<K, V>[] newArray(final int capacity) {
		return new CacheEntry[Root.calculateHashTableSize(capacity)];
	}

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	K key;
	V value;
	int index;
	CacheEntry<K, V> listNext;
	CacheEntry<K, V> listPrev;
	CacheEntry<K, V> mapNext;
	CacheEntry<K, V> mapPrev;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	CacheEntry(final K key, final V value, final int index, final CacheEntry<K, V> mapHead) {
		this.key = key;
		this.value = value;
		this.index = index;
		this.mapNext = mapHead;
		this.mapPrev = null;

		if (mapHead != null) {
			mapHead.mapPrev = this;
		}
	}

	// <><><><><><><><><><><><><><> Package Methods ><><><><><><><><><><><><><>

	/**
	 *
	 * @param key
	 * @param value
	 * @param index
	 * @param mapHead
	 * @return <code>this</code> {@link CacheEntry} reference with the updated attributes
	 */
	final CacheEntry<K, V> recycle(final K key, final V value, final int index, final CacheEntry<K, V> mapHead) {
		this.key = key;
		this.value = value;
		this.index = index;
		this.mapNext = mapHead;
		this.mapPrev = null;

		if (mapHead != null) {
			mapHead.mapPrev = this;
		}

		return this;
	}

} // End CacheEntry
