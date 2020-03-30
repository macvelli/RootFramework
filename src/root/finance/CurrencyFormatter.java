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

import root.finance.NumberFormat.IntFormatter;
import root.finance.NumberFormat.LongFormatter;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
class CurrencyFormatter {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final NumberFormat numberFormat;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	CurrencyFormatter(final NumberFormat numberFormat) {
		this.numberFormat = numberFormat;
	}

	// <><><><><><><><><><><><><>< Package Methods ><><><><><><><><><><><><><>

	final void format(int i, final CurrencySymbol symbol, final StringExtractor extractor) {
		if (i == 0) {
			extractor.append(symbol).append('0').append(this.numberFormat.decimalSeparator).append('0').append('0');
			return;
		}

		if (i == Integer.MIN_VALUE) {
			extractor.append('-').append(symbol).append(new char[] { '2', '1', this.numberFormat.groupSeparator, '4', '7', '4',
					this.numberFormat.groupSeparator, '8', '3', '6', this.numberFormat.decimalSeparator, '4', '8' }, 0, 13);
			return;
		}

		final boolean sign = i < 0;
		final int formatSize;
		final IntFormatter intFormatter;
		if (sign) {
			i = -i;
			intFormatter = NumberFormat.getIntFormatter(i);
			formatSize = intFormatter.getLength() + symbol.getLength() + 1;
		} else {
			intFormatter = NumberFormat.getIntFormatter(i);
			formatSize = intFormatter.getLength() + symbol.getLength();
		}

		final char[] buf = extractor.getCharArray(formatSize);
		int charPos = formatSize + extractor.getLength();

		// Format the number
		intFormatter.format(i, buf, charPos, this.numberFormat);
		charPos -= intFormatter.getLength();

		// Add currency symbol
		symbol.extract(buf, charPos);
		charPos -= symbol.getLength();

		if (sign) {
			buf[charPos - 1] = '-';
		}
	}

	final void format(long l, final CurrencySymbol symbol, final StringExtractor extractor) {
		if (l == 0) {
			extractor.append(symbol).append('0').append(this.numberFormat.decimalSeparator).append('0').append('0');
			return;
		}

		if (l == Long.MIN_VALUE) {
			extractor.append('-')
					.append(symbol)
					.append('9')
					.append('2')
					.append(this.numberFormat.groupSeparator)
					.append('2')
					.append('3')
					.append('3')
					.append(this.numberFormat.groupSeparator)
					.append('7')
					.append('2')
					.append('0')
					.append(this.numberFormat.groupSeparator)
					.append('3')
					.append('6')
					.append('8')
					.append(this.numberFormat.groupSeparator)
					.append('5')
					.append('4')
					.append('7')
					.append(this.numberFormat.groupSeparator)
					.append('7')
					.append('5')
					.append('8')
					.append(this.numberFormat.decimalSeparator)
					.append('0')
					.append('8');
			return;
		}

		final boolean sign = l < 0;
		final int formatSize;
		final LongFormatter longFormatter;
		if (sign) {
			l = -l;
			longFormatter = NumberFormat.getLongFormatter(l);
			formatSize = longFormatter.getLength() + symbol.getLength() + 1;
		} else {
			longFormatter = NumberFormat.getLongFormatter(l);
			formatSize = longFormatter.getLength() + symbol.getLength();
		}

		final char[] buf = extractor.getCharArray(formatSize);
		int charPos = extractor.getLength();

		// Format the number
		longFormatter.format(l, buf, charPos, this.numberFormat);
		charPos -= longFormatter.getLength();

		// Add currency symbol
		symbol.extract(buf, charPos);
		charPos -= symbol.getLength();

		if (sign) {
			buf[charPos - 1] = '-';
		}
	}

} // End CurrencyFormatter
