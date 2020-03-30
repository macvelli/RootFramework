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
import java.util.Iterator;

import root.lang.Extractable;
import root.lang.Immutable;
import root.lang.StringExtractor;
import root.random.RNG;
import root.util.Root;
import root.validation.IndexOutOfBoundsException;

/**
 * This class is just like {@link ListArray} except it works with primitive {@code long} values.
 * <p>
 * Since it is impossible to have an {@link Iterator} over primitive values, this class <b>does not</b> have one. Instead, use the
 * {@link #getValues()} method to get the internal {@code values} array and iterate over it. Remember to only iterate up to {@link #getSize()} so
 * don't use the enhanced for() loop!
 * <p>
 * For reference, here is a list of all the methods that are <b>not</b> implemented by this class:
 * <table>
 * <tr>
 * <td>{@link RootList#iterator()}</td>
 * </tr>
 * <tr>
 * <td>{@link RootList#listIterator()}</td>
 * </tr>
 * <tr>
 * <td>{@link RootList#listIterator(int)}</td>
 * </tr>
 * <tr>
 * <td>{@link RootList#remove(Object)} (implemented as {@link #removeValue(long)} so as not to collide with {@link #remove(int)})</td>
 * </tr>
 * <tr>
 * <td>{@link RootList#subset(int)}</td>
 * </tr>
 * <tr>
 * <td>{@link RootList#subset(int, int)}</td>
 * </tr>
 * <tr>
 * <td>{@link RootList#toArray(Object[])}</td>
 * </tr>
 * <tr>
 * <td>{@link Immutable#toImmutable()}</td>
 * </tr>
 * <tr>
 * <td>{@link RootList#toSet()}</td>
 * </tr>
 * </table>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ListArrayLong implements Cloneable, Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	int size;
	long[] values;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor. Uses a default size of 8 for the internal {@code values} array.
	 */
	public ListArrayLong() {
		this.values = new long[8];
	}

	/**
	 * A constructor that adds all of the elements within the {@link Collection} to this list upon creation.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list upon creation
	 */
	public ListArrayLong(final Collection<? extends Number> collection) {
		this.values = new long[Root.max(8, collection.size())];

		for (final Number n : collection) {
			this.values[this.size++] = n == null ? 0 : n.longValue();
		}
	}

	/**
	 * A constructor that accepts a predetermined capacity. Uses a default size of 8 for the internal {@code values} array if the specified capacity
	 * is less than 8.
	 *
	 * @param capacity
	 *            the predetermined capacity
	 */
	public ListArrayLong(final int capacity) {
		this.values = new long[Root.max(8, capacity)];
	}

	/**
	 * A constructor that takes an array and uses it to initialize the list upon creation. The array is directly assigned to the internal
	 * {@code values} reference and the list size is set to {@code array.length}.
	 *
	 * @param array
	 *            the array to initialize the list with upon creation
	 */
	@SafeVarargs
	public ListArrayLong(final long... array) {
		this.values = array;
		this.size = array.length;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Inserts the {@code long} at the specified index.
	 *
	 * @param index
	 *            the index to insert the {@code long} at
	 * @param l
	 *            the {@code long} to insert
	 * @throws IndexOutOfBoundsException
	 *             if the index is greater than the list size or less than zero
	 */
	public final void add(final int index, final long l) {
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

		this.values[index] = l;
		this.size++;
	}

	/**
	 * Adds the {@code long} at the end of the list.
	 *
	 * @param l
	 *            the {@code long} to add to the list
	 */
	public final void add(final long l) {
		if (this.size == this.values.length) {
			this.resize(this.size << 1);
		}

		this.values[this.size++] = l;
	}

	/**
	 * Adds all the {@code long} values from the {@link Collection} to the end of the list.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list
	 */
	public final void addAll(final Collection<? extends Number> collection) {
		if (collection != null && collection.size() > 0) {
			final int newSize = this.size + collection.size();

			if (newSize > this.values.length) {
				this.resize(newSize + (newSize >> 1));
			}

			for (final Number n : collection) {
				this.values[this.size++] = n == null ? 0 : n.longValue();
			}
		}
	}

	/**
	 * Inserts all the {@code long} values from the {@link Collection} into the list at the specified index.
	 *
	 * @param index
	 *            the index to insert the {@link Collection} at
	 * @param collection
	 *            the {@link Collection} to add to the list
	 * @throws IndexOutOfBoundsException
	 *             if the index is greater than the list size or less than zero
	 */
	public final void addAll(int index, final Collection<? extends Number> collection) {
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
			for (final Number n : collection) {
				this.values[index++] = n == null ? 0 : n.longValue();
			}

			// Set the list size to the newSize
			this.size = newSize;
		}
	}

	/**
	 * Adds the {@code long} values from the {@code long[]} array starting from {@code offset} and for the specified {@code length} to the end of the
	 * list.
	 *
	 * @param array
	 *            the {@code long[]} array to add to the list
	 * @param offset
	 *            the starting position in the array to add
	 * @param length
	 *            the number of elements from the array to add
	 */
	public final void addAll(final long[] array, final int offset, final int length) {
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
	public final void clear() {
		for (int i = 0; i < this.size; i++) {
			this.values[i] = 0L;
		}

		this.size = 0;
	}

	/**
	 * Returns a copy of this {@link ListArrayLong} instance.
	 *
	 * @return a copy of this {@link ListArrayLong} instance
	 */
	@Override
	public final ListArrayLong clone() {
		final long[] clonedValues = new long[this.size];

		System.arraycopy(this.values, 0, clonedValues, 0, this.size);

		return new ListArrayLong(clonedValues);
	}

	/**
	 * Returns {@code true} if the list contains the specified {@code long}, {@code false} otherwise.
	 *
	 * @param l
	 *            the {@code long} to check whether or not it is present in the list
	 * @return {@code true} if the list contains the specified {@code long}, {@code false} otherwise
	 */
	public final boolean contains(final long l) {
		for (int i = 0; i < this.size; i++) {
			if (this.values[i] == l) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns {@code true} if the list contains <b>all</b> of the {@code long} values within the specified {@link Collection}, {@code false}
	 * otherwise.
	 *
	 * @param collection
	 *            the {@link Collection} to check whether or not all of its {@code long} values are present in the list
	 * @return {@code true} if the list contains <b>all</b> of the {@code long} values within the specified {@link Collection}, {@code false}
	 *         otherwise
	 */
	public final boolean containsAll(final Collection<? extends Number> collection) {
		int i;
		long l;

		items: for (final Number n : collection) {
			if (n != null) {
				l = n.longValue();

				for (i = 0; i < this.size; i++) {
					if (this.values[i] == l) {
						continue items;
					}
				}

				return false;
			}
		}

		return true;
	}

	/**
	 * Returns {@code true} if the list contains <b>any</b> of the {@code long} values within the specified {@link Iterable}, {@code false} otherwise.
	 *
	 * @param iterable
	 *            the {@link Iterable} to check whether or not any of its {@code long} values are present in the list
	 * @return {@code true} if the list contains <b>any</b> of the {@code long} values within the specified {@link Iterable}, {@code false} otherwise
	 */
	public final boolean containsAny(final Iterable<? extends Number> iterable) {
		int i;
		long l;

		for (final Number n : iterable) {
			if (n != null) {
				l = n.longValue();

				for (i = 0; i < this.size; i++) {
					if (this.values[i] == l) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Adds the {@code long} at the end of the list and returns the value from the method.
	 *
	 * @param l
	 *            the {@code long} to add to the list
	 * @return always returns the value added to the list
	 */
	public final long echo(final long l) {
		if (this.size == this.values.length) {
			this.resize(this.size << 1);
		}

		this.values[this.size++] = l;

		return l;
	}

	/**
	 * Returns {@code true} if the specified {@link Object} is equal to {@code this} object. The specified {@link Object} is equal to {@code this}
	 * object if:
	 * <ul>
	 * <li>The {@link Class} of the specified {@link Object} is either an {@link ListArrayLong} or an instance of {@link Collection}</li>
	 * <li>The {@code size} of the specified object and {@code this} object are equal</li>
	 * <li>All {@code long} values in both the specified object and {@code this} object are equal to each other</li>
	 * </ul>
	 *
	 * @param obj
	 *            the specified {@link Object} to compare for equality to {@code this} object
	 * @return {@code true} if the specified {@link Object} is equal to {@code this} object, false otherwise
	 */
	@Override
	public final boolean equals(final Object param) {
		if (param != null) {
			if (Root.equalToClass(param, ListArrayLong.class)) {
				final ListArrayLong list = (ListArrayLong) param;

				if (this.size == list.size) {
					for (int i = 0; i < this.size; i++) {
						if (this.values[i] != list.values[i]) {
							return false;
						}
					}

					return true;
				}
			} else if (param instanceof Collection) {
				final Collection<?> collection = (Collection<?>) param;

				if (this.size == collection.size()) {
					final Iterator<?> itr = collection.iterator();

					if (itr.hasNext()) {
						final Object obj = itr.next();

						if (obj instanceof Number) {
							Number n = (Number) obj;

							if (n == null || this.values[0] != n.longValue()) {
								return false;
							}

							@SuppressWarnings("unchecked")
							final Iterator<? extends Number> numberItr = (Iterator<? extends Number>) itr;
							int i = 1;

							while (numberItr.hasNext()) {
								n = numberItr.next();

								if (n == null || this.values[i++] != n.longValue()) {
									return false;
								}
							}
						}
					}

					return true;
				}
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
	 * Returns the {@code long} value located at the specified index.
	 *
	 * @param index
	 *            the index of the {@code long} value to return
	 * @return the {@code long} value located at the specified index
	 */
	public final long get(final int index) {
		return this.values[index];
	}

	/**
	 * Returns the capacity of the list.
	 *
	 * @return the capacity of the list
	 */
	public final int getCapacity() {
		return this.values.length;
	}

	/**
	 * Returns the size of the list, which is how many elements are actually in the list.
	 *
	 * @return the size of the list
	 */
	public final int getSize() {
		return this.size;
	}

	/**
	 * Returns the internal {@code values} array of the list.
	 *
	 * @return the internal {@code values} array of the list
	 */
	public final long[] getValues() {
		return this.values;
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
	 * Returns the index of the first occurrence of the specified {@code long} value in the list, or -1 if the list does not contain the value.
	 *
	 * @param l
	 *            the {@code long} value to check for its index in the list
	 * @return the index of the first occurrence of the specified {@code long} value in the list, or -1 if the list does not contain the value
	 */
	public final int indexOf(final long l) {
		for (int i = 0; i < this.size; i++) {
			if (this.values[i] == l) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Inserts the {@code long} value at the specified index.
	 *
	 * @param index
	 *            the index to insert the {@code obj} at
	 * @param obj
	 *            the object to insert
	 * @throws IndexOutOfBoundsException
	 *             if the index is greater than the list size or less than zero
	 */
	public final void insert(final int index, final long l) {
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		if (this.size == this.values.length) {
			this.resize(this.size << 1);
		}

		if (index < this.size++) {
			System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
		}

		this.values[index] = l;
	}

	/**
	 * Inserts all the {@code long} values from the {@link Collection} into the list at the specified index.
	 *
	 * @param index
	 *            the index to insert the {@link Collection} at
	 * @param collection
	 *            the {@link Collection} to add to the list
	 * @throws IndexOutOfBoundsException
	 *             if the index is greater than the list size or less than zero
	 */
	public final void insertAll(int index, final Collection<? extends Number> collection) {
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
			for (final Number n : collection) {
				this.values[index++] = n == null ? 0 : n.longValue();
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
	public final boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Returns the last element of the list, or zero if the list is empty.
	 *
	 * @return the last element of the list, or zero if the list is empty
	 */
	public final long last() {
		return this.size > 0 ? this.values[this.size - 1] : 0L;
	}

	/**
	 * Returns the index of the last occurrence of the specified {@code long} value in the list, or -1 if the list does not contain the value.
	 *
	 * @param l
	 *            the {@code long} value to check for its last index in the list
	 * @return the index of the last occurrence of the specified {@code long} value in the list, or -1 if the list does not contain the value
	 */
	public final int lastIndexOf(final long l) {
		for (int i = this.size - 1; i >= 0; i--) {
			if (this.values[i] == l) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Returns a random element from the list using the {@link RNG#nextIndex(int)} method to pick the element. A zero value is returned if the list is
	 * empty.
	 *
	 * @param rng
	 *            the random number generator to use
	 * @return a random element from the list
	 */
	public final long random(final RNG rng) {
		return this.size == 0 ? 0L : this.values[rng.nextIndex(this.size)];
	}

	/**
	 * Removes the element in the list at the specified index.
	 *
	 * @param index
	 *            the index of the element to remove
	 * @return the element removed from the list
	 */
	public final long remove(final int index) {
		if (index >= this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		final long oldVal = this.values[index];
		System.arraycopy(this.values, index + 1, this.values, index, this.size - index);
		this.values[--this.size] = 0;

		return oldVal;
	}

	/**
	 * Removes all elements in the list that are found in the specified collection.
	 *
	 * @param collection
	 *            the {@link Collection} of objects to remove from the list
	 * @return {@code true} if at least one value was removed from the list, {@code false} otherwise
	 */
	public final boolean removeAll(final Collection<? extends Number> collection) {
		final int origSize = this.size;

		int i;
		long l;
		items: for (final Number n : collection) {
			l = n.longValue();

			for (i = 0; i < this.size; i++) {
				if (n != null && this.values[i] == l) {
					System.arraycopy(this.values, i + 1, this.values, i, this.size - i);
					this.values[--this.size] = 0;
					continue items;
				}
			}
		}

		return origSize != this.size;
	}

	/**
	 * Removes the first element in the list that equals the specified {@code long} value.
	 *
	 * @param l
	 *            the {@code long} value to remove from the list
	 * @return {@code true} if the {@code long} value was removed from the list, {@code false} otherwise
	 */
	public final boolean removeValue(final long l) {
		for (int i = 0; i < this.size; i++) {
			if (this.values[i] == l) {
				System.arraycopy(this.values, i + 1, this.values, i, this.size - i);
				this.values[--this.size] = 0;

				return true;
			}
		}

		return false;
	}

	/**
	 * Replaces the first occurrence of {@code oldValue} in the list with {@code newValue}.
	 *
	 * @param oldValue
	 *            the original {@code long} value to replace
	 * @param newValue
	 *            the new {@code long} value
	 * @return {@code true} if the replace was successful, {@code false} otherwise
	 */
	public final boolean replace(final long oldValue, final long newValue) {
		for (int i = 0; i < this.size; i++) {
			if (this.values[i] == oldValue) {
				this.values[i] = newValue;
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
	public final boolean retainAll(final Collection<? extends Number> collection) {
		if (collection != null && collection.size() > 0) {
			int i = 0, j = 0;
			long l;

			for (; i < this.size; i++) {
				l = this.values[i];

				if (collection.contains(l)) {
					this.values[j++] = l;
				}
			}

			if (this.size != j) {
				for (i = j; i < this.size; i++) {
					this.values[i] = 0L;
				}

				this.size = j;
				return true;
			}
		}

		return false;
	}

	/**
	 * Sets the element in the list at the specified index to the specified {@code long} value.
	 *
	 * @param index
	 *            the index whose element to set
	 * @param l
	 *            the {@code long} value to set
	 * @return the original value at the specified index
	 */
	public final long set(final int index, final long l) {
		if (index >= this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		final long oldValue = this.values[index];
		this.values[index] = l;
		return oldValue;
	}

	/**
	 * Shuffles the contents of the {@link ListArrayLong} using the Knuth shuffling algorithm.
	 *
	 * @param rng
	 *            The random number generator to use during the shuffle
	 */
	public final void shuffle(final RNG rng) {
		int r;
		long temp;

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
	public final int size() {
		return this.size;
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @return a {@link ListArrayLong} that is the sub-list of the list starting at {@code fromIndex}
	 */
	public final ListArrayLong subList(final int fromIndex) {
		return this.subList(fromIndex, this.size);
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @param toIndex
	 *            the index the end the sub-list
	 * @return a {@link ListArrayLong} that is the sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}
	 */
	public final ListArrayLong subList(final int fromIndex, final int toIndex) {
		if (fromIndex >= toIndex || fromIndex < 0) {
			throw new IndexOutOfBoundsException(fromIndex, toIndex);
		}

		if (toIndex > this.size) {
			throw new IndexOutOfBoundsException(toIndex, this.size);
		}

		final int len = toIndex - fromIndex;
		final ListArrayLong a = new ListArrayLong(len);

		System.arraycopy(this.values, fromIndex, a.values, 0, len);
		a.size = len;

		return a;
	}

	/**
	 * Returns a {@code long[]} array representation of the list.
	 *
	 * @return a {@code long[]} array representation of the list
	 */
	public final long[] toArray() {
		final long[] array = new long[this.size];

		System.arraycopy(this.values, 0, array, 0, this.size);

		return array;
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

	private void resize(final int newCapacity) {
		final long[] l = new long[newCapacity];
		System.arraycopy(this.values, 0, l, 0, this.size);
		this.values = l;
	}

} // End ListArrayLong
