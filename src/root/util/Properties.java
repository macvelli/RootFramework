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
package root.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import root.annotation.Todo;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Todo("Right now this is just a wrapper around java.util.Properties but this thing really needs to be completely rewritten -- just look at the implementation from the JDK for evidence")
public final class Properties {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	protected final java.util.Properties properties;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Properties(final Class<?> clazz, final String... resources) {
		this.properties = new java.util.Properties();

		InputStream is = null;
		for (final String resource : resources) {
			try {
				is = Root.getResourceAsStream(clazz, resource);

				if (is != null) {
					this.properties.load(is);
				}
			} catch (final IOException e) {
				throw new RuntimeException("An error occurred when attempting to load " + resource, e);
			} finally {
				Root.close(is);
			}
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void clear() {
		this.properties.clear();
	}

	public final boolean contains(final String value) {
		return this.properties.contains(value);
	}

	public final boolean containsKey(final String key) {
		return this.properties.containsKey(key);
	}

	public final String get(final String key) {
		return this.properties.getProperty(key);
	}

	public final String get(final String key, final String defaultValue) {
		return this.properties.getProperty(key, defaultValue);
	}

	@Todo("Need to come up with a Fast parseInt() method that I can use in place of Integer.valueOf()")
	public final int getInt(final String key) {
		final String i = this.properties.getProperty(key);

		return i != null ? Integer.valueOf(i) : 0;
	}

	public final String remove(final String key) {
		return (String) this.properties.remove(key);
	}

	public final void set(final String key, final String value) {
		this.properties.setProperty(key, value);
	}

	@Override
	public final String toString() {
		final StringWriter writer = new StringWriter();

		this.properties.list(new PrintWriter(writer));

		return writer.toString();
	}

} // End Properties
