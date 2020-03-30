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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import root.annotation.Delegate;
import root.annotation.Todo;
import root.cache.CacheLRU;
import root.clock.Timer;
import root.lang.Extractable;
import root.lang.StringExtractor;
import root.log.Log;
import root.util.Jdbc;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Delegate
@Todo("I should be able to move the hasError functionality up into SQLBroker, just check if Connection != null and Statement != null before setting")
public final class PooledConnection implements java.sql.Connection, Extractable {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Log log = new Log(PooledConnection.class);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	/** Keeps track of how long the pooled connection has been idle and if it has expired */
	final Timer t;

	/** Set to {@code true} when the underlying connection throws an {@link SQLException} */
	boolean hasError;

	/** Keeps track of the original transaction isolation level on the underlying connection */
	private TransactionIsolationLevel previousIsolationLevel;

	/** Set to {@code true} during a transaction when the underlying connection autoCommit value is {@code true} */
	private boolean resetAutoCommit;

	/**
	 * Set to <code>false</code> whenever a transaction is currently in progress involving this connection
	 */
	private boolean noTrans;

	/** {@link PreparedStatement} cache if enabled on the {@link PooledDataSource} */
	final CacheLRU<CachedSQLStatement, CachedPreparedStatement> stmtCache;

	private final RootDataSource dataSource;

	private final Connection connection;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	PooledConnection(final RootDataSource dataSource, final Connection connection) {
		this.dataSource = dataSource;
		this.connection = connection;
		this.noTrans = true;
		this.t = new Timer(dataSource.getMaxIdleTime());

		if (dataSource.getStmtCacheSize() == 0) {
			this.stmtCache = null;
		} else {
			this.stmtCache = new CacheLRU<>(dataSource.getStmtCacheSize());
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void abort(final Executor executor) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	@Todo("This should probably be done on a periodic basis with a PooledConnection since the warnings could pile up (double-check this via Google)")
	public final void clearWarnings() throws SQLException {
		try {
			this.connection.clearWarnings();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void close() {
		if (this.noTrans) {
			this.dataSource.pool.abandon(this);
			this.t.reset();
		}
	}

	@Override
	public final void commit() throws SQLException {
		try {
			this.connection.commit();

			if (this.resetAutoCommit) {
				this.connection.setAutoCommit(true);
			}

			if (this.previousIsolationLevel != null) {
				this.connection.setTransactionIsolation(this.previousIsolationLevel.getValue());
			}
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		} finally {
			this.previousIsolationLevel = null;
			this.noTrans = true;
			this.close();
		}
	}

	@Override
	public final Array createArrayOf(final String typeName, final Object[] elements) throws SQLException {
		try {
			return this.connection.createArrayOf(typeName, elements);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final Blob createBlob() throws SQLException {
		try {
			return this.connection.createBlob();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final Clob createClob() throws SQLException {
		try {
			return this.connection.createClob();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final NClob createNClob() throws SQLException {
		try {
			return this.connection.createNClob();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final SQLXML createSQLXML() throws SQLException {
		try {
			return this.connection.createSQLXML();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final Statement createStatement() throws SQLException {
		try {
			return this.connection.createStatement();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
		try {
			return this.connection.createStatement(resultSetType, resultSetConcurrency);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		try {
			return this.connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final Struct createStruct(final String typeName, final Object[] attributes) throws SQLException {
		try {
			return this.connection.createStruct(typeName, attributes);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append("PooledConnection [");
		extractor.append("hasError=").append(this.hasError ? "yes" : "no");
		extractor.append(", stmt cache size=").append(this.dataSource.getStmtCacheSize());
		extractor.append(", trans=").append(this.noTrans ? "no" : "yes");
		extractor.append(", resetAutoCommit=").append(this.resetAutoCommit ? "yes" : "no");
		extractor.append(", isolation level=");

		try {
			extractor.append(this.getTransactionIsolationLevel());
		} catch (final Exception e) {
			extractor.append("UNKNOWN");
		}

		extractor.append(']');
	}

	@Override
	public final boolean getAutoCommit() throws SQLException {
		try {
			return this.connection.getAutoCommit();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final String getCatalog() throws SQLException {
		try {
			return this.connection.getCatalog();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final Properties getClientInfo() throws SQLException {
		try {
			return this.connection.getClientInfo();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final String getClientInfo(final String name) throws SQLException {
		try {
			return this.connection.getClientInfo(name);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final int getHoldability() throws SQLException {
		try {
			return this.connection.getHoldability();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final DatabaseMetaData getMetaData() throws SQLException {
		try {
			return this.connection.getMetaData();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final int getNetworkTimeout() throws SQLException {
		try {
			return this.connection.getNetworkTimeout();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final String getSchema() throws SQLException {
		try {
			return this.connection.getSchema();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final int getTransactionIsolation() throws SQLException {
		try {
			return this.connection.getTransactionIsolation();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	public final TransactionIsolationLevel getTransactionIsolationLevel() throws SQLException {
		try {
			return TransactionIsolationLevel.valueOf(this.connection.getTransactionIsolation());
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final Map<String, Class<?>> getTypeMap() throws SQLException {
		try {
			return this.connection.getTypeMap();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final SQLWarning getWarnings() throws SQLException {
		try {
			return this.connection.getWarnings();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final boolean isClosed() throws SQLException {
		return false;
	}

	@Override
	public final boolean isReadOnly() throws SQLException {
		try {
			return this.connection.isReadOnly();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final boolean isValid(final int timeout) {
		return !this.hasError && !this.t.hasExpired();
	}

	@Override
	public final boolean isWrapperFor(final Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public final String nativeSQL(final String sql) throws SQLException {
		try {
			return this.connection.nativeSQL(sql);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final CallableStatement prepareCall(final String sql) throws SQLException {
		try {
			return this.connection.prepareCall(sql);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		try {
			return this.connection.prepareCall(sql, resultSetType, resultSetConcurrency);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		try {
			return this.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	public final PreparedStatement prepareStatement(final CachedSQLStatement cachedSql) throws SQLException {
		try {
			CachedPreparedStatement stmt = this.stmtCache.get(cachedSql);

			if (stmt == null) {
				log.debug("Caching PreparedStatement {P}", cachedSql.sql);

				stmt = new CachedPreparedStatement(this, this.connection.prepareStatement(cachedSql.sql.string), cachedSql);

				final CachedPreparedStatement old = this.stmtCache.put(cachedSql, stmt);

				if (old != null) {
					Jdbc.close(old.stmt);
				}
			}

			return stmt;
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final PreparedStatement prepareStatement(final String sql) throws SQLException {
		try {
			return this.connection.prepareStatement(sql);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys) throws SQLException {
		try {
			return this.connection.prepareStatement(sql, autoGeneratedKeys);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		try {
			return this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws SQLException {
		try {
			return this.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
		try {
			return this.connection.prepareStatement(sql, columnIndexes);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
		try {
			return this.connection.prepareStatement(sql, columnNames);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void releaseSavepoint(final Savepoint savepoint) throws SQLException {
		try {
			this.connection.releaseSavepoint(savepoint);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void rollback() throws SQLException {
		try {
			this.connection.rollback();

			if (this.resetAutoCommit) {
				this.connection.setAutoCommit(true);
			}

			if (this.previousIsolationLevel != null) {
				this.connection.setTransactionIsolation(this.previousIsolationLevel.getValue());
			}
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		} finally {
			this.previousIsolationLevel = null;
			this.noTrans = true;
			this.close();
		}
	}

	@Override
	public final void rollback(final Savepoint savepoint) throws SQLException {
		try {
			this.connection.rollback(savepoint);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void setAutoCommit(final boolean autoCommit) throws SQLException {
		try {
			this.connection.setAutoCommit(autoCommit);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void setCatalog(final String catalog) throws SQLException {
		try {
			this.connection.setCatalog(catalog);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void setClientInfo(final Properties properties) throws SQLClientInfoException {
		try {
			this.connection.setClientInfo(properties);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void setClientInfo(final String name, final String value) throws SQLClientInfoException {
		try {
			this.connection.setClientInfo(name, value);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void setHoldability(final int holdability) throws SQLException {
		try {
			this.connection.setHoldability(holdability);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
		try {
			this.connection.setNetworkTimeout(executor, milliseconds);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	@Todo("This method cannot be called during a transaction")
	public final void setReadOnly(final boolean readOnly) throws SQLException {
		try {
			this.connection.setReadOnly(readOnly);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final Savepoint setSavepoint() throws SQLException {
		try {
			return this.connection.setSavepoint();
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final Savepoint setSavepoint(final String name) throws SQLException {
		try {
			return this.setSavepoint(name);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void setSchema(final String schema) throws SQLException {
		try {
			this.connection.setSchema(schema);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void setTransactionIsolation(final int level) throws SQLException {
		try {
			this.connection.setTransactionIsolation(level);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	public final void setTransactionIsolationLevel(final TransactionIsolationLevel level) throws SQLException {
		try {
			this.connection.setTransactionIsolation(level.getValue());
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		try {
			this.connection.setTypeMap(map);
		} catch (final SQLException e) {
			this.hasError = true;
			throw e;
		}
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(128);
		this.extract(extractor);
		return extractor.toString();
	}

	@Override
	public final <T> T unwrap(final Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	// <><><><><><><><><><><><><>< Package Methods ><><><><><><><><><><><><><>

	final void destroy() {
		if (this.stmtCache != null) {
			for (CachedPreparedStatement stmt = this.stmtCache.removeNext(); stmt != null; stmt = this.stmtCache.removeNext()) {
				Jdbc.close(stmt.stmt);
			}
		}

		Jdbc.close(this.connection);
	}

	final void initTransaction(final TransactionIsolationLevel isoLevel) throws SQLException {
		if (isoLevel != TransactionIsolationLevel.DEFAULT && isoLevel != this.getTransactionIsolationLevel()) {
			this.previousIsolationLevel = this.getTransactionIsolationLevel();
			this.setTransactionIsolationLevel(isoLevel);
		}

		this.resetAutoCommit = this.getAutoCommit();
		if (this.resetAutoCommit) {
			this.setAutoCommit(false);
		}

		this.noTrans = false;
	}

} // End PooledConnection
