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
import root.lang.StringExtractor;
import root.util.Root;

/**
 * <ul>
 * <li>TODO: Add final designation to every non-private method (done)</li>
 * <li>TODO: Add final designation to every method argument that is not used as a local variable (done)</li>
 * <li>TODO: Remove @Override annotations from Stack<T> methods (done)</li>
 * <li>TODO: Implement Cloneable in all data structures!!</li>
 * <li>TODO: Create unit test and ensure all methods and corner cases within each method are tested</li>
 * <li>TODO: Check http://math.hws.edu/eck/cs124/javanotes3/c11/s3.html out for some ideas</li>
 * </ul>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the stack
 */
public class StackArray<T> implements RootStack<T> {

	// <><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><><>

	private final class Ascend implements Itemizer<T> {

		private int i = StackArray.this.head;

		@Override
		public final int getIndex() {
			throw new UnsupportedOperationException();
		}

		@Override
		public final int getSize() {
			return StackArray.this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.i < StackArray.this.values.length;
		}

		@Override
		public final Itemizer<T> iterator() {
			return this;
		}

		@Override
		public final T next() {
			if (this.i == StackArray.this.values.length) {
				throw new NoSuchElementException();
			}

			return StackArray.this.values[this.i++];
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public final void reset() {
			this.i = StackArray.this.head;
		}

	} // End Ascend

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	protected int head;
	protected int size;

	protected T[] values;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public StackArray() {
		this.head = 8;
		this.values = Root.newArray(8);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public StackArray(final int capacity) {
		this.head = Root.max(capacity, 8);
		this.values = Root.newArray(this.head);
	}

	public StackArray(final Itemizable<? extends T> c) {
		this.head = Root.max(c.getSize(), 8);
		this.values = Root.newArray(this.head);

		for (final T t : c) {
			this.values[--this.head] = t;
		}
		this.size = c.getSize();
	}

	@Override
	public final void clear() {
		for (int i = this.head; i < this.values.length; i++) {
			this.values[i] = null;
		}

		this.head = this.values.length;
		this.size = 0;
	}

	@Override
	public final boolean equals(final Object o) {
		if (o == null || !(o instanceof Iterable)) {
			return false;
		}

		final Iterable<?> i = (Iterable<?>) o;

		int index = this.head;
		for (final Object t : i) {
			if (index == this.values.length) {
				return false;
			}

			if (Root.notEqual(this.values[index], t)) {
				return false;
			}

			index++;
		}

		return index == this.values.length;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append(this.values, this.head, this.size);
	}

	@Override
	public final int getSize() {
		return this.size;
	}

	@Override
	public final int hashCode() {
		// TODO: Use the Safe.hashCode() method instead
		int h = this.size;
		for (int i = this.head; i < this.values.length; i++) {
			if (this.values[i] != null) {
				h ^= this.values[i].hashCode();
			}
			h <<= 1;
		}

		return h;
	}

	@Override
	public final boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public final Itemizer<T> iterator() {
		return new Ascend();
	}

	@Override
	public final T peek() {
		return this.size == 0 ? null : this.values[this.head];
	}

	@Override
	public final T pop() {
		if (this.size == 0) {
			throw new NoSuchElementException();
		}

		final T t = this.values[this.head];
		this.values[this.head++] = null;
		this.size--;

		return t;
	}

	@Override
	public final void push(final T item) {
		if (this.head == 0) {
			// resize
			final T[] a = Root.newArray(this.values.length << 1);
			this.head = a.length - this.values.length;
			System.arraycopy(this.values, 0, a, this.head, this.values.length);
			this.values = a;
		}

		this.values[--this.head] = item;
		this.size++;
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(this.size << 4);
		this.extract(extractor);
		return extractor.toString();
	}

} // End StackArray
