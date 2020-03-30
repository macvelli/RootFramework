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

import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;

import root.adt.QueueBounded;
import root.lang.ConcurrentItemizer;
import root.lang.Itemizer;
import root.util.Root;

/**
 * TODO: http://stackoverflow.com/questions/3802370/java-time-based-map-cache-with-expiring-keys<br>
 * TODO: Need a CacheTimedConcurrent and a CacheTimedMultiKey implementation<br>
 * TODO: Need a Duration class so that I can say new CacheTimed(10, Duration.minutes(10));<br>
 * TODO: Test all of the remove() methods for correctness (do this for every Cache class)
 * <p>
 * This cache class is a simple implementation of an expiring item cache. There are no background threads or other nonsense used to manage expired
 * cache entries. The expiring cache uses {@link root.clock.Timer} to determine when cached entries have expired.
 * <p>
 * The algorithm used to manage cache entries is as follows:
 * <ul>
 * <li>Every time {@link #get(K)} is called, the oldest item in the cache is checked to see if it has expired
 * <ul>
 * <li>If the oldest item in the cache has expired, maintenance is performed to remove all expired entries from the cache</li>
 * </ul>
 * </li>
 * <li>If an existing cached item is updated during a {@link #put(K, V)}, its timer is reset and it becomes the newest item in the cache</li>
 * <li>If a new item is being cached during a {@link #put(K, V)} and the cache is full, LRU-based behavior is taken where the oldest cached item
 * (regardless of whether it has expired or not) is removed from the cache to make room for the new item</li>
 * </ul>
 * Because there will be fluctuations in the number of cached entries over time, an internal pool is used to capture expired
 * {@link root.cache.CacheEntryTimed} objects so that they may be reused when new entries are {@link #put(K, V)} into the cache. This reduces the
 * strain the cache puts onto the garbage collector during runtime.
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
public class CacheTimedConcurrent<K, V> implements RootCache<K, V> {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 *
	 * @param <V>
	 *            The value type of the cache entry
	 */
	private final class Ascend implements ConcurrentItemizer<V> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private int index;
		private CacheEntryTimed<K, V> cursor;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private Ascend() {
			this.reset();
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final int getIndex() {
			return this.index;
		}

		@Override
		public final int getSize() {
			return CacheTimedConcurrent.this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.cursor != null;
		}

		@Override
		public final Itemizer<V> iterator() {
			return this;
		}

		@Override
		public final void lock() {
			CacheTimedConcurrent.this.cacheLock.lock();
		}

		@Override
		public final V next() {
			if (this.cursor == null) {
				throw new NoSuchElementException();
			}

			final V v = this.cursor.value;
			this.cursor = this.cursor.listNext;
			this.index++;

			return v;
		}

		/**
		 * @throws UnsupportedOperationException
		 */
		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public final void reset() {
			this.index = -1;
			this.cursor = CacheTimedConcurrent.this.listHead;
		}

		@Override
		public final void unlock() {
			CacheTimedConcurrent.this.cacheLock.unlock();
		}

	} // End Ascend

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private CacheEntryTimed<K, V> listHead;
	private CacheEntryTimed<K, V> listTail;

	private int size;
	private final int capacity;
	private final long expireDuration;
	private final CacheEntryTimed<K, V>[] cache;

	private final QueueBounded<CacheEntryTimed<K, V>> cacheEntryPool;
	private final ReentrantLock cacheLock;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public CacheTimedConcurrent(final int capacity, final long expireDuration) {
		this.capacity = Root.calculateHashTableCapacity(capacity);
		this.cache = CacheEntryTimed.newArray(this.capacity);
		this.expireDuration = expireDuration;
		this.cacheEntryPool = new QueueBounded<>(this.capacity);
		this.cacheLock = new ReentrantLock();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 *
	 *
	 * @see root.cache.RootCache#clear()
	 */
	@Override
	public final void clear() {
		this.cacheLock.lock();

		try {
			if (this.size != 0) {
				CacheEntryTimed<K, V> e, next;

				// 1. Clear all entries from the cache
				for (int i = 0; i < this.cache.length; i++) {
					for (e = this.cache[i]; e != null; e = next) {
						// a) Clean up the CacheEntryTimed for GC
						next = e.mapNext;
						e.key = null;
						e.value = null;
						e.listNext = null;
						e.listPrev = null;
						e.mapNext = null;
						e.mapPrev = null;

						// b) Add the CacheEntryTimed to the cacheEntryPool
						this.cacheEntryPool.enqueue(e);
					}

					this.cache[i] = null;
				}

				// 2. Reset the listHead and listTail
				this.listHead = null;
				this.listTail = null;

				// 3. Reset the size to zero
				this.size = 0;
			}
		} finally {
			this.cacheLock.unlock();
		}
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 *
	 * @see root.cache.RootCache#get(K)
	 */
	@Override
	public final V get(final K key) {
		if (this.listHead != null) {
			final int i = Root.hashCode(key) % this.cache.length;
			final long currentTime = Root.systemTimePerSecond.currentTime;

			this.cacheLock.lock();

			try {
				if (this.listHead != null) {
					// 1. Purge all expired entries if the listHead has expired
					if (this.listHead.itemTimer.hasExpired(currentTime)) {
						this.purgeExpiredEntries(this.listHead.listNext, currentTime);
					}

					// 2. Look for the cached item
					for (CacheEntryTimed<K, V> e = this.cache[i]; e != null; e = e.mapNext) {
						if (Root.equals(e.key, key)) {
							// a) Return cached value
							return e.value;
						}
					}
				}
			} finally {
				this.cacheLock.unlock();
			}
		}

		return null;
	}

	/**
	 *
	 *
	 * @return
	 *
	 * @see root.cache.RootCache#getCapacity()
	 */
	@Override
	public final int getCapacity() {
		return this.capacity;
	}

	@Override
	public final int getSize() {
		return this.size;
	}

	@Override
	public final boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 *
	 *
	 * @return
	 *
	 * @see root.cache.RootCache#iterator()
	 */
	@Override
	public final ConcurrentItemizer<V> iterator() {
		return new Ascend();
	}

	/**
	 *
	 *
	 * @param key
	 * @param value
	 * @return
	 *
	 * @see root.cache.RootCache#put(K, V)
	 */
	@Override
	public final V put(final K key, final V value) {
		final int i = Root.hashCode(key) % this.cache.length;
		final long currentTime = Root.systemTimePerSecond.currentTime;

		this.cacheLock.lock();

		try {
			CacheEntryTimed<K, V> e = this.cache[i];

			// 1. Check to see if the key is already mapped to the cache
			for (; e != null; e = e.mapNext) {
				if (Root.equals(e.key, key)) {
					// a) Update cached value and move to the tail of the list
					final V oldValue = e.value;
					e.value = value;
					e.itemTimer.reset(currentTime);

					if (this.listTail != e) {
						if (this.listHead == e) {
							this.listHead = this.listHead.listNext;
							this.listHead.listPrev = null;
						} else {
							e.listPrev.listNext = e.listNext;
							e.listNext.listPrev = e.listPrev;
						}

						this.listTail.listNext = e;
						e.listPrev = this.listTail;
						e.listNext = null;
						this.listTail = e;
					}

					// c) Return the old cached value
					return oldValue;
				}
			}

			// 2. Recycle oldest cached item object if cache is full
			if (this.size == this.capacity) {
				// a) Remove the oldest CacheEntryTimed from both the list and cache
				final CacheEntryTimed<K, V> oldestItem = this.listHead;
				this.listHead = this.listHead.listNext;
				this.listHead.listPrev = null;

				if (oldestItem == this.cache[oldestItem.index]) {
					this.cache[oldestItem.index] = oldestItem.mapNext;
					if (oldestItem.mapNext != null) {
						oldestItem.mapNext.mapPrev = null;
					}
				} else {
					oldestItem.mapPrev.mapNext = oldestItem.mapNext;
					if (oldestItem.mapNext != null) {
						oldestItem.mapNext.mapPrev = oldestItem.mapPrev;
					}
				}

				// b) Reuse the oldest CachedItem for the new cache entry
				final V oldValue = oldestItem.value;

				this.cache[i] = oldestItem.recycle(key, value, i, currentTime, this.cache[i]);

				// c) Attach recycled item to the tail of the list
				this.listTail.listNext = oldestItem;
				oldestItem.listPrev = this.listTail;
				oldestItem.listNext = null;
				this.listTail = oldestItem;

				// d) Return old cached value
				return oldValue;
			}

			// 3. Otherwise create new/reuse existing cache item and append it to the list tail
			if (this.cacheEntryPool.isEmpty()) {
				e = this.cache[i] = new CacheEntryTimed<K, V>(key, value, i, this.expireDuration, this.cache[i]);
			} else {
				e = this.cacheEntryPool.dequeue();
				this.cache[i] = e.recycle(key, value, i, currentTime, this.cache[i]);
			}

			if (this.listTail == null) {
				this.listHead = e;
			} else {
				this.listTail.listNext = e;
				e.listPrev = this.listTail;
			}

			this.listTail = e;
			this.size++;
		} finally {
			this.cacheLock.unlock();
		}

		return null;
	}

	/**
	 *
	 * @param
	 * @return
	 * @see root.cache.RootCache#remove(Object)
	 */
	@Override
	public final V remove(final K key) {
		if (this.size != 0) {
			final int i = Root.hashCode(key) % this.cache.length;
			CacheEntryTimed<K, V> foundItem;

			this.cacheLock.lock();

			try {
				// 1. Find the CachedItem associated with the key
				for (foundItem = this.cache[i]; foundItem != null; foundItem = foundItem.mapNext) {
					if (Root.equals(foundItem.key, key)) {
						break;
					}
				}

				if (foundItem != null) {
					// 2. Remove the found item from the cache and list
					if (this.size == 1) {
						this.listHead = null;
						this.listTail = null;
						this.cache[i] = null;
					} else {
						if (this.listHead == foundItem) {
							this.listHead = this.listHead.listNext;
							this.listHead.listPrev = null;
						} else if (this.listTail == foundItem) {
							this.listTail = foundItem.listPrev;
							this.listTail.listNext = null;
						} else {
							foundItem.listPrev.listNext = foundItem.listNext;
							foundItem.listNext.listPrev = foundItem.listPrev;
						}

						if (foundItem == this.cache[i]) {
							this.cache[i] = foundItem.mapNext;
							if (foundItem.mapNext != null) {
								foundItem.mapNext.mapPrev = null;
							}
						} else {
							foundItem.mapPrev.mapNext = foundItem.mapNext;
							if (foundItem.mapNext != null) {
								foundItem.mapNext.mapPrev = foundItem.mapPrev;
							}
						}
					}

					// 3. Clean up foundItem for garbage collection purposes
					final V oldValue = foundItem.value;

					foundItem.key = null;
					foundItem.value = null;
					foundItem.listNext = null;
					foundItem.listPrev = null;
					foundItem.mapNext = null;
					foundItem.mapPrev = null;

					// 4. Add the foundItem to the cacheEntryPool
					this.cacheEntryPool.enqueue(foundItem);

					// 5. Decrement the size by one
					this.size--;

					// 6. Return old cached value
					return oldValue;
				}
			} finally {
				this.cacheLock.unlock();
			}
		}

		return null;
	}

	/**
	 *
	 * @return
	 * @see root.cache.RootCache#removeNext()
	 */
	@Override
	public final V removeNext() {
		this.cacheLock.lock();

		try {
			if (this.size != 0) {
				// 1. Remove the oldest CachedItem from both the list and the cache
				final CacheEntryTimed<K, V> oldestItem = this.listHead;

				if (this.size == 1) {
					this.listHead = null;
					this.listTail = null;
					this.cache[oldestItem.index] = null;
				} else {
					this.listHead = this.listHead.listNext;
					this.listHead.listPrev = null;

					if (oldestItem == this.cache[oldestItem.index]) {
						this.cache[oldestItem.index] = oldestItem.mapNext;
						if (oldestItem.mapNext != null) {
							oldestItem.mapNext.mapPrev = null;
						}
					} else {
						oldestItem.mapPrev.mapNext = oldestItem.mapNext;
						if (oldestItem.mapNext != null) {
							oldestItem.mapNext.mapPrev = oldestItem.mapPrev;
						}
					}
				}

				// 2. Clean up oldestItem for garbage collection purposes
				final V oldValue = oldestItem.value;

				oldestItem.key = null;
				oldestItem.value = null;
				oldestItem.listNext = null;
				oldestItem.listPrev = null;
				oldestItem.mapNext = null;
				oldestItem.mapPrev = null;

				// 3. Add the oldestItem to the cacheEntryPool
				this.cacheEntryPool.enqueue(oldestItem);

				// 4. Decrement the size by one
				this.size--;

				// 5. Return old cached value
				return oldValue;
			}
		} finally {
			this.cacheLock.unlock();
		}

		return null;
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private void purgeExpiredEntries(CacheEntryTimed<K, V> lastValidEntry, final long currentTime) {
		// 1. Find the last valid entry in the list
		for (; lastValidEntry != null && lastValidEntry.itemTimer.hasExpired(currentTime); lastValidEntry = lastValidEntry.listNext) {
			;
		}

		// 2. Manage the expired cache entries
		if (lastValidEntry == null) {
			// a) Just clear the entire cache since there are no valid entries
			this.clear();
		} else {
			CacheEntryTimed<K, V> e, next;

			// b) Purge all of the expired entries from listHead to lastValidEntry
			for (e = this.listHead; e != lastValidEntry; e = next) {
				next = e.listNext;

				// Remove the CacheEntryTimed from the cache
				if (e == this.cache[e.index]) {
					this.cache[e.index] = e.mapNext;
					if (e.mapNext != null) {
						e.mapNext.mapPrev = null;
					}
				} else {
					e.mapPrev.mapNext = e.mapNext;
					if (e.mapNext != null) {
						e.mapNext.mapPrev = e.mapPrev;
					}
				}

				// Clean up the CacheEntryTimed for GC
				e.key = null;
				e.value = null;
				e.listNext = null;
				e.listPrev = null;
				e.mapNext = null;
				e.mapPrev = null;

				// Add the CacheEntryTimed to the cacheEntryPool
				this.cacheEntryPool.enqueue(e);

				// Decrement the size by one
				this.size--;
			}

			// c) Reset the listHead to lastValidEntry
			this.listHead = lastValidEntry;
			this.listHead.listPrev = null;
		}
	}

} // End CacheTimed
