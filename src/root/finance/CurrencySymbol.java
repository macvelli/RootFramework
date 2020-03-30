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
package root.finance;

import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
enum CurrencySymbol implements Extractable {

	// <><><><><><><><><><><><><><>< Enum Values ><><><><><><><><><><><><><><>

	DOLLAR('$', null),
	EURO('€', null),
	FRANC('\u0000', new char[] { 'C', 'H', 'F' }),
	KRONE('\u0000', new char[] { 'k', 'r' }),
	POUND('£', null),
	RAND('R', null),
	REAL('\u0000', new char[] { 'R', '$' }),
	RUBLE('\u0000', new char[] { 'р', 'у', 'б' }),
	RUPEE('₹', null),
	SHEKEL('₪', null),
	TRY('₺', null),
	TWD('\u0000', new char[] { 'N', 'T', '$' }),
	WON('₩', null),
	YEN('¥', null);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final char symbolChar;
	private final char[] symbolCharArray;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private CurrencySymbol(final char symbolChar, final char[] symbolCharArray) {
		this.symbolChar = symbolChar;
		this.symbolCharArray = symbolCharArray;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		if (this.symbolCharArray == null) {
			extractor.append(this.symbolChar);
		} else {
			extractor.append(this.symbolCharArray, 0, this.symbolCharArray.length);
		}
	}

	// <><><><><><><><><><><><><>< Package Methods ><><><><><><><><><><><><><>

	final void extract(final char[] buf, final int charPos) {
		if (this.symbolCharArray == null) {
			buf[charPos - 1] = this.symbolChar;
		} else if (this.symbolCharArray.length == 2) {
			buf[charPos - 1] = this.symbolCharArray[1];
			buf[charPos - 2] = this.symbolCharArray[0];
		} else if (this.symbolCharArray.length == 3) {
			buf[charPos - 1] = this.symbolCharArray[2];
			buf[charPos - 2] = this.symbolCharArray[1];
			buf[charPos - 3] = this.symbolCharArray[0];
		} else {
			buf[charPos - 1] = this.symbolCharArray[3];
			buf[charPos - 2] = this.symbolCharArray[2];
			buf[charPos - 3] = this.symbolCharArray[1];
			buf[charPos - 4] = this.symbolCharArray[0];
		}
	}

	final int getLength() {
		return this.symbolCharArray == null ? 1 : this.symbolCharArray.length;
	}

	final boolean parse(final char[] moneyCharArray, final int charPos) {
		if (this.symbolCharArray == null) {
			return moneyCharArray[charPos] == this.symbolChar;
		} else if (this.symbolCharArray.length == 2) {
			return moneyCharArray.length > charPos + 1 && moneyCharArray[charPos] == this.symbolCharArray[0]
					&& moneyCharArray[charPos + 1] == this.symbolCharArray[1];
		} else if (this.symbolCharArray.length == 3) {
			return moneyCharArray.length > charPos + 2 && moneyCharArray[charPos] == this.symbolCharArray[0]
					&& moneyCharArray[charPos + 1] == this.symbolCharArray[1] && moneyCharArray[charPos + 2] == this.symbolCharArray[2];
		}

		return moneyCharArray.length > charPos + 3 && moneyCharArray[charPos] == this.symbolCharArray[0]
				&& moneyCharArray[charPos + 1] == this.symbolCharArray[1] && moneyCharArray[charPos + 2] == this.symbolCharArray[2]
				&& moneyCharArray[charPos + 3] == this.symbolCharArray[3];
	}

} // End CurrencySymbol
