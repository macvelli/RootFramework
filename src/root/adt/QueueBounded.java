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

import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.util.Root;

/**
 * TODO: Add final designation to every non-private method (done)<br>
 * TODO: Add final designation to every method argument that is not used as a local variable (done) <br>
 * TODO: Remove @Override annotations from Queue<T> methods (done)<br>
 * TODO: Implement Cloneable in all data structures!!<br>
 * TODO: Create unit test and ensure all methods and corner cases within each method are tested<br>
 * TODO: Check http://math.hws.edu/eck/cs124/javanotes3/c11/s3.html out for some ideas<br>
 * TODO: http://en.wikipedia.org/wiki/Queue_(data_structure)<br>
 * TODO: http://en.wikipedia.org/wiki/Circular_buffer<br>
 * TODO: Make a BoundedQueue implementation as a circular buffer. The difference is that the buffer is fixed in size<br>
 * TODO: Maybe data structures like this one could benefit from a getCollection() method since they are not in fact Collections? Don't know if that is
 * any better than just making RootQueue implement the Collection interface
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the queue
 */
public class QueueBounded<T> implements RootQueue<T> {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 *
	 * @param <T>
	 *            the type of elements in this queue
	 */
	private final class Ascend implements Itemizer<T> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private int cursor, index;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private Ascend() {
			this.reset();
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final int getIndex() {
			return this.index - 1;
		}

		@Override
		public final int getSize() {
			return QueueBounded.this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.index < QueueBounded.this.size;
		}

		@Override
		public final Itemizer<T> iterator() {
			return this;
		}

		@Override
		public final T next() {
			if (this.index == QueueBounded.this.size) {
				throw new NoSuchElementException();
			}

			final T obj = QueueBounded.this.queue[this.cursor];
			this.cursor = QueueBounded.this.inc(this.cursor);
			this.index++;

			return obj;
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public final void reset() {
			this.cursor = QueueBounded.this.head;
			this.index = 0;
		}

	} // End Ascend

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int head;
	private int tail;
	private int size;
	private final T[] queue;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public QueueBounded(final int capacity) {
		this.queue = Root.newArray(capacity);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void clear() {
		for (int j = 0; j < this.size; this.incHead(), j++) {
			this.queue[this.head] = null;
		}

		this.head = this.tail = this.size = 0;
	}

	@Override
	public final T dequeue() {
		if (this.size == 0) {
			throw new DataStructureEmptyException();
		}

		final T obj = this.queue[this.head];
		this.queue[this.head] = null;
		this.incHead();
		this.size--;

		return obj;
	}

	@Override
	public final void enqueue(final T t) {
		if (this.size == this.queue.length) {
			throw new DataStructureFullException();
		}

		this.queue[this.tail] = t;
		this.incTail();
		this.size++;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append('[');

		for (int i = this.head, j = 0; j < this.size; i = this.inc(i), j++) {
			if (j > 0) {
				extractor.addSeparator();
			}

			extractor.append(this.queue[i]);
		}

		extractor.append(']');
	}

	public final int getCapacity() {
		return this.queue.length - this.size;
	}

	@Override
	public final int getSize() {
		return this.size;
	}

	@Override
	public final boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public final boolean isFull() {
		return this.size == this.queue.length;
	}

	@Override
	public final Itemizer<T> iterator() {
		return new Ascend();
	}

	@Override
	public final T peek() {
		return this.queue[this.head];
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(this.size << 4);
		this.extract(extractor);
		return extractor.toString();
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private int inc(int i) {
		return ++i == this.queue.length ? 0 : i;
	}

	private void incHead() {
		if (++this.head == this.queue.length) {
			this.head = 0;
		}
	}

	private void incTail() {
		if (++this.tail == this.queue.length) {
			this.tail = 0;
		}
	}

} // End QueueBounded
