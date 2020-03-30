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

import java.util.NoSuchElementException;

import root.lang.Extractable;
import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.util.Root;

/**
 * This {@link Collector} simply collects {@code char[]} arrays.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class CollectorCharArray implements Collector<char[]>, Extractable {

	// <><><><><><><><><><><><><><>< Private Classes <><><><><><><><><><><><><>

	/**
	 * An {@link Itemizer} for the {@code CollectorCharArray}.
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private final class Iterator implements Itemizer<char[]> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private int index;

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final int getIndex() {
			return this.index - 1;
		}

		@Override
		public final int getSize() {
			return CollectorCharArray.this.size;
		}

		@Override
		public final boolean hasNext() {
			return this.index < CollectorCharArray.this.size;
		}

		@Override
		public final Itemizer<char[]> iterator() {
			return this;
		}

		@Override
		public final char[] next() {
			if (this.index == CollectorCharArray.this.size) {
				throw new NoSuchElementException();
			}

			return CollectorCharArray.this.values[this.index++];
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
	char[][] values;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Default constructor. Uses a default size of 8 for the internal {@code values} array.
	 */
	public CollectorCharArray() {
		this.values = new char[8][];
	}

	/**
	 * A constructor that accepts a predetermined capacity. Uses a default size of 8 for the internal {@code values} array if the specified capacity
	 * is less than 8.
	 *
	 * @param capacity
	 *            the predetermined capacity
	 */
	public CollectorCharArray(final int capacity) {
		this.values = new char[Root.max(8, capacity)][];
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Adds a {@code char[]} array to the collector.
	 *
	 * @param array
	 *            the {@code char[]} array to add
	 */
	@Override
	public final void add(final char[] array) {
		if (this.size == this.values.length) {
			this.resize();
		}

		this.values[this.size++] = array;
	}

	/**
	 * Adds a {@link CharSequence} to the collector.
	 *
	 * @param charSeq
	 *            the {@link CharSequence} to add
	 */
	public final void add(final CharSequence charSeq) {
		if (this.size == this.values.length) {
			this.resize();
		}

		final char[] c = new char[charSeq.length()];
		for (int i = 0; i < c.length; i++) {
			c[i] = charSeq.charAt(i);
		}

		this.values[this.size++] = c;
	}

	/**
	 * Adds the {@code char[]} array from a {@link String} to the collector.
	 *
	 * @param str
	 *            the {@link String} whose {@code char[]} array to add
	 */
	public final void add(final String str) {
		if (this.size == this.values.length) {
			this.resize();
		}

		this.values[this.size++] = Root.toCharArray(str);
	}

	/**
	 * Adds the {@code char[]} array from a call to {@link StringExtractor#toArray()} to the collector.
	 *
	 * @param extractor
	 *            the {@link StringExtractor} whose {@code char[]} array to add by calling {@link StringExtractor#toArray()}
	 */
	public final void add(final StringExtractor extractor) {
		if (this.size == this.values.length) {
			this.resize();
		}

		this.values[this.size++] = extractor.toArray();
	}

	/**
	 * Clears the collector.
	 */
	@Override
	public final void clear() {
		for (int i = 0; i < this.size; i++) {
			this.values[i] = null;
		}

		this.size = 0;
	}

	/**
	 * Returns {@code true} if the specified {@link Object} is equal to {@code this} object. The specified {@link Object} is equal to {@code this}
	 * object if:
	 * <ul>
	 * <li>The {@link Class} of the specified {@link Object} is equal to {@link CollectorCharArray}</li>
	 * <li>The {@code size} of the specified {@link CollectorCharArray} and {@code this} object are equal</li>
	 * <li>All {@code char[]} arrays in both the specified {@link CollectorCharArray} and {@code this} object are equal to each other</li>
	 * </ul>
	 *
	 * @param obj
	 *            the specified {@link Object} to compare for equality to {@code this} object
	 * @return {@code true} if the specified {@link Object} is equal to {@code this} object, false otherwise
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (Root.equalToClass(obj, CollectorCharArray.class)) {
			final CollectorCharArray collector = (CollectorCharArray) obj;

			if (this.size == collector.size) {
				for (int i = 0; i < this.size; i++) {
					if (Root.notEqual(this.values[i], collector.values[i])) {
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}

	/**
	 * Extracts a {@link String} representation of the collector.
	 *
	 * @param extractor
	 *            the {@link StringExtractor} to populate
	 */
	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append('[');

		if (this.size > 0) {
			Root.extract(extractor, this.values[0]);

			for (int i = 1; i < this.size; i++) {
				extractor.append(',').append(' ');
				Root.extract(extractor, this.values[i]);
			}
		}

		extractor.append(']');
	}

	/**
	 * Returns the {@code char[]} array located at the specified index.
	 *
	 * @param index
	 *            the index of the item in the collector to return
	 * @return the {@code char[]} array located at the specified index
	 */
	@Override
	public final char[] get(final int index) {
		return this.values[index];
	}

	/**
	 * Returns the size of the collector, which is how many elements are actually in the collector.
	 *
	 * @return the size of the collector
	 */
	@Override
	public final int getSize() {
		return this.size;
	}

	/**
	 * Returns the hash code of the collector.
	 *
	 * @return the hash code of the collector
	 */
	@Override
	public final int hashCode() {
		int hash = this.size;
		char[] array;
		int j, arrayHash;

		for (int i = 0; i < this.size; i++) {
			array = this.values[i];

			if (array != null) {
				arrayHash = array.length;

				for (j = 0; j < array.length; j++) {
					arrayHash <<= 1;
					arrayHash += array[i];
				}

				hash ^= arrayHash;
			}
		}

		return hash;
	}

	/**
	 * Returns {@code true} if the collector is empty, which means its size is equal to zero.
	 *
	 * @return {@code true} if the collector is empty
	 */
	@Override
	public final boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Returns an {@link Itemizer} for the collector.
	 *
	 * @return an {@link Itemizer} for the collector
	 */
	@Override
	public final Itemizer<char[]> iterator() {
		return new Iterator();
	}

	/**
	 * Returns a {@code char[][]} array representation of the collector.
	 *
	 * @return a {@code char[][]} array representation of the collector
	 */
	@Override
	public final char[][] toArray() {
		final char[][] array = new char[this.size][];
		System.arraycopy(this.values, 0, array, 0, this.size);
		return array;
	}

	/**
	 * Returns a {@link String} representation of the collector.
	 *
	 * @return a {@link String} representation of the collector
	 */
	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(this.size << 4);
		this.extract(extractor);
		return extractor.toString();
	}

	// <><><><><><><><><><><><><><> Private Methods ><><><><><><><><><><><><><>

	/**
	 * Resizes the internal values array
	 */
	private void resize() {
		final char[][] array = new char[this.size << 1][];
		System.arraycopy(this.values, 0, array, 0, this.size);
		this.values = array;
	}

} // End CharArrayCollector
