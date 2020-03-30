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
package root.xml;

import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ChildElementInt extends ChildElement {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final int value;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public ChildElementInt(final String name, final int value, final int indent) {
		super(name, indent);

		this.value = value;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		// 1. Indent opening element
		for (int i = 0; i < this.indent; i++) {
			extractor.append(' ').append(' ');
		}

		// 2. Create the opening element tag
		extractor.append('<').append(this.name);

		// 3. Put attributes on the element, if present
		if (this.attributeList.size() > 0) {
			for (final Attribute a : this.attributeList) {
				extractor.append(' ');
				a.extract(extractor);
			}
		}

		// 4. Put the value on the element
		extractor.append('>').append(this.value).append('<').append('/').append(this.name).append('>').append('\n');
	}

} // End ChildElementInt
