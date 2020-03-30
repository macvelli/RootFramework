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

import java.math.BigDecimal;

import root.lang.Extractable;
import root.lang.StringExtractor;
import root.util.Root;
import root.validation.NullParameterException;

/**
 * The Money class is built to work with exact monetary values. The amount is stored internally as a <code>long</code> to preserve data integrity
 * while performing financial calculations. The minimum value of a Money object is -92,233,720,368,547,758.08 while the maximum value is
 * 92,233,720,368,547,758.07 (inclusive). Safe to replace up to DECIMAL(17,2) with BIGINT in database applications.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Money implements Comparable<Money>, Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private BigDecimal bigDecimal;

	long amount;
	final Currency currency;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Creates an instance initialized with the supplied <code>long</code> amount using <code>USD</code> as default {@link Currency}.
	 *
	 * @param amount
	 *            The monetary amount kept in precise <code>long</code> format
	 */
	public Money(final long amount) {
		this.amount = amount;
		this.currency = Currency.USD;
	}

	/**
	 * Creates an instance initialized with the supplied <code>long</code> amount using the supplied {@link Currency}.
	 *
	 * @param amount
	 *            The monetary amount kept in precise <code>long</code> format
	 * @param currency
	 *            The currency of the monetary amount
	 */
	public Money(final long amount, final Currency currency) {
		if (currency == null) {
			throw new NullParameterException(Money.class.getName(), Currency.class, "currency");
		}

		this.amount = amount;
		this.currency = currency;
	}

	/**
	 * Creates an instance initialized with the supplied {@link String} amount using <code>USD</code> as default {@link Currency}.
	 *
	 * @param moneyStr
	 *            The monetary amount in <code>USD</code> {@link Currency} format
	 */
	public Money(final String moneyStr) {
		this.currency = Currency.USD;
		this.amount = this.currency.parse(moneyStr);
	}

	/**
	 * Creates an instance initialized with the supplied {@link String} amount using <code>USD</code> as default {@link Currency}.
	 *
	 * @param moneyStr
	 *            The monetary amount in the supplied {@link Currency} format
	 * @param currency
	 *            The currency of the monetary amount
	 */
	public Money(final String moneyStr, final Currency currency) {
		if (currency == null) {
			throw new NullParameterException(Money.class.getName(), Currency.class, "currency");
		}

		this.currency = currency;
		this.amount = this.currency.parse(moneyStr);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void add(final Money m) {
		this.amount += m.amount;
		this.bigDecimal = null;
	}

	@Override
	public final int compareTo(final Money m) {
		return this.amount < m.amount ? -1 : this.amount > m.amount ? 1 : 0;
	}

	public final Money divide(final int value) {
		return new Money(this.amount / value, this.currency);
	}

	public final Percent divide(final Money divisor) {
		return new Percent(this, divisor);
	}

	@Override
	public final boolean equals(final Object obj) {
		return Root.equalToClass(obj, Money.class) && ((Money) obj).amount == this.amount;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		this.currency.format(this.amount, extractor);
	}

	public final long getAmount() {
		return this.amount;
	}

	public final BigDecimal getBigDecimal() {
		if (this.bigDecimal == null) {
			this.bigDecimal = new BigDecimal(this.amount);
		}

		return this.bigDecimal;
	}

	public final Currency getCurrency() {
		return this.currency;
	}

	@Override
	public final int hashCode() {
		return (int) (this.amount ^ this.amount >>> 32);
	}

	public final Money multiply(final int value) {
		return new Money(this.amount * value, this.currency);
	}

	public final Money multiply(final Percent p) {
		return p.multiply(this);
	}

	public final void subtract(final Money m) {
		this.amount -= m.amount;
		this.bigDecimal = null;
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(30);
		this.currency.format(this.amount, extractor);
		return extractor.toString();
	}

} // End Money
