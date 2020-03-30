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

import java.util.GregorianCalendar;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Calendar {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final GregorianCalendar GREGORIAN_CALENDAR = new GregorianCalendar();
	private static final ReentrantLock calendarLock = new ReentrantLock();

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public static final root.time.Date getDate() {
		final long now = System.currentTimeMillis();
		final root.time.Date date = new root.time.Date();

		calendarLock.lock();
		try {
			GREGORIAN_CALENDAR.setTimeInMillis(now);

			date.year = GREGORIAN_CALENDAR.get(java.util.Calendar.YEAR);
			date.month = GREGORIAN_CALENDAR.get(java.util.Calendar.MONTH) + 1;
			date.day = GREGORIAN_CALENDAR.get(java.util.Calendar.DAY_OF_MONTH);
		} finally {
			calendarLock.unlock();
		}

		return date;
	}

	public static final root.time.Date getDate(final java.util.Date javaDate) {
		final long javaDateMillis = javaDate.getTime();
		final root.time.Date date = new root.time.Date();

		calendarLock.lock();
		try {
			GREGORIAN_CALENDAR.setTimeInMillis(javaDateMillis);

			date.year = GREGORIAN_CALENDAR.get(java.util.Calendar.YEAR);
			date.month = GREGORIAN_CALENDAR.get(java.util.Calendar.MONTH) + 1;
			date.day = GREGORIAN_CALENDAR.get(java.util.Calendar.DAY_OF_MONTH);
		} finally {
			calendarLock.unlock();
		}

		return date;
	}

	public static final root.time.DateTime getDateTime() {
		final long now = System.currentTimeMillis();
		final root.time.DateTime dateTime = new root.time.DateTime();

		calendarLock.lock();
		try {
			GREGORIAN_CALENDAR.setTimeInMillis(now);

			dateTime.year = GREGORIAN_CALENDAR.get(java.util.Calendar.YEAR);
			dateTime.month = GREGORIAN_CALENDAR.get(java.util.Calendar.MONTH) + 1;
			dateTime.day = GREGORIAN_CALENDAR.get(java.util.Calendar.DAY_OF_MONTH);
			dateTime.hour = GREGORIAN_CALENDAR.get(java.util.Calendar.HOUR_OF_DAY);
			dateTime.minute = GREGORIAN_CALENDAR.get(java.util.Calendar.MINUTE);
			dateTime.second = GREGORIAN_CALENDAR.get(java.util.Calendar.SECOND);
		} finally {
			calendarLock.unlock();
		}

		return dateTime;
	}

	public static final root.time.DateTime getDateTime(final java.util.Date javaDate) {
		final long javaDateMillis = javaDate.getTime();
		final root.time.DateTime dateTime = new root.time.DateTime();

		calendarLock.lock();
		try {
			GREGORIAN_CALENDAR.setTimeInMillis(javaDateMillis);

			dateTime.year = GREGORIAN_CALENDAR.get(java.util.Calendar.YEAR);
			dateTime.month = GREGORIAN_CALENDAR.get(java.util.Calendar.MONTH) + 1;
			dateTime.day = GREGORIAN_CALENDAR.get(java.util.Calendar.DAY_OF_MONTH);
			dateTime.hour = GREGORIAN_CALENDAR.get(java.util.Calendar.HOUR_OF_DAY);
			dateTime.minute = GREGORIAN_CALENDAR.get(java.util.Calendar.MINUTE);
			dateTime.second = GREGORIAN_CALENDAR.get(java.util.Calendar.SECOND);
		} finally {
			calendarLock.unlock();
		}

		return dateTime;
	}

	public static final root.time.Time getTime() {
		final long now = System.currentTimeMillis();
		final root.time.Time time = new root.time.Time();

		calendarLock.lock();
		try {
			GREGORIAN_CALENDAR.setTimeInMillis(now);

			time.hour = GREGORIAN_CALENDAR.get(java.util.Calendar.HOUR_OF_DAY);
			time.minute = GREGORIAN_CALENDAR.get(java.util.Calendar.MINUTE);
			time.second = GREGORIAN_CALENDAR.get(java.util.Calendar.SECOND);
		} finally {
			calendarLock.unlock();
		}

		return time;
	}

	public static final root.time.Time getTime(final java.util.Date javaDate) {
		final long javaDateMillis = javaDate.getTime();
		final root.time.Time time = new root.time.Time();

		calendarLock.lock();
		try {
			GREGORIAN_CALENDAR.setTimeInMillis(javaDateMillis);

			time.hour = GREGORIAN_CALENDAR.get(java.util.Calendar.HOUR_OF_DAY);
			time.minute = GREGORIAN_CALENDAR.get(java.util.Calendar.MINUTE);
			time.second = GREGORIAN_CALENDAR.get(java.util.Calendar.SECOND);
		} finally {
			calendarLock.unlock();
		}

		return time;
	}

} // End Calendar
