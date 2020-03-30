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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import root.lang.Extractable;
import root.lang.Immutable;
import root.lang.StringExtractor;
import root.random.RNG;
import root.util.Root;
import root.validation.IndexOutOfBoundsException;

/**
 * Similar to the {@link ListArrayLong} though it keeps its elements in sorted order. This class is just like {@link ListArraySorted} except it works
 * with primitive {@code long} values.
 * <p>
 * Since it is impossible to have an {@link Iterator} over primitive values, this class <b>does not</b> have one. Instead, use the
 * {@link #getValues()} method to get the internal {@code values} array and iterate over it. Remember to only iterate up to {@link #getSize()} so
 * don't use the enhanced for() loop!
 * <p>
 * For reference, here is a list of all the methods that are <b>not</b> implemented by this class:
 * <table>
 * <tr>
 * <td>{@link RootList#add(int, Object)}</td>
 * </tr>
 * <tr>
 * <td>{@link RootList#addAll(int, Collection)}</td>
 * </tr>
 * <tr>
 * <td>{@link RootList#insert(int, Object)}</td>
 * </tr>
 * <tr>
 * <td>{@link RootList#insertAll(int, Collection)}</td>
 * </tr>
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
 * <td>{@link RootList#set(int, Object)}</td>
 * </tr>
 * <tr>
 * <td>{@link RootList#shuffle(RNG)}</td>
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
public final class ListArrayLongSorted implements Cloneable, Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	int size;
	long[] values;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor. Uses a default size of 8 for the internal {@code values} array.
	 */
	public ListArrayLongSorted() {
		this.values = new long[8];
	}

	/**
	 * A constructor that adds all of the elements within the {@link Collection} to this list upon creation.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list upon creation
	 */
	public ListArrayLongSorted(final Collection<? extends Number> collection) {
		this.values = new long[Root.max(8, collection.size())];

		for (final Number n : collection) {
			this.values[this.size++] = n.longValue();
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
	public ListArrayLongSorted(final int capacity) {
		this.values = new long[Root.max(8, capacity)];
	}

	/**
	 * A constructor that takes a {@link ListArrayLong} and uses it to initialize the list upon creation. The internal {@code values} reference and
	 * list size are set to the size of the {@link ListArrayLong}.
	 *
	 * @param array
	 *            the array to initialize the list with upon creation
	 */
	public ListArrayLongSorted(final ListArrayLong list) {
		this.values = new long[list.size];
		this.size = list.size;

		System.arraycopy(list.values, 0, this.values, 0, this.size);

		Arrays.sort(this.values);
	}

	/**
	 * A constructor that takes an array and uses it to initialize the list upon creation. The array is directly assigned to the internal
	 * {@code values} reference and the list size is set to {@code array.length}.
	 *
	 * @param array
	 *            the array to initialize the list with upon creation
	 */
	@SafeVarargs
	public ListArrayLongSorted(final long... array) {
		this.values = array;
		this.size = array.length;

		Arrays.sort(this.values);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Adds the {@code long} value to the list in its sorted order.
	 *
	 * @param l
	 *            the {@code long} value to add to the list
	 * @return always returns {@code true}
	 */
	public final boolean add(final long l) {
		if (this.size == this.values.length) {
			this.resize(this.size << 1);
		}

		int index = this.indexOf(l);
		if (index < 0) {
			index = ~index;
		}

		if (index < this.size) {
			// Make room for index == 2 in [3, 5, 16, 19, 82]
			System.arraycopy(this.values, index, this.values, index + 1, this.size - index);
		}

		this.values[index] = l;
		this.size++;

		return true;
	}

	/**
	 * Adds all the {@code long} values from the {@link Collection} to the list. The list is sorted after the values are added to the list.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list
	 * @return {@code true} if items were added to the list, {@code false} otherwise
	 */
	public final boolean addAll(final Collection<? extends Number> collection) {
		if (collection != null && collection.size() > 0) {
			final int newSize = this.size + collection.size();

			if (newSize > this.values.length) {
				this.resize(newSize + (newSize >> 1));
			}

			for (final Number n : collection) {
				this.values[this.size++] = n.longValue();
			}

			Arrays.sort(this.values, 0, this.size);

			return true;
		}

		return false;
	}

	/**
	 * Adds the values from the {@code long[]} array starting from {@code offset} and for the specified {@code length} to list. The list is sorted
	 * after the values are added to the list.
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

		// Sort the internal values array
		Arrays.sort(this.values, 0, newSize);

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
	 * Returns a copy of this {@link ListArrayLongSorted} instance.
	 *
	 * @return a copy of this {@link ListArrayLongSorted} instance
	 */
	@Override
	public final ListArrayLongSorted clone() {
		final ListArrayLongSorted clone = new ListArrayLongSorted(this.size);

		System.arraycopy(this.values, 0, clone.values, 0, this.size);
		clone.size = this.size;

		return clone;
	}

	/**
	 * Returns {@code true} if the list contains the specified {@code long} value, {@code false} otherwise.
	 *
	 * @param l
	 *            the {@code long} value to check whether or not it is present in the list
	 * @return {@code true} if the list contains the specified {@code long} value, {@code false} otherwise
	 */
	public final boolean contains(final long l) {
		return this.indexOf(l) >= 0;
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
		for (final Number n : collection) {
			if (this.indexOf(n.longValue()) < 0) {
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
		for (final Number n : iterable) {
			if (this.indexOf(n.longValue()) >= 0) {
				return true;
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
		this.add(l);

		return l;
	}

	/**
	 * Returns {@code true} if the specified {@link Object} is equal to {@code this} object. The specified {@link Object} is equal to {@code this}
	 * object if:
	 * <ul>
	 * <li>The {@link Class} of the specified {@link Object} is either an {@link ListArrayLongSorted} or an instance of {@link Collection}</li>
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
			if (Root.equalToClass(param, ListArrayLongSorted.class)) {
				final ListArrayLongSorted list = (ListArrayLongSorted) param;

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
	 * Returns the index of the first occurrence of the specified {@code long} value in the list, or a negative integer that denotes the position the
	 * element <b>would have</b> if it were in the list but is not.
	 *
	 * @param l
	 *            the {@code long} value to check for its index in the list
	 * @return the index of the first occurrence of the specified {@code long} value in the list, or a negative integer that denotes the position the
	 *         element if the list does not contain the object
	 */
	public final int indexOf(final long l) {
		int mid, low = 0, high = this.size;

		while (low < high) {
			mid = low + high >> 1;

			if (this.values[mid] == l) {
				return mid;
			} else if (this.values[mid] < l) {
				low = mid + 1;
			} else {
				high = mid;
			}
		}

		return ~low;
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
	 * @param the
	 *            object to check for its last index in the list
	 * @return the index of the last occurrence of the specified object in the list, or -1 if the list does not contain the object
	 */
	public final int lastIndexOf(final long l) {
		int i = this.indexOf(l);

		if (i < 0) {
			return -1;
		}

		while (++i < this.size && this.values[i] == l) {
			;
		}

		return i - 1;
	}

	/**
	 * Returns a random element from the list, using the {@link RNG#nextIndex(int)} method to pick the element.
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

		int i, j;
		for (final Number n : collection) {
			i = this.indexOf(n.longValue());

			if (i >= 0) {
				j = i + 1;
				System.arraycopy(this.values, j, this.values, i, this.size - j);
				this.values[--this.size] = 0L;
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
		final int i = this.indexOf(l);

		if (i >= 0) {
			System.arraycopy(this.values, i + 1, this.values, i, this.size - i);
			this.values[--this.size] = 0L;

			return true;
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
		final int oi = this.indexOf(oldValue);

		if (oi < 0) {
			return false;
		}

		int ni = this.indexOf(newValue);
		if (ni < 0) {
			ni = ~ni;
		}

		if (oi < ni) {
			System.arraycopy(this.values, oi + 1, this.values, oi, --ni - oi);
		} else {
			System.arraycopy(this.values, ni, this.values, ni + 1, oi - ni);
		}

		this.values[ni] = newValue;

		return true;
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
	 * @return a {@link ListArrayLongSorted} that is the sub-list of the list starting at {@code fromIndex}
	 */
	public final ListArrayLongSorted subList(final int fromIndex) {
		return this.subList(fromIndex, this.size);
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @param toIndex
	 *            the index the end the sub-list
	 * @return a {@link ListArrayLongSorted} that is the sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}
	 */
	public final ListArrayLongSorted subList(final int fromIndex, final int toIndex) {
		if (fromIndex >= toIndex || fromIndex < 0) {
			throw new IndexOutOfBoundsException(fromIndex, toIndex);
		}

		if (toIndex > this.size) {
			throw new IndexOutOfBoundsException(toIndex, this.size);
		}

		final int len = toIndex - fromIndex;
		final ListArrayLongSorted a = new ListArrayLongSorted(len);

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

} // End ListArraySortedLong
