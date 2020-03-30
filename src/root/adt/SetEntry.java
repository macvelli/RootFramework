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

import root.annotation.Todo;
import root.util.Root;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <K>
 *            The type of element in the set entry
 */
final class SetEntry<K> {

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	@SuppressWarnings("unchecked")
	@Todo("Why is this a) a static method, and b) in SetEntry? Or more likely, why isn't the same logic applied to MapHashed?")
	static final <T> SetEntry<T>[] newArray(final int capacity) {
		return new SetEntry[Root.calculateHashTableSize(capacity)];
	}

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	final K key;
	final int hash;
	SetEntry<K> next;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	SetEntry(final K key, final int h, final SetEntry<K> next) {
		this.key = key;
		this.hash = h;
		this.next = next;
	}

} // End SetEntry
