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
package root.log;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import root.adt.MapBuilder;
import root.adt.MapImmutable;
import root.lang.StringExtractor;
import root.text.HourMinSecMillis;
import root.util.Root;

/**
 * "java.util.logging.ConsoleHandler will only load formatter classes from the system classloader."
 * <p>
 * That's nice. How the hell do you get around that?
 * <p>
 * The only solution I have found is to put all the {@link root.log} classes in a JAR under the JRE's lib\ext directory.
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class JBossFormatter extends Formatter {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final String lineSep = System.getProperty("line.separator");;

	private static final MapImmutable<Level, char[]> levelMap = new MapBuilder<Level, char[]>()
			.put(Level.FINE, new char[] { ' ', 'D', 'E', 'B', 'U', 'G', ' ' })
			.put(Level.INFO, new char[] { ' ', 'I', 'N', 'F', 'O', ' ', ' ' })
			.put(Level.WARNING, new char[] { ' ', 'W', 'A', 'R', 'N', ' ', ' ' })
			.put(Level.SEVERE, new char[] { ' ', 'F', 'A', 'I', 'L', ' ', ' ' })
			.put(Level.FINEST, new char[] { ' ', 'T', 'R', 'A', 'C', 'E', ' ' })
			.build();

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final String format(final LogRecord record) {
		final StringExtractor extractor = new StringExtractor(256);

		// Put log record time in "HH:mm:ss,SSS" format
		HourMinSecMillis.format(record.getMillis(), extractor);

		// Print the logging level
		extractor.append(levelMap.get(record.getLevel()));

		// Populate the simple logger name
		this.populateSimpleLoggerName(record.getLoggerName(), extractor);

		// Populate the log message
		extractor.append(' ');
		extractor.append(record.getMessage());
		extractor.append(lineSep);

		return extractor.toString();
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private void populateSimpleLoggerName(final String loggerName, final StringExtractor extractor) {
		final char[] loggerNameCharArray = Root.toCharArray(loggerName);
		int offset = loggerNameCharArray.length;

		while (offset > 0 && loggerNameCharArray[--offset] != '.') {
			// Find the last index of '.'
		}

		extractor.append('[');
		if (offset == 0) {
			extractor.append(loggerNameCharArray, 0, loggerNameCharArray.length);
		} else {
			offset++;
			extractor.append(loggerNameCharArray, offset, loggerNameCharArray.length - offset);
		}
		extractor.append(']');
	}

} // End JBossFormatter
