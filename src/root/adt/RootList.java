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

import root.lang.Extractable;
import root.lang.Immutable;
import root.lang.Itemizable;
import root.lang.Itemizer;
import root.lang.ListItemizer;
import root.lang.StringExtractor;
import root.random.RNG;

/**
 * {@link RootList} is an interface that extends the {@link java.util.List}, {@link root.lang.Itemizable}, and {@link root.lang.Extractable}
 * interfaces in order to define additional methods that the standard JDK does not support.
 * <h2>Extended Interfaces</h2>
 * <ul>
 * <li>{@link java.util.List}</li>
 * <li>{@link root.lang.Itemizable}</li>
 * <li>{@link root.lang.Extractable}</li>
 * </ul>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the list
 */
public interface RootList<T> extends java.util.List<T>, Itemizable<T>, Extractable, Cloneable, Immutable {

	/**
	 * @see java.util.List#add(int, Object)
	 */
	@Override
	void add(int index, T obj);

	/**
	 * @see java.util.List#add(Object)
	 */
	@Override
	boolean add(T obj);

	/**
	 * @see java.util.List#addAll(Collection)
	 */
	@Override
	boolean addAll(Collection<? extends T> collection);

	/**
	 * @see java.util.List#addAll(int, Collection)
	 */
	@Override
	boolean addAll(int index, Collection<? extends T> collection);

	void addAll(T[] array, int offset, int length);

	/**
	 * @see java.util.List#clear()
	 */
	@Override
	void clear();

	/**
	 * @see Object#clone()
	 */
	RootList<T> clone();

	/**
	 * @see java.util.List#contains(Object)
	 */
	@Override
	boolean contains(Object obj);

	/**
	 * @see java.util.List#containsAll(Collection)
	 */
	@Override
	boolean containsAll(Collection<?> collection);

	/**
	 * Returns <code>true</code> if this list contains any of the elements of the specified {@link Collection}.
	 *
	 * @param collection
	 *            the {@link Collection} to check if any of its elements are contained within this list
	 * @return <code>true</code> if this list contains any of the elements of the specified {@link Collection}, <code>false</code> otherwise
	 */
	boolean containsAny(Iterable<? extends T> iterable);

	/**
	 * Echo adds the object to the list and then returns it back to the calling code. This is useful when you need to create a new object and add it
	 * to the list at the same time.
	 *
	 * @param obj
	 *            the object to add to the list and return from the method call
	 * @return the object parameter
	 */
	T echo(T obj);

	/**
	 * @see root.lang.Extractable#extract(StringExtractor)
	 */
	@Override
	void extract(StringExtractor extractor);

	/**
	 * @see java.util.List#get(int)
	 */
	@Override
	T get(int index);

	/**
	 * Returns the capacity of the list.
	 *
	 * @return the capacity of the list
	 */
	int getCapacity();

	/**
	 * Returns the size of the list.
	 *
	 * @return the size of the list
	 */
	@Override
	int getSize();

	/**
	 * @see java.util.List#indexOf(Object)
	 */
	@Override
	int indexOf(Object obj);

	/**
	 * Inserts the specified object at the given index within the list.
	 *
	 * @param index
	 *            the index to insert the object at within the list
	 * @param obj
	 *            the object to insert
	 */
	void insert(int index, T obj);

	/**
	 * Inserts all objects in the {@link Collection} at the given index within the list.
	 *
	 * @param index
	 *            the index to insert the objects from the {@link Collection} at within the list
	 * @param collection
	 *            the objects to insert
	 */
	void insertAll(int index, Collection<? extends T> collection);

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
	 * Returns the last element of the list, or <code>null</code> if the list is empty.
	 *
	 * @return the last element of the list, or <code>null</code> if the list is empty
	 */
	T last();

	/**
	 * @see java.util.List#lastIndexOf(Object)
	 */
	@Override
	int lastIndexOf(Object obj);

	/**
	 * @see java.util.List#listIterator()
	 */
	@Override
	ListItemizer<T> listIterator();

	/**
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	ListItemizer<T> listIterator(int index);

	/**
	 * Returns a random element from the list.
	 *
	 * @return a random element from the list
	 */
	T random(RNG rng);

	/**
	 * @see java.util.List#remove(int)
	 */
	@Override
	T remove(int index);

	/**
	 * @see java.util.List#remove(Object)
	 */
	@Override
	boolean remove(Object obj);

	/**
	 * @see java.util.List#removeAll(Collection)
	 */
	@Override
	boolean removeAll(Collection<?> collection);

	/**
	 * Replaces the first occurrence of <code>oldObj</code> with <code>newObj</code>.
	 *
	 * @param oldObj
	 *            the object to replace
	 * @param newObj
	 *            the object to put in place of the old object
	 * @return <code>true</code> if the replace operation was successful
	 */
	boolean replace(T oldObj, T newObj);

	/**
	 * @see java.util.List#retainAll(Collection)
	 */
	@Override
	boolean retainAll(Collection<?> collection);

	/**
	 * @see java.util.List#set(int, Object)
	 */
	@Override
	T set(int index, T obj);

	/**
	 * Randomly shuffles the elements within the list.
	 */
	void shuffle(RNG rng);

	/**
	 * @see java.util.List#size()
	 */
	@Override
	int size();

	/**
	 * TODO: Should this just go ahead and return a SubList?
	 *
	 * @param fromIndex
	 * @return
	 */
	RootList<T> subList(int fromIndex);

	/**
	 * TODO: Should this just go ahead and return a SubList?
	 *
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	@Override
	RootList<T> subList(int fromIndex, int toIndex);

	RootSet<T> subset(int fromIndex);

	RootSet<T> subset(int fromIndex, int toIndex);

	/**
	 * @see java.util.List#toArray()
	 */
	@Override
	T[] toArray();

	/**
	 * @see java.util.List#toArray(Object[])
	 */
	@Override
	<E> E[] toArray(E[] array);

	/**
	 * @see Immutable#toImmutable()
	 */
	@Override
	RootList<T> toImmutable();

	RootSet<T> toSet();

} // End RootList
