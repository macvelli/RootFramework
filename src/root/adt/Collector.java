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

import root.lang.Itemizable;
import root.lang.Itemizer;

/**
 * A collector is a very basic data structure that simply accumulates objects. Those objects can then either be iterated over or converted to an
 * array.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of element collected by this {@link Collector}
 */
public interface Collector<T> extends Itemizable<T> {

	void add(T e);

	/**
	 * @see root.lang.Itemizable#clear()
	 */
	@Override
	void clear();

	T get(int index);

	/**
	 * @see root.lang.Itemizable#getSize()
	 */
	@Override
	int getSize();

	/**
	 * @see root.lang.Itemizable#isEmpty()
	 */
	@Override
	boolean isEmpty();

	/**
	 * @see root.lang.Itemizable#iterator()
	 */
	@Override
	Itemizer<T> iterator();

	T[] toArray();

} // End Collector
