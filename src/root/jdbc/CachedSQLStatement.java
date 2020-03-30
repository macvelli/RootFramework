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

import root.lang.Extractable;
import root.lang.FastString;
import root.lang.StringExtractor;
import root.validation.NullParameterException;

/**
 * This is just an idea I had which would help looking up things in the cache. This might be too lower-level as caches typically deal with objects
 * instead of SQL statements.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class CachedSQLStatement implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	public final FastString sql;
	private final int hashCode;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public CachedSQLStatement(final String sql) {
		if (sql == null) {
			throw new NullParameterException(CachedSQLStatement.class.getName(), String.class, "sql");
		}

		this.sql = new FastString(sql);
		this.hashCode = sql.hashCode();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final boolean equals(final Object obj) {
		return this == obj;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		this.sql.extract(extractor);
	}

	@Override
	public final int hashCode() {
		return this.hashCode;
	}

	@Override
	public final String toString() {
		return this.sql.string;
	}

} // End CachedSQLStatement
