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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import root.lang.Extractable;
import root.log.Log;
import root.pool.PoolConcurrent;
import root.pool.PoolObjectFactory;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public abstract class RootDataSource implements javax.sql.DataSource, Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	/** The size of the {@link PreparedStatement} cache to create on each {@link PooledConnection} */
	protected int stmtCacheSize;

	/** The maximum amount of time a {@link PooledConnection} may be idle before it is recycled */
	protected long maxIdleTime;

	/** The {@link PoolConcurrent} of {@link PooledConnection} managed by this {@link JndiDataSource} */
	protected final PoolConcurrent<PooledConnection> pool;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	protected RootDataSource(final int capacity) {
		this.pool = new PoolConcurrent<>(capacity, this.getPooledConnectionFactory());
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Gracefully closes all {@link PooledConnection} objects contained within the {@link PooledDataSource}.
	 */
	public final void close() {
		try {
			this.pool.close();
		} catch (final InterruptedException e) {
		}
	}

	@Override
	public final PooledConnection getConnection() {
		final TransactionLocalScope txnScope = Transaction.getLocalScope();

		try {
			if (txnScope == null || !txnScope.manages(this)) {
				return this.pool.acquire();
			}

			if (txnScope.isBeginning()) {
				this.getLog().debug("Starting transaction on {P} with isolation level {P}", this, txnScope.isoLevel);
				return txnScope.initialize(this, this.pool.acquire());
			}

			return txnScope.getConnection();
		} catch (final Exception e) {
			this.getLog().error("Cannot acquire a connection from the pool", e);
			throw new DatabaseException("Cannot acquire a connection from the pool", e);
		}
	}

	@Override
	public final Connection getConnection(final String username, final String password) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the maximum number of milliseconds a connection may be idle in the pool before it is recycled.
	 *
	 * @return the maximum number of milliseconds a connection may be idle
	 */
	public final long getMaxIdleTime() {
		return this.maxIdleTime;
	}

	/**
	 * Returns the maximum amount of time in milliseconds the pool will wait for a connection to become available before erroring out.
	 *
	 * @return the maximum amount of time in milliseconds the pool will wait for a connection to become available
	 */
	public final long getMaxWait() {
		return this.pool.getMaxWait();
	}

	/**
	 * Returns the size of the {@link PreparedStatement} cache for each pooled {@link Connection}.
	 *
	 * @return the size of the {@link PreparedStatement} cache
	 */
	public final int getStmtCacheSize() {
		// TODO: I think this should go away and a PooledConnection can just support however many CachedSQLStatement objects come its way
		return this.stmtCacheSize;
	}

	@Override
	public final boolean isWrapperFor(final Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the maximum number of milliseconds a connection may be idle in the pool before it is recycled. A value less than or equal to zero is
	 * ignored.
	 *
	 * @param maxIdleTime
	 *            the maximum number of milliseconds a connection may be idle
	 */
	public final void setMaxIdleTime(final long maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	/**
	 * Sets the maximum amount of time in milliseconds the pool will wait for a connection to become available before erroring out. A value less than
	 * or equal to zero is ignored and the pool will wait indefinitely.
	 *
	 * @param maxWait
	 *            the maximum amount of time the pool will wait for a connection
	 */
	public final void setMaxWait(final int maxWait) {
		this.pool.setMaxWait(maxWait);
	}

	/**
	 * Sets the size of the {@link PreparedStatement} cache for each {@link PooledConnection}. The default is <code>0</code> where no statement cache
	 * is created.
	 *
	 * @param stmtCacheSize
	 *            the size of the {@link PreparedStatement} cache
	 */
	public final void setStmtCacheSize(final int stmtCacheSize) {
		// TODO: I think this should go away and a PooledConnection can just support however many CachedSQLStatement objects come its way
		this.stmtCacheSize = stmtCacheSize;
	}

	@Override
	public final <T> T unwrap(final Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	// <><><><><><><><><><><><><> Protected Methods <><><><><><><><><><><><><>

	protected abstract Log getLog();

	protected abstract PoolObjectFactory<PooledConnection> getPooledConnectionFactory();

} // End RootDataSource
