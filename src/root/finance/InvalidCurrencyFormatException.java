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

import root.lang.ParamString;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class InvalidCurrencyFormatException extends RuntimeException {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	enum Type {

		LENGTH,
		CHARACTER;

	} // End Type

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final long serialVersionUID = -5739275066956965390L;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 *
	 * @param type
	 * @param moneyStr
	 * @param currency
	 */
	public InvalidCurrencyFormatException(final Type type, final String moneyStr, final Currency currency) {
		super(ParamString.formatMsg("Type {P}: Invalid input {P} for currency {P}", type, moneyStr, currency));
	}

} // End InvalidCurrencyFormatException
