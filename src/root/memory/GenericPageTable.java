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
package root.memory;

import java.util.Collection;

import root.annotation.Incorrect;
import root.annotation.Todo;
import root.annotation.Unfinished;
import root.util.Root;
import root.validation.IndexOutOfBoundsException;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements stored in the page table
 */
@Todo({ "Any chance of turning this into a native implementation?",
		"Need a 'PagedList' that only pulls in data that qualify for the page window to display (i.e. pageSize = * 10, page = 3, only pull elements 21 thru 30)",
		"Actually I think a 'PagedList' can just be a ListItemizer view of an existing list...but you still need to keep track of which page you are on, how many pages there are in total, etc" })
@Unfinished("Need to incorporate size into this class and fix the incorrect and unfinished methods")
public class GenericPageTable<T> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int capacity;
	private int size;

	private T[][] pageTable;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	@SuppressWarnings("unchecked")
	public GenericPageTable() {
		this.capacity = 128;
		this.pageTable = (T[][]) new Object[8][];
	}

	@SuppressWarnings("unchecked")
	public GenericPageTable(final int numPages) {
		this.capacity = numPages << 4;
		this.pageTable = (T[][]) new Object[numPages][];
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Unfinished("Implement")
	public final void add(final T t) {

	}

	@Incorrect("Needs to do range checking to ensure I can copy all elements into the destination array")
	public final void arraycopy(int srcPos, final Object[] dest, int destPos, final int length) {
		int i = 0;

		for (int pageIndex = srcPos >> 4; i < length && pageIndex < this.pageTable.length; pageIndex++) {
			final T[] page = this.pageTable[pageIndex];

			for (int offset = srcPos - (pageIndex << 4); i < length && offset < page.length; i++, srcPos++, offset++) {
				dest[destPos++] = page[offset];
			}
		}
	}

	@Incorrect("This needs to work with size")
	public final void clear() {
		T[] page;
		int offset;

		for (int pageIndex = 0; pageIndex < this.pageTable.length; pageIndex++) {
			page = this.pageTable[pageIndex];

			if (page != null) {
				for (offset = 0; offset < page.length; offset++) {
					page[offset] = null;
				}

				this.pageTable[pageIndex] = null;
			}
		}
	}

	public final T get(final int index) {
		if (index >= this.size) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		final int pageIndex = index >> 4;
		final int pageOffset = index - (pageIndex << 4);
		final T[] page = this.pageTable[pageIndex];

		return page == null ? null : page[pageOffset];
	}

	@Incorrect("This needs to move data to make room for the additional elements in the collection")
	public final void insertAll(int index, final Collection<? extends T> collection) {
		final int lastIndex = index + collection.size();

		if (lastIndex >= this.capacity) {
			this.resize(lastIndex);
		}

		// Incorrect: Rewrite this to take advantage of knowledge of page boundaries
		for (final T t : collection) {
			final int pageIndex = index >> 4;
			final T[] page = this.loadPage(pageIndex);
			page[index - (pageIndex << 4)] = t;
			index++;
		}
	}

	@Incorrect("This needs to move data to make room for the additional elements in the array")
	public final void insertAll(int index, final T[] array) {
		final int lastIndex = index + array.length;

		if (lastIndex >= this.capacity) {
			this.resize(lastIndex);
		}

		// Incorrect: Rewrite this to take advantage of knowledge of page boundaries
		for (final T t : array) {
			final int pageIndex = index >> 4;
			final T[] page = this.loadPage(pageIndex);
			page[index - (pageIndex << 4)] = t;
			index++;
		}
	}

	public final T set(final int index, final T t) {
		if (index >= this.size) {
			throw new IndexOutOfBoundsException(index, this.size);
		}

		final int pageIndex = index >> 4;
		final int pageOffset = index - (pageIndex << 4);
		final T[] page = this.pageTable[pageIndex];

		final T oldValue = page[pageOffset];
		page[pageOffset] = t;
		return oldValue;
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	@Incorrect("I am pretty sure this either goes away or is rewritten")
	private T[] loadPage(final int pageIndex) {
		T[] page = this.pageTable[pageIndex];

		if (page == null) {
			page = Root.newArray(16);
			this.pageTable[pageIndex] = page;
		}

		return page;
	}

	@SuppressWarnings("unchecked")
	@Incorrect("This definitely needs to be rewritten")
	private void resize(final int index) {
		int shiftCount = 0;

		do {
			this.capacity <<= 1;
			shiftCount++;
		} while (this.capacity <= index);

		final T[][] array = (T[][]) new Object[this.pageTable.length << shiftCount][];
		System.arraycopy(this.pageTable, 0, array, 0, this.pageTable.length);
		this.pageTable = array;
	}

} // End GenericPageTable
