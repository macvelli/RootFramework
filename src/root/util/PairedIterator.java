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
package root.util;

import java.util.Iterator;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 */
public final class PairedIterator<T> implements Iterable<PairedIterator.Each<T>> {

	// <><><><><><><><><><><><><><> Inner Classes <><><><><><><><><><><><><><>

	public static class Each<E> implements Iterator<Each<E>> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private E original;
		private E updated;

		private final Iterator<E> originalItr;
		private final Iterator<E> updatedItr;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private Each(final Iterator<E> original, final Iterator<E> updated) {
			this.originalItr = original;
			this.updatedItr = updated;
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		public E getOriginal() {
			return this.original;
		}

		public E getUpdated() {
			return this.updated;
		}

		@Override
		public boolean hasNext() {
			return this.originalItr.hasNext() || this.updatedItr.hasNext();
		}

		@Override
		public Each<E> next() {
			this.original = this.originalItr.hasNext() ? this.originalItr.next() : null;
			this.updated = this.updatedItr.hasNext() ? this.updatedItr.next() : null;

			return this;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	} // End Each

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final Iterable<T> original;

	private final Iterable<T> updated;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public PairedIterator(final Iterable<T> original, final Iterable<T> updated) {
		this.original = original;
		this.updated = updated;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public Iterator<Each<T>> iterator() {
		return new Each<T>(this.original.iterator(), this.updated.iterator());
	}

} // End PairedIterator
