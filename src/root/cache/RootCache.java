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
package root.cache;

import root.lang.Itemizable;
import root.lang.Itemizer;

/**
 * This interface defines the contract for a Root Cache implementation.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <K>
 *            The key type of the cache entry
 * @param <V>
 *            The value type of the cache entry
 */
public interface RootCache<K, V> extends Itemizable<V> {

	/**
	 * @see root.lang.Itemizable#clear()
	 */
	@Override
	void clear();

	/**
	 * Returns the value associated with the key, or <code>null</code> if no association exists.
	 *
	 * @param key
	 *            the key to lookup the value in the cache
	 * @return the value associated with the key, or <code>null</code> if no association exists
	 */
	V get(K key);

	/**
	 * Returns the capacity of the cache.
	 *
	 * @return the capacity of the cache
	 */
	int getCapacity();

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
	Itemizer<V> iterator();

	/**
	 * Puts element value V in the cache associated with key K.
	 *
	 * @param key
	 *            the key associated with element value V
	 * @param value
	 *            the value to store in the cache
	 * @return the previous value associated with key, or <code>null</code> if there was no previous
	 *         mapping for key
	 */
	V put(K key, V value);

	/**
	 * Removes and returns the element of type V that is associated with the key of type K, if
	 * present in the cache, <code>null</code> otherwise.
	 *
	 * @param key
	 *            the key to look up element value V in the cache
	 * @return the element of type V that is associated with the key of type K, if present in the
	 *         cache, <code>null</code> otherwise
	 */
	V remove(K key);

	/**
	 * Removes and returns the next element in the cache. What is defined as the "next" element is
	 * cache-specific.
	 *
	 * @return the next element in the cache
	 */
	V removeNext();

} // End RootCache
