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

import root.util.Root;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class FastString implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	public final String string;
	private final char[] charArray;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public FastString(final String string) {
		this.string = string;
		this.charArray = Root.toCharArray(string);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final boolean equals(final Object obj) {
		if (Root.equalToClass(obj, FastString.class)) {
			return this.string.equals(((FastString) obj).string);
		} else if (Root.equalToClass(obj, String.class)) {
			return this.string.equals(obj);
		}

		return false;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append(this.charArray, 0, this.charArray.length);
	}

	public final char[] getChars() {
		return this.charArray;
	}

	@Override
	public final int hashCode() {
		return this.string.hashCode();
	}

	public final int length() {
		return this.charArray.length;
	}

	@Override
	public final String toString() {
		return this.string;
	}

} // End FastString
