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
import java.util.Collection;

import root.annotation.Delegate;
import root.lang.Itemizer;
import root.lang.ListItemizer;
import root.lang.StringExtractor;
import root.random.RNG;
import root.validation.IndexOutOfBoundsException;

/**
 * Turns any {@link RootList} into a list with an accompanying {@link SetHashed} instance. The default implementation is a combination of
 * {@link ListArray} and {@link SetHashed} in one easy-to-use class. The following methods utilize the internal {@link SetHashed} instance to maximize
 * runtime performance:
 * <p>
 * <table>
 * <tr>
 * <td>{@link #contains(Object)}</td>
 * </tr>
 * <tr>
 * <td>{@link #containsAll(Collection)}</td>
 * </tr>
 * <tr>
 * <td>{@link #containsAny(Iterable)}</td>
 * </tr>
 * </table>
 * <p>
 * Obviously, the enhanced runtime performance comes with a cost of using additional memory for the internal {@link SetHashed} instance.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the list
 */
@Delegate
public final class ListHashed<T> implements RootList<T> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	final RootList<T> list;

	final SetHashed<T> set;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor.
	 */
	public ListHashed() {
		this.list = new ListArray<>();
		this.set = new SetHashed<>();
	}

	/**
	 * A constructor that adds all of the elements within the {@link Collection} to this list upon creation.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list upon creation
	 */
	public ListHashed(final Collection<? extends T> collection) {
		this.list = new ListArray<>(collection);
		this.set = new SetHashed<>(collection);
	}

	/**
	 * A constructor that accepts a predetermined capacity.
	 *
	 * @param capacity
	 *            the predetermined capacity
	 */
	public ListHashed(final int capacity) {
		this.list = new ListArray<>(capacity);
		this.set = new SetHashed<>(capacity);
	}

	/**
	 * A constructor that uses the {@link RootList} parameter to initialize this list.
	 *
	 * @param rootList
	 *            the {@link RootList} to use
	 */
	public ListHashed(final RootList<T> rootList) {
		this.list = rootList;
		this.set = new SetHashed<>(rootList);
	}

	/**
	 * A constructor that takes an array and uses it to initialize the list upon creation.
	 *
	 * @param array
	 *            the array to initialize the list with upon creation
	 */
	@SafeVarargs
	public ListHashed(final T... array) {
		this.list = new ListArray<>(array);
		this.set = new SetHashed<>(array);
	}

	/**
	 * Used by {@link #clone()} to initialize the {@link ListHashed} when cloning.
	 *
	 * @param listParam
	 *            the cloned {@link RootList}
	 * @param setParam
	 *            the cloned {@link SetHashed}
	 */
	private ListHashed(final RootList<T> listParam, final SetHashed<T> setParam) {
		this.list = listParam;
		this.set = setParam;
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
		this.set.add(obj);
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
		this.set.add(obj);
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
		this.set.addAll(collection);
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
		this.set.addAll(collection);
		return this.list.addAll(index, collection);
	}

	/**
	 * Adds the objects from the {@code String[]} array starting from {@code offset} and for the specified {@code length} to the end of the list.
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
		this.set.addAll(array, offset, length);
		this.list.addAll(array, offset, length);
	}

	/**
	 * Clears the list.
	 */
	@Override
	public final void clear() {
		this.set.clear();
		this.list.clear();
	}

	/**
	 * Returns a shallow copy of this {@link ListHashed} instance. (The elements themselves are not copied.)
	 *
	 * @return a shallow copy of this {@link ListHashed} instance
	 */
	@Override
	public final ListHashed<T> clone() {
		return new ListHashed<>(this.list.clone(), this.set.clone());
	}

	/**
	 * Returns {@code true} if the list contains the specified object, {@code false} otherwise.
	 * <p>
	 * <b>Note:</b> this method uses the internal {@link SetHashed} to perform the operation.
	 *
	 * @param obj
	 *            the object to check whether or not it is present in the list
	 * @return {@code true} if the list contains the specified object, {@code false} otherwise
	 */
	@Override
	public final boolean contains(final Object obj) {
		return this.set.contains(obj);
	}

	/**
	 * Returns {@code true} if the list contains <b>all</b> of the objects within the specified {@link Collection}, {@code false} otherwise.
	 * <p>
	 * <b>Note:</b> this method uses the internal {@link SetHashed} to perform the operation.
	 *
	 * @param collection
	 *            the {@link Collection} to check whether or not all of its objects are present in the list
	 * @return {@code true} if the list contains <b>all</b> of the objects within the specified {@link Collection}, {@code false} otherwise
	 */
	@Override
	public final boolean containsAll(final Collection<?> collection) {
		return this.set.containsAll(collection);
	}

	/**
	 * Returns {@code true} if the list contains <b>any</b> of the objects within the specified {@link Iterable}, {@code false} otherwise.
	 * <p>
	 * <b>Note:</b> this method uses the internal {@link SetHashed} to perform the operation.
	 *
	 * @param iterable
	 *            the {@link Iterable} to check whether or not any of its objects are present in the list
	 * @return {@code true} if the list contains <b>any</b> of the objects within the specified {@link Iterable}, {@code false} otherwise
	 */
	@Override
	public final boolean containsAny(final Iterable<? extends T> iterable) {
		return this.set.containsAny(iterable);
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
		this.set.add(obj);
		return this.list.echo(obj);
	}

	/**
	 * Returns {@code true} if the specified {@link Object} is equal to {@code this} object. This method calls the {@link ListArray#equals(Object)}
	 * method to determine equality.
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
		this.list.extract(extractor);
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
	 * Returns the hash code of the list. This method calls the {@link ListArray#hashCode()} method to determine the hash code.
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
		this.set.add(obj);
		this.list.insert(index, obj);
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
	public final void insertAll(final int index, final Collection<? extends T> collection) {
		this.set.addAll(collection);
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
		final T oldVal = this.list.remove(index);

		if (!this.list.contains(oldVal)) {
			this.set.remove(oldVal);
		}

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
		if (this.list.remove(obj)) {
			if (!this.list.contains(obj)) {
				this.set.remove(obj);
			}

			return true;
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
		if (this.list.removeAll(collection)) {
			for (final Object obj : collection) {
				if (!this.list.contains(obj)) {
					this.set.remove(obj);
				}
			}

			return true;
		}

		return false;
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
		if (this.list.replace(oldObj, newObj)) {
			this.set.add(newObj);

			if (!this.list.contains(oldObj)) {
				this.set.remove(oldObj);
			}

			return true;
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
		if (this.list.retainAll(collection)) {
			this.set.retainAll(this.list);

			return true;
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
		final T oldVal = this.list.set(index, obj);
		this.set.add(obj);

		if (!this.list.contains(oldVal)) {
			this.set.remove(oldVal);
		}

		return oldVal;
	}

	/**
	 * Shuffles the contents of the {@link ListHashed} using the Knuth shuffling algorithm.
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
	 * @return a {@link ListHashed} that is the sub-list of the list starting at {@code fromIndex}
	 */
	@Override
	public final ListHashed<T> subList(final int fromIndex) {
		final RootList<T> subList = this.list.subList(fromIndex, this.list.size());
		final SetHashed<T> subSet = new SetHashed<>(subList);

		return new ListHashed<>(subList, subSet);
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @param toIndex
	 *            the index the end the sub-list
	 * @return a {@link ListHashed} that is the sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}
	 */
	@Override
	public final ListHashed<T> subList(final int fromIndex, final int toIndex) {
		final RootList<T> subList = this.list.subList(fromIndex, toIndex);
		final SetHashed<T> subSet = new SetHashed<>(subList);

		return new ListHashed<>(subList, subSet);
	}

	/**
	 * Returns a subset of the list starting at {@code fromIndex}.
	 *
	 * @param fromIndex
	 *            the index to start the subset
	 * @return a {@link RootSet} that is the subset of the list starting at {@code fromIndex}
	 */
	@Override
	public final RootSet<T> subset(final int fromIndex) {
		return this.list.subset(fromIndex, this.list.size());
	}

	/**
	 * Returns a subset of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}.
	 *
	 * @param fromIndex
	 *            the index to start the subset
	 * @param toIndex
	 *            the index the end the subset
	 * @return a {@link RootSet} that is the subset of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}
	 */
	@Override
	public final RootSet<T> subset(final int fromIndex, final int toIndex) {
		return this.list.subset(fromIndex, toIndex);
	}

	/**
	 * Returns a {@code T[]} array representation of the list.
	 *
	 * @return a {@code T[]} array representation of the list
	 */
	@Override
	public final T[] toArray() {
		return this.list.toArray();
	}

	/**
	 * Returns an {@code E[]} array representation of the list.
	 *
	 * @return an {@code E[]} array representation of the list
	 */
	@Override
	public final <E> E[] toArray(final E[] array) {
		return this.list.toArray(array);
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
		return this.set.clone();
	}

	/**
	 * Returns a {@link String} representation of the list.
	 *
	 * @return a {@link String} representation of the list
	 */
	@Override
	public final String toString() {
		return this.list.toString();
	}

} // End ListArrayHashed
