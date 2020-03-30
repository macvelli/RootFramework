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

import java.util.Iterator;

/**
 * Extends the {@link Iterable} interface to incorporate the {@link Itemizer} concept. The <code>iterator()</code> method returns an {@link Itemizer}
 * which extends {@link Iterator} with additional methods that are useful in various situations.
 * <p>
 * The following methods are defined here for {@link Itemizable} implementations as they are generally useful extensions of the {@link Iterable}
 * interface.
 * <table cellpadding="5">
 * <tr>
 * <th>Methods</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>{@link #clear()}</td>
 * <td>Clears this instance of all items it currently holds</td>
 * </tr>
 * <tr>
 * <td>{@link #getSize()}</td>
 * <td>Returns the size of this instance which represents the number of items it currently holds</td>
 * </tr>
 * <tr>
 * <td>{@link #isEmpty()}</td>
 * <td>Returns <code>true</code> if the size of this instance is zero, <code>false</code> otherwise</td>
 * </tr>
 * </table>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements returned by the {@link Itemizer}
 */
public interface Itemizable<T> extends Iterable<T> {

	/**
	 * Removes all of the elements from this {@link Itemizable} (optional operation).
	 */
	void clear();

	/**
	 * Returns the number of elements in this {@link Itemizable} implementation.
	 *
	 * @return the number of elements in this {@link Itemizable} implementation
	 */
	int getSize();

	/**
	 * Returns <code>true</code> if this {@link Itemizable} contains no elements, <code>false</code> otherwise.
	 *
	 * @return <code>true</code> if this {@link Itemizable} contains no elements, <code>false</code> otherwise
	 */
	boolean isEmpty();

	/**
	 * Returns an {@link Itemizer} over a set of elements of type T.
	 *
	 * @return an {@link Itemizer} over a set of elements of type T
	 */
	@Override
	Itemizer<T> iterator();

} // End Itemizable
