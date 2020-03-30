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

import root.lang.Extractable;
import root.lang.Itemizable;
import root.lang.Itemizer;
import root.lang.StringExtractor;

/**
 * The quintessential First-In-First-Out (FIFO) abstract data type. Objects are appended to the tail of the queue with an {@link #enqueue(Object)}
 * operation, while objects are removed from the head of the queue with a {@link #dequeue()} operation.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the queue
 */
public interface RootQueue<T> extends Itemizable<T>, Extractable {

	/**
	 * @see root.lang.Itemizable#clear()
	 */
	@Override
	void clear();

	/**
	 * Retrieves and removes the head of the queue, or returns <code>null</code> if the queue is empty.
	 *
	 * @return the head of the queue, or <code>null</code> if the queue is empty
	 */
	T dequeue();

	/**
	 * Appends the object to the tail of the queue, if the queue is not already full. If the queue is full, a {@link DataStructureFullException} will
	 * be thrown.
	 *
	 * @param t
	 *            The object to append to the tail of the queue
	 */
	void enqueue(T t);

	/**
	 * @see root.lang.Extractable#extract(StringExtractor)
	 */
	@Override
	void extract(StringExtractor extractor);

	/**
	 * @see root.lang.Itemizable#getSize()
	 */
	@Override
	int getSize();

	/**
	 * @see root.lang.Itemizable#isEmpty()
	 */
	@Override
	boolean isEmpty();

	/**
	 * Returns <code>true</code> if the queue is full, <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if the queue is full, <code>false</code> otherwise
	 */
	boolean isFull();

	/**
	 * @see root.lang.Itemizable#iterator()
	 */
	@Override
	Itemizer<T> iterator();

	/**
	 * Retrieves, but does not remove, the head of the queue. Or returns <code>null</code> if the queue is empty.
	 *
	 * @return the head of the queue, or <code>null</code> if the queue is empty
	 */
	T peek();

} // End RootQueue
