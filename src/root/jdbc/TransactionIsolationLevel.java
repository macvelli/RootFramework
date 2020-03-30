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
package root.jdbc;

import java.sql.Connection;

import root.lang.Extractable;
import root.lang.FastString;
import root.lang.StringExtractor;
import root.validation.InvalidParameterException;

/**
 * Encapsulates the various transaction isolation levels as Enum values instead of how they are defined on the {@link java.sql.Connection} interface
 * as primitive {@code int} constants.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public enum TransactionIsolationLevel implements Extractable {

	// <><><><><><><><><><><><><><>< Enum Values ><><><><><><><><><><><><><><>

	DEFAULT(-1),
	NONE(Connection.TRANSACTION_NONE),
	READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
	READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
	REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
	SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

	// <><><><><><><><><><><><><><><> Constants ><><><><><><><><><><><><><><><>

	private static final TransactionIsolationLevel[] enumValueArray = TransactionIsolationLevel.values();

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	public static final TransactionIsolationLevel valueOf(final int isoLevelInt) {
		for (final TransactionIsolationLevel isoLevelEnum : enumValueArray) {
			if (isoLevelEnum.isoLevel == isoLevelInt) {
				return isoLevelEnum;
			}
		}

		throw new InvalidParameterException("valueOf", int.class, "isoLevelInt", "Invalid transaction isolation level: {P}",
				Integer.valueOf(isoLevelInt));
	}

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final int isoLevel;
	private final FastString isoLevelName;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private TransactionIsolationLevel(final int isoLevel) {
		this.isoLevel = isoLevel;
		this.isoLevelName = new FastString(this.name());
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		this.isoLevelName.extract(extractor);
	}

	public final int getValue() {
		return this.isoLevel;
	}

} // End TransactionIsolationLevel
