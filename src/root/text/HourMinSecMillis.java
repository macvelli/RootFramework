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
package root.text;

import java.text.SimpleDateFormat;

import root.lang.Constants;
import root.lang.StringExtractor;

/**
 * This is the same as a {@link SimpleDateFormat} of "HH:mm:ss,SSS", except it works with {@link Extractor}s!
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class HourMinSecMillis {

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	public static final void format(final long time, final StringExtractor extractor) {
		int millis = (int) (time % 86400000);

		final int hours = millis / 3600000;
		millis %= 3600000;

		final int minutes = millis / 60000;
		millis %= 60000;

		final int seconds = millis / 1000;
		millis %= 1000;

		final char[] buf = extractor.getCharArray(12);
		int charPos = extractor.getLength();

		final int r = millis % 100;
		millis /= 100;

		buf[--charPos] = Constants.digitOnes[r];
		buf[--charPos] = Constants.digitTens[r];
		buf[--charPos] = Constants.digits[millis];
		buf[--charPos] = ',';
		buf[--charPos] = Constants.digitOnes[seconds];
		buf[--charPos] = Constants.digitTens[seconds];
		buf[--charPos] = ':';
		buf[--charPos] = Constants.digitOnes[minutes];
		buf[--charPos] = Constants.digitTens[minutes];
		buf[--charPos] = ':';
		buf[--charPos] = Constants.digitOnes[hours];
		buf[--charPos] = Constants.digitTens[hours];
	}

} // End HourMinSecMillisFormat
