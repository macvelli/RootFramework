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
 * The quintessential Last-In-First-Out (LIFO) abstract data type. Objects are appended to the head of the stack with a {@link #push(Object)}
 * operation, while objects are removed from the head of the stack with a {@link #pop()} operation.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the stack
 */
public interface RootStack<T> extends Itemizable<T>, Extractable {

	/**
	 * @see root.lang.Itemizable#clear()
	 */
	@Override
	void clear();

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
	 * @see root.lang.Itemizable#iterator()
	 */
	@Override
	Itemizer<T> iterator();

	/**
	 * Retrieves, but does not remove, the head of the stack. Or returns <code>null</code> if the stack is empty.
	 *
	 * @return the head of the stack, or <code>null</code> if the stack is empty
	 */
	T peek();

	/**
	 * Retrieves and removes the head of the stack, or returns <code>null</code> if the stack is empty.
	 *
	 * @return the head of the stack, or <code>null</code> if the stack is empty
	 */
	T pop();

	/**
	 * Appends the object to the head of the stack.
	 *
	 * @param t
	 *            The object to append to the head of the stack
	 */
	void push(T t);

} // End RootStack
