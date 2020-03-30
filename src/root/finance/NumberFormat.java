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

import root.lang.Constants;

/**
 * https://docs.oracle.com/cd/E19455-01/806-0169/overview-9/index.html
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public enum NumberFormat {

	// <><><><><><><><><><><><><><>< Enum Values ><><><><><><><><><><><><><><>

	Comma(',', '\u0000'),
	CommaPeriod(',', '.'),
	Period('.', '\u0000'),
	PeriodComma('.', ','),
	SpaceComma(' ', ','),
	SpacePeriod(' ', '.'),
	TickPeriod('\'', '.');

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	interface IntFormatter {

		void format(int i, char[] buf, int charPos, NumberFormat numberFormat);

		int getLength();

	} // End IntFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	interface LongFormatter {

		void format(long l, char[] buf, int charPos, NumberFormat numberFormat);

		int getLength();

	} // End LongFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num10DigitFormatter implements IntFormatter, LongFormatter {

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void format(int i, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = i % 100;
			i /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;
			buf[charPos - 12] = Constants.digitOnes[i];
			buf[charPos - 13] = Constants.digitTens[i];
		}

		@Override
		public final void format(final long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;
			buf[charPos - 12] = Constants.digitOnes[i];
			buf[charPos - 13] = Constants.digitTens[i];
		}

		@Override
		public final int getLength() {
			return 13;
		}

	} // End Num10DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num11DigitFormatter implements LongFormatter {

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void format(final long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 12] = Constants.digitOnes[r];
			buf[charPos - 13] = Constants.digitTens[r];
			buf[charPos - 14] = Constants.digits[i];
		}

		@Override
		public final int getLength() {
			return 14;
		}

	} // End Num11DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num12DigitFormatter implements LongFormatter {

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void format(long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			l /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 12] = Constants.digitOnes[r];
			buf[charPos - 13] = Constants.digitTens[r];
			buf[charPos - 14] = Constants.digitOnes[i];
			buf[charPos - 15] = numberFormat.groupSeparator;
			buf[charPos - 16] = Constants.digitTens[i];
		}

		@Override
		public final int getLength() {
			return 16;
		}

	} // End Num12DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num13DigitFormatter implements LongFormatter {

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void format(long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			l /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 12] = Constants.digitOnes[r];
			buf[charPos - 13] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 14] = Constants.digitOnes[r];
			buf[charPos - 15] = numberFormat.groupSeparator;
			buf[charPos - 16] = Constants.digitTens[r];
			buf[charPos - 17] = Constants.digits[i];
		}

		@Override
		public final int getLength() {
			return 17;
		}

	} // End Num13DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num14DigitFormatter implements LongFormatter {

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void format(long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			l /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 12] = Constants.digitOnes[r];
			buf[charPos - 13] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 14] = Constants.digitOnes[r];
			buf[charPos - 15] = numberFormat.groupSeparator;
			buf[charPos - 16] = Constants.digitTens[r];
			buf[charPos - 17] = Constants.digitOnes[i];
			buf[charPos - 18] = Constants.digitTens[i];
		}

		@Override
		public final int getLength() {
			return 18;
		}

	} // End Num14DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num15DigitFormatter implements LongFormatter {

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void format(long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			l /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 12] = Constants.digitOnes[r];
			buf[charPos - 13] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 14] = Constants.digitOnes[r];
			buf[charPos - 15] = numberFormat.groupSeparator;
			buf[charPos - 16] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 17] = Constants.digitOnes[r];
			buf[charPos - 18] = Constants.digitTens[r];
			buf[charPos - 19] = numberFormat.groupSeparator;
			buf[charPos - 20] = Constants.digits[i];
		}

		@Override
		public final int getLength() {
			return 20;
		}

	} // End Num15DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num16DigitFormatter implements LongFormatter {

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void format(long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			l /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 12] = Constants.digitOnes[r];
			buf[charPos - 13] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 14] = Constants.digitOnes[r];
			buf[charPos - 15] = numberFormat.groupSeparator;
			buf[charPos - 16] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 17] = Constants.digitOnes[r];
			buf[charPos - 18] = Constants.digitTens[r];
			buf[charPos - 19] = numberFormat.groupSeparator;
			buf[charPos - 20] = Constants.digitOnes[i];
			buf[charPos - 21] = Constants.digitTens[i];
		}

		@Override
		public final int getLength() {
			return 21;
		}

	} // End Num16DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num17DigitFormatter implements LongFormatter {

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void format(long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			l /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 12] = Constants.digitOnes[r];
			buf[charPos - 13] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 14] = Constants.digitOnes[r];
			buf[charPos - 15] = numberFormat.groupSeparator;
			buf[charPos - 16] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 17] = Constants.digitOnes[r];
			buf[charPos - 18] = Constants.digitTens[r];
			buf[charPos - 19] = numberFormat.groupSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 20] = Constants.digitOnes[r];
			buf[charPos - 21] = Constants.digitTens[r];
			buf[charPos - 22] = Constants.digits[i];
		}

		@Override
		public final int getLength() {
			return 22;
		}

	} // End Num17DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num18DigitFormatter implements LongFormatter {

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void format(long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			l /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;

			r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 12] = Constants.digitOnes[r];
			buf[charPos - 13] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 14] = Constants.digitOnes[r];
			buf[charPos - 15] = numberFormat.groupSeparator;
			buf[charPos - 16] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 17] = Constants.digitOnes[r];
			buf[charPos - 18] = Constants.digitTens[r];
			buf[charPos - 19] = numberFormat.groupSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 20] = Constants.digitOnes[r];
			buf[charPos - 21] = Constants.digitTens[r];
			buf[charPos - 22] = Constants.digitOnes[i];
			buf[charPos - 23] = numberFormat.groupSeparator;
			buf[charPos - 24] = Constants.digitTens[i];
		}

		@Override
		public final int getLength() {
			return 24;
		}

	} // End Num18DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num19DigitFormatter implements LongFormatter {

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void format(long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			l /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = (int) (l % 100);
			l /= 100;

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;

			r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 12] = Constants.digitOnes[r];
			buf[charPos - 13] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 14] = Constants.digitOnes[r];
			buf[charPos - 15] = numberFormat.groupSeparator;
			buf[charPos - 16] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 17] = Constants.digitOnes[r];
			buf[charPos - 18] = Constants.digitTens[r];
			buf[charPos - 19] = numberFormat.groupSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 20] = Constants.digitOnes[r];
			buf[charPos - 21] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 22] = Constants.digitOnes[r];
			buf[charPos - 23] = numberFormat.groupSeparator;
			buf[charPos - 24] = Constants.digitTens[r];
			buf[charPos - 25] = Constants.digits[i];
		}

		@Override
		public final int getLength() {
			return 25;
		}

	} // End Num19DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num3DigitFormatter implements IntFormatter, LongFormatter {

		// <><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><>

		@Override
		public final void format(int i, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			final int r = i % 100;
			i /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;
			buf[charPos - 4] = Constants.digits[i];
		}

		@Override
		public final void format(final long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			final int r = (int) (l % 100);
			final int i = (int) (l / 100);

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;
			buf[charPos - 4] = Constants.digits[i];
		}

		@Override
		public final int getLength() {
			return 4;
		}

	} // End Num3DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num4DigitFormatter implements IntFormatter, LongFormatter {

		// <><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><>

		@Override
		public final void format(int i, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			final int r = i % 100;
			i /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;
			buf[charPos - 4] = Constants.digitOnes[i];
			buf[charPos - 5] = Constants.digitTens[i];
		}

		@Override
		public final void format(final long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			final int r = (int) (l % 100);
			final int i = (int) (l / 100);

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;
			buf[charPos - 4] = Constants.digitOnes[i];
			buf[charPos - 5] = Constants.digitTens[i];
		}

		@Override
		public final int getLength() {
			return 5;
		}

	} // End Num4DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num5DigitFormatter implements IntFormatter, LongFormatter {

		// <><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><>

		@Override
		public final void format(int i, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = i % 100;
			i /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];
			buf[charPos - 6] = Constants.digits[i];
		}

		@Override
		public final void format(final long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];
			buf[charPos - 6] = Constants.digits[i];
		}

		@Override
		public final int getLength() {
			return 6;
		}

	} // End Num5DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num6DigitFormatter implements IntFormatter, LongFormatter {

		// <><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><>

		@Override
		public final void format(int i, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = i % 100;
			i /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];
			buf[charPos - 6] = Constants.digitOnes[i];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[i];
		}

		@Override
		public final void format(final long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];
			buf[charPos - 6] = Constants.digitOnes[i];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[i];
		}

		@Override
		public final int getLength() {
			return 8;
		}

	} // End Num6DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num7DigitFormatter implements IntFormatter, LongFormatter {

		// <><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><>

		@Override
		public final void format(int i, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = i % 100;
			i /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];
			buf[charPos - 9] = Constants.digits[i];
		}

		@Override
		public final void format(final long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];
			buf[charPos - 9] = Constants.digits[i];
		}

		@Override
		public final int getLength() {
			return 9;
		}

	} // End Num7DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num8DigitFormatter implements IntFormatter, LongFormatter {

		// <><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><>

		@Override
		public final void format(int i, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = i % 100;
			i /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];
			buf[charPos - 9] = Constants.digitOnes[i];
			buf[charPos - 10] = Constants.digitTens[i];
		}

		@Override
		public final void format(final long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];
			buf[charPos - 9] = Constants.digitOnes[i];
			buf[charPos - 10] = Constants.digitTens[i];
		}

		@Override
		public final int getLength() {
			return 10;
		}

	} // End Num8DigitFormatter

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private static class Num9DigitFormatter implements IntFormatter, LongFormatter {

		// <><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><>

		@Override
		public final void format(int i, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = i % 100;
			i /= 100;

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;
			buf[charPos - 12] = Constants.digits[i];
		}

		@Override
		public final void format(final long l, final char[] buf, final int charPos, final NumberFormat numberFormat) {
			int r = (int) (l % 100);
			int i = (int) (l / 100);

			buf[charPos - 1] = Constants.digitOnes[r];
			buf[charPos - 2] = Constants.digitTens[r];
			buf[charPos - 3] = numberFormat.decimalSeparator;

			r = i % 100;
			i /= 100;

			buf[charPos - 4] = Constants.digitOnes[r];
			buf[charPos - 5] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 6] = Constants.digitOnes[r];
			buf[charPos - 7] = numberFormat.groupSeparator;
			buf[charPos - 8] = Constants.digitTens[r];

			r = i % 100;
			i /= 100;

			buf[charPos - 9] = Constants.digitOnes[r];
			buf[charPos - 10] = Constants.digitTens[r];
			buf[charPos - 11] = numberFormat.groupSeparator;
			buf[charPos - 12] = Constants.digits[i];
		}

		@Override
		public final int getLength() {
			return 12;
		}

	} // End Num9DigitFormatter

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Num3DigitFormatter threeDigitFormatter = new Num3DigitFormatter();
	private static final Num4DigitFormatter fourDigitFormatter = new Num4DigitFormatter();
	private static final Num5DigitFormatter fiveDigitFormatter = new Num5DigitFormatter();
	private static final Num6DigitFormatter sixDigitFormatter = new Num6DigitFormatter();
	private static final Num7DigitFormatter sevenDigitFormatter = new Num7DigitFormatter();
	private static final Num8DigitFormatter eightDigitFormatter = new Num8DigitFormatter();
	private static final Num9DigitFormatter nineDigitFormatter = new Num9DigitFormatter();
	private static final Num10DigitFormatter tenDigitFormatter = new Num10DigitFormatter();
	private static final Num11DigitFormatter elevenDigitFormatter = new Num11DigitFormatter();
	private static final Num12DigitFormatter twelveDigitFormatter = new Num12DigitFormatter();
	private static final Num13DigitFormatter thirteenDigitFormatter = new Num13DigitFormatter();
	private static final Num14DigitFormatter fourteenDigitFormatter = new Num14DigitFormatter();
	private static final Num15DigitFormatter fifteenDigitFormatter = new Num15DigitFormatter();
	private static final Num16DigitFormatter sixteenDigitFormatter = new Num16DigitFormatter();
	private static final Num17DigitFormatter seventeenDigitFormatter = new Num17DigitFormatter();
	private static final Num18DigitFormatter eighteenDigitFormatter = new Num18DigitFormatter();
	private static final Num19DigitFormatter nineteenDigitFormatter = new Num19DigitFormatter();

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	public static final IntFormatter getIntFormatter(final int i) {
		if (i < 1000) {
			return threeDigitFormatter;
		}
		if (i < 10000) {
			return fourDigitFormatter;
		}
		if (i < 100000) {
			return fiveDigitFormatter;
		}
		if (i < 1000000) {
			return sixDigitFormatter;
		}
		if (i < 10000000) {
			return sevenDigitFormatter;
		}
		if (i < 100000000) {
			return eightDigitFormatter;
		}
		if (i < 1000000000) {
			return nineDigitFormatter;
		}

		return tenDigitFormatter;
	}

	public static final LongFormatter getLongFormatter(final long l) {
		if (l < 1000L) {
			return threeDigitFormatter;
		}
		if (l < 10000L) {
			return fourDigitFormatter;
		}
		if (l < 100000L) {
			return fiveDigitFormatter;
		}
		if (l < 1000000L) {
			return sixDigitFormatter;
		}
		if (l < 10000000L) {
			return sevenDigitFormatter;
		}
		if (l < 100000000L) {
			return eightDigitFormatter;
		}
		if (l < 1000000000L) {
			return nineDigitFormatter;
		}
		if (l < 10000000000L) {
			return tenDigitFormatter;
		}
		if (l < 100000000000L) {
			return elevenDigitFormatter;
		}
		if (l < 1000000000000L) {
			return twelveDigitFormatter;
		}
		if (l < 10000000000000L) {
			return thirteenDigitFormatter;
		}
		if (l < 100000000000000L) {
			return fourteenDigitFormatter;
		}
		if (l < 1000000000000000L) {
			return fifteenDigitFormatter;
		}
		if (l < 10000000000000000L) {
			return sixteenDigitFormatter;
		}
		if (l < 100000000000000000L) {
			return seventeenDigitFormatter;
		}
		if (l < 1000000000000000000L) {
			return eighteenDigitFormatter;
		}

		return nineteenDigitFormatter;
	}

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	final char groupSeparator;
	final char decimalSeparator;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private NumberFormat(final char groupSeparator, final char decimalSeparator) {
		this.groupSeparator = groupSeparator;
		this.decimalSeparator = decimalSeparator;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final char getDecimalSeparator() {
		return this.decimalSeparator;
	}

	public final char getGroupSeparator() {
		return this.groupSeparator;
	}

} // End NumberFormat
