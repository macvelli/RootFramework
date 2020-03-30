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

import root.lang.Itemizer;
import root.util.Root;

/**
 * TODO: How should a cache entry be invalidated, or marked dirty, when an update is performed?<br>
 * TODO: Think about a cache.update() method and what that should do<br>
 * TODO: Also think about an CacheLRU<K, List<V>> implementation that caches lists of data (or Sets for that matter)<br>
 * TODO: What about cache synchronization across servers?<br>
 * TODO: Test all of the remove() methods for correctness (do this for every Cache class)
 * <p>
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
public final class CacheLRU<K, V> implements RootCache<K, V> {

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
	private final class Ascend implements Itemizer<V> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private int index;
		private CacheEntry<K, V> cursor;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private Ascend() {
			this.reset();
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		/**
		 * @see root.lang.Itemizer#getIndex()
		 */
		@Override
		public final int getIndex() {
			return this.index;
		}

		/**
		 * @see root.lang.Itemizer#getSize()
		 */
		@Override
		public final int getSize() {
			return CacheLRU.this.size;
		}

		/**
		 * @see root.lang.Itemizer#hasNext()
		 */
		@Override
		public final boolean hasNext() {
			return this.cursor != null;
		}

		/**
		 * @see root.lang.Itemizer#iterator()
		 */
		@Override
		public final Itemizer<V> iterator() {
			return this;
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
			this.cursor = CacheLRU.this.listHead;
		}

	} // End Ascend

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private CacheEntry<K, V> listHead;
	private CacheEntry<K, V> listTail;

	private int size;
	private final int capacity;

	private final CacheEntry<K, V>[] cache;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public CacheLRU(final int capacity) {
		this.capacity = Root.calculateHashTableCapacity(capacity);
		this.cache = CacheEntry.newArray(this.capacity);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 *
	 *
	 * @see root.cache.RootCache#clear()
	 */
	@Override
	public final void clear() {
		if (this.size != 0) {
			CacheEntry<K, V> e, next;

			// 1. Clear all entries from the cache
			for (int i = 0; i < this.cache.length; i++) {
				for (e = this.cache[i]; e != null; e = next) {
					next = e.mapNext;
					e.key = null;
					e.value = null;
					e.listNext = null;
					e.listPrev = null;
					e.mapNext = null;
					e.mapPrev = null;
				}

				this.cache[i] = null;
			}

			// 2. Reset the listHead and listTail
			this.listHead = null;
			this.listTail = null;

			// 3. Reset the size to zero
			this.size = 0;
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
		final int i = Root.hashCode(key) % this.cache.length;

		for (CacheEntry<K, V> e = this.cache[i]; e != null; e = e.mapNext) {
			if (Root.equals(e.key, key)) {
				// 1. Move the cached item to the tail of the list
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

				// 2. Return cached value
				return e.value;
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
	public final Itemizer<V> iterator() {
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
		CacheEntry<K, V> e = this.cache[i];

		// 1. Check to see if the key is already mapped to the cache
		for (; e != null; e = e.mapNext) {
			if (Root.equals(e.key, key)) {
				final V oldValue = e.value;
				e.value = value;
				return oldValue;
			}
		}

		// 2. Recycle oldest cached item object if cache is full
		if (this.size == this.capacity) {
			// a) Remove the oldest CachedItem from both the list and the cache
			final CacheEntry<K, V> oldestItem = this.listHead;
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

			this.cache[i] = oldestItem.recycle(key, value, i, this.cache[i]);

			// c) Attach recycled item to the tail of the list
			this.listTail.listNext = oldestItem;
			oldestItem.listPrev = this.listTail;
			oldestItem.listNext = null;
			this.listTail = oldestItem;

			// d) Return old cached value
			return oldValue;
		}

		// 3. Otherwise create new cache item and append it to the list tail
		e = this.cache[i] = new CacheEntry<K, V>(key, value, i, this.cache[i]);

		if (this.listTail == null) {
			this.listHead = e;
		} else {
			this.listTail.listNext = e;
			e.listPrev = this.listTail;
		}

		this.listTail = e;
		this.size++;

		return null;
	}

	/**
	 *
	 *
	 * @param key
	 * @return
	 *
	 * @see root.cache.RootCache#remove(Object)
	 */
	@Override
	public final V remove(final K key) {
		if (this.size != 0) {
			final int i = Root.hashCode(key) % this.cache.length;
			CacheEntry<K, V> foundItem;

			// 1. Find the CachedItem associated with the key
			for (foundItem = this.cache[i]; foundItem != null; foundItem = foundItem.mapNext) {
				if (Root.equals(foundItem.key, key)) {
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

					// 4. Decrement the size by one
					this.size--;

					// 5. Return old cached value
					return oldValue;
				}
			}
		}

		return null;
	}

	/**
	 *
	 *
	 * @return
	 *
	 * @see root.cache.RootCache#remove()
	 */
	@Override
	public final V removeNext() {
		if (this.size != 0) {
			// 1. Remove the oldest CachedItem from both the list and the cache
			final CacheEntry<K, V> oldestItem = this.listHead;

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

			// 3. Decrement the size by one
			this.size--;

			// 4. Return old cached value
			return oldValue;
		}

		return null;
	}

} // End CacheLRU
