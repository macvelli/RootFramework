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

import root.annotation.Factory;

/**
 * This class is a pseudo-factory in that you specify a prefix and then generate {@link String}s with the {@link #toString(String)} method that
 * appends the suffix to the prefix and then generates the full {@link String}.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Factory
public final class Prefix {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final int prefixLength;
	private final char[] prefixBuf;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Prefix(final String prefix) {
		this(prefix, 64);
	}

	public Prefix(final String prefix, final int maxSuffixSize) {
		this.prefixLength = prefix.length();
		this.prefixBuf = new char[this.prefixLength + maxSuffixSize];
		prefix.getChars(0, this.prefixLength, this.prefixBuf, 0);
	}

	public Prefix(final StringExtractor prefix) {
		this.prefixLength = prefix.size;
		this.prefixBuf = prefix.chars;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final String toString() {
		return new String(this.prefixBuf, 0, this.prefixLength);
	}

	public final String toString(final String suffix) {
		final int suffixLength = suffix.length();
		suffix.getChars(0, suffixLength, this.prefixBuf, this.prefixLength);
		return new String(this.prefixBuf, 0, this.prefixLength + suffixLength);
	}

} // End Prefix
