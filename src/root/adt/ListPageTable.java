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
import java.util.NoSuchElementException;

import root.annotation.NeedsTesting;
import root.annotation.Refactor;
import root.annotation.Todo;
import root.annotation.Unfinished;
import root.lang.Itemizer;
import root.lang.ListItemizer;
import root.lang.StringExtractor;
import root.memory.GenericPageTable;
import root.random.RNG;
import root.util.Root;
import root.validation.IndexOutOfBoundsException;

/**
 * <b>This needs a lot of work</b>
 * <p>
 * TODO: Put Itemizer<T> methods for addAll(), insertAll(), etc (done)<br>
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in this list
 */
@NeedsTesting
@Todo("Implement Cloneable")
@Unfinished("This was supposed to work one way but needs to work another way")
public final class ListPageTable<T> implements RootList<T> {

	private class Ascend implements Itemizer<T> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private int i;

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final int getIndex() {
			return this.i - 1;
		}

		@Override
		public final int getSize() {
			return ListPageTable.this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.i < ListPageTable.this.size;
		}

		@Override
		public final Itemizer<T> iterator() {
			return this;
		}

		@Override
		public final T next() {
			if (this.i >= ListPageTable.this.size) {
				throw new NoSuchElementException();
			}

			return ListPageTable.this.pageTable.get(this.i++);
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public final void reset() {
			this.i = 0;
		}

	} // End Ascend

	private final class ListPageTableItemizer implements ListItemizer<T> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private int currentPos;
		private final int startingPoint;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private ListPageTableItemizer(final int startingPoint) {
			this.currentPos = startingPoint;
			this.startingPoint = startingPoint;
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void add(final T t) {
			ListPageTable.this.add(this.currentPos++, t);
		}

		@Override
		public final int getIndex() {
			return this.currentPos - 1;
		}

		@Override
		public final int getSize() {
			return ListPageTable.this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.currentPos < ListPageTable.this.size;
		}

		@Override
		public final boolean hasPrevious() {
			return this.currentPos > 0;
		}

		@Override
		public final ListItemizer<T> iterator() {
			return this;
		}

		@Override
		public final T next() {
			if (this.currentPos == ListPageTable.this.size) {
				throw new NoSuchElementException();
			}

			return ListPageTable.this.pageTable.get(this.currentPos++);
		}

		@Override
		public final int nextIndex() {
			return this.currentPos;
		}

		@Override
		public final T previous() {
			if (this.currentPos == 0) {
				throw new NoSuchElementException();
			}

			return ListPageTable.this.pageTable.get(--this.currentPos);
		}

		@Override
		public final int previousIndex() {
			return this.currentPos - 1;
		}

		@Override
		public final void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public final void reset() {
			this.currentPos = this.startingPoint;
		}

		@Override
		@Refactor
		public final void set(final T t) {
			// final int index = this.currentPos - 1;
			//
			// if (index < 0) {
			// throw new IndexOutOfBoundsException(index, ListPageTable.this.size);
			// }
			//
			// ListPageTable.this.pageTable.put(index, t);
		}

	} // End ListPageTableItemizer

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int size;
	private final GenericPageTable<T> pageTable;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public ListPageTable() {
		this.pageTable = new GenericPageTable<>();
	}

	public ListPageTable(final int numPages) {
		this.pageTable = new GenericPageTable<>(numPages);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	@Unfinished
	public final void add(final int index, final T obj) {

	}

	@Override
	@Refactor
	public final boolean add(final T obj) {
		// this.pageTable.put(this.size, obj);
		// this.size++;
		return true;
	}

	@Override
	@Refactor
	public final boolean addAll(final Collection<? extends T> collection) {
		// this.pageTable.putAll(this.size, collection);
		// this.size += collection.size();
		return true;
	}

	@Override
	@Unfinished
	public final boolean addAll(final int index, final Collection<? extends T> collection) {
		return false;
	}

	@Override
	@Unfinished
	public final void addAll(final T[] array, final int offset, final int length) {

	}

	@Override
	@Refactor
	public final void clear() {
		this.pageTable.clear();
		this.size = 0;
	}

	@Override
	public RootList<T> clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Refactor
	public final boolean contains(final Object obj) {
		// TODO: This needs to iterate over the list of elements in the page table
		for (int i = 0; i < this.size; i++) {
			if (Root.equals(this.pageTable.get(i), obj)) {
				return true;
			}
		}

		return false;
	}

	@Override
	@Refactor
	public final boolean containsAll(final Collection<?> collection) {
		int i;

		items: for (final Object obj : collection) {
			// TODO: This needs to iterate over the list of elements in the page table
			for (i = 0; i < this.size; i++) {
				if (Root.equals(this.pageTable.get(i), obj)) {
					continue items;
				}
			}

			return false;
		}

		return true;
	}

	@Override
	@Refactor
	public final boolean containsAny(final Iterable<? extends T> collection) {
		for (final T t : collection) {
			for (int i = 0; i < this.size; i++) {
				// TODO: This needs to iterate over the list of elements in the page table
				if (Root.equals(this.pageTable.get(i), t)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	@Refactor
	public final T echo(final T obj) {
		// this.pageTable.put(this.size, obj);
		// this.size++;

		return obj;
	}

	@Override
	@Refactor
	public final boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof Iterable)) {
			return false;
		}

		final Iterable<?> i = (Iterable<?>) obj;

		final int j = 0;
		for (final Object t : i) {
			// TODO: This needs to iterate over the list of elements in the page table
			if (j == this.size) {
				return false;
			}

			if (Root.notEqual(this.pageTable.get(j), t)) {
				return false;
			}
		}

		return j == this.size;
	}

	@Override
	@Unfinished
	public final void extract(final StringExtractor extractor) {
		// TODO Auto-generated method stub
	}

	@Override
	@Refactor
	public final T get(final int index) {
		return this.pageTable.get(index);
	}

	@Override
	@Unfinished
	public final int getCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public final int getSize() {
		return this.size;
	}

	@Override
	@Refactor
	public final int hashCode() {
		int h = this.size;
		for (int i = 0; i < this.size; i++) {
			// TODO: This needs to iterate over the list of elements in the page table
			final T t = this.pageTable.get(i);
			if (t != null) {
				h ^= t.hashCode();
			}
			h <<= 1;
		}

		return h;
	}

	@Override
	@Refactor
	public final int indexOf(final Object obj) {
		for (int i = 0; i < this.size; i++) {
			// TODO: This needs to iterate over the list of elements in the page table
			if (Root.equals(this.pageTable.get(i), obj)) {
				return i;
			}
		}

		return -1;
	}

	@Override
	@Unfinished
	public final void insert(final int index, final T obj) {

	}

	@Override
	@Unfinished
	public void insertAll(final int index, final Collection<? extends T> collection) {

	}

	@Override
	public final boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public final Itemizer<T> iterator() {
		return new Ascend();
	}

	@Override
	@Refactor
	public final T last() {
		return this.size > 0 ? this.pageTable.get(this.size - 1) : null;
	}

	@Override
	@Refactor
	public final int lastIndexOf(final Object obj) {
		for (int i = this.size - 1; i >= 0; i--) {
			if (Root.equals(this.pageTable.get(i), obj)) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public final ListItemizer<T> listIterator() {
		return new ListPageTableItemizer(0);
	}

	@Override
	public final ListItemizer<T> listIterator(final int index) {
		return new ListPageTableItemizer(index);
	}

	@Override
	@Refactor
	public final T random(final RNG rng) {
		return this.pageTable.get(rng.nextIndex(this.size));
	}

	@Override
	@Unfinished
	public final T remove(final int index) {
		return null;
	}

	@Override
	@Unfinished
	public final boolean remove(final Object obj) {
		return false;
	}

	@Override
	@Unfinished
	public final boolean removeAll(final Collection<?> collection) {
		return false;
	}

	@Override
	@Refactor
	public final boolean replace(final T o, final T n) {
		// for (int i = 0; i < this.size; i++) {
		// if (Safe.equals(this.pageTable.get(i), o)) {
		// // TODO: This needs to iterate over the list of elements in the page table
		// this.pageTable.put(i, n);
		// return true;
		// }
		// }

		return false;
	}

	@Override
	@Unfinished
	public final boolean retainAll(final Collection<?> c) {
		return false;
	}

	@Override
	@Refactor
	public final T set(final int i, final T t) {
		if (i >= this.size || i < 0) {
			throw new IndexOutOfBoundsException(i, this.size);
		}

		final T oldValue = this.pageTable.get(i);
		// this.pageTable.put(i, t);
		return oldValue;
	}

	@Override
	@Refactor
	public final void shuffle(final RNG rng) {
		// for (int i = 0; i < this.size; i++) {
		// final int j = Random.nextIndex(this.size);
		// final T t = this.pageTable.get(i);
		// this.pageTable.put(i, this.pageTable.get(j));
		// this.pageTable.put(j, t);
		// }
	}

	@Override
	public final int size() {
		return this.size;
	}

	@Override
	@Refactor
	@Todo("This needs to return a ListPageTable")
	public final ListArray<T> subList(final int fromIndex) {
		return this.subList(fromIndex, this.size);
	}

	@Override
	@Refactor
	@Todo("This needs to return a ListPageTable")
	public final ListArray<T> subList(final int fromIndex, final int toIndex) {
		if (fromIndex >= toIndex || fromIndex < 0) {
			throw new IndexOutOfBoundsException(fromIndex, toIndex);
		}

		if (toIndex > this.size) {
			throw new IndexOutOfBoundsException(toIndex, this.size);
		}

		final int len = toIndex - fromIndex;
		final ListArray<T> a = new ListArray<T>(len);

		this.pageTable.arraycopy(fromIndex, a.values, 0, len);
		a.size = len;

		return a;
	}

	@Override
	public final SetHashed<T> subset(final int fromIndex) {
		return this.subset(fromIndex, this.size);
	}

	@Override
	@Refactor
	public final SetHashed<T> subset(final int fromIndex, final int toIndex) {
		if (fromIndex >= toIndex || fromIndex < 0) {
			throw new IndexOutOfBoundsException(fromIndex, toIndex);
		}

		if (toIndex > this.size) {
			throw new IndexOutOfBoundsException(toIndex, this.size);
		}

		final SetHashed<T> s = new SetHashed<T>(toIndex - fromIndex);

		for (int i = fromIndex; i < toIndex; i++) {
			s.add(this.pageTable.get(i));
		}

		return s;
	}

	@Override
	@Refactor
	public final T[] toArray() {
		final T[] array = Root.newArray(this.size);

		this.pageTable.arraycopy(0, array, 0, this.size);

		return array;
	}

	@Override
	@Refactor
	public final <E> E[] toArray(final E[] arrayParam) {
		final E[] array = Root.newArray(arrayParam, this.size);

		this.pageTable.arraycopy(0, array, 0, this.size);

		return array;
	}

	@Override
	public RootList<T> toImmutable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final SetHashed<T> toSet() {
		return this.subset(0, this.size);
	}

	@Override
	@Refactor
	public final String toString() {
		return new StringExtractor(this.size << 4).append(this.pageTable).toString();
	}

} // End ListPageTable
