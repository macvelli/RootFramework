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

import java.util.Collection;

import root.annotation.Builder;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements in the set
 */
@Builder
public final class SetBuilder<T> implements root.lang.Builder {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final SetHashed<T> set;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public SetBuilder() {
		this.set = new SetHashed<>();
	}

	public SetBuilder(final int capacity) {
		this.set = new SetHashed<>(capacity);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final SetBuilder<T> add(final T t) {
		this.set.add(t);
		return this;
	}

	public final SetBuilder<T> addAll(final Collection<? extends T> collection) {
		this.set.addAll(collection);
		return this;
	}

	@Override
	public final SetImmutable<T> build() {
		return this.set.toImmutable();
	}

} // End SetBuilder
