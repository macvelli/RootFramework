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

import java.util.Collection;
import java.util.NoSuchElementException;

import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.util.Root;

/**
 * Basic node class for singly-linked data structures.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements contained within the node
 */
class Node<T> {

	// <><><><><><><><><><><><><>< Package Classes ><><><><><><><><><><><><><>

	/**
	 * An {@link Itemizer} for any {@link Node}-based structure.
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	static final class Iterator<T> implements Itemizer<T> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private Node<T> cursor;
		private int index;

		private final Node<T> head;
		private final int size;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		Iterator(final Node<T> head, final int size) {
			this.cursor = head;
			this.head = head;
			this.index = -1;
			this.size = size;
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final int getIndex() {
			return this.index;
		}

		@Override
		public final int getSize() {
			return this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.cursor != null;
		}

		@Override
		public final Itemizer<T> iterator() {
			return this;
		}

		@Override
		public final T next() {
			if (this.cursor == null) {
				throw new NoSuchElementException();
			}

			final T t = this.cursor.data;
			this.cursor = this.cursor.next;
			this.index++;

			return t;
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public final void reset() {
			this.cursor = this.head;
			this.index = -1;
		}

	} // End Iterator

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	static final void clear(Node<?> head) {
		for (Node<?> next; head != null; head = next) {
			next = head.next;
			head.data = null;
			head.next = null;
		}
	}

	static final boolean equals(final Object param, final Node<?> head, final int size) {
		if (param != null && param instanceof Collection) {
			final Collection<?> collection = (Collection<?>) param;

			if (size == collection.size()) {
				Node<?> n = head;

				for (final Object obj : collection) {
					if (Root.notEqual(n.data, obj)) {
						return false;
					}

					n = n.next;
				}

				return true;
			}
		}

		return false;
	}

	static final void extract(final StringExtractor extractor, final Node<?> head) {
		extractor.append('[');
		int i = 0;

		for (Node<?> n = head; n != null; n = n.next) {
			if (i++ > 0) {
				extractor.addSeparator();
			}

			extractor.append(n.data);
		}

		extractor.append(']');
	}

	static final int hashCode(final Node<?> head, final int size) {
		int h = size;
		for (Node<?> n = head; n != null; n = n.next) {
			h <<= 1;

			if (n.data != null) {
				if (n.hash == 0) {
					n.hash = n.data.hashCode();
				}

				h ^= n.hash;
			}
		}

		return h;
	}

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	T data;
	Node<T> next;
	int hash;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	Node(final T data) {
		this.data = data;
	}

} // End Node
