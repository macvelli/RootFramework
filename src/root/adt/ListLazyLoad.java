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

import root.annotation.Delegate;
import root.lang.Constants;
import root.lang.ImmutableListItemizer;
import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.random.RNG;
import root.validation.IndexOutOfBoundsException;

/**
 * A {@link ListArray} implementation that <b>does not</b> create its internal list upon object creation. The internal list is lazy loaded <b>only
 * when</b> it is necessary to do so.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the list
 */
@Delegate
public final class ListLazyLoad<T> implements RootList<T> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	ListArray<T> list;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor. No internal list is created, hence the lazy load effect.
	 */
	public ListLazyLoad() {
		// No-op default constructor
	}

	/**
	 * Used by {@link #clone()} to clone the internal {@link ListArray}.
	 *
	 * @param list
	 *            the {@link ListArray} to clone
	 */
	private ListLazyLoad(final ListArray<T> list) {
		this.list = list.clone();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Inserts the {@code obj} at the specified index.
	 * <p>
	 * If the internal list is {@code null}, a new {@link ListArray} instance is created before performing the operation.
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
		if (this.list == null) {
			this.list = new ListArray<>();
		}

		this.list.add(index, obj);
	}

	/**
	 * Adds the {@code obj} at the end of the list.
	 * <p>
	 * If the internal list is {@code null}, a new {@link ListArray} instance is created before performing the operation.
	 *
	 * @param obj
	 *            the object to add to the list
	 * @return always returns {@code true}
	 */
	@Override
	public final boolean add(final T obj) {
		if (this.list == null) {
			this.list = new ListArray<>();
		}

		return this.list.add(obj);
	}

	/**
	 * Adds all the objects from the {@link Collection} to the end of the list.
	 * <p>
	 * If the internal list is {@code null}, a new {@link ListArray} instance is created before performing the operation.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list
	 * @return {@code true} if items were added to the list, {@code false} otherwise
	 */
	@Override
	public final boolean addAll(final Collection<? extends T> collection) {
		if (this.list == null) {
			this.list = new ListArray<>();
		}

		return this.list.addAll(collection);
	}

	/**
	 * Inserts all the objects from the {@link Collection} into the list at the specified index.
	 * <p>
	 * If the internal list is {@code null}, a new {@link ListArray} instance is created before performing the operation.
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
		if (this.list == null) {
			this.list = new ListArray<>();
		}

		return this.list.addAll(index, collection);
	}

	/**
	 * Adds the objects from the {@code T[]} array starting from {@code offset} and for the specified {@code length} to the end of the list.
	 * <p>
	 * If the internal list is {@code null}, a new {@link ListArray} instance is created before performing the operation.
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
		if (this.list == null) {
			this.list = new ListArray<>();
		}

		this.list.addAll(array, offset, length);
	}

	/**
	 * Clears the list.
	 * <p>
	 * If the internal list is {@code null}, nothing is done.
	 */
	@Override
	public final void clear() {
		if (this.list != null) {
			this.list.clear();
		}
	}

	/**
	 * Returns a shallow copy of this {@link ListLazyLoad} instance. (The elements themselves are not copied.)
	 *
	 * @return a shallow copy of this {@link ListLazyLoad} instance
	 */
	@Override
	public ListLazyLoad<T> clone() {
		return this.list == null ? new ListLazyLoad<T>() : new ListLazyLoad<T>(this.list);
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
		return this.list != null && this.list.contains(obj);
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
		return this.list != null && this.list.containsAll(collection);
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
		return this.list != null && this.list.containsAny(iterable);
	}

	/**
	 * Adds the {@code obj} at the end of the list and returns the object from the method. This method is useful when you want to create an object and
	 * add it to the list at the same time.
	 * <p>
	 * If the internal list is {@code null}, a new {@link ListArray} instance is created before performing the operation.
	 *
	 * @param obj
	 *            the object to add to the list
	 * @return always returns the object added to the list
	 */
	@Override
	public final T echo(final T obj) {
		if (this.list == null) {
			this.list = new ListArray<>();
		}

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
		if (this.list == null) {
			if (param != null && param instanceof Collection) {
				return ((Collection<?>) param).size() == 0;
			}

			return false;
		}

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
		if (this.list == null) {
			extractor.appendArray(Constants.emptyArray);
		} else {
			this.list.extract(extractor);
		}
	}

	/**
	 * Returns the object located at the specified index.
	 * <p>
	 * If the internal list is {@code null}, a {@code null} value is always returned.
	 *
	 * @param index
	 *            the index of the item in the list to return
	 * @return the object located at the specified index
	 */
	@Override
	public final T get(final int index) {
		if (this.list != null) {
			return this.list.get(index);
		}

		return null;
	}

	/**
	 * Returns the capacity of the list.
	 *
	 * @return the capacity of the list
	 */
	@Override
	public final int getCapacity() {
		return this.list == null ? 0 : this.list.values.length;
	}

	/**
	 * Returns the size of the list, which is how many elements are actually in the list.
	 *
	 * @return the size of the list
	 */
	@Override
	public final int getSize() {
		return this.list == null ? 0 : this.list.size;
	}

	/**
	 * Returns the hash code of the list.
	 *
	 * @return the hash code of the list
	 */
	@Override
	public final int hashCode() {
		return this.list == null ? 0 : this.list.hashCode();
	}

	/**
	 * Returns the index of the first occurrence of the specified object in the list, or -1 if the list does not contain the object.
	 *
	 * @param obj
	 *            the object to check for its index in the list
	 * @return the index of the first occurrence of the specified object in the list, or -1 if the list does not contain the object
	 */
	@Override
	public final int indexOf(final Object obj) {
		return this.list == null ? -1 : this.list.indexOf(obj);
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
		if (this.list == null) {
			if (index != 0) {
				throw new IndexOutOfBoundsException(index, 0);
			}

			this.list = new ListArray<>();

			this.list.add(obj);
		} else {
			this.list.insert(index, obj);
		}
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
		if (this.list == null) {
			if (index != 0) {
				throw new IndexOutOfBoundsException(index, 0);
			}

			this.list = new ListArray<>();

			this.list.addAll(collection);
		} else {
			this.list.insertAll(index, collection);
		}
	}

	/**
	 * Returns {@code true} if the list is empty, which means its size is equal to zero.
	 *
	 * @return {@code true} if the list is empty
	 */
	@Override
	public final boolean isEmpty() {
		return this.list == null ? true : this.list.size == 0;
	}

	/**
	 * Returns an {@link Itemizer} for the list.
	 * <p>
	 * If the internal list is {@code null}, a new {@link ListArray} instance is created before performing the operation.
	 *
	 * @return an {@link Itemizer} for the list
	 */
	@Override
	public final Itemizer<T> iterator() {
		if (this.list == null) {
			this.list = new ListArray<>();
		}

		return this.list.iterator();
	}

	/**
	 * Returns the last element of the list, or {@code null} if the list is empty.
	 *
	 * @return the last element of the list, or {@code null} if the list is empty
	 */
	@Override
	public final T last() {
		return this.list == null ? null : this.list.last();
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
		return this.list == null ? -1 : this.list.lastIndexOf(obj);
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final ImmutableListItemizer<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final ImmutableListItemizer<T> listIterator(final int index) {
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
		return this.list == null ? null : this.list.random(rng);
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
		if (this.list == null) {
			throw new IndexOutOfBoundsException(index, 0);
		}

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
		return this.list == null ? false : this.list.remove(obj);
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
		return this.list == null ? false : this.list.removeAll(collection);
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
		return this.list == null ? false : this.list.replace(oldObj, newObj);
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
		return this.list == null ? false : this.list.retainAll(collection);
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
		if (this.list == null) {
			throw new IndexOutOfBoundsException(index, 0);
		}

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
		if (this.list != null) {
			this.list.shuffle(rng);
		}
	}

	/**
	 * Returns the size of the list, which is how many elements are actually in the list.
	 *
	 * @return the size of the list
	 */
	@Override
	public final int size() {
		return this.list == null ? 0 : this.list.size;
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @return a {@link ListArray} that is the sub-list of the list starting at {@code fromIndex}
	 */
	@Override
	public final ListArray<T> subList(final int fromIndex) {
		if (this.list == null) {
			throw new IndexOutOfBoundsException(fromIndex, 0);
		}

		return this.list.subList(fromIndex);
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @param toIndex
	 *            the index the end the sub-list
	 * @return a {@link ListArray} that is the sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}
	 */
	@Override
	public final ListArray<T> subList(final int fromIndex, final int toIndex) {
		if (this.list == null) {
			throw new IndexOutOfBoundsException(toIndex, 0);
		}

		return this.list.subList(fromIndex, toIndex);
	}

	/**
	 * Returns a subset of the list starting at {@code fromIndex}.
	 *
	 * @param fromIndex
	 *            the index to start the subset
	 * @return a {@link SetHashed} that is the subset of the list starting at {@code fromIndex}
	 */
	@Override
	public final SetHashed<T> subset(final int fromIndex) {
		if (this.list == null) {
			throw new IndexOutOfBoundsException(fromIndex, 0);
		}

		return this.list.subset(fromIndex);
	}

	/**
	 * Returns a subset of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}.
	 *
	 * @param fromIndex
	 *            the index to start the subset
	 * @param toIndex
	 *            the index the end the subset
	 * @return a {@link SetHashed} that is the subset of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}
	 */
	@Override
	public final SetHashed<T> subset(final int fromIndex, final int toIndex) {
		if (this.list == null) {
			throw new IndexOutOfBoundsException(toIndex, 0);
		}

		return this.list.subset(fromIndex, toIndex);
	}

	/**
	 * Returns a {@code T[]} array representation of the list.
	 *
	 * @return a {@code T[]} array representation of the list
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final T[] toArray() {
		if (this.list == null) {
			return (T[]) new Object[0];
		}

		return this.list.toArray();
	}

	/**
	 * Returns an {@code E[]} array representation of the list.
	 *
	 * @return an {@code E[]} array representation of the list
	 */
	@Override
	public final <E> E[] toArray(final E[] array) {
		if (this.list == null) {
			return array;
		}

		return this.list.toArray(array);
	}

	/**
	 * Returns an immutable version of the list.
	 *
	 * @return an immutable version of the list
	 */
	@Override
	public final ListImmutable<T> toImmutable() {
		return this.list == null ? new ListImmutable<T>() : new ListImmutable<T>(this.list);
	}

	/**
	 * Returns a set containing all the elements of the list.
	 *
	 * @return a {@link SetHashed} containing all the elements of the list
	 */
	@Override
	public final SetHashed<T> toSet() {
		return new SetHashed<>(this);
	}

	/**
	 * Returns a {@link String} representation of the list.
	 *
	 * @return a {@link String} representation of the list
	 */
	@Override
	public final String toString() {
		return this.list == null ? Constants.EMPTY_ARRAY_STRING : this.list.toString();
	}

} // End ListLazyLoad
