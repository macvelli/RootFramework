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

import root.lang.Extractable;
import root.lang.Immutable;
import root.lang.Itemizable;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <K>
 *            The key type of the map
 * @param <V>
 *            The value type of the map
 */
public interface RootMap<K, V> extends java.util.Map<K, V>, Itemizable<MapEntry<K, V>>, Extractable, Cloneable, Immutable {

	/**
	 * @see java.util.Map#clear()
	 */
	@Override
	void clear();

	/**
	 * @see Object#clone()
	 */
	RootMap<K, V> clone();

	/**
	 * Returns {@code true} if {@code value} exist under {@code key}, {@code false} otherwise.
	 *
	 * @param key
	 * @param value
	 * @return {@code true} if {@code value} exist under {@code key}
	 */
	boolean containsEntry(Object key, Object value);

	/**
	 * @see java.util.Map#containsKey(Object)
	 */
	@Override
	boolean containsKey(Object key);

	/**
	 * @see java.util.Map#containsValue(Object)
	 */
	@Override
	boolean containsValue(Object value);

	/**
	 * @see java.util.Map#entrySet()
	 */
	@Override
	java.util.Set<java.util.Map.Entry<K, V>> entrySet();

	/**
	 * @see java.util.Map#equals(Object)
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * Returns the {@code value} associated with {@code key}, or puts and returns a new instance of {@link Class} using its default constructor.
	 *
	 * @param key
	 * @param clazz
	 * @return the {@code value} associated with {@code key}, or puts and returns a new instance of {@link Class} using its default constructor
	 */
	V get(K key, Class<? extends V> clazz);

	/**
	 * Returns the {@code value} associated with {@code key}, or returns {@code defaultVal} if no entry is associated with {@code key}.
	 *
	 * @param key
	 * @param defaultVal
	 * @return the {@code value} associated with {@code key}, or returns {@code defaultVal} if no entry is associated with {@code key}
	 */
	V get(K key, V defaultVal);

	/**
	 * @see java.util.Map#get(Object)
	 */
	@Override
	V get(Object key);

	/**
	 * Returns the capacity of the map.
	 *
	 * @return the capacity of the map
	 */
	int getCapacity();

	/**
	 * @see root.lang.Itemizable#getSize()
	 */
	@Override
	int getSize();

	/**
	 * @see java.util.Map#hashCode()
	 */
	@Override
	int hashCode();

	/**
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	boolean isEmpty();

	/**
	 * @see java.util.Map#keySet()
	 */
	@Override
	java.util.Set<K> keySet();

	/**
	 * @see java.util.Map#put(Object, Object)
	 */
	@Override
	V put(K key, V value);

	/**
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	void putAll(java.util.Map<? extends K, ? extends V> map);

	/**
	 * @see java.util.Map#remove(Object)
	 */
	@Override
	V remove(Object key);

	/**
	 * @see java.util.Map#size()
	 */
	@Override
	int size();

	/**
	 * @see Immutable#toImmutable()
	 */
	@Override
	RootMap<K, V> toImmutable();

	/**
	 * @see java.util.Map#values()
	 */
	@Override
	java.util.Collection<V> values();

} // End RootMap
