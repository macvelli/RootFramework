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
package root.lang;

/**
 * A <a href="https://en.wikipedia.org/wiki/Delegation_pattern">Delegate Pattern</a> implementation that wraps an {@link Itemizer} and makes it
 * immutable by having the {@link #remove()} method always throw an {@link UnsupportedOperationException}.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the {@link Itemizer}
 */
public final class ImmutableItemizer<T> implements Itemizer<T> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	/**
	 * The {@link Itemizer} to delegate function calls to.
	 */
	private final Itemizer<? extends T> itemizer;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Creates an immutable version of the delegate {@link Itemizer} passed as the parameter.
	 *
	 * @param itemizer
	 *            the {@link Itemizer} to make immutable
	 */
	public ImmutableItemizer(final Itemizer<? extends T> itemizer) {
		this.itemizer = itemizer;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * @see root.lang.Itemizer#getIndex()
	 */
	@Override
	public final int getIndex() {
		return this.itemizer.getIndex();
	}

	/**
	 * @see root.lang.Itemizer#getSize()
	 */
	@Override
	public final int getSize() {
		return this.itemizer.getSize();
	}

	/**
	 * @see root.lang.Itemizer#hasNext()
	 */
	@Override
	public final boolean hasNext() {
		return this.itemizer.hasNext();
	}

	/**
	 * @see root.lang.Itemizer#iterator()
	 */
	@Override
	public final Itemizer<T> iterator() {
		return this;
	}

	/**
	 * @see root.lang.Itemizer#next()
	 */
	@Override
	public final T next() {
		return this.itemizer.next();
	}

	/**
	 * @throws UnsupportedOperationException
	 *
	 * @see root.lang.Itemizer#remove()
	 */
	@Override
	public final void remove() {
		throw new UnsupportedOperationException("Cannot remove items from an ImmutableItemizer");
	}

	/**
	 * @see root.lang.Itemizer#reset()
	 */
	@Override
	public final void reset() {
		this.itemizer.reset();
	}

} // End ImmutableItemizer
