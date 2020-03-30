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

import root.cache.CacheFixed;
import root.util.Root;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class FastInteger extends Number implements Extractable {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final long serialVersionUID = 4226154212440639958L;
	private static final CacheFixed<FastInteger> fastIntegerCache = new CacheFixed<>(256);

	static {
		// Initialize the FastInteger cache
		for (int i = 0; i < fastIntegerCache.getCapacity(); i++) {
			fastIntegerCache.put(i, new FastInteger(i));
		}
	}

	public static final FastInteger valueOf(final int integer) {
		final FastInteger cachedValue = fastIntegerCache.get(integer);

		return cachedValue != null ? cachedValue : new FastInteger(integer);
	}

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	public int value;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public FastInteger(final int value) {
		this.value = value;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final byte byteValue() {
		return (byte) this.value;
	}

	@Override
	public final double doubleValue() {
		return this.value;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		Root.extract(extractor, this.value);
	}

	@Override
	public final float floatValue() {
		return this.value;
	}

	@Override
	public final int intValue() {
		return this.value;
	}

	@Override
	public final long longValue() {
		return this.value;
	}

	@Override
	public final short shortValue() {
		return (short) this.value;
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(true);
		Root.extract(extractor, this.value);
		return extractor.toString();
	}

} // End FastInteger
