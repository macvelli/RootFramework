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

import java.sql.Types;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public enum SqlType {

	ARRAY(Types.ARRAY),
	BIGINT(Types.BIGINT),
	BINARY(Types.BINARY),
	BIT(Types.BIT),
	BLOB(Types.BLOB),
	BOOLEAN(Types.BOOLEAN),
	CHAR(Types.CHAR),
	CLOB(Types.CLOB),
	DATALINK(Types.DATALINK),
	DATE(Types.DATE),
	DECIMAL(Types.DECIMAL),
	DISTINCT(Types.DISTINCT),
	DOUBLE(Types.DOUBLE),
	FLOAT(Types.FLOAT),
	INTEGER(Types.INTEGER),
	JAVA_OBJECT(Types.JAVA_OBJECT),
	LONGNVARCHAR(Types.LONGNVARCHAR),
	LONGVARBINARY(Types.LONGVARBINARY),
	LONGVARCHAR(Types.LONGVARCHAR),
	NCHAR(Types.NCHAR),
	NCLOB(Types.NCLOB),
	NVARCHAR(Types.NVARCHAR),
	NULL(Types.NULL),
	NUMERIC(Types.NUMERIC),
	OTHER(Types.OTHER),
	REAL(Types.REAL),
	REF(Types.REF),
	ROWID(Types.ROWID),
	SMALLINT(Types.SMALLINT),
	SQLXML(Types.SQLXML),
	STRUCT(Types.STRUCT),
	TIME(Types.TIME),
	TIMESTAMP(Types.TIMESTAMP),
	TINYINT(Types.TINYINT),
	VARBINARY(Types.VARBINARY),
	VARCHAR(Types.VARCHAR);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final int code;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private SqlType(final int code) {
		this.code = code;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final int getCode() {
		return this.code;
	}

} // End SqlType
