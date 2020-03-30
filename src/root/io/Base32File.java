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
package root.io;

import java.io.File;

import root.codec.Base32;
import root.lang.Constants;
import root.lang.StringExtractor;
import root.util.Root;
import root.validation.InvalidParameterException;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Base32File {

	public static final void main(final String[] args) {
		final StringExtractor extractor = new StringExtractor();
		final String s = null;

		extractor.append("Attempt to retrieve Base32File with string input of ");
		extractor.append(s);
		extractor.append(" failed. String input must be at least three characters long");

		System.out.println(extractor.length());

		System.exit(0);
	}

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	public static File retrieve(final String str, String basePath, final int dirDepth) {
		// Ensure str parameter is not null and at least three characters long
		if (str == null || str.length() < 3) {
			throw new InvalidParameterException("retrieve", String.class, "str", "Input must be at least three characters long");
		}

		// Ensure dirDepth is between zero and two
		if (dirDepth < 0 || dirDepth > 2) {
			throw new InvalidParameterException("retrieve", int.class, "dirDepth", "Value must be between zero and two");
		}

		// Default basePath to empty string if null
		if (basePath == null) {
			basePath = Constants.EMPTY_STRING;
		}

		// Encode str parameter in Base32 format
		final String base32Str = Base32.encode(str, Constants.CHARSET_UTF8);

		// Build the full file path
		final StringExtractor fullPath = new StringExtractor(basePath.length() + base32Str.length() + dirDepth + 1);

		fullPath.append(basePath);
		if (!basePath.endsWith(File.separator)) {
			fullPath.append(File.separatorChar);
		}

		if (dirDepth == 0) {
			fullPath.append(base32Str);
		} else {
			final char[] base32CharArray = Root.toCharArray(base32Str);

			fullPath.append(base32CharArray, 0, 2);
			fullPath.append(File.separatorChar);

			if (dirDepth == 1) {
				fullPath.append(base32CharArray, 2, base32CharArray.length);
			} else {
				fullPath.append(base32CharArray, 2, 4);
				fullPath.append(File.separatorChar);
				fullPath.append(base32CharArray, 4, base32CharArray.length);
			}
		}

		return new File(fullPath.toString());
	}

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private Base32File() {
	}

} // End Base32File
