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

import java.util.ArrayList;
import java.util.Collection;

import root.annotation.Delegate;
import root.lang.Extractable;
import root.lang.Itemizer;
import root.lang.ListItemizer;
import root.lang.StringExtractor;
import root.random.RNG;
import root.validation.IndexOutOfBoundsException;

/**
 * Turns any {@link RootList} into an {@link Extractable} list, meaning it will accept {@link Extractable} objects of type {@code T} and utilizes the
 * efficiencies of the {@link Extractable} interface on {@link #extract(StringExtractor)} and {@link #toString()}.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the list
 */
@Delegate
public final class ListExtractable<T extends Extractable> implements RootList<T> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	final RootList<T> list;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor. Uses a default size of 8 for the internal {@code values} array.
	 */
	public ListExtractable() {
		this.list = new ListArray<>();
	}

	/**
	 * A constructor that adds all of the elements within the {@link Collection} to this list upon creation.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list upon creation
	 */
	public ListExtractable(final Collection<? extends T> collection) {
		this.list = new ListArray<>(collection);
	}

	/**
	 * A constructor that accepts a predetermined capacity. Uses a default size of 8 for the internal {@code values} array if the specified capacity
	 * is less than 8.
	 *
	 * @param capacity
	 *            the predetermined capacity
	 */
	public ListExtractable(final int capacity) {
		this.list = new ListArray<>(capacity);
	}

	/**
	 * A constructor that uses the {@link RootList} parameter to initialize this list.
	 *
	 * @param rootList
	 *            the {@link RootList} to use
	 */
	public ListExtractable(final RootList<T> rootList) {
		this.list = rootList;
	}

	/**
	 * A constructor that takes an array and uses it to initialize the list upon creation. The array is directly assigned to the internal
	 * {@code values} reference and the list size is set to {@code array.length}.
	 *
	 * @param array
	 *            the array to initialize the list with upon creation
	 */
	@SafeVarargs
	public ListExtractable(final T... array) {
		this.list = new ListArray<>(array);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Inserts the {@code obj} at the specified index.
	 *
	 * @param index
	 *            the index to insert the {@code obj} at
	 * @param obj
	 *            the object to insert
	 * @throws IndexOutOfBoundsException
	 *             if the index is greater than the list size or less than zero
	 */
	@Override
	public final void add(final int index, final T obj) {
		this.list.add(index, obj);
	}

	/**
	 * Adds the {@code obj} at the end of the list.
	 *
	 * @param obj
	 *            the object to add to the list
	 * @return always returns {@code true}
	 */
	@Override
	public final boolean add(final T obj) {
		return this.list.add(obj);
	}

	/**
	 * Adds all the objects from the {@link Collection} to the end of the list.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list
	 * @return {@code true} if items were added to the list, {@code false} otherwise
	 */
	@Override
	public final boolean addAll(final Collection<? extends T> collection) {
		return this.list.addAll(collection);
	}

	/**
	 * Inserts all the objects from the {@link Collection} into the list at the specified index.
	 *
	 * @param index
	 *            the index to insert the {@link Collection} at
	 * @param collection
	 *            the {@link Collection} to add to the list
	 * @return {@code true} if items were added to the list, {@code false} otherwise
	 * @throws IndexOutOfBoundsException
	 *             if the index is greater than the list size or less than zero
	 */
	@Override
	public final boolean addAll(final int index, final Collection<? extends T> collection) {
		return this.list.addAll(index, collection);
	}

	/**
	 * Adds the objects from the {@code T[]} array starting from {@code offset} and for the specified {@code length} to the end of the list.
	 *
	 * @param array
	 *            the {@code T[]} array to add to the list
	 * @param offset
	 *            the starting position in the array to add
	 * @param length
	 *            the number of elements from the array to add
	 */
	@Override
	public final void addAll(final T[] array, final int offset, final int length) {
		this.list.addAll(array, offset, length);
	}

	/**
	 * Clears the list.
	 */
	@Override
	public final void clear() {
		this.list.clear();
	}

	/**
	 * Returns a shallow copy of this {@link ListExtractable} instance. (The elements themselves are not copied.)
	 *
	 * @return a shallow copy of this {@link ListExtractable} instance
	 */
	@Override
	public ListExtractable<T> clone() {
		return new ListExtractable<>(this.list.clone());
	}

	/**
	 * Returns {@code true} if the list contains the specified object, {@code false} otherwise.
	 *
	 * @param obj
	 *            the object to check whether or not it is present in the list
	 * @return {@code true} if the list contains the specified object, {@code false} otherwise
	 */
	@Override
	public final boolean contains(final Object obj) {
		return this.list.contains(obj);
	}

	/**
	 * Returns {@code true} if the list contains <b>all</b> of the objects within the specified {@link Collection}, {@code false} otherwise.
	 *
	 * @param collection
	 *            the {@link Collection} to check whether or not all of its objects are present in the list
	 * @return {@code true} if the list contains <b>all</b> of the objects within the specified {@link Collection}, {@code false} otherwise
	 */
	@Override
	public final boolean containsAll(final Collection<?> collection) {
		return this.list.containsAll(collection);
	}

	/**
	 * Returns {@code true} if the list contains <b>any</b> of the objects within the specified {@link Iterable}, {@code false} otherwise.
	 *
	 * @param iterable
	 *            the {@link Iterable} to check whether or not any of its objects are present in the list
	 * @return {@code true} if the list contains <b>any</b> of the objects within the specified {@link Iterable}, {@code false} otherwise
	 */
	@Override
	public final boolean containsAny(final Iterable<? extends T> iterable) {
		return this.list.containsAny(iterable);
	}

	/**
	 * Adds the {@code obj} at the end of the list and returns the object from the method. This method is useful when you want to create an object and
	 * add it to the list at the same time.
	 *
	 * @param obj
	 *            the object to add to the list
	 * @return always returns the object added to the list
	 */
	@Override
	public final T echo(final T obj) {
		return this.list.echo(obj);
	}

	/**
	 * Returns {@code true} if the specified {@link Object} is equal to {@code this} object. This method calls {@link RootList#equals(Object)} on the
	 * delegate list attribute to test equality.
	 *
	 * @param obj
	 *            the specified {@link Object} to compare for equality to {@code this} object
	 * @return {@code true} if the specified {@link Object} is equal to {@code this} object, false otherwise
	 */
	@Override
	public final boolean equals(final Object param) {
		return this.list.equals(param);
	}

	/**
	 * Extracts a {@link String} representation of the list.
	 *
	 * @param extractor
	 *            the {@link StringExtractor} to populate
	 */
	@Override
	public final void extract(final StringExtractor extractor) {
		int i = 0;

		extractor.append('[');

		for (final Extractable e : this.list) {
			if (i++ > 0) {
				extractor.addSeparator();
			}

			extractor.append(e);
		}

		extractor.append(']');
	}

	/**
	 * Returns the object located at the specified index.
	 *
	 * @param index
	 *            the index of the item in the list to return
	 * @return the object located at the specified index
	 */
	@Override
	public final T get(final int index) {
		return this.list.get(index);
	}

	/**
	 * Returns the capacity of the list.
	 *
	 * @return the capacity of the list
	 */
	@Override
	public final int getCapacity() {
		return this.list.getCapacity();
	}

	/**
	 * Returns the size of the list, which is how many elements are actually in the list.
	 *
	 * @return the size of the list
	 */
	@Override
	public final int getSize() {
		return this.list.getSize();
	}

	/**
	 * Returns the hash code of the list.
	 *
	 * @return the hash code of the list
	 */
	@Override
	public final int hashCode() {
		return this.list.hashCode();
	}

	/**
	 * Returns the index of the first occurrence of the specified object in the list, or -1 if the list does not contain the object.
	 *
	 * @param the
	 *            object to check for its index in the list
	 * @return the index of the first occurrence of the specified object in the list, or -1 if the list does not contain the object
	 */
	@Override
	public final int indexOf(final Object obj) {
		return this.list.indexOf(obj);
	}

	/**
	 * Inserts the {@code obj} at the specified index.
	 *
	 * @param index
	 *            the index to insert the {@code obj} at
	 * @param obj
	 *            the object to insert
	 * @throws IndexOutOfBoundsException
	 *             if the index is greater than the list size or less than zero
	 */
	@Override
	public final void insert(final int index, final T obj) {
		this.list.insert(index, obj);
	}

	/**
	 * Inserts all the objects from the {@link Collection} into the list at the specified index.
	 *
	 * @param index
	 *            the index to insert the {@link Collection} at
	 * @param collection
	 *            the {@link Collection} to add to the list
	 * @throws IndexOutOfBoundsException
	 *             if the index is greater than the list size or less than zero
	 */
	@Override
	public final void insertAll(final int index, final Collection<? extends T> collection) {
		this.list.insertAll(index, collection);
	}

	/**
	 * Returns {@code true} if the list is empty, which means its size is equal to zero.
	 *
	 * @return {@code true} if the list is empty
	 */
	@Override
	public final boolean isEmpty() {
		return this.list.isEmpty();
	}

	/**
	 * Returns an {@link Itemizer} for the list.
	 *
	 * @return an {@link Itemizer} for the list
	 */
	@Override
	public final Itemizer<T> iterator() {
		return this.list.iterator();
	}

	/**
	 * Returns the last element of the list, or {@code null} if the list is empty.
	 *
	 * @return the last element of the list, or {@code null} if the list is empty
	 */
	@Override
	public final T last() {
		return this.list.last();
	}

	/**
	 * Returns the index of the last occurrence of the specified object in the list, or -1 if the list does not contain the object.
	 *
	 * @param the
	 *            object to check for its last index in the list
	 * @return the index of the last occurrence of the specified object in the list, or -1 if the list does not contain the object
	 */
	@Override
	public final int lastIndexOf(final Object obj) {
		return this.list.lastIndexOf(obj);
	}

	/**
	 * Returns a {@link ListItemizer} for the list, starting at the beginning of the list.
	 *
	 * @return a {@link ListItemizer} for the list
	 * @throws UnsupportedOperationException
	 *             Use {@link ArrayList} if you really need to use a {@link java.util.ListIterator}
	 */
	@Override
	public final ListItemizer<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns a {@link ListItemizer} for the list, starting at the specified index in the list.
	 *
	 * @param index
	 *            the index in the list to start the {@link ListItemizer}
	 * @return a {@link ListItemizer} for the list
	 * @throws UnsupportedOperationException
	 *             Use {@link ArrayList} if you really need to use a {@link java.util.ListIterator}
	 */
	@Override
	public final ListItemizer<T> listIterator(final int index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns a random element from the list using the {@link RNG#nextIndex(int)} method to pick the element. A {@code null} value is returned if the
	 * list is empty.
	 *
	 * @param rng
	 *            the random number generator to use
	 * @return a random element from the list
	 */
	@Override
	public final T random(final RNG rng) {
		return this.list.random(rng);
	}

	/**
	 * Removes the element in the list at the specified index.
	 *
	 * @param index
	 *            the index of the element to remove
	 * @return the element removed from the list
	 */
	@Override
	public final T remove(final int index) {
		return this.list.remove(index);
	}

	/**
	 * Removes the first element in the list that equals the specified object.
	 *
	 * @param obj
	 *            the object to remove from the list
	 * @return {@code true} if the object was removed from the list, {@code false} otherwise
	 */
	@Override
	public final boolean remove(final Object obj) {
		return this.list.remove(obj);
	}

	/**
	 * Removes all elements in the list that are found in the specified collection.
	 *
	 * @param collection
	 *            the {@link Collection} of objects to remove from the list
	 * @return {@code true} if at least one object was removed from the list, {@code false} otherwise
	 */
	@Override
	public final boolean removeAll(final Collection<?> collection) {
		return this.list.removeAll(collection);
	}

	/**
	 * Replaces the first occurrence of {@code oldObj} in the list with {@code newObj}.
	 *
	 * @param oldObj
	 *            the original object to replace
	 * @param newObj
	 *            the new object
	 * @return {@code true} if the replace was successful, {@code false} otherwise
	 */
	@Override
	public final boolean replace(final T oldObj, final T newObj) {
		return this.list.replace(oldObj, newObj);
	}

	/**
	 * Retains all elements in the list that are found in the specified collection.
	 *
	 * @param collection
	 *            the {@link Collection} of objects to retain within the list
	 * @return {@code true} if the list was modified in any way, {@code false} otherwise
	 */
	@Override
	public final boolean retainAll(final Collection<?> collection) {
		return this.list.retainAll(collection);
	}

	/**
	 * Sets the element in the list at the specified index to the specified object.
	 *
	 * @param index
	 *            the index whose element to set
	 * @param obj
	 *            the object to set
	 * @return the original element at the specified index
	 */
	@Override
	public final T set(final int index, final T obj) {
		return this.list.set(index, obj);
	}

	/**
	 * Shuffles the contents of the {@link ListArray} using the Knuth shuffling algorithm.
	 *
	 * @param rng
	 *            The random number generator to use during the shuffle
	 */
	@Override
	public final void shuffle(final RNG rng) {
		this.list.shuffle(rng);
	}

	/**
	 * Returns the size of the list, which is how many elements are actually in the list.
	 *
	 * @return the size of the list
	 */
	@Override
	public final int size() {
		return this.list.size();
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @return a {@link ListExtractable} that is the sub-list of the list starting at {@code fromIndex}
	 */
	@Override
	public final ListExtractable<T> subList(final int fromIndex) {
		return new ListExtractable<>(this.list.subList(fromIndex));
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @param toIndex
	 *            the index the end the sub-list
	 * @return a {@link ListExtractable} that is the sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}
	 */
	@Override
	public final ListExtractable<T> subList(final int fromIndex, final int toIndex) {
		return new ListExtractable<>(this.list.subList(fromIndex, toIndex));
	}

	/**
	 * Returns a subset of the list starting at {@code fromIndex}.
	 *
	 * @param fromIndex
	 *            the index to start the subset
	 * @return a {@link SetExtractable} that is the subset of the list starting at {@code fromIndex}
	 */
	@Override
	public final SetExtractable<T> subset(final int fromIndex) {
		return new SetExtractable<>(this.list.subList(fromIndex));
	}

	/**
	 * Returns a subset of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}.
	 *
	 * @param fromIndex
	 *            the index to start the subset
	 * @param toIndex
	 *            the index the end the subset
	 * @return a {@link SetExtractable} that is the subset of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}
	 */
	@Override
	public final SetExtractable<T> subset(final int fromIndex, final int toIndex) {
		return new SetExtractable<>(this.list.subList(fromIndex, toIndex));
	}

	/**
	 * Returns a {@code T[]} array representation of the list.
	 *
	 * @return a {@code T[]} array representation of the list
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final T[] toArray() {
		return (T[]) this.list.toArray(new Extractable[this.list.size()]);
	}

	/**
	 * Returns an {@code E[]} array representation of the list.
	 *
	 * @return an {@code E[]} array representation of the list
	 */
	@Override
	public final <E> E[] toArray(final E[] arrayParam) {
		return this.list.toArray(arrayParam);
	}

	/**
	 * Returns an immutable version of the list.
	 *
	 * @return an immutable version of the list
	 */
	@Override
	public final ListImmutable<T> toImmutable() {
		return new ListImmutable<>(this.list);
	}

	/**
	 * Returns a set containing all the elements of the list.
	 *
	 * @return a {@link SetExtractable} containing all the elements of the list
	 */
	@Override
	public final SetExtractable<T> toSet() {
		return new SetExtractable<>(this.list);
	}

	/**
	 * Returns a {@link String} representation of the list.
	 *
	 * @return a {@link String} representation of the list
	 */
	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(this.list.size() << 4);
		this.extract(extractor);
		return extractor.toString();
	}

} // End ListExtractable
