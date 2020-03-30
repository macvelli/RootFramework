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
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class CacheFixed<V> {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	private final class Iterator implements Itemizer<V> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private int index;

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final int getIndex() {
			return this.index - 1;
		}

		@Override
		public final int getSize() {
			return CacheFixed.this.cache.length;
		}

		@Override
		public final boolean hasNext() {
			return this.index < CacheFixed.this.cache.length;
		}

		@Override
		public final Itemizer<V> iterator() {
			return this;
		}

		@Override
		public final V next() {
			if (this.index == CacheFixed.this.cache.length) {
				throw new NoSuchElementException();
			}

			return CacheFixed.this.cache[this.index++];
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public final void reset() {
			this.index = 0;
		}

	} // End Iterator

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final V[] cache;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public CacheFixed(final int capacity) {
		this.cache = Root.newArray(capacity);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final V get(final int key) {
		if (key < this.cache.length) {
			return this.cache[key];
		}

		return null;
	}

	public final int getCapacity() {
		return this.cache.length;
	}

	public final int getSize() {
		return this.cache.length;
	}

	public final boolean isEmpty() {
		return false;
	}

	public final Itemizer<V> iterator() {
		return new Iterator();
	}

	public final V put(final int key, final V value) {
		if (key < this.cache.length) {
			final V oldValue = this.cache[key];
			this.cache[key] = value;
			return oldValue;
		}

		return null;
	}

} // End CacheFixed
