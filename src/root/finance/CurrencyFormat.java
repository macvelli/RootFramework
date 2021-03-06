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
package root.finance;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
enum CurrencyFormat {

	// <><><><><><><><><><><><><><>< Enum Values ><><><><><><><><><><><><><><>

	Comma(NumberFormat.Comma),
	CommaPeriod(NumberFormat.CommaPeriod),
	Period(NumberFormat.Period),
	PeriodComma(NumberFormat.PeriodComma),
	SpaceComma(NumberFormat.SpaceComma),
	SpacePeriod(NumberFormat.SpacePeriod),
	TickPeriod(NumberFormat.TickPeriod);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final CurrencyFormatter formatter;
	private final CurrencyParser parser;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private CurrencyFormat(final NumberFormat numberFormat) {
		this.formatter = new CurrencyFormatter(numberFormat);
		this.parser = new CurrencyParser(numberFormat);
	}

	// <><><><><><><><><><><><><>< Package Methods ><><><><><><><><><><><><><>

	CurrencyFormatter getFormatter() {
		return this.formatter;
	}

	CurrencyParser getParser() {
		return this.parser;
	}

} // End CurrencyFormat
