/*
 * Copyright 2006-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package root.adt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;

import root.lang.Itemizer;
import root.lang.ListItemizer;
import root.lang.StringExtractor;
import root.random.RNG;
import root.util.Root;
import root.validation.IndexOutOfBoundsException;

/**
 * Similar to the {@link ListArray} though it keeps its elements in sorted order.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in this list
 */
public final class ListArraySorted<T extends Comparable<T>> implements RootList<T> {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	/**
	 * An {@link Itemizer} for the {@code ListArraySorted}.
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private final class Iterator implements Itemizer<T> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private int index;

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final int getIndex() {
			return this.index - 1;
		}

		@Override
		public final int getSize() {
			return ListArraySorted.this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.index < ListArraySorted.this.size;
		}

		@Override
		public final Itemizer<T> iterator() {
			return this;
		}

		@Override
		public final T next() {
			if (this.index == ListArraySorted.this.size) {
				throw new NoSuchElementException();
			}

			return ListArraySorted.this.values[this.index++];
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public final void reset() {
			this.index = 0;
		}

	} // End Iterator

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	int size;
	T[] values;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor. Uses a default size of 8 for the internal {@code values} array.
	 */
	@SuppressWarnings("unchecked")
	public ListArraySorted() {
		this.values = (T[]) new Comparable[8];
	}

	/**
	 * A constructor that adds all of the elements within the {@link Collection} to this list upon creation.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list upon creation
	 */
	@SuppressWarnings("unchecked")
	public ListArraySorted(final Collection<? extends T> collection) {
		this.values = (T[]) new Comparable[Root.max(8, collection.size())];

		for (final T t : collection) {
			this.values[this.size++] = t;
		}

		Arrays.sort(this.values, 0, this.size);
	}

	/**
	 * A constructor that accepts a predetermined capacity. Uses a default size of 8 for the internal {@code values} array if the specified capacity
	 * is less than 8.
	 *
	 * @param capacity
	 *            the predetermined capacity
	 */
	@SuppressWarnings("unchecked")
	public ListArraySorted(final int capacity) {
		this.values = (T[]) new Comparable[Root.max(8, capacity)];
	}

	/**
	 * A constructor that takes an array and uses it to initialize the list upon creation. The array is directly assigned to the internal
	 * {@code values} reference and the list size is set to {@code array.length}.
	 *
	 * @param array
	 *            the array to initialize the list with upon creation
	 */
	@SafeVarargs
	public ListArraySorted(final T... array) {
		this.values = array;
		this.size = array.length;

		Arrays.sort(this.values);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Throws {@link UnsupportedOperationException}. Use {@link #add(Comparable)} instead.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final void add(final int index, final T obj) {
		throw new UnsupportedOperationException("Use the add(T) function to insert an element into a ListArraySorted");
	}

	/**
	 * Adds the {@code obj} to the list in its sorted order.
	 *
	 * @param obj
	 *            the object to add to the list
	 * @return always returns {@code true}
	 */
	@Override
	public final boolean add(final T obj) {
		if (this.size == this.values.length) {
			this.resize(this.size << 1);
		}

		int index = this.indexOf(obj);
		if (index < 0) {
			index = ~index;
		}

		if (index < this.size) {
			// Make room for index == 2 in [3, 5, 16, 19, 82]
			System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
		}

		this.values[index] = obj;
		this.size++;

		return true;
	}

	/**
	 * Adds all the objects from the {@link Collection} to the list. The list is sorted after the objects are added to the list.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list
	 * @return {@code true} if items were added to the list, {@code false} otherwise
	 */
	@Override
	public final boolean addAll(final Collection<? extends T> collection) {
		if (collection != null && collection.size() > 0) {
			final int newSize = this.size + collection.size();

			if (newSize > this.values.length) {
				this.resize(newSize + (newSize >> 1));
			}

			for (final T obj : collection) {
				this.values[this.size++] = obj;
			}

			Arrays.sort(this.values, 0, this.size);

			return true;
		}

		return false;
	}

	/**
	 * Throws {@link UnsupportedOperationException}. Use {@link #addAll(Collection)} instead.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final boolean addAll(final int index, final Collection<? extends T> collection) {
		throw new UnsupportedOperationException("Use the addAll(Collection) function to insert elements into a ListArraySorted");
	}

	/**
	 * Adds the objects from the {@code T[]} array starting from {@code offset} and for the specified {@code length} to list. The list is sorted after
	 * the objects are added to the list.
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
		final int newSize = this.size + length;

		if (newSize > this.values.length) {
			this.resize(newSize + (newSize >> 1));
		}

		// Copy the appropriate items into the internal values array
		System.arraycopy(array, offset, this.values, this.size, length);

		// Sort the internal values array
		Arrays.sort(this.values, 0, newSize);

		// Set the list size to the newSize
		this.size = newSize;
	}

	/**
	 * Clears the list.
	 */
	@Override
	public final void clear() {
		for (int i = 0; i < this.size; i++) {
			this.values[i] = null;
		}

		this.size = 0;
	}

	/**
	 * Returns a shallow copy of this {@link ListArraySorted} instance. (The elements themselves are not copied.)
	 *
	 * @return a shallow copy of this {@link ListArraySorted} instance
	 */
	@Override
	public final ListArraySorted<T> clone() {
		@SuppressWarnings("unchecked")
		final T[] clonedValues = (T[]) new Comparable[this.size];

		System.arraycopy(this.values, 0, clonedValues, 0, this.size);

		return new ListArraySorted<>(clonedValues);
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
		return this.indexOf(obj) >= 0;
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
		for (final Object obj : collection) {
			if (this.indexOf(obj) < 0) {
				return false;
			}
		}

		return true;
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
		for (final T obj : iterable) {
			if (this.indexOf(obj) >= 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Adds the {@code obj} to the list and returns the object from the method. This method is useful when you want to create an object and add it to
	 * the list at the same time.
	 *
	 * @param obj
	 *            the object to add to the list
	 * @return always returns the object added to the list
	 */
	@Override
	public final T echo(final T obj) {
		this.add(obj);

		return obj;
	}

	/**
	 * Returns {@code true} if the specified {@link Object} is equal to {@code this} object. The specified {@link Object} is equal to {@code this}
	 * object if:
	 * <ul>
	 * <li>The {@link Class} of the specified {@link Object} is an instance of {@link Collection}</li>
	 * <li>The {@code size} of the specified {@link Collection} and {@code this} object are equal</li>
	 * <li>All objects in both the specified {@link Collection} and {@code this} object are equal to each other</li>
	 * </ul>
	 *
	 * @param obj
	 *            the specified {@link Object} to compare for equality to {@code this} object
	 * @return {@code true} if the specified {@link Object} is equal to {@code this} object, false otherwise
	 */
	@Override
	public final boolean equals(final Object param) {
		if (param != null && param instanceof Collection) {
			final Collection<?> collection = (Collection<?>) param;

			if (this.size == collection.size()) {
				int i = 0;

				for (final Object obj : collection) {
					if (Root.notEqual(this.values[i++], obj)) {
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}

	/**
	 * Extracts a {@link String} representation of the list.
	 *
	 * @param extractor
	 *            the {@link StringExtractor} to populate
	 */
	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append(this.values, 0, this.size);
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
		return this.values[index];
	}

	/**
	 * Returns the capacity of the list.
	 *
	 * @return the capacity of the list
	 */
	@Override
	public final int getCapacity() {
		return this.values.length;
	}

	/**
	 * Returns the size of the list, which is how many elements are actually in the list.
	 *
	 * @return the size of the list
	 */
	@Override
	public final int getSize() {
		return this.size;
	}

	/**
	 * Returns the hash code of the list.
	 *
	 * @return the hash code of the list
	 */
	@Override
	public final int hashCode() {
		return Root.hashCode(this.values, this.size);
	}

	/**
	 * Returns the index of the first occurrence of the specified object in the list, or a negative integer that denotes the position the element
	 * <b>would have</b> if it were in the list but is not.
	 *
	 * @param the
	 *            object to check for its index in the list
	 * @return the index of the first occurrence of the specified object in the list, or a negative integer that denotes the position the element if
	 *         the list does not contain the object
	 */
	@Override
	public final int indexOf(final Object objParam) {
		final T obj = Root.cast(objParam);
		int mid, low = 0, high = this.size;

		while (low < high) {
			mid = low + high >>> 1;

			if (Root.equals(this.values[mid], obj)) {
				return mid;
			} else if (this.values[mid].compareTo(obj) < 0) {
				low = mid + 1;
			} else {
				high = mid;
			}
		}

		return low < this.size && this.values[low].equals(obj) ? low : ~low;
	}

	/**
	 * Throws {@link UnsupportedOperationException}. Use {@link #add(Comparable)} instead.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final void insert(final int index, final T obj) {
		throw new UnsupportedOperationException("Use the add(T) function to insert an element into a ListArraySorted");
	}

	/**
	 * Throws {@link UnsupportedOperationException}. Use {@link #addAll(Collection)} instead.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final void insertAll(final int index, final Collection<? extends T> collection) {
		throw new UnsupportedOperationException("Use the addAll(Collection) function to insert elements into a ListArraySorted");
	}

	/**
	 * Returns {@code true} if the list is empty, which means its size is equal to zero.
	 *
	 * @return {@code true} if the list is empty
	 */
	@Override
	public final boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Returns an {@link Itemizer} for the list.
	 *
	 * @return an {@link Itemizer} for the list
	 */
	@Override
	public final Itemizer<T> iterator() {
		return new Iterator();
	}

	/**
	 * Returns the last element of the list, or {@code null} if the list is empty.
	 *
	 * @return the last element of the list, or {@code null} if the list is empty
	 */
	@Override
	public final T last() {
		return this.size > 0 ? this.values[this.size - 1] : null;
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
		int i = this.indexOf(obj);

		if (i < 0) {
			return -1;
		}

		while (++i < this.size && Root.equals(this.values[i], obj)) {
			;
		}

		return i - 1;
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
		return this.size == 0 ? null : this.values[rng.nextIndex(this.size)];
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
		if (index >= this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		final T oldVal = this.values[index];
		System.arraycopy(this.values, index + 1, this.values, index, this.size - index);
		this.values[--this.size] = null;

		return oldVal;
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
		final int index = this.indexOf(obj);

		if (index < 0) {
			return false;
		}

		System.arraycopy(this.values, index + 1, this.values, index, this.size - index);
		this.values[--this.size] = null;

		return true;
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
		final int origSize = this.size;

		int i, j;
		for (final Object obj : collection) {
			i = this.indexOf(obj);

			if (i >= 0) {
				j = i + 1;
				System.arraycopy(this.values, j, this.values, i, this.size - j);
				this.values[--this.size] = null;
			}
		}

		return origSize != this.size;
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
		final int oi = this.indexOf(oldObj);

		if (oi < 0) {
			return false;
		}

		int ni = this.indexOf(newObj);
		if (ni < 0) {
			ni = ~ni;
		}

		if (oi < ni) {
			System.arraycopy(this.values, oi + 1, this.values, oi, --ni - oi);
		} else {
			System.arraycopy(this.values, ni, this.values, ni + 1, oi - ni);
		}

		this.values[ni] = newObj;

		return true;
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
		if (collection != null && collection.size() > 0) {
			int j = 0;
			T obj;

			for (int i = 0; i < this.size; i++) {
				obj = this.values[i];

				if (collection.contains(obj)) {
					this.values[j++] = obj;
				}
			}

			if (this.size != j) {
				for (int i = j; i < this.size; i++) {
					this.values[i] = null;
				}

				this.size = j;
				return true;
			}
		}

		return false;
	}

	/**
	 * Throws {@link UnsupportedOperationException}. Use {@link #add(Comparable)} instead.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final T set(final int index, final T obj) {
		throw new UnsupportedOperationException("Use the add(T) function to put an element into a ListArraySorted");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final void shuffle(final RNG rng) {
		throw new UnsupportedOperationException("Cannot shuffle the elements of a ListArraySorted");
	}

	/**
	 * Returns the size of the list, which is how many elements are actually in the list.
	 *
	 * @return the size of the list
	 */
	@Override
	public final int size() {
		return this.size;
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @return a {@link ListArraySorted} that is the sub-list of the list starting at {@code fromIndex}
	 */
	@Override
	public final ListArraySorted<T> subList(final int fromIndex) {
		return this.subList(fromIndex, this.size);
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @param toIndex
	 *            the index the end the sub-list
	 * @return a {@link ListArraySorted} that is the sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}
	 */
	@Override
	public final ListArraySorted<T> subList(final int fromIndex, final int toIndex) {
		if (fromIndex >= toIndex || fromIndex < 0) {
			throw new IndexOutOfBoundsException(fromIndex, toIndex);
		}

		if (toIndex > this.size) {
			throw new IndexOutOfBoundsException(toIndex, this.size);
		}

		final int len = toIndex - fromIndex;
		final ListArraySorted<T> a = new ListArraySorted<T>(len);

		System.arraycopy(this.values, fromIndex, a.values, 0, len);
		a.size = len;

		return a;
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
		return this.subset(fromIndex, this.size);
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
		if (fromIndex >= toIndex || fromIndex < 0) {
			throw new IndexOutOfBoundsException(fromIndex, toIndex);
		}

		if (toIndex > this.size) {
			throw new IndexOutOfBoundsException(toIndex, this.size);
		}

		final SetHashed<T> s = new SetHashed<T>(toIndex - fromIndex);

		for (int i = fromIndex; i < toIndex; i++) {
			s.add(this.values[i]);
		}

		return s;
	}

	/**
	 * Returns a {@code T[]} array representation of the list.
	 *
	 * @return a {@code T[]} array representation of the list
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final T[] toArray() {
		final T[] array = (T[]) new Comparable[this.size];

		System.arraycopy(this.values, 0, array, 0, this.size);

		return array;
	}

	/**
	 * Returns an {@code E[]} array representation of the list.
	 *
	 * @return an {@code E[]} array representation of the list
	 */
	@Override
	public final <E> E[] toArray(final E[] arrayParam) {
		final E[] array = Root.newArray(arrayParam, this.size);

		System.arraycopy(this.values, 0, array, 0, this.size);

		return array;
	}

	/**
	 * Returns an immutable version of the list.
	 *
	 * @return an immutable version of the list
	 */
	@Override
	public final ListImmutable<T> toImmutable() {
		return new ListImmutable<>(this);
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
		return new StringExtractor(this.size << 4).append(this.values, 0, this.size).toString();
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	/**
	 * Resizes the internal values array to the {@code newCapacity}.
	 *
	 * @param newCapacity
	 *            the new capacity of the internal values array
	 */
	@SuppressWarnings("unchecked")
	private final void resize(final int newCapacity) {
		final T[] t = (T[]) new Comparable[newCapacity];
		System.arraycopy(this.values, 0, t, 0, this.size);
		this.values = t;
	}

} // End ListArraySorted
