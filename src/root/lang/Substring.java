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

import root.annotation.Todo;
import root.util.Root;
import root.validation.InvalidFormatException;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class Substring implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final int beginIndex;
	private final int endIndex;
	private final int length;
	private final char[] charArray;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Substring(final String string) {
		this.beginIndex = 0;
		this.endIndex = string.length();
		this.length = string.length();
		this.charArray = Root.toCharArray(string);
	}

	public Substring(final String string, final int beginIndex, final int endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
		this.length = endIndex - beginIndex;
		this.charArray = Root.toCharArray(string);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append(this.charArray, this.beginIndex, this.length);
	}

	public final boolean getBoolean() {
		int charPos = this.beginIndex;
		char ch;

		// Skip all whitespace
		while (this.charArray[charPos] == ' ' && charPos < this.endIndex) {
			charPos++;
		}

		if (charPos + 4 <= this.endIndex) {
			// Process the first character of the boolean value
			ch = this.charArray[charPos];

			if (ch == 't' || ch == 'T') {
				ch = this.charArray[++charPos];

				if (ch == 'r' || ch == 'R') {
					ch = this.charArray[++charPos];

					if (ch == 'u' || ch == 'U') {
						ch = this.charArray[++charPos];

						return ch == 'e' || ch == 'E';
					}
				}
			}
		}

		return false;
	}

	@Todo("Range test this with Integer.MAX_VALUE and Integer.MIN_VALUE")
	public final int getInt() {
		final boolean negative;
		int charPos = this.beginIndex;
		int value;
		char ch;

		// Skip all whitespace
		while (this.charArray[charPos] == ' ' && charPos < this.endIndex) {
			charPos++;
		}

		if (charPos == this.endIndex) {
			return 0;
		}

		// Process the first character of the integer value
		ch = this.charArray[charPos];
		if (ch == '-') {
			negative = true;
			charPos++;

			if (charPos == this.endIndex) {
				throw new InvalidFormatException("Invalid number format {P}", new String(this.charArray, this.beginIndex, this.length));
			}

			ch = this.charArray[charPos];
		} else {
			negative = false;
		}

		if (ch < '0' || ch > '9') {
			throw new InvalidFormatException("Invalid number format {P}", new String(this.charArray, this.beginIndex, this.length));
		}

		value = ch - '0'; // First digit

		while (this.endIndex < ++charPos) {
			ch = this.charArray[charPos];

			if (ch == ' ') {
				break;
			}

			if (ch < '0' || ch > '9') {
				throw new InvalidFormatException("Invalid number format {P}", new String(this.charArray, this.beginIndex, this.length));
			}

			value = value * 10 + ch - '0';

			if (value < 0) {
				throw new InvalidFormatException("Invalid number format {P}", new String(this.charArray, this.beginIndex, this.length));
			}
		}

		return negative ? -value : value;
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(this.length);
		this.extract(extractor);
		return extractor.toString();
	}

} // End Substring
