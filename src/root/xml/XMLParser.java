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

import root.adt.ListLinked;
import root.annotation.Todo;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Todo({ "Wow this needs a lot of work also...attributes anyone? What is considered to be valid XML? How about checking against an XML Schema for validation?",
		"Also, a Substring class needs to be implemented in the JDK that worked like String did before otherwise this is a bunch of crap with respect to memory management",
		"And how can I make an Extractable Substring?" })
public final class XMLParser {

	// <><><><><><><><><><><><><>< Static Artifacts ><><><><><><><><><><><><><>

	// public static final Element parse(final String xml) {
	// return parse(xml, 0);
	// }

	// private static final Element parse(final String xml, final int indent) {
	// final Element e;
	// final int startTagOpenIndex = xml.indexOf('<') + 1;
	//
	// if (startTagOpenIndex == 0) {
	// throw new XMLParserException("Cannot find element tag in " + xml);
	// }
	//
	// int startTagCloseIndex = xml.indexOf('>', startTagOpenIndex);
	//
	// if (xml.charAt(startTagCloseIndex - 1) == '/') {
	// // Create and return an empty element
	// e = new Element(xml.substring(startTagOpenIndex, startTagCloseIndex - 1), Constants.EMPTY_STRING, indent);
	// e.fragmentLength = startTagCloseIndex + 1;
	// return e;
	// }
	//
	// final String openTagName = xml.substring(startTagOpenIndex, startTagCloseIndex);
	//
	// int endTagOpenIndex = xml.indexOf('<', ++startTagCloseIndex);
	// if (endTagOpenIndex == 0) {
	// throw new XMLParserException("Cannot find end tag </" + openTagName + "> from index " + startTagCloseIndex);
	// }
	//
	// if (xml.charAt(endTagOpenIndex + 1) == '/') {
	// final int endTagCloseIndex = xml.indexOf('>', endTagOpenIndex + 2);
	// final String closeTagName = xml.substring(endTagOpenIndex + 2, endTagCloseIndex);
	//
	// if (!openTagName.equals(closeTagName)) {
	// throw new XMLParserException(
	// "Open Tag Name <" + openTagName + ">/Close Tag Name </" + closeTagName + "> mismatch at index " + endTagOpenIndex);
	// }
	//
	// e = new Element(openTagName, xml.substring(startTagCloseIndex, endTagOpenIndex), indent);
	// e.fragmentLength = endTagCloseIndex + 1;
	// return e;
	// } else {
	// final ListArray<Element> children = new ListArray<>();
	//
	// do {
	// final Element child = parse(xml.substring(endTagOpenIndex), indent + 1);
	// children.add(child);
	//
	// endTagOpenIndex = xml.indexOf('<', endTagOpenIndex + child.fragmentLength);
	// if (endTagOpenIndex == 0) {
	// throw new XMLParserException("Cannot find end tag </" + openTagName + "> from index " + startTagCloseIndex);
	// }
	// } while (xml.charAt(endTagOpenIndex + 1) != '/');
	//
	// final int endTagCloseIndex = xml.indexOf('>', endTagOpenIndex + 2);
	// final String closeTagName = xml.substring(endTagOpenIndex + 2, endTagCloseIndex);
	//
	// if (!openTagName.equals(closeTagName)) {
	// throw new XMLParserException(
	// "Open Tag Name <" + openTagName + ">/Close Tag Name </" + closeTagName + "> mismatch at index " + endTagOpenIndex);
	// }
	//
	// e = new Element(openTagName, children, indent);
	// e.fragmentLength = endTagCloseIndex + 1;
	// return e;
	// }
	// }

	// <><><><><><><><><><><><><>< Class Attributes ><><><><><><><><><><><><><>

	private final String xml;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public XMLParser(final String xml) {
		this.xml = xml;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final ListLinked<XMLParser> getFragments(final String elementName) {
		final ListLinked<XMLParser> fragments = new ListLinked<>();
		final String startTag = '<' + elementName + '>';
		final String endTag = "</" + elementName + '>';
		int beginIndex, endIndex;

		beginIndex = this.xml.indexOf(startTag);
		while (beginIndex >= 0) {
			beginIndex += startTag.length();
			endIndex = this.xml.indexOf(endTag, beginIndex);

			if (endIndex < 0) {
				throw new XMLParserException("Cannot find end tag " + endTag);
			}

			fragments.add(new XMLParser(this.xml.substring(beginIndex, endIndex)));

			endIndex += endTag.length();
			beginIndex = this.xml.indexOf(startTag, endIndex);
		}

		return fragments;
	}

	public final String getString() {
		return this.xml;
	}

	public final String getString(final String elementName) {
		final String startTag = '<' + elementName + '>';
		final String endTag = "</" + elementName + '>';
		int beginIndex, endIndex;

		beginIndex = this.xml.indexOf(startTag);
		if (beginIndex >= 0) {
			beginIndex += startTag.length();
			endIndex = this.xml.indexOf(endTag, beginIndex);

			if (endIndex < 0) {
				throw new XMLParserException("Cannot find end tag " + endTag);
			}

			return this.xml.substring(beginIndex, endIndex);
		}

		return null;
	}

	@Override
	public final String toString() {
		return this.xml;
	}

}
