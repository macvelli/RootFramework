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
package root.util;

import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 */
public final class Pair<T extends Extractable> implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	public final T left;
	public final T right;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Pair(final T left, final T right) {
		this.left = left;
		this.right = right;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append('(');
		this.left.extract(extractor);
		extractor.append(',');
		this.right.extract(extractor);
		extractor.append(')');
	}

	public final T getLeft() {
		return this.left;
	}

	public final T getRight() {
		return this.right;
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(32);
		this.extract(extractor);
		return extractor.toString();
	}

} // End Pair
