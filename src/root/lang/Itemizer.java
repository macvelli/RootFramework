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
 * The {@link Itemizer} class combines the {@link Iterator} and {@link Iterable} interfaces since an {@link Iterator} is by its very nature is
 * {@link Iterable}.
 * <p>
 * The following methods are defined here for {@link Itemizer} implementations as they are generally useful extensions of the {@link Iterator}
 * interface.
 * <table cellpadding="5">
 * <tr>
 * <th>Methods</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>{@link #getIndex()}</td>
 * <td>Returns the index (starting at zero) of the current element being iterated over</td>
 * </tr>
 * <tr>
 * <td>{@link #getSize()}</td>
 * <td>Returns the size of the instance which is being iterated over</td>
 * </tr>
 * <tr>
 * <td>{@link #reset()}</td>
 * <td>Resets this {@link Itemizer} so that it can be reused to iterate again from the beginning</td>
 * </tr>
 * </table>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of the elements to iterate over
 */
public interface Itemizer<T> extends Iterator<T>, Iterable<T> {

	/**
	 * Returns the current index of the {@link Itemizer}
	 *
	 * @return the current index of the {@link Itemizer}
	 */
	int getIndex();

	/**
	 * Returns the number of elements in the {@link Itemizer}
	 *
	 * @return the number of elements in the {@link Itemizer}
	 */
	int getSize();

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	boolean hasNext();

	/**
	 * The sole purpose of this method is to enable an {@link Itemizer} to be iterated over. Hence, this method should <b>ALWAYS</b> return
	 * <code>this</code>.
	 *
	 * @return should <b>ALWAYS</b> return <code>this</code>
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	Itemizer<T> iterator();

	/**
	 * @see java.util.Iterator#next()
	 */
	@Override
	T next();

	/**
	 * @see java.util.Iterator#remove()
	 */
	@Override
	void remove();

	/**
	 * Resets the {@link Itemizer} to the beginning so it can be reused to iterate over the same items without calling <code>iterator()</code> again.
	 */
	void reset();

} // End Itemizer
