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

import root.lang.ImmutableListItemizer;
import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.random.RNG;
import root.util.Root;
import root.validation.IndexOutOfBoundsException;

/**
 * Single-linked list implementation of the {@link RootList} interface.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the list
 */
public final class ListLinked<T> implements RootList<T> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	int size;
	Node<T> head;
	Node<T> tail;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor.
	 */
	public ListLinked() {
		// No-op default constructor
	}

	/**
	 * A constructor that adds all of the elements within the {@link Collection} to this list upon creation.
	 *
	 * @param collection
	 *            the {@link Collection} to add to the list upon creation
	 */
	public ListLinked(final Collection<? extends T> collection) {
		this.addAll(collection);
	}

	/**
	 * A constructor that takes an array and uses it to initialize the list upon creation.
	 *
	 * @param array
	 *            the array to initialize the list with upon creation
	 */
	@SafeVarargs
	public ListLinked(final T... array) {
		this.addAll(array, 0, array.length);
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

		final Node<T> node = new Node<T>(obj);

		if (index == this.size) {
			if (this.tail == null) {
				this.head = node;
			} else {
				this.tail.next = node;
			}

			this.tail = node;
		} else if (index == 0) {
			node.next = this.head;
			this.head = node;
		} else {
			Node<T> prev = this.head;
			for (int i = 1; i < index; i++) {
				prev = prev.next;
			}

			node.next = prev.next;
			prev.next = node;
		}

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
		final Node<T> node = new Node<T>(obj);

		if (this.tail == null) {
			this.head = node;
		} else {
			this.tail.next = node;
		}

		this.tail = node;
		this.size++;

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
			final Iterator<? extends T> iterator = collection.iterator();
			Node<T> node = new Node<T>(iterator.next());

			if (this.tail == null) {
				this.head = node;
			} else {
				this.tail.next = node;
			}

			this.tail = node;

			while (iterator.hasNext()) {
				node = new Node<T>(iterator.next());
				this.tail.next = node;
				this.tail = node;
			}

			this.size += collection.size();

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
	public final boolean addAll(final int index, final Collection<? extends T> collection) {
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		if (collection != null && collection.size() > 0) {
			Node<T> collHead = null, collTail = null, node;

			for (final T obj : collection) {
				node = new Node<T>(obj);

				if (collTail == null) {
					collHead = node;
				} else {
					collTail.next = node;
				}

				collTail = node;
			}

			if (index == this.size) {
				if (this.tail == null) {
					this.head = collHead;
				} else {
					this.tail.next = collHead;
				}

				this.tail = collTail;
			} else if (index == 0) {
				collTail.next = this.head;
				this.head = collHead;
			} else {
				Node<T> prev = this.head;
				for (int i = 1; i < index; i++) {
					prev = prev.next;
				}

				collTail.next = prev.next;
				prev.next = collHead;
			}

			this.size += collection.size();
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
		if (length > 0) {
			Node<T> node = new Node<T>(array[offset]);

			if (this.tail == null) {
				this.head = node;
			} else {
				this.tail.next = node;
			}

			this.tail = node;

			final int endLoop = offset + length;
			for (int i = offset + 1; i < endLoop; i++) {
				node = new Node<T>(array[i]);

				this.tail.next = node;
				this.tail = node;
			}

			this.size += length;
		}
	}

	/**
	 * Clears the list.
	 */
	@Override
	public final void clear() {
		Node.clear(this.head);

		this.head = null;
		this.tail = null;
		this.size = 0;
	}

	/**
	 * Returns a shallow copy of this {@link ListLinked} instance. (The elements themselves are not copied.)
	 *
	 * @return a shallow copy of this {@link ListLinked} instance
	 */
	@Override
	public final ListLinked<T> clone() {
		return new ListLinked<>(this);
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
		for (Node<T> n = this.head; n != null; n = n.next) {
			if (Root.equals(n.data, obj)) {
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
		Node<T> n;

		items: for (final Object obj : collection) {
			for (n = this.head; n != null; n = n.next) {
				if (Root.equals(n.data, obj)) {
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
		Node<T> n;

		for (final T t : iterable) {
			for (n = this.head; n != null; n = n.next) {
				if (Root.equals(n.data, t)) {
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
	public final boolean equals(final Object obj) {
		return Node.equals(obj, this.head, this.size);
	}

	/**
	 * Extracts a {@link String} representation of the list.
	 *
	 * @param extractor
	 *            the {@link StringExtractor} to populate
	 */
	@Override
	public final void extract(final StringExtractor extractor) {
		Node.extract(extractor, this.head);
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
		if (index >= this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		Node<T> n = this.head;
		for (int i = 0; i < index; i++) {
			n = n.next;
		}

		return n.data;
	}

	/**
	 * Returns the capacity of the list.
	 *
	 * @return the capacity of the list
	 */
	@Override
	public final int getCapacity() {
		return Integer.MAX_VALUE;
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
		return Node.hashCode(this.head, this.size);
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
		int i = 0;

		for (Node<T> n = this.head; n != null; n = n.next, i++) {
			if (Root.equals(n.data, obj)) {
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

		final Node<T> node = new Node<T>(obj);

		if (index == this.size) {
			if (this.tail == null) {
				this.head = node;
			} else {
				this.tail.next = node;
			}

			this.tail = node;
		} else if (index == 0) {
			node.next = this.head;
			this.head = node;
		} else {
			Node<T> prev = this.head;
			for (int i = 1; i < index; i++) {
				prev = prev.next;
			}

			node.next = prev.next;
			prev.next = node;
		}

		this.size++;
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
		if (index > this.size || index < 0) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		if (collection != null && collection.size() > 0) {
			Node<T> collHead = null, collTail = null, node;

			for (final T obj : collection) {
				node = new Node<T>(obj);

				if (collTail == null) {
					collHead = node;
				} else {
					collTail.next = node;
				}

				collTail = node;
			}

			if (index == this.size) {
				if (this.tail == null) {
					this.head = collHead;
				} else {
					this.tail.next = collHead;
				}

				this.tail = collTail;
			} else if (index == 0) {
				collTail.next = this.head;
				this.head = collHead;
			} else {
				Node<T> prev = this.head;
				for (int i = 1; i < index; i++) {
					prev = prev.next;
				}

				collTail.next = prev.next;
				prev.next = collHead;
			}

			this.size += collection.size();
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
		return new Node.Iterator<T>(this.head, this.size);
	}

	/**
	 * Returns the last element of the list, or {@code null} if the list is empty.
	 *
	 * @return the last element of the list, or {@code null} if the list is empty
	 */
	@Override
	public final T last() {
		return this.tail == null ? null : this.tail.data;
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
		Node<T> node = this.head;
		int lastIndex = -1;

		for (int i = 0; node != null; i++) {
			if (Root.equals(node.data, obj)) {
				lastIndex = i;
			}

			node = node.next;
		}

		return lastIndex;
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
		if (this.size == 0) {
			return null;
		}

		final int index = rng.nextIndex(this.size);

		Node<T> n = this.head;
		for (int i = 0; i < index; i++) {
			n = n.next;
		}

		return n.data;
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

		Node<T> prev = null, node = this.head;
		for (int i = 0; i < index; i++) {
			prev = node;
			node = node.next;
		}

		if (this.head == node) {
			this.head = this.head.next;
		}

		if (this.tail == node) {
			this.tail = prev;
		}

		if (prev != null) {
			prev.next = node.next;
		}

		final T oldVal = node.data;
		node.data = null;
		node.next = null;
		node = null;
		this.size--;

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
		Node<T> prev = null, node = this.head;

		for (; node != null; prev = node, node = node.next) {
			if (Root.equals(node.data, obj)) {
				if (this.head == node) {
					this.head = this.head.next;
				}

				if (this.tail == node) {
					this.tail = prev;
				}

				if (prev != null) {
					prev.next = node.next;
				}

				node.data = null;
				node.next = null;
				node = null;
				this.size--;

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

		for (final Object obj : collection) {
			this.remove(obj);
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
		for (Node<T> n = this.head; n != null; n = n.next) {
			if (Root.equals(n.data, oldObj)) {
				n.data = newObj;
				n.hash = 0; // reset the cached hash value
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
			Node<T> prev = null, node = this.head, toDelete;
			final int origSize = this.size;

			for (; node != null;) {
				if (!collection.contains(node.data)) {
					toDelete = node;

					if (this.head == node) {
						this.head = this.head.next;
					}

					if (this.tail == node) {
						this.tail = prev;
					}

					if (prev != null) {
						prev.next = node.next;
					}

					node = node.next;

					toDelete.data = null;
					toDelete.next = null;
					toDelete = null;
					this.size--;
				} else {
					prev = node;
					node = node.next;
				}
			}

			return origSize != this.size;
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

		Node<T> n = this.head;
		for (int i = 0; i < index; i++) {
			n = n.next;
		}

		final T oldValue = n.data;
		n.data = obj;
		n.hash = 0; // reset the cached hash value
		return oldValue;
	}

	/**
	 * Shuffles the contents of the {@link ListLinked} using the Knuth shuffling algorithm.
	 *
	 * @param rng
	 *            The random number generator to use during the shuffle
	 */
	@Override
	public final void shuffle(final RNG rng) {
		if (this.size > 1) {
			int i = 0;

			// 1. Create an array of Nodes to make this easier to implement
			@SuppressWarnings("unchecked")
			final Node<T>[] nodes = new Node[this.size];
			for (Node<T> n = this.head; n != null; n = n.next) {
				nodes[i++] = n;
			}

			// 2. Shuffle the data elements using the Knuth shuffling algorithm
			int r, tempHash;
			T tempData;

			for (i = this.size; i > 1;) {
				r = rng.nextIndex(i--);
				tempData = nodes[i].data;
				tempHash = nodes[i].hash;

				nodes[i].data = nodes[r].data;
				nodes[i].hash = nodes[r].hash;

				nodes[r].data = tempData;
				nodes[r].hash = tempHash;
			}
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
	 * @return a {@link ListLinked} that is the sub-list of the list starting at {@code fromIndex}
	 */
	@Override
	public final ListLinked<T> subList(final int fromIndex) {
		return this.subList(fromIndex, this.size);
	}

	/**
	 * Returns a sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}.
	 *
	 * @param fromIndex
	 *            the index to start the sub-list
	 * @param toIndex
	 *            the index the end the sub-list
	 * @return a {@link ListLinked} that is the sub-list of the list starting at {@code fromIndex} and ending at {@code toIndex - 1}
	 */
	@Override
	public final ListLinked<T> subList(final int fromIndex, final int toIndex) {
		if (fromIndex >= toIndex || fromIndex < 0 || toIndex < 0) {
			throw new IndexOutOfBoundsException(fromIndex, toIndex);
		}

		if (toIndex > this.size) {
			throw new IndexOutOfBoundsException(toIndex, this.size);
		}

		final ListLinked<T> l = new ListLinked<>();

		int i = 0;
		Node<T> n = this.head;
		for (; i < fromIndex; i++) {
			n = n.next;
		}

		for (; i < toIndex; i++) {
			l.add(n.data);
			n = n.next;
		}

		return l;
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
		if (fromIndex >= toIndex || fromIndex < 0 || toIndex < 0) {
			throw new IndexOutOfBoundsException(fromIndex, toIndex);
		}

		if (toIndex > this.size) {
			throw new IndexOutOfBoundsException(toIndex, this.size);
		}

		final SetHashed<T> s = new SetHashed<>(toIndex - fromIndex);

		int i = 0;
		Node<T> n = this.head;
		for (; i < fromIndex; i++) {
			n = n.next;
		}

		for (; i < toIndex; i++) {
			s.add(n.data);
			n = n.next;
		}

		return s;
	}

	/**
	 * Returns a {@code T[]} array representation of the list.
	 *
	 * @return a {@code T[]} array representation of the list
	 */
	@Override
	public final T[] toArray() {
		final T[] array = Root.newArray(this.size);
		Node<T> node = this.head;

		for (int i = 0; i < this.size; i++) {
			array[i] = node.data;
			node = node.next;
		}

		return array;
	}

	/**
	 * Returns an {@code E[]} array representation of the list.
	 *
	 * @return an {@code E[]} array representation of the list
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final <E> E[] toArray(final E[] arrayParam) {
		final E[] array = Root.newArray(arrayParam, this.size);
		Node<T> node = this.head;

		for (int i = 0; i < this.size; i++) {
			array[i] = (E) node.data;
			node = node.next;
		}

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
		final StringExtractor extractor = new StringExtractor(this.size << 4);
		this.extract(extractor);
		return extractor.toString();
	}

} // End ListLinked
