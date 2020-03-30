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
package root.lang;

import java.util.ListIterator;

/**
 * Extends the {@link Itemizer} concept to the {@link ListIterator}.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements returned by this {@link ListItemizer}
 */
public interface ListItemizer<T> extends Itemizer<T>, ListIterator<T> {

	/**
	 * @see java.util.ListIterator#add(Object)
	 */
	@Override
	void add(T t);

	/**
	 * @see root.lang.Itemizer#getIndex()
	 */
	@Override
	int getIndex();

	/**
	 * @see root.lang.Itemizer#getSize()
	 */
	@Override
	int getSize();

	/**
	 * @see java.util.ListIterator#hasNext()
	 * @see root.lang.Itemizer#hasNext()
	 */
	@Override
	boolean hasNext();

	/**
	 * @see java.util.ListIterator#hasPrevious()
	 */
	@Override
	boolean hasPrevious();

	/**
	 * @see root.lang.Itemizer#iterator()
	 */
	@Override
	ListItemizer<T> iterator();

	/**
	 * @see java.util.ListIterator#next()
	 * @see root.lang.Itemizer#next()
	 */
	@Override
	T next();

	/**
	 * @see java.util.ListIterator#nextIndex()
	 */
	@Override
	int nextIndex();

	/**
	 * @see java.util.ListIterator#previous()
	 */
	@Override
	T previous();

	/**
	 * @see java.util.ListIterator#previousIndex()
	 */
	@Override
	int previousIndex();

	/**
	 * @see java.util.ListIterator#remove()
	 * @see root.lang.Itemizer#remove()
	 */
	@Override
	void remove();

	/**
	 * @see root.lang.Itemizer#reset()
	 */
	@Override
	void reset();

	/**
	 * @see java.util.ListIterator#set(Object)
	 */
	@Override
	void set(T t);

} // End ListItemizer
