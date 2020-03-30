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

import root.lang.StringExtractor;

/**
 * http://www.xe.com/symbols.php<br>
 * http://www.thefinancials.com/Default.aspx?SubSectionID=curformat
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public enum Currency {

	// <><><><><><><><><><><><><><>< Enum Values ><><><><><><><><><><><><><><>

	ARS("Argentina Peso", CurrencySymbol.DOLLAR, CurrencyFormat.PeriodComma),
	AUD("Australian Dollar", CurrencySymbol.DOLLAR, CurrencyFormat.SpacePeriod),
	BRL("Brazil Real", CurrencySymbol.REAL, CurrencyFormat.PeriodComma),
	CAD("Canadian Dollar", CurrencySymbol.DOLLAR, CurrencyFormat.CommaPeriod),
	CHF("Switzerland Franc", CurrencySymbol.FRANC, CurrencyFormat.TickPeriod),
	CLP("Chile Peso", CurrencySymbol.DOLLAR, CurrencyFormat.Period),
	CNY("Chinese Yuan", CurrencySymbol.YEN, CurrencyFormat.CommaPeriod),
	COP("Colombia Peso", CurrencySymbol.DOLLAR, CurrencyFormat.PeriodComma),
	DKK("Denmark Krone", CurrencySymbol.KRONE, CurrencyFormat.PeriodComma),
	EGP("Egypt Pound", CurrencySymbol.POUND, CurrencyFormat.CommaPeriod),
	EUR("Euro", CurrencySymbol.EURO, CurrencyFormat.CommaPeriod),
	GBP("British Pound", CurrencySymbol.POUND, CurrencyFormat.CommaPeriod),
	HKD("Hong Kong Dollar", CurrencySymbol.DOLLAR, CurrencyFormat.CommaPeriod),
	ILS("Israel Shekel", CurrencySymbol.SHEKEL, CurrencyFormat.CommaPeriod),
	INR("India Rupee", CurrencySymbol.RUPEE, CurrencyFormat.CommaPeriod),
	JPY("Japanese Yen", CurrencySymbol.YEN, CurrencyFormat.Comma),
	KRW("South Korea Won", CurrencySymbol.WON, CurrencyFormat.Comma),
	MXN("Mexico Peso", CurrencySymbol.DOLLAR, CurrencyFormat.CommaPeriod),
	NOK("Norway Krone", CurrencySymbol.KRONE, CurrencyFormat.PeriodComma),
	NZD("New Zealand Dollar", CurrencySymbol.DOLLAR, CurrencyFormat.CommaPeriod),
	RUB("Russian Ruble", CurrencySymbol.RUBLE, CurrencyFormat.CommaPeriod),
	SEK("Sweden Krona", CurrencySymbol.KRONE, CurrencyFormat.SpaceComma),
	SGD("Singapore Dollar", CurrencySymbol.DOLLAR, CurrencyFormat.CommaPeriod),
	TRY("Turkey Lira", CurrencySymbol.TRY, CurrencyFormat.CommaPeriod),
	TWD("Taiwan New Dollar", CurrencySymbol.TWD, CurrencyFormat.CommaPeriod),
	USD("US Dollar", CurrencySymbol.DOLLAR, CurrencyFormat.CommaPeriod),
	ZAR("South Africa Rand", CurrencySymbol.RAND, CurrencyFormat.SpacePeriod);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final String name;
	private final CurrencySymbol symbol;
	private final CurrencyFormatter formatter;
	private final CurrencyParser parser;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private Currency(final String name, final CurrencySymbol symbol, final CurrencyFormat currencyFormat) {
		this.name = name;
		this.symbol = symbol;
		this.formatter = currencyFormat.getFormatter();
		this.parser = currencyFormat.getParser();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void format(final int i, final StringExtractor extractor) {
		this.formatter.format(i, this.symbol, extractor);
	}

	public final void format(final long l, final StringExtractor extractor) {
		this.formatter.format(l, this.symbol, extractor);
	}

	public final String getCode() {
		return this.name();
	}

	public final String getName() {
		return this.name;
	}

	public final long parse(final String moneyStr) {
		return this.parser.parse(moneyStr, this);
	}

	// <><><><><><><><><><><><><>< Package Methods ><><><><><><><><><><><><><>

	final CurrencySymbol getSymbol() {
		return this.symbol;
	}

} // End Currency
