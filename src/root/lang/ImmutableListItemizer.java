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
 * A <a href="https://en.wikipedia.org/wiki/Delegation_pattern">Delegate Pattern</a> implementation that wraps a {@link ListItemizer} and makes it
 * immutable by having the {@link #add(Object)}, {@link #remove()}, and {@link #set(Object)} methods always throw an
 * {@link UnsupportedOperationException}.
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the {@link ListItemizer}
 */
public final class ImmutableListItemizer<T> implements ListItemizer<T> {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	/**
	 * The {@link ListItemizer} to delegate function calls to.
	 */
	private final ListItemizer<T> itemizer;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Creates an immutable version of the delegate {@link ListItemizer} passed as the parameter.
	 *
	 * @param listItemizer
	 *            the {@link ListItemizer} to make immutable
	 */
	public ImmutableListItemizer(final ListItemizer<T> listItemizer) {
		this.itemizer = listItemizer;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 *
	 * @param t
	 * @throws UnsupportedOperationException
	 * @see root.lang.ListItemizer#add(Object)
	 */
	@Override
	public final void add(final T t) {
		throw new UnsupportedOperationException();
	}

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

	@Override
	public final boolean hasPrevious() {
		return this.itemizer.hasPrevious();
	}

	/**
	 * @see root.lang.Itemizer#iterator()
	 */
	@Override
	public final ImmutableListItemizer<T> iterator() {
		return this;
	}

	/**
	 * @see root.lang.Itemizer#next()
	 */
	@Override
	public final T next() {
		return this.itemizer.next();
	}

	@Override
	public final int nextIndex() {
		return this.itemizer.nextIndex();
	}

	@Override
	public final T previous() {
		return this.itemizer.previous();
	}

	@Override
	public final int previousIndex() {
		return this.itemizer.previousIndex();
	}

	/**
	 * @throws UnsupportedOperationException
	 * @see root.lang.Itemizer#remove()
	 */
	@Override
	public final void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see root.lang.Itemizer#reset()
	 */
	@Override
	public final void reset() {
		this.itemizer.reset();
	}

	/**
	 *
	 * @param t
	 * @throws UnsupportedOperationException
	 * @see root.lang.ListItemizer#set(Object)
	 */
	@Override
	public final void set(final T t) {
		throw new UnsupportedOperationException();
	}

} // End ImmutableListItemizer
