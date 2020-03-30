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

import root.finance.InvalidCurrencyFormatException.Type;
import root.util.Root;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
class CurrencyParser {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final NumberFormat numberFormat;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	CurrencyParser(final NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final long parse(final String moneyStr, final Currency currency) {
		if (moneyStr == null || moneyStr.length() == 0) {
			return 0L;
		}

		final char[] moneyCharArray = Root.toCharArray(moneyStr);
		final boolean negative = moneyCharArray[0] == '-';
		int charPos = negative ? 1 : 0;
		long moneyAmt;
		char c;

		if (negative && moneyCharArray.length == 1) {
			throw new InvalidCurrencyFormatException(Type.LENGTH, moneyStr, currency);
		}

		if (currency.getSymbol().parse(moneyCharArray, charPos)) {
			charPos += currency.getSymbol().getLength();
		}

		if (moneyCharArray.length == charPos) {
			throw new InvalidCurrencyFormatException(Type.LENGTH, moneyStr, currency);
		}

		c = moneyCharArray[charPos++];
		if (c < '0' || c > '9') {
			throw new InvalidCurrencyFormatException(Type.CHARACTER, moneyStr, currency);
		}

		moneyAmt = c - '0'; // First digit

		if (moneyCharArray.length > charPos) {
			c = moneyCharArray[charPos++];

			if (c != this.numberFormat.decimalSeparator && c != this.numberFormat.groupSeparator) {
				if (c < '0' || c > '9') {
					throw new InvalidCurrencyFormatException(Type.CHARACTER, moneyStr, currency);
				}

				moneyAmt = moneyAmt * 10 + c - '0'; // Second digit

				if (moneyCharArray.length > charPos) {
					c = moneyCharArray[charPos++];

					if (c != this.numberFormat.decimalSeparator && c != this.numberFormat.groupSeparator) {
						if (c < '0' || c > '9') {
							throw new InvalidCurrencyFormatException(Type.CHARACTER, moneyStr, currency);
						}

						moneyAmt = moneyAmt * 10 + c - '0'; // Third digit

						if (moneyCharArray.length > charPos) {
							c = moneyCharArray[charPos++];

							if (c != this.numberFormat.groupSeparator) {
								while (c != this.numberFormat.decimalSeparator) {
									if (c < '0' || c > '9') {
										throw new InvalidCurrencyFormatException(Type.CHARACTER, moneyStr, currency);
									}

									moneyAmt = moneyAmt * 10 + c - '0';

									if (moneyCharArray.length == charPos) {
										break;
									}

									c = moneyCharArray[charPos++];
								}
							}
						}
					}
				}
			}
		}

		while (c == this.numberFormat.groupSeparator) {
			if (moneyCharArray.length - charPos < 3) {
				throw new InvalidCurrencyFormatException(Type.LENGTH, moneyStr, currency);
			}

			c = moneyCharArray[charPos++];
			if (c < '0' || c > '9') {
				throw new InvalidCurrencyFormatException(Type.CHARACTER, moneyStr, currency);
			}

			moneyAmt = moneyAmt * 10 + c - '0';

			c = moneyCharArray[charPos++];
			if (c < '0' || c > '9') {
				throw new InvalidCurrencyFormatException(Type.CHARACTER, moneyStr, currency);
			}

			moneyAmt = moneyAmt * 10 + c - '0';

			c = moneyCharArray[charPos++];
			if (c < '0' || c > '9') {
				throw new InvalidCurrencyFormatException(Type.CHARACTER, moneyStr, currency);
			}

			moneyAmt = moneyAmt * 10 + c - '0';

			if (moneyCharArray.length > charPos) {
				c = moneyCharArray[charPos++];

				if (c != this.numberFormat.decimalSeparator && c != this.numberFormat.groupSeparator) {
					throw new InvalidCurrencyFormatException(Type.CHARACTER, moneyStr, currency);
				}
			}
		}

		if (c == this.numberFormat.decimalSeparator) {
			if (moneyCharArray.length - charPos < 2) {
				throw new InvalidCurrencyFormatException(Type.LENGTH, moneyStr, currency);
			}

			c = moneyCharArray[charPos++];
			if (c < '0' || c > '9') {
				throw new InvalidCurrencyFormatException(Type.CHARACTER, moneyStr, currency);
			}

			moneyAmt = moneyAmt * 10 + c - '0';

			c = moneyCharArray[charPos++];
			if (c < '0' || c > '9') {
				throw new InvalidCurrencyFormatException(Type.CHARACTER, moneyStr, currency);
			}

			moneyAmt = moneyAmt * 10 + c - '0';

			if (moneyCharArray.length > charPos) {
				throw new InvalidCurrencyFormatException(Type.LENGTH, moneyStr, currency);
			}
		}

		return negative ? -moneyAmt : moneyAmt;
	}

} // End CurrencyParser
