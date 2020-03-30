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
import root.util.Root;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Duration {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final char[] labelNs = { ' ', 'n', 's' };

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Converts the {@code time} parameter to a duration of the form:
	 * <p>
	 * DDDDd HHh MMm SSs SSSms
	 *
	 * @param duration
	 *            the duration in milliseconds to convert to a timestamp
	 * @param extractor
	 *            the extractor to put the timestamp
	 */
	public static final void format(final long duration, final StringExtractor extractor) {
		final int numDays = (int) (duration / 86400000L);

		int millis = (int) (duration % 86400000L);

		final int hours = millis / 3600000;
		millis %= 3600000;

		final int minutes = millis / 60000;
		millis %= 60000;

		final int seconds = millis / 1000;
		millis %= 1000;

		int charPos = extractor.length();
		final char[] buf = extractor.getCharArray(23);

		// Append number of days
		if (numDays > 0) {
			if (numDays < 10) {
				buf[charPos++] = Constants.digits[numDays];
			} else {
				final int r = numDays % 100;
				final int q = numDays / 100;

				buf[charPos++] = Constants.digitTens[r];
				buf[charPos++] = Constants.digitOnes[r];

				if (q > 0) {
					if (q < 10) {
						buf[charPos++] = Constants.digits[q];
					} else {
						buf[charPos++] = Constants.digitTens[q];
						buf[charPos++] = Constants.digitOnes[q];
					}
				}
			}

			buf[charPos++] = 'd';
			buf[charPos++] = ' ';
		}

		// Append number of hours
		if (hours > 0) {
			if (hours < 10) {
				buf[charPos++] = Constants.digits[hours];
			} else {
				buf[charPos++] = Constants.digitTens[hours];
				buf[charPos++] = Constants.digitOnes[hours];
			}

			buf[charPos++] = 'h';
			buf[charPos++] = ' ';
		}

		// Append number of minutes
		if (minutes > 0) {
			if (minutes < 10) {
				buf[charPos++] = Constants.digits[minutes];
			} else {
				buf[charPos++] = Constants.digitTens[minutes];
				buf[charPos++] = Constants.digitOnes[minutes];
			}

			buf[charPos++] = 'm';
			buf[charPos++] = ' ';
		}

		// Append number of seconds
		if (seconds > 0) {
			if (seconds < 10) {
				buf[charPos++] = Constants.digits[seconds];
			} else {
				buf[charPos++] = Constants.digitTens[seconds];
				buf[charPos++] = Constants.digitOnes[seconds];
			}

			buf[charPos++] = 's';
			buf[charPos++] = ' ';
		}

		// Append number of milliseconds
		if (millis < 10) {
			buf[charPos++] = Constants.digits[millis];
		} else {
			final int r = millis % 100;
			millis /= 100;

			if (millis > 0) {
				buf[charPos++] = Constants.digits[millis];
			}

			buf[charPos++] = Constants.digitTens[r];
			buf[charPos++] = Constants.digitOnes[r];
		}

		buf[charPos++] = 'm';
		buf[charPos++] = 's';

		extractor.reduceLength(charPos);
	}

	/**
	 * Formats the {@code nanos} parameter as a time duration.
	 * <ul>
	 * <li>If {@code nanos} is less than a millisecond, then put the number of nanoseconds as the duration</li>
	 * <li>Otherwise, convert {@code nanos} to number of milliseconds and format the time duration as {@link Duration#format(long, StringExtractor)}
	 * </li>
	 * </ul>
	 *
	 * @param nanos
	 *            the number of nanoseconds to format
	 * @param extractor
	 *            the {@link StringExtractor} to populate
	 */
	public static final void formatNanos(final long nanos, final StringExtractor extractor) {
		if (nanos < 1000000L) {
			Root.extract(extractor, (int) nanos);

			extractor.appendArray(labelNs);
		} else {
			format(nanos / 1000000L, extractor);
		}
	}

} // End Duration
