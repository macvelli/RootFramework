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

import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import root.adt.MapBuilder;
import root.adt.MapImmutable;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Log4JHandler extends Handler {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final String FQCN = Log4JHandler.class.getName();

	private static final MapImmutable<Level, org.apache.log4j.Level> levelMap = new MapBuilder<Level, org.apache.log4j.Level>()
			.put(Level.ALL, org.apache.log4j.Level.ALL)
			.put(Level.CONFIG, org.apache.log4j.Level.INFO)
			.put(Level.FINE, org.apache.log4j.Level.DEBUG)
			.put(Level.FINER, org.apache.log4j.Level.DEBUG)
			.put(Level.FINEST, org.apache.log4j.Level.TRACE)
			.put(Level.INFO, org.apache.log4j.Level.INFO)
			.put(Level.OFF, org.apache.log4j.Level.OFF)
			.put(Level.SEVERE, org.apache.log4j.Level.ERROR)
			.put(Level.WARNING, org.apache.log4j.Level.WARN)
			.build();

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void close() {
	}

	@Override
	public final void flush() {
	}

	@Override
	public final void publish(final LogRecord record) {
		try {
			final Logger logger = Logger.getLogger(record.getLoggerName());
			logger.callAppenders(new LoggingEvent(FQCN, logger, record.getMillis(), levelMap.get(record.getLevel(), org.apache.log4j.Level.INFO),
					record.getMessage(), record.getThrown()));
		} catch (final Exception e) {
			this.reportError(null, e, ErrorManager.GENERIC_FAILURE);
		}
	}

} // End Log4JHandler
