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

import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import root.adt.ListArray;
import root.adt.ListImmutable;
import root.lang.Extractable;
import root.lang.ParamString;
import root.util.Root;

/**
 * TODO: Tracking entering and exiting a method is classic AOP...come up with such to not just track entering and exiting but also log what is being
 * passed into the method and also what the return value is, if any<br>
 * TODO: How to dynamically change logging levels per HTML request? Filter? Promote in the beginning and restore at the end? Works on root logger and
 * its dumb derivatives but what about a logger with its own level?<br>
 * TODO: Next, need to come up with a standard way to glean configuration data from the properties file<br>
 * TODO: Convert to using try/catch with resources<br>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Log {

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	public static final void init(final Class<?> clazz, final String propertiesFile) {
		final InputStream in = Root.getResourceAsStream(clazz, propertiesFile);

		if (in == null) {
			throw new RuntimeException("Cannot find " + propertiesFile + " on either the classpath or the file system");
		}

		try {
			LogManager.getLogManager().readConfiguration(in);
		} catch (final Exception e) {
			throw new RuntimeException("Error during log initialization: " + propertiesFile, e);
		} finally {
			Root.close(in);
		}
	}

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private String resourceBundleName;
	private ResourceBundle resourceBundle;

	private final Logger logger;
	private final Filter loggerFilter;
	private final ListImmutable<Handler> handlerList;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Log(final Class<?> clazz) {
		this(clazz.getName());
	}

	public Log(final String name) {
		this.logger = Logger.getLogger(name);
		this.loggerFilter = this.logger.getFilter();

		// TODO: There may be a timing issue here between reading the properties file and this code
		Logger l;
		for (l = this.logger; l != null; l = l.getParent()) {
			if (l.getResourceBundleName() != null) {
				this.resourceBundleName = l.getResourceBundleName();
				this.resourceBundle = l.getResourceBundle();
				break;
			}

			if (!l.getUseParentHandlers()) {
				break;
			}
		}

		Handler[] handlers;
		final ListArray<Handler> handlerListArray = new ListArray<>();
		for (l = this.logger; l != null; l = l.getParent()) {
			handlers = l.getHandlers();

			if (handlers != null && handlers.length > 0) {
				handlerListArray.addAll(handlers, 0, handlers.length);
			}

			if (!l.getUseParentHandlers()) {
				break;
			}
		}

		this.handlerList = handlerListArray.toImmutable();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void close() {
		for (final Handler h : this.handlerList) {
			try {
				h.close();
			} catch (final Throwable t) {
			}
		}
	}

	public final void debug(final ParamString paramString, final Extractable... params) {
		if (this.logger.isLoggable(Level.FINE)) {
			this.log(new LogRecord(Level.FINE, paramString.format(params)));
		}
	}

	public final void debug(final String msg) {
		if (this.logger.isLoggable(Level.FINE)) {
			this.log(new LogRecord(Level.FINE, msg));
		}
	}

	public final void debug(final String msg, final Extractable... params) {
		if (this.logger.isLoggable(Level.FINE)) {
			this.log(new LogRecord(Level.FINE, ParamString.formatMsg(msg, params)));
		}
	}

	public final void debug(final String msg, final Object... params) {
		if (this.logger.isLoggable(Level.FINE)) {
			this.log(new LogRecord(Level.FINE, ParamString.formatMsg(msg, params)));
		}
	}

	public final void debug(final String msg, final Throwable t) {
		if (this.logger.isLoggable(Level.FINE)) {
			final LogRecord lr = new LogRecord(Level.FINE, msg);
			lr.setThrown(t);
			this.log(lr);
		}
	}

	public final void error(final Object obj, final Throwable t) {
		if (this.logger.isLoggable(Level.SEVERE)) {
			final LogRecord lr = new LogRecord(Level.SEVERE, obj.toString());
			lr.setThrown(t);
			this.log(lr);
		}
	}

	public final void error(final ParamString paramString, final Extractable... params) {
		if (this.logger.isLoggable(Level.SEVERE)) {
			this.log(new LogRecord(Level.SEVERE, paramString.format(params)));
		}
	}

	public final void error(final String msg) {
		if (this.logger.isLoggable(Level.SEVERE)) {
			this.log(new LogRecord(Level.SEVERE, msg));
		}
	}

	public final void error(final String msg, final Extractable... params) {
		if (this.logger.isLoggable(Level.SEVERE)) {
			this.log(new LogRecord(Level.SEVERE, ParamString.formatMsg(msg, params)));
		}
	}

	public final void error(final String msg, final Object... params) {
		if (this.logger.isLoggable(Level.SEVERE)) {
			this.log(new LogRecord(Level.SEVERE, ParamString.formatMsg(msg, params)));
		}
	}

	public final void error(final String msg, final Throwable t) {
		if (this.logger.isLoggable(Level.SEVERE)) {
			final LogRecord lr = new LogRecord(Level.SEVERE, msg);
			lr.setThrown(t);
			this.log(lr);
		}
	}

	public final void error(final String msg, final Throwable t, final Extractable... params) {
		if (this.logger.isLoggable(Level.SEVERE)) {
			final LogRecord lr = new LogRecord(Level.SEVERE, ParamString.formatMsg(msg, params));
			lr.setThrown(t);
			this.log(lr);
		}
	}

	public final void error(final String msg, final Throwable t, final Object... params) {
		if (this.logger.isLoggable(Level.SEVERE)) {
			final LogRecord lr = new LogRecord(Level.SEVERE, ParamString.formatMsg(msg, params));
			lr.setThrown(t);
			this.log(lr);
		}
	}

	public final void error(final Throwable t) {
		if (this.logger.isLoggable(Level.SEVERE)) {
			final LogRecord lr = new LogRecord(Level.SEVERE, t.getMessage());
			lr.setThrown(t);
			this.log(lr);
		}
	}

	public final Logger getParent() {
		return this.logger.getParent();
	}

	public final void info(final ParamString paramString, final Extractable... params) {
		if (this.logger.isLoggable(Level.INFO)) {
			this.log(new LogRecord(Level.INFO, paramString.format(params)));
		}
	}

	public final void info(final String msg) {
		if (this.logger.isLoggable(Level.INFO)) {
			this.log(new LogRecord(Level.INFO, msg));
		}
	}

	public final void info(final String msg, final Extractable... params) {
		if (this.logger.isLoggable(Level.INFO)) {
			this.log(new LogRecord(Level.INFO, ParamString.formatMsg(msg, params)));
		}
	}

	public final void info(final String msg, final Object... params) {
		if (this.logger.isLoggable(Level.INFO)) {
			this.log(new LogRecord(Level.INFO, ParamString.formatMsg(msg, params)));
		}
	}

	public final void info(final String msg, final Throwable t) {
		if (this.logger.isLoggable(Level.INFO)) {
			final LogRecord lr = new LogRecord(Level.INFO, msg);
			lr.setThrown(t);
			this.log(lr);
		}
	}

	public final void throwException(final RuntimeException e) {
		if (this.logger.isLoggable(Level.SEVERE)) {
			final LogRecord lr = new LogRecord(Level.SEVERE, e.getMessage());
			lr.setThrown(e);
			this.log(lr);
		}

		throw e;
	}

	public final void trace(final ParamString paramString, final Extractable... params) {
		if (this.logger.isLoggable(Level.FINEST)) {
			this.log(new LogRecord(Level.FINEST, paramString.format(params)));
		}
	}

	public final void trace(final String msg) {
		if (this.logger.isLoggable(Level.FINEST)) {
			this.log(new LogRecord(Level.FINEST, msg));
		}
	}

	public final void trace(final String msg, final Extractable... params) {
		if (this.logger.isLoggable(Level.FINEST)) {
			this.log(new LogRecord(Level.FINEST, ParamString.formatMsg(msg, params)));
		}
	}

	public final void trace(final String msg, final Object... params) {
		if (this.logger.isLoggable(Level.FINEST)) {
			this.log(new LogRecord(Level.FINEST, ParamString.formatMsg(msg, params)));
		}
	}

	public final void trace(final String msg, final Throwable t) {
		if (this.logger.isLoggable(Level.FINEST)) {
			final LogRecord lr = new LogRecord(Level.FINEST, msg);
			lr.setThrown(t);
			this.log(lr);
		}
	}

	public final void warn(final ParamString paramString, final Extractable... params) {
		if (this.logger.isLoggable(Level.WARNING)) {
			this.log(new LogRecord(Level.WARNING, paramString.format(params)));
		}
	}

	public final void warn(final String msg) {
		if (this.logger.isLoggable(Level.WARNING)) {
			this.log(new LogRecord(Level.WARNING, msg));
		}
	}

	public final void warn(final String msg, final Extractable... params) {
		if (this.logger.isLoggable(Level.WARNING)) {
			this.log(new LogRecord(Level.WARNING, ParamString.formatMsg(msg, params)));
		}
	}

	public final void warn(final String msg, final Object... params) {
		if (this.logger.isLoggable(Level.WARNING)) {
			this.log(new LogRecord(Level.WARNING, ParamString.formatMsg(msg, params)));
		}
	}

	public final void warn(final String msg, final Throwable t) {
		if (this.logger.isLoggable(Level.WARNING)) {
			final LogRecord lr = new LogRecord(Level.WARNING, msg);
			lr.setThrown(t);
			this.log(lr);
		}
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private void log(final LogRecord lr) {
		lr.setLoggerName(this.logger.getName());

		if (this.resourceBundle != null) {
			lr.setResourceBundle(this.resourceBundle);
			lr.setResourceBundleName(this.resourceBundleName);
		}

		if (this.loggerFilter == null || this.loggerFilter.isLoggable(lr)) {
			for (final Handler handler : this.handlerList) {
				handler.publish(lr);
			}
		}
	}

} // End Log
