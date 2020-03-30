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
package root.time;

import root.lang.Constants;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Timestamp {

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	/**
	 * Converts the {@code time} parameter to a timestamp of the form:
	 * <p>
	 * dow mon dd hh:mm:ss zzz yyyy
	 *
	 * @param time
	 *            the time to convert to a timestamp
	 * @param extractor
	 *            the extractor to put the timestamp
	 */
	public static final void format(final long time, final StringExtractor extractor) {
		int charPos = extractor.length();
		final char[] buf = extractor.getCharArray(28);

		final int numDays = (int) (time / 86400000);
		final int numDaysInYear;
		int year = 1970;

		// Append the day of the week
		System.arraycopy(Constants.dayOfWeek[numDays % 7], 0, buf, charPos, 3);
		charPos += 3;

		// Append the month and day
		if (numDays < 730) {
			year += numDays / 365;
			numDaysInYear = numDays % 365;
			charPos = calcMonthAndDay(false, numDaysInYear, buf, charPos);
		} else {
			final int numDaysSinceLastLeapYear = (numDays - 730) % 1461;
			final boolean isLeapYear = 366 > numDaysSinceLastLeapYear;
			final int numLeapYears = (numDays - 730) / 1461 + 1;

			// Correct year for leap years since 1970 (starting with 1972)
			year += (numDays - numLeapYears) / 365;
			if (numDaysSinceLastLeapYear == 0) {
				year++;
			}

			numDaysInYear = isLeapYear ? numDaysSinceLastLeapYear : (numDaysSinceLastLeapYear - 1) % 365;
			charPos = calcMonthAndDay(isLeapYear, numDaysInYear, buf, charPos);
		}

		// Calculate and append the time portion of the Timestamp
		int millis = (int) (time % 86400000);

		final int hours = millis / 3600000;
		millis %= 3600000;

		final int minutes = millis / 60000;
		millis %= 60000;

		final int seconds = millis / 1000;

		buf[charPos++] = Constants.digitTens[hours];
		buf[charPos++] = Constants.digitOnes[hours];
		buf[charPos++] = ':';
		buf[charPos++] = Constants.digitTens[minutes];
		buf[charPos++] = Constants.digitOnes[minutes];
		buf[charPos++] = ':';
		buf[charPos++] = Constants.digitTens[seconds];
		buf[charPos++] = Constants.digitOnes[seconds];

		// All Timestamps are calculated in UTC
		buf[charPos++] = ' ';
		buf[charPos++] = 'U';
		buf[charPos++] = 'T';
		buf[charPos++] = 'C';
		buf[charPos++] = ' ';

		// Finally append the year
		final int r = year % 100;
		year /= 100;

		buf[charPos++] = Constants.digitTens[year];
		buf[charPos++] = Constants.digitOnes[year];
		buf[charPos++] = Constants.digitTens[r];
		buf[charPos] = Constants.digitOnes[r];
	}

	private static int calcMonthAndDay(final boolean isLeapYear, int numDaysInYear, final char[] buf, int charPos) {
		buf[charPos++] = ' ';

		while (true) {
			// January
			if (numDaysInYear < 31) {
				System.arraycopy(Constants.month[0], 0, buf, charPos, 3);
				break;
			} else {
				numDaysInYear -= 31;
			}

			// February
			if (isLeapYear) {
				if (numDaysInYear < 29) {
					System.arraycopy(Constants.month[1], 0, buf, charPos, 3);
					break;
				} else {
					numDaysInYear -= 29;
				}
			} else if (numDaysInYear < 28) {
				System.arraycopy(Constants.month[1], 0, buf, charPos, 3);
				break;
			} else {
				numDaysInYear -= 28;
			}

			// March
			if (numDaysInYear < 31) {
				System.arraycopy(Constants.month[2], 0, buf, charPos, 3);
				break;
			} else {
				numDaysInYear -= 31;
			}

			// April
			if (numDaysInYear < 30) {
				System.arraycopy(Constants.month[3], 0, buf, charPos, 3);
				break;
			} else {
				numDaysInYear -= 30;
			}

			// May
			if (numDaysInYear < 31) {
				System.arraycopy(Constants.month[4], 0, buf, charPos, 3);
				break;
			} else {
				numDaysInYear -= 31;
			}

			// June
			if (numDaysInYear < 30) {
				System.arraycopy(Constants.month[5], 0, buf, charPos, 3);
				break;
			} else {
				numDaysInYear -= 30;
			}

			// July
			if (numDaysInYear < 31) {
				System.arraycopy(Constants.month[6], 0, buf, charPos, 3);
				break;
			} else {
				numDaysInYear -= 31;
			}

			// August
			if (numDaysInYear < 31) {
				System.arraycopy(Constants.month[7], 0, buf, charPos, 3);
				break;
			} else {
				numDaysInYear -= 31;
			}

			// September
			if (numDaysInYear < 30) {
				System.arraycopy(Constants.month[8], 0, buf, charPos, 3);
				break;
			} else {
				numDaysInYear -= 30;
			}

			// October
			if (numDaysInYear < 31) {
				System.arraycopy(Constants.month[9], 0, buf, charPos, 3);
				break;
			} else {
				numDaysInYear -= 31;
			}

			// November
			if (numDaysInYear < 30) {
				System.arraycopy(Constants.month[10], 0, buf, charPos, 3);
				break;
			} else {
				numDaysInYear -= 30;
			}

			// December
			System.arraycopy(Constants.month[11], 0, buf, charPos, 3);
			break;
		}

		charPos += 3;
		numDaysInYear++;

		buf[charPos++] = ' ';
		buf[charPos++] = Constants.digitTens[numDaysInYear];
		buf[charPos++] = Constants.digitOnes[numDaysInYear];
		buf[charPos++] = ' ';

		return charPos;
	}

} // End Timestamp
