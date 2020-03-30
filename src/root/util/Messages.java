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

import root.annotation.Todo;
import root.lang.ParamString;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Todo({ "This could really benefit from the precompiled ParamString and store those instead of the raw Properties",
		"Also see about supporting Extractables with an Extractor" })
public final class Messages {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final Properties messages;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Messages(final Class<?> clazz, final String... resources) {
		this.messages = new Properties(clazz, resources);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final String get(final String key) {
		final String msg = this.messages.get(key);

		return msg == null ? key : msg;
	}

	public final String get(final String key, final Object... params) {
		return ParamString.formatMsg(this.messages.get(key), params);
	}

	@Override
	public final String toString() {
		return this.messages.toString();
	}

} // End Messages
