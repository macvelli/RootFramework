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

import java.sql.SQLException;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
final class TransactionLocalScope {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private PooledConnection connection;
	private RootDataSource dataSource;

	final TransactionIsolationLevel isoLevel;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	TransactionLocalScope(final TransactionIsolationLevel isoLevel) {
		this.isoLevel = isoLevel;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	final void commit() throws SQLException {
		if (this.connection != null) {
			this.connection.commit();
		}
	}

	final PooledConnection getConnection() {
		return this.connection;
	}

	final PooledConnection initialize(final RootDataSource dataSource, final PooledConnection conn) throws SQLException {
		this.dataSource = dataSource;
		this.connection = conn;
		conn.initTransaction(this.isoLevel);

		return conn;
	}

	final boolean isBeginning() {
		return this.dataSource == null;
	}

	final boolean manages(final RootDataSource dataSource) {
		return this.dataSource == dataSource;
	}

	final void rollback() throws SQLException {
		if (this.connection != null) {
			this.connection.rollback();
		}
	}

} // End TransactionLocalScope
