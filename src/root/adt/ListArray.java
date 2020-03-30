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
import java.util.NoSuchElementException;

import root.lang.Itemizer;
import root.lang.ListItemizer;
import root.lang.StringExtractor;
import root.random.RNG;
import root.util.Root;
import root.validation.IndexOutOfBoundsException;

/**
 * Modern take on the classic {@link ArrayList}.
 *
 * TODO: An ArrayList is Serializable but its elementData array is marked as transient. This is possibly because the elementData array is of type
 * Object[] which is not in of itself Serializable.<br>
 * TODO: Test this theory by serializing a ListArray of Strings and see what happens
 * <ul>
 * <li>An ArrayList now starts off with an empty elementData array and grows it when the first element is added. So a growth hit is taken for every
 * single ArrayList that is actually used.</li>
 * <li>An ArrayList has overflow and range checking whereas ListArray relies on [what does it rely on? figure this out and document]</li>
 * <li>An ArrayList has a default capacity of 10 whereas a ListArray has a default capacity of 5.</li>
 * <li>An ArrayList grows by 50% each time the size limit is reached whereas a ListArray grows by 100% each time its size limit is reached.</li>
 * </ul>
 * TODO: Put a table in here illustrating the difference
 * <p>
 * TODO: Add final designation to every non-private method (done)<br>
 * TODO: Add final designation to every method argument that is not used as a local variable (done) <br>
 * TODO: Put Itemizer<T> methods for addAll(), insertAll(), etc (done)<br>
 * TODO: Remove @Override annotations<br>
 * TODO: Implement Cloneable in all data structures!!<br>
 * TODO: Do we need something like from ArrayList:<br>
 * <ul>
 * <li>ensureCapacity(int)</li>
 * <li>What the hell does ListIterator give that Itemizer doesn't have?</li>
 * <li>trimToSize(), which I would call compact() ** This makes sense for an Immutable **</li>
 * </ul>
 * TODO: What about having equals() be a catch-all and implement n number of isEqualTo() methods for specific Root implementations (whichever make
 * sense for a given data structure)?<br>
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the list
 */
public final class ListArray<T> implements RootList<T> {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	/**
	 * An {@link Itemizer} for the {@code ListArray}.
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
			return ListArray.this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.index < ListArray.this.size;
		}

		@Override
		public final Itemizer<T> iterator() {
			return this;
		}

		@Override
		public final T next() {
			if (this.index == ListArray.this.size) {
				throw new NoSuchElementException();
			}

			return ListArray.this.values[this.index++];
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
	public ListArray() {
		this.values = (T[]) new Object[8];
	}

	/**
	 * A constructor that adds all of the elements within the {@link Collection} to this list upon creation.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list upon creation
	 */
	@SuppressWarnings("unchecked")
	public ListArray(final Collection<? extends T> collection) {
		this.values = (T[]) new Object[Root.max(8, collection.size())];

		for (final T t : collection) {
			this.values[this.size++] = t;
		}
	}

	/**
	 * A constructor that accepts a predetermined capacity. Uses a default size of 8 for the internal {@code values} array if the specified capacity
	 * is less than 8.
	 *
	 * @param capacity
	 *            the predetermined capacity
	 */
	@SuppressWarnings("unchecked")
	public ListArray(final int capacity) {
		this.values = (T[]) new Object[Root.max(8, capacity)];
	}

	/**
	 * A constructor that takes an array and uses it to initialize the list upon creation. The array is directly assigned to the internal
	 * {@code values} reference and the list size is set to {@code array.length}.
	 *
	 * @param array
	 *            the array to initialize the list with upon creation
	 */
	@SafeVarargs
	public ListArray(final T... array) {
		this.values = array;
		this.size = array.length;
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
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		if (this.size == this.values.length) {
			this.resize(this.size << 1);
		}

		// Make room for the new item
		if (index < this.size) {
			System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
		}

		this.values[index] = obj;
		this.size++;
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
		if (this.size == this.values.length) {
			this.resize(this.size << 1);
		}

		this.values[this.size++] = obj;

		return true;
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
		if (collection != null && collection.size() > 0) {
			final int newSize = this.size + collection.size();

			if (newSize > this.values.length) {
				this.resize(newSize + (newSize >> 1));
			}

			for (final T t : collection) {
				this.values[this.size++] = t;
			}

			return true;
		}

		return false;
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
	public final boolean addAll(int index, final Collection<? extends T> collection) {
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		if (collection != null && collection.size() > 0) {
			final int collSize = collection.size();
			final int newSize = this.size + collSize;

			if (newSize > this.values.length) {
				this.resize(newSize + (newSize >> 1));
			}

			// Make room for the collection items
			if (index < this.size) {
				System.arraycopy(this.values, index, this.values, index + collSize, collSize);
			}

			// Add the collection items starting at index
			for (final T t : collection) {
				this.values[index++] = t;
			}

			// Set the list size to the newSize
			this.size = newSize;

			return true;
		}

		return false;
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
		final int newSize = this.size + length;

		if (newSize > this.values.length) {
			this.resize(newSize + (newSize >> 1));
		}

		// Copy the appropriate items into the internal values array
		System.arraycopy(array, offset, this.values, this.size, length);

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
	 * Returns a shallow copy of this {@link ListArray} instance. (The elements themselves are not copied.)
	 *
	 * @return a shallow copy of this {@link ListArray} instance
	 */
	@Override
	public ListArray<T> clone() {
		final T[] clonedValues = Root.newArray(this.size);

		System.arraycopy(this.values, 0, clonedValues, 0, this.size);

		return new ListArray<>(clonedValues);
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
		for (int i = 0; i < this.size; i++) {
			if (Root.equals(this.values[i], obj)) {
				return true;
			}
		}

		return false;
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
		int i;

		items: for (final Object obj : collection) {
			for (i = 0; i < this.size; i++) {
				if (Root.equals(this.values[i], obj)) {
					continue items;
				}
			}

			return false;
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
		int i;

		for (final T t : iterable) {
			for (i = 0; i < this.size; i++) {
				if (Root.equals(this.values[i], t)) {
					return true;
				}
			}
		}

		return false;
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
		if (this.size == this.values.length) {
			this.resize(this.size << 1);
		}

		this.values[this.size++] = obj;

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
	 * Returns the index of the first occurrence of the specified object in the list, or -1 if the list does not contain the object.
	 *
	 * @param obj
	 *            the object to check for its index in the list
	 * @return the index of the first occurrence of the specified object in the list, or -1 if the list does not contain the object
	 */
	@Override
	public final int indexOf(final Object obj) {
		for (int i = 0; i < this.size; i++) {
			if (Root.equals(this.values[i], obj)) {
				return i;
			}
		}

		return -1;
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
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		if (this.size == this.values.length) {
			this.resize(this.size << 1);
		}

		if (index < this.size++) {
			System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
		}

		this.values[index] = obj;
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
	public final void insertAll(int index, final Collection<? extends T> collection) {
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		if (collection != null && collection.size() > 0) {
			final int collSize = collection.size();
			final int newSize = this.size + collSize;

			if (newSize > this.values.length) {
				this.resize(newSize + (newSize >> 1));
			}

			// Make room for the collection items
			if (index < this.size) {
				System.arraycopy(this.values, index, this.values, index + collSize, collSize);
			}

			// Add the collection items starting at index
			for (final T t : collection) {
				this.values[index++] = t;
			}

			// Set the list size to the newSize
			this.size = newSize;
		}
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
		for (int i = this.size - 1; i >= 0; i--) {
			if (Root.equals(this.values[i], obj)) {
				return i;
			}
		}

		return -1;
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
		for (int i = 0; i < this.size; i++) {
			if (Root.equals(this.values[i], obj)) {
				System.arraycopy(this.values, i + 1, this.values, i, this.size - i);
				this.values[--this.size] = null;

				return true;
			}
		}

		return false;
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

		int i;
		items: for (final Object obj : collection) {
			for (i = 0; i < this.size; i++) {
				if (Root.equals(this.values[i], obj)) {
					System.arraycopy(this.values, i + 1, this.values, i, this.size - i);
					this.values[--this.size] = null;
					continue items;
				}
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
		for (int i = 0; i < this.size; i++) {
			if (Root.equals(this.values[i], oldObj)) {
				this.values[i] = newObj;
				return true;
			}
		}

		return false;
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
		if (index >= this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		final T oldValue = this.values[index];
		this.values[index] = obj;
		return oldValue;
	}

	/**
	 * Shuffles the contents of the {@link ListArray} using the Knuth shuffling algorithm.
	 *
	 * @param rng
	 *            The random number generator to use during the shuffle
	 */
	@Override
	public final void shuffle(final RNG rng) {
		int r;
		T temp;

		for (int i = this.size; i > 1;) {
			r = rng.nextIndex(i--);
			temp = this.values[i];
			this.values[i] = this.values[r];
			this.values[r] = temp;
		}
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
	 * @return a {@link ListArray} that is the sub-list of the list starting at {@code fromIndex}
	 */
	@Override
	public final ListArray<T> subList(final int fromIndex) {
		return this.subList(fromIndex, this.size);
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
		if (fromIndex >= toIndex || fromIndex < 0) {
			throw new IndexOutOfBoundsException(fromIndex, toIndex);
		}

		if (toIndex > this.size) {
			throw new IndexOutOfBoundsException(toIndex, this.size);
		}

		final int len = toIndex - fromIndex;
		final ListArray<T> a = new ListArray<T>(len);

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
		final T[] array = (T[]) new Object[this.size];

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
		final T[] t = (T[]) new Object[newCapacity];
		System.arraycopy(this.values, 0, t, 0, this.size);
		this.values = t;
	}

} // End ListArray
