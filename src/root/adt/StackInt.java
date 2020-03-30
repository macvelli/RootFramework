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

import java.util.NoSuchElementException;

import root.lang.Itemizable;
import root.lang.Itemizer;
import root.util.Root;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class StackInt implements Itemizable<Integer> {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	private final class Iterator implements Itemizer<Integer> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private int index;
		private int cursor;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private Iterator() {
			this.index = -1;
			this.cursor = StackInt.this.head;
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final int getIndex() {
			return this.index;
		}

		@Override
		public final int getSize() {
			return StackInt.this.head + 1;
		}

		@Override
		public final boolean hasNext() {
			return this.cursor >= 0;
		}

		@Override
		public final Itemizer<Integer> iterator() {
			return this;
		}

		@Override
		public final Integer next() {
			if (this.cursor < 0) {
				throw new NoSuchElementException();
			}

			this.index++;

			return Integer.valueOf(StackInt.this.values[this.cursor--]);
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public final void reset() {
			this.index = -1;
			this.cursor = StackInt.this.head;
		}

	} // End Iterator

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int head;
	private int[] values;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public StackInt() {
		this.head = -1;
		this.values = new int[8];
	}

	public StackInt(final int capacity) {
		this.head = -1;
		this.values = new int[Root.max(8, capacity)];
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void clear() {
		this.head = -1;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (Root.equalToClass(obj, StackInt.class)) {
			final StackInt other = (StackInt) obj;

			if (this.head == other.head) {
				for (int i = this.head; i >= 0; i--) {
					if (this.values[i] != other.values[i]) {
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public final int getSize() {
		return this.head + 1;
	}

	@Override
	public final int hashCode() {
		int h = this.head;

		for (int i = this.head; i >= 0; i--) {
			h <<= 1;
			h ^= this.values[i];
		}

		return h;
	}

	@Override
	public final boolean isEmpty() {
		return this.head < 0;
	}

	@Override
	public final Itemizer<Integer> iterator() {
		return new Iterator();
	}

	public final int peek() {
		return this.head < 0 ? 0 : this.values[this.head];
	}

	public final int pop() {
		if (this.head < 0) {
			throw new DataStructureEmptyException();
		}

		return this.values[this.head--];
	}

	public final void push(final int item) {
		if (++this.head == this.values.length) {
			this.resize();
		}

		this.values[this.head] = item;
	}

	// <><><><><><><><><><><><><><> Private Methods ><><><><><><><><><><><><><>

	private void resize() {
		final int[] intArray = new int[this.head + (this.head >> 1)];
		System.arraycopy(this.values, 0, intArray, 0, this.head);
		this.values = intArray;
	}

} // End StackInt
