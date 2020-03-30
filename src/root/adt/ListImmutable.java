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
import root.lang.ImmutableItemizer;
import root.lang.ImmutableListItemizer;
import root.lang.StringExtractor;
import root.random.RNG;

/**
 * Turns any {@link RootList} into an immutable list.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the list
 */
@Delegate
public final class ListImmutable<T> implements RootList<T> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	final RootList<T> list;
	int hashCode;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * A constructor that adds all of the elements within the {@link Collection} to this list upon creation.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list upon creation
	 */
	public ListImmutable(final Collection<? extends T> collection) {
		@SuppressWarnings("unchecked")
		final T[] array = (T[]) collection.toArray();

		this.list = new ListArray<>(array);
	}

	/**
	 * A constructor that uses the {@link RootList} parameter to initialize this list. A call to {@link RootList#clone()} is made to make sure the
	 * delegate list cannot be modified outside {@link ListImmutable}.
	 *
	 * @param rootList
	 *            the {@link RootList} to use
	 */
	public ListImmutable(final RootList<T> list) {
		this.list = list.clone();
	}

	/**
	 * A constructor that takes an array and uses it to initialize the list upon creation.
	 *
	 * @param array
	 *            the array to initialize the list with upon creation
	 */
	@SafeVarargs
	public ListImmutable(final T... array) {
		this.list = new ListArray<>(array);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final void add(final int index, final T obj) {
		throw new UnsupportedOperationException("Cannot add items to a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final boolean add(final T obj) {
		throw new UnsupportedOperationException("Cannot add items to a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final boolean addAll(final Collection<? extends T> collection) {
		throw new UnsupportedOperationException("Cannot add items to a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final boolean addAll(final int index, final Collection<? extends T> collection) {
		throw new UnsupportedOperationException("Cannot add items to a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final void addAll(final T[] array, final int offset, final int length) {
		throw new UnsupportedOperationException("Cannot add items to a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final void clear() {
		throw new UnsupportedOperationException("Cannot clear items from a ListImmutable");
	}

	/**
	 * Returns {@code this} object reference since it doesn't matter about having different objects for immutables since they cannot be modified
	 * anyway.
	 *
	 * @return {@code this} object reference
	 */
	@Override
	public final ListImmutable<T> clone() {
		return this;
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
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final T echo(final T obj) {
		throw new UnsupportedOperationException("Cannot add items to a ListImmutable");
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
	public final boolean equals(final Object obj) {
		return this.list.equals(obj);
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
	 * Returns the hash code of the list.
	 *
	 * @return the hash code of the list
	 */
	@Override
	public final int hashCode() {
		if (this.hashCode == 0) {
			this.hashCode = this.list.hashCode();
		}

		return this.hashCode;
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
		return this.list.indexOf(obj);
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final void insert(final int index, final T obj) {
		throw new UnsupportedOperationException("Cannot add items to a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final void insertAll(final int index, final Collection<? extends T> collection) {
		throw new UnsupportedOperationException("Cannot add items to a ListImmutable");
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
	 * Returns an {@link ImmutableItemizer} for the list.
	 *
	 * @return an {@link ImmutableItemizer} for the list
	 */
	@Override
	public final ImmutableItemizer<T> iterator() {
		return new ImmutableItemizer<>(this.list.iterator());
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
		return this.list.random(rng);
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final T remove(final int index) {
		throw new UnsupportedOperationException("Cannot remove items from a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final boolean remove(final Object obj) {
		throw new UnsupportedOperationException("Cannot remove items from a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final boolean removeAll(final Collection<?> collection) {
		throw new UnsupportedOperationException("Cannot remove items from a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final boolean replace(final T oldObj, final T newObj) {
		throw new UnsupportedOperationException("Cannot modify a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final boolean retainAll(final Collection<?> collection) {
		throw new UnsupportedOperationException("Cannot modify a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final T set(final int index, final T obj) {
		throw new UnsupportedOperationException("Cannot modify a ListImmutable");
	}

	/**
	 * Throws {@link UnsupportedOperationException}.
	 *
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final void shuffle(final RNG rng) {
		throw new UnsupportedOperationException("Cannot modify a ListImmutable");
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
	 * @return a {@link RootList} that is the sub-list of the list starting at {@code fromIndex}
	 */
	@Override
	public final RootList<T> subList(final int fromIndex) {
		return this.list.subList(fromIndex, this.list.size());
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @param toIndex
	 *            the index the end the sub-list
	 * @return a {@link RootList} that is the sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}
	 */
	@Override
	public final RootList<T> subList(final int fromIndex, final int toIndex) {
		return this.list.subList(fromIndex, toIndex);
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
		return this;
	}

	/**
	 * Returns a set containing all the elements of the list.
	 *
	 * @return a {@link RootSet} containing all the elements of the list
	 */
	@Override
	public final RootSet<T> toSet() {
		return this.list.toSet();
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

} // End ListImmutable
