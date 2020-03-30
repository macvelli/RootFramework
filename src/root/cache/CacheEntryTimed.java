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

import root.clock.Timer;
import root.util.Root;

/**
 * Package class used to manage a single cache entry with expiration behavior within an expiring cache implementation.
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
final class CacheEntryTimed<K, V> {

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	@SuppressWarnings("unchecked")
	static final <K, V> CacheEntryTimed<K, V>[] newArray(final int capacity) {
		return new CacheEntryTimed[Root.calculateHashTableSize(capacity)];
	}

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	K key;
	V value;
	int index;
	CacheEntryTimed<K, V> listNext;
	CacheEntryTimed<K, V> listPrev;
	CacheEntryTimed<K, V> mapNext;
	CacheEntryTimed<K, V> mapPrev;

	final Timer itemTimer;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	CacheEntryTimed(final K key, final V value, final int index, final long expiry, final CacheEntryTimed<K, V> mapHead) {
		this.key = key;
		this.value = value;
		this.index = index;
		this.mapNext = mapHead;
		this.mapPrev = null;
		this.itemTimer = new Timer(expiry);

		if (mapHead != null) {
			mapHead.mapPrev = this;
		}
	}

	// <><><><><><><><><><><><><><> Package Methods ><><><><><><><><><><><><><>

	/**
	 *
	 *
	 * @param key
	 * @param value
	 * @param index
	 * @param currentTime
	 * @param mapHead
	 * @return
	 */
	final CacheEntryTimed<K, V> recycle(final K key, final V value, final int index, final long currentTime, final CacheEntryTimed<K, V> mapHead) {
		this.key = key;
		this.value = value;
		this.index = index;
		this.mapNext = mapHead;
		this.mapPrev = null;
		this.itemTimer.reset(currentTime);

		if (mapHead != null) {
			mapHead.mapPrev = this;
		}

		return this;
	}

} // End CacheEntryTimed
