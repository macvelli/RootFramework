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
public class StackLinked<T> implements RootStack<T> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int size;
	private Node<T> head;

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void clear() {
		Node.clear(this.head);

		this.head = null;
		this.size = 0;
	}

	@Override
	public final boolean equals(final Object param) {
		return Node.equals(param, this.head, this.size);
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		Node.extract(extractor, this.head);
	}

	@Override
	public final int getSize() {
		return this.size;
	}

	@Override
	public final int hashCode() {
		return Node.hashCode(this.head, this.size);
	}

	@Override
	public final boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public final Itemizer<T> iterator() {
		return new Node.Iterator<>(this.head, this.size);
	}

	@Override
	public final T peek() {
		return this.head == null ? null : this.head.data;
	}

	@Override
	public final T pop() {
		if (this.size == 0) {
			throw new NoSuchElementException();
		}

		final Node<T> n = this.head;
		this.head = this.head.next;

		final T t = n.data;
		n.data = null;
		n.next = null;
		this.size--;

		return t;
	}

	@Override
	public final void push(final T item) {
		final Node<T> node = new Node<T>(item);
		node.next = this.head;
		this.head = node;
		this.size++;
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(this.size << 4);
		this.extract(extractor);
		return extractor.toString();
	}

} // End StackLinked
