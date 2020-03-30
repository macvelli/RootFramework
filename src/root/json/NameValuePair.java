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
package root.json;

import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
class NameValuePair implements Extractable {

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	private final String name;
	private final JSONValue value;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	NameValuePair(final String name, final JSONValue value) {
		this.name = name;
		this.value = value;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor chars) {
		chars.append('"').append(this.name).append('"');
		chars.append(':');
		chars.append(this.value);
	}

} // End NameValuePair
