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

import java.math.BigDecimal;
import java.math.RoundingMode;

import root.finance.InvalidPercentFormatException.Type;
import root.finance.NumberFormat.LongFormatter;
import root.lang.Extractable;
import root.lang.StringExtractor;
import root.util.Root;

/**
 * Percent - http://www.math.com/school/subject1/lessons/S1U1L7GL.html<br>
 * TODO: The toString() implementations need to use Locale-specific number formatting<br>
 * TODO: Need to implement the extract() method
 *
 * The default precision can be two decimal places, but in the wild you will see up to five decimal places
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Percent implements Comparable<Percent>, Extractable {

	// <><><><><><><><><><><><><><><> Constants ><><><><><><><><><><><><><><><>

	private static final BigDecimal HUNDRED = new BigDecimal(100);
	private static final BigDecimal TEN_THOUSAND = new BigDecimal(10000);
	private static final BigDecimal MILLION = new BigDecimal(1000000);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private BigDecimal bigDecimal;

	final long value;
	final NumberFormat numberFormat;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 *
	 * @param dividend
	 * @param divisor
	 */
	public Percent(final long dividend, final long divisor) {
		if (divisor != 0) {
			final BigDecimal value = new BigDecimal(dividend).divide(new BigDecimal(divisor), 6, RoundingMode.HALF_DOWN);

			this.value = value.multiply(MILLION).setScale(0).longValue();
		} else {
			this.value = 0;
		}

		this.numberFormat = NumberFormat.CommaPeriod;
	}

	/**
	 *
	 * @param dividend
	 * @param divisor
	 */
	public Percent(final Money dividend, final Money divisor) {
		if (dividend != null && divisor != null && divisor.amount != 0) {
			final BigDecimal value = dividend.getBigDecimal().divide(divisor.getBigDecimal(), 6, RoundingMode.HALF_DOWN);

			this.value = value.multiply(MILLION).setScale(0).longValue();
		} else {
			this.value = 0;
		}

		this.numberFormat = NumberFormat.CommaPeriod;
	}

	/**
	 *
	 * @param percentStr
	 *            The percent {@link String}
	 */
	public Percent(final String percentStr) {
		this.numberFormat = NumberFormat.CommaPeriod;
		this.value = this.parse(percentStr, this.numberFormat);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final int compareTo(final Percent p) {
		return this.value < p.value ? -1 : this.value > p.value ? 1 : 0;
	}

	@Override
	public final boolean equals(final Object obj) {
		return Root.equalToClass(obj, Percent.class) && ((Percent) obj).value == this.value;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		if (this.value == 0) {
			extractor.append(new char[] { '0', this.numberFormat.decimalSeparator, '0', '0', '%' }, 0, 5);
			return;
		}

		if (this.value == Long.MIN_VALUE) {
			extractor.append(new char[] { '-', '9', '2', '2', this.numberFormat.groupSeparator, '3', '3', '7', this.numberFormat.groupSeparator, '2',
					'0', '3', this.numberFormat.groupSeparator, '6', '8', '5', this.numberFormat.groupSeparator, '4', '7', '7',
					this.numberFormat.decimalSeparator, '5', '8', '%' }, 0, 24);
			return;
		}

		final boolean sign = this.value < 0;
		long l = sign ? -this.value : this.value;
		l = l % 100 > 50 ? l / 100 + 1 : l / 100;

		final LongFormatter longFormatter = NumberFormat.getLongFormatter(l);
		final int formatSize = sign ? longFormatter.getLength() + 2 : longFormatter.getLength() + 1;

		final char[] buf = extractor.getCharArray(formatSize);
		int charPos = extractor.getLength();

		// Add percent symbol
		buf[--charPos] = '%';

		// Format the number
		longFormatter.format(l, buf, charPos, this.numberFormat);
		charPos -= longFormatter.getLength();

		if (sign) {
			buf[charPos - 1] = '-';
		}
	}

	public final BigDecimal getBigDecimal() {
		if (this.bigDecimal == null) {
			this.bigDecimal = new BigDecimal(this.value);
		}

		return this.bigDecimal;
	}

	public final long getValue() {
		return this.value;
	}

	@Override
	public final int hashCode() {
		return (int) (this.value ^ this.value >>> 32);
	}

	public final Money multiply(final Money m) {
		final BigDecimal value = m.getBigDecimal().multiply(this.getBigDecimal()).divide(TEN_THOUSAND).setScale(0, RoundingMode.HALF_DOWN);
		final long answer = value.remainder(HUNDRED).intValue() > 50 ? value.divide(HUNDRED).longValue() + 1 : value.divide(HUNDRED).longValue();

		return new Money(answer, m.currency);
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(true);
		this.extract(extractor);
		return extractor.toString();
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private long parse(final String percentStr, final NumberFormat numberFormat) {
		if (percentStr == null || percentStr.length() == 0) {
			return 0L;
		}

		final char[] percentCharArray = Root.toCharArray(percentStr);
		final boolean negative = percentCharArray[0] == '-';
		int charPos = negative ? 1 : 0;
		long percentAmt;
		char c;

		if (negative && percentCharArray.length == 1) {
			throw new InvalidPercentFormatException(Type.LENGTH, percentStr);
		}

		c = percentCharArray[charPos++];
		if (c < '0' || c > '9') {
			throw new InvalidPercentFormatException(Type.CHARACTER, percentStr);
		}

		percentAmt = c - '0'; // First digit

		if (percentCharArray.length > charPos) {
			c = percentCharArray[charPos++];

			if (c != numberFormat.decimalSeparator && c != numberFormat.groupSeparator && c != '%') {
				if (c < '0' || c > '9') {
					throw new InvalidPercentFormatException(Type.CHARACTER, percentStr);
				}

				percentAmt = percentAmt * 10 + c - '0'; // Second digit

				if (percentCharArray.length > charPos) {
					c = percentCharArray[charPos++];

					if (c != numberFormat.decimalSeparator && c != numberFormat.groupSeparator && c != '%') {
						if (c < '0' || c > '9') {
							throw new InvalidPercentFormatException(Type.CHARACTER, percentStr);
						}

						percentAmt = percentAmt * 10 + c - '0'; // Third digit

						if (percentCharArray.length > charPos) {
							c = percentCharArray[charPos++];

							if (c != numberFormat.groupSeparator) {
								while (c != numberFormat.decimalSeparator && c != '%') {
									if (c < '0' || c > '9') {
										throw new InvalidPercentFormatException(Type.CHARACTER, percentStr);
									}

									percentAmt = percentAmt * 10 + c - '0';

									if (percentCharArray.length == charPos) {
										break;
									}

									c = percentCharArray[charPos++];
								}
							}
						}
					}
				}
			}
		}

		while (c == numberFormat.groupSeparator) {
			if (percentCharArray.length - charPos < 3) {
				throw new InvalidPercentFormatException(Type.LENGTH, percentStr);
			}

			c = percentCharArray[charPos++];
			if (c < '0' || c > '9') {
				throw new InvalidPercentFormatException(Type.CHARACTER, percentStr);
			}

			percentAmt = percentAmt * 10 + c - '0';

			c = percentCharArray[charPos++];
			if (c < '0' || c > '9') {
				throw new InvalidPercentFormatException(Type.CHARACTER, percentStr);
			}

			percentAmt = percentAmt * 10 + c - '0';

			c = percentCharArray[charPos++];
			if (c < '0' || c > '9') {
				throw new InvalidPercentFormatException(Type.CHARACTER, percentStr);
			}

			percentAmt = percentAmt * 10 + c - '0';

			if (percentCharArray.length > charPos) {
				c = percentCharArray[charPos++];

				if (c != numberFormat.decimalSeparator && c != numberFormat.groupSeparator && c != '%') {
					throw new InvalidPercentFormatException(Type.CHARACTER, percentStr);
				}
			}
		}

		int fractionScale = 10000;
		int fractionScaleOverflow = 0;

		if (c == numberFormat.decimalSeparator) {
			if (percentCharArray.length == charPos) {
				throw new InvalidPercentFormatException(Type.LENGTH, percentStr);
			}

			c = percentCharArray[charPos++];
			if (c < '0' || c > '9') {
				throw new InvalidPercentFormatException(Type.CHARACTER, percentStr);
			}

			fractionScale /= 10;
			percentAmt = percentAmt * 10 + c - '0';

			while (percentCharArray.length > charPos) {
				c = percentCharArray[charPos++];

				if (c == '%') {
					if (percentCharArray.length > charPos) {
						throw new InvalidPercentFormatException(Type.LENGTH, percentStr);
					}
				} else {
					if (c < '0' || c > '9') {
						throw new InvalidPercentFormatException(Type.CHARACTER, percentStr);
					}

					if (fractionScale > 0) {
						fractionScale /= 10;
						percentAmt = percentAmt * 10 + c - '0';
					} else {
						fractionScaleOverflow = fractionScaleOverflow * 10 + c - '0';

						if (fractionScaleOverflow >= 10) {
							break;
						}
					}
				}
			}
		}

		// Adjust the percent amount by the fraction scale and overflow
		if (fractionScale > 0) {
			percentAmt *= fractionScale;
		} else if (fractionScaleOverflow > 0) {
			if (fractionScaleOverflow > 50) {
				percentAmt++;
			} else if (fractionScaleOverflow < 10 && fractionScaleOverflow > 5) {
				percentAmt++;
			}
		}

		return negative ? -percentAmt : percentAmt;
	}

} // End Percent
