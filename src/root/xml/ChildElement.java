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

import root.adt.ListLazyLoad;
import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public abstract class ChildElement implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	protected final String name;
	protected final int indent;

	protected final ListLazyLoad<Attribute> attributeList;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	protected ChildElement(final String name, final int indent) {
		this.name = name;
		this.indent = indent;

		this.attributeList = new ListLazyLoad<>();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void addAttribute(final String attributeName, final boolean value) {
		this.attributeList.add(new AttributeBoolean(attributeName, value));
	}

	public final void addAttribute(final String attributeName, final Extractable value) {
		this.attributeList.add(new AttributeExtractable(attributeName, value));
	}

	public final void addAttribute(final String attributeName, final int value) {
		this.attributeList.add(new AttributeInt(attributeName, value));
	}

	public final void addAttribute(final String attributeName, final Object value) {
		this.attributeList.add(new AttributeObject(attributeName, value));
	}

	public final void addAttribute(final String attributeName, final String value) {
		this.attributeList.add(new AttributeString(attributeName, value));
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor();
		this.extract(extractor);
		return extractor.toString();
	}

} // End ChildElement
