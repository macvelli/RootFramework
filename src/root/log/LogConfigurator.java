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

import java.io.UnsupportedEncodingException;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;

import root.adt.MapBuilder;
import root.adt.MapImmutable;
import root.lang.Prefix;
import root.util.Root;

public final class LogConfigurator {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final MapImmutable<String, Level> levelMap = new MapBuilder<String, Level>().put(Level.ALL.getName(), Level.ALL)
			.put(Level.CONFIG.getName(), Level.CONFIG)
			.put(Level.FINE.getName(), Level.FINE)
			.put(Level.FINER.getName(), Level.FINER)
			.put(Level.FINEST.getName(), Level.FINEST)
			.put(Level.INFO.getName(), Level.INFO)
			.put(Level.OFF.getName(), Level.OFF)
			.put(Level.SEVERE.getName(), Level.SEVERE)
			.put(Level.WARNING.getName(), Level.WARNING)
			.build();

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final LogManager manager;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public LogConfigurator() {
		this.manager = LogManager.getLogManager();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final Filter getFilter(final Prefix className, final Filter defaultValue) {
		final String filterVal = this.manager.getProperty(className.toString(".filter"));

		if (filterVal == null) {
			return defaultValue;
		}

		try {
			return (Filter) Root.newInstance(ClassLoader.getSystemClassLoader(), filterVal.trim());
		} catch (final Exception ex) {
			// Ignore any exceptions thrown
		}

		return defaultValue;
	}

	public final Formatter getFormatter(final Prefix className, final Formatter defaultValue) {
		final String formatterVal = this.manager.getProperty(className.toString(".formatter"));

		if (formatterVal == null) {
			return defaultValue;
		}

		try {
			return (Formatter) Root.newInstance(ClassLoader.getSystemClassLoader(), formatterVal.trim());
		} catch (final Exception ex) {
			// Ignore any exceptions thrown
		}

		return defaultValue;
	}

	public final Level getLevel(final Prefix className, final Level defaultValue) {
		final String levelVal = this.manager.getProperty(className.toString(".level"));

		if (levelVal == null) {
			return defaultValue;
		}

		return levelMap.get(levelVal.trim(), defaultValue);
	}

	public final void setEncoding(final Handler handler, final String defaultValue) {
		final Class<? extends Handler> clazz = handler.getClass();
		String encodingStr = this.manager.getProperty(clazz.getName() + ".encoding");

		encodingStr = encodingStr == null ? defaultValue : encodingStr.trim();

		if (encodingStr != null) {
			try {
				handler.setEncoding(encodingStr);
			} catch (SecurityException | UnsupportedEncodingException e) {
				// Do nothing because the encoding should not have changed
			}
		}
	}

} // End LogConfigurator
