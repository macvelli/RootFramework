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

import java.math.BigDecimal;

import root.annotation.Todo;
import root.lang.Extractable;
import root.lang.StringExtractor;
import root.time.Calendar;

/**
 * https://www.w3schools.com/xml/schema_simple.asp
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Todo("Does Extractable play any role here at all, whether for names and/or values? Maybe make everything FastString so they all fly when extracted...maybe the names should be FastString and the values should be Extractable so that I can support FastString, FastInteger, FastWhatever, etc")
public final class XMLBroker implements Extractable {

	public static void main(final String[] args) {
		final XMLBroker xmlBroker = new XMLBroker("bookstore");

		final ParentElement bookstore = xmlBroker.getRootElement();

		final ParentElement book = bookstore.addParentElement("book");
		book.addAttribute("category", "cooking");

		final ChildElement child = book.addChildElement("title", "Everyday Italian");
		child.addAttribute("lang", "en");

		book.addChildElement("author", "Giada De Laurentiis");
		book.addChildElement("year", 2005);
		book.addChildElement("price", new BigDecimal(30).setScale(2));
		book.addChildElement("onSale", false);
		book.addChildElement("publishDate", Calendar.getDate());
		book.addChildElement("currentTime", Calendar.getTime());
		book.addChildElement("timestamp", Calendar.getDateTime());

		System.out.println(xmlBroker);

		System.exit(0);
	}

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final ParentElement rootElement;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public XMLBroker(final String elementName) {
		this.rootElement = new ParentElement(elementName);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		this.rootElement.extract(extractor);
	}

	public final ParentElement getRootElement() {
		return this.rootElement;
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(1024);
		this.extract(extractor);
		return extractor.toString();
	}

} // End XMLBroker
