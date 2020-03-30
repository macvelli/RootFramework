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

import java.io.PrintStream;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import root.lang.Prefix;

/**
 * TODO: Find out how {@link SimpleFormatter} works and create a <code>BasicFormatter</code> that does exactly the same thing except that its *fast*!
 * <br>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class SysoutHandler extends Handler {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private static final int offLevelValue = Level.OFF.intValue();

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int levelValue;

	private Level level;
	private Filter filter;
	private Formatter formatter;

	private final PrintStream out;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public SysoutHandler() {
		final LogConfigurator configurator = new LogConfigurator();
		final Prefix className = new Prefix(this.getClass().getName());

		this.out = System.out;

		// Set the logging level
		this.level = configurator.getLevel(className, Level.ALL);
		this.levelValue = this.level.intValue();

		// Set the logging filter
		this.filter = configurator.getFilter(className, null);

		// Set the logging formatter
		this.formatter = configurator.getFormatter(className, new EchoFormatter());

		// Set the character encoding
		configurator.setEncoding(this, null);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void close() {
		this.out.flush();
	}

	@Override
	public final void flush() {
		this.out.flush();
	}

	@Override
	public final Filter getFilter() {
		return this.filter;
	}

	@Override
	public final Formatter getFormatter() {
		return this.formatter;
	}

	@Override
	public final Level getLevel() {
		return this.level;
	}

	@Override
	public final boolean isLoggable(final LogRecord record) {
		final int a = record.getLevel().intValue();

		return a != offLevelValue && a >= this.levelValue;
	}

	@Override
	public final void publish(final LogRecord record) {
		if (this.isLoggable(record) && (this.filter == null || this.filter.isLoggable(record))) {
			try {
				this.out.print(this.formatter.format(record));

				final Throwable t = record.getThrown();
				if (t != null) {
					t.printStackTrace(this.out);
				}
			} catch (final Exception e) {
				this.reportError(null, e, ErrorManager.FORMAT_FAILURE);
			}
		}
	}

	@Override
	public final void setFilter(final Filter newFilter) {
		this.filter = newFilter;
	}

	@Override
	public final void setFormatter(final Formatter newFormatter) {
		this.formatter = newFormatter;
	}

	@Override
	public final void setLevel(final Level newLevel) {
		this.level = newLevel;
		this.levelValue = this.level.intValue();
	}

} // End SysoutHandler
