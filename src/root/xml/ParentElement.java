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

import root.adt.ListArray;
import root.adt.ListLazyLoad;
import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ParentElement implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final String name;
	private final int indent;

	private final ListLazyLoad<Attribute> attributeList;
	private final ListArray<Extractable> elementList;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public ParentElement(final String name) {
		this.name = name;
		this.indent = 0;
		this.attributeList = new ListLazyLoad<>();
		this.elementList = new ListArray<>();
	}

	public ParentElement(final String name, final int indent) {
		this.name = name;
		this.indent = indent;
		this.attributeList = new ListLazyLoad<>();
		this.elementList = new ListArray<>();
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

	public final ChildElement addChildElement(final String elementName, final boolean value) {
		final ChildElement child = new ChildElementBoolean(elementName, value, this.indent + 1);
		this.elementList.add(child);
		return child;
	}

	public final ChildElement addChildElement(final String elementName, final Extractable value) {
		final ChildElement child = new ChildElementExtractable(elementName, value, this.indent + 1);
		this.elementList.add(child);
		return child;
	}

	public final ChildElement addChildElement(final String elementName, final int value) {
		final ChildElement child = new ChildElementInt(elementName, value, this.indent + 1);
		this.elementList.add(child);
		return child;
	}

	public final ChildElement addChildElement(final String elementName, final Object value) {
		final ChildElement child = new ChildElementObject(elementName, value, this.indent + 1);
		this.elementList.add(child);
		return child;
	}

	public final ChildElement addChildElement(final String elementName, final String value) {
		final ChildElement child = new ChildElementString(elementName, value, this.indent + 1);
		this.elementList.add(child);
		return child;
	}

	public final ParentElement addParentElement(final String elementName) {
		final ParentElement parent = new ParentElement(elementName, this.indent + 1);
		this.elementList.add(parent);
		return parent;
	}

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

		// 4. Put the child elements on the parent, if present
		if (this.elementList.isEmpty()) {
			extractor.append('/').append('>');
		} else {
			extractor.append('>').append('\n');

			for (final Extractable e : this.elementList) {
				e.extract(extractor);
			}

			// b) Indent closing element
			for (int i = 0; i < this.indent; i++) {
				extractor.append(' ').append(' ');
			}

			extractor.append('<').append('/').append(this.name).append('>');
		}

		extractor.append('\n');
	}

} // End ParentElement
