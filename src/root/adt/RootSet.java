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

import root.lang.Itemizable;
import root.lang.Itemizer;

/**
 * A set is an abstract data type that stores objects in no particular order, and contains only unique objects.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the set
 */
public interface RootSet<T> extends java.util.Set<T>, Itemizable<T> {

	/**
	 * @see java.util.Set#add(Object)
	 */
	@Override
	boolean add(T t);

	/**
	 * @see java.util.Set#addAll(Collection)
	 */
	@Override
	boolean addAll(Collection<? extends T> collection);

	boolean addAll(Iterable<? extends T> iterable);

	boolean addAll(T[] array, int offset, int length);

	/**
	 * @see java.util.Set#clear()
	 */
	@Override
	void clear();

	/**
	 * @see java.util.Set#contains(Object)
	 */
	@Override
	boolean contains(Object obj);

	/**
	 * @see java.util.Set#containsAll(Collection)
	 */
	@Override
	boolean containsAll(Collection<?> collection);

	boolean containsAll(Iterable<? extends T> iterable);

	boolean containsAny(Iterable<? extends T> iterable);

	RootSet<T> difference(Iterable<? extends T> iterable);

	/**
	 * @see java.util.Set#equals(Object)
	 */
	@Override
	boolean equals(Object obj);

	T get(T t);

	/**
	 * @see root.lang.Itemizable#getSize()
	 */
	@Override
	int getSize();

	/**
	 * @see java.util.Set#hashCode()
	 */
	@Override
	int hashCode();

	RootSet<T> intersect(Iterable<? extends T> iterable);

	/**
	 * @see java.util.Set#isEmpty()
	 */
	@Override
	boolean isEmpty();

	/**
	 * @see root.lang.Itemizable#iterator()
	 */
	@Override
	Itemizer<T> iterator();

	/**
	 * @see java.util.Set#remove(Object)
	 */
	@Override
	boolean remove(Object obj);

	/**
	 * @see java.util.Set#removeAll(Collection)
	 */
	@Override
	boolean removeAll(Collection<?> collection);

	boolean replace(T oldObj, T newObj);

	/**
	 * @see java.util.Set#retainAll(Collection)
	 */
	@Override
	boolean retainAll(Collection<?> collection);

	/**
	 * @see java.util.Set#size()
	 */
	@Override
	int size();

	/**
	 * @see java.util.Set#toArray()
	 */
	@Override
	Object[] toArray();

	/**
	 * @see java.util.Set#toArray(Object[])
	 */
	@Override
	<E> E[] toArray(E[] arrayParam);

	RootList<T> toList();

	RootSet<T> union(Iterable<? extends T> iterable);

} // End RootSet
