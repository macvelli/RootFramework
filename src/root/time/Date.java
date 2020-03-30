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

import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Date implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	public int year;
	public int month;
	public int day;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Date() {
		// No-op default constructor
	}

	public Date(final int year, final int month, final int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		// 1. Append the year
		extractor.append(this.year);
		extractor.append('-');

		// 2. Append the month
		if (this.month < 10) {
			extractor.append('0');
		}
		extractor.append(this.month);
		extractor.append('-');

		// 3. Append the day
		if (this.day < 10) {
			extractor.append('0');
		}
		extractor.append(this.day);
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(10);
		this.extract(extractor);
		return extractor.toString();
	}

} // End Date
