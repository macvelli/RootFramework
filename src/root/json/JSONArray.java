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

import root.adt.ListLinked;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class JSONArray extends JSONValue {

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	private final ListLinked<JSONValue> valueList;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public JSONArray() {
		this.valueList = new ListLinked<>();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void add(final boolean value) {
		this.valueList.add(new JSONBoolean(value));
	}

	public final void add(final double value) {
		this.valueList.add(new JSONDouble(value));
	}

	public final void add(final int value) {
		this.valueList.add(new JSONInteger(value));
	}

	public final void add(final JSON json) {
		this.valueList.add(json == null ? new JSONString(null) : json.marshall());
	}

	public final void add(final JSONValue value) {
		this.valueList.add(value);
	}

	public final void add(final String value) {
		this.valueList.add(new JSONString(value));
	}

	@Override
	public final void extract(final StringExtractor chars) {
		chars.append('[');

		final int start = chars.getLength();
		for (final JSONValue value : this.valueList) {
			if (start < chars.getLength()) {
				chars.append(',');
			}
			chars.append(value);
		}

		chars.append(']');
	}

	@Override
	public final String toString() {
		final StringExtractor chars = new StringExtractor(1024);
		this.extract(chars);
		return chars.toString();
	}

} // End JSONArray
