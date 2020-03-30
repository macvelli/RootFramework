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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import root.annotation.Inheritance;
import root.lang.StringExtractor;
import root.log.Log;
import root.pool.PoolObjectFactory;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Inheritance
public final class PooledDataSource extends RootDataSource {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private class PooledConnectionFactory implements PoolObjectFactory<PooledConnection> {

		@Override
		public final PooledConnection create() {
			try {
				final Connection conn = PooledDataSource.this.driver.connect(PooledDataSource.this.url, PooledDataSource.this.dbProperties);
				return new PooledConnection(PooledDataSource.this, conn);
			} catch (final SQLException e) {
				throw new DatabaseException(e);
			}
		}

		@Override
		public final void destroy(final PooledConnection conn) {
			conn.destroy();
		}

		@Override
		public final String getObjectClassName() {
			return PooledConnection.class.getName();
		}

		@Override
		public final boolean validate(final PooledConnection conn) {
			return conn.isValid(0);
		}

	} // End PooledConnectionFactory

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Log log = new Log(PooledDataSource.class);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	/** The URL connection string used to create a JDBC connection to the database */
	private final String url;

	/** The {@link Driver} used to connect to the database with the specified URL */
	private final Driver driver;

	/** The set of {@link Properties} used to connect to the database with the {@link Driver} */
	private final Properties dbProperties;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Creates a {@link PooledDataSource} using the specified JDBC driver, URL connection string, and pool capacity.
	 *
	 * @param driverName
	 *            The fully qualified JDBC driver class name
	 * @param url
	 *            The URL connection string
	 * @param capacity
	 *            The pool capacity
	 */
	public PooledDataSource(final String driverName, final String url, final int capacity) {
		super(capacity);

		log.debug("Creating PooledDataSource from JDBC driver [{P}] [URL={P}]", driverName, url);

		try {
			Class.forName(driverName);
			this.driver = DriverManager.getDriver(url);
		} catch (final ClassNotFoundException e) {
			log.error("Cannot load JDBC driver [{P}]", e, driverName);
			throw new DatabaseException("Cannot load JDBC driver [{P}]", e, driverName);
		} catch (final SQLException e) {
			log.error("Cannot get JDBC driver [{P}] from DriverManager with URL [{P}]", e, driverName, url);
			throw new DatabaseException("Cannot get JDBC driver [{P}] from DriverManager with URL [{P}]", e, driverName, url);
		}

		this.url = url;
		this.dbProperties = new Properties();
	}

	/**
	 * Creates a {@link PooledDataSource} using the specified JDBC driver, URL connection string, pool capacity, and database properties. The database
	 * properties most commonly used are {@code user} and {@code password}, although other vendor-specific database properties may be passed along as
	 * well.
	 *
	 * @param driverName
	 *            The fully qualified JDBC driver class name
	 * @param url
	 *            The URL connection string
	 * @param capacity
	 *            The pool capacity
	 * @param dbProperties
	 *            The database properties to use when creating a {@link Connection}
	 */
	public PooledDataSource(final String driverName, final String url, final int capacity, final Properties dbProperties) {
		this(driverName, url, capacity);

		this.dbProperties.putAll(dbProperties);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append("PooledDataSource [");
		extractor.append("driver=").append(this.driver.getClass().getName());
		extractor.append(", url=").append(this.url);
		extractor.append(", stmt cache size=").append(this.stmtCacheSize);
		extractor.append(", maxIdleTime=").append(this.maxIdleTime);
		extractor.append(", maxWait=").append(this.pool.getMaxWait());
		extractor.append(", poolCapacity=").append(this.pool.getCapacity());
		extractor.append(']');
	}

	@Override
	public final int getLoginTimeout() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public final PrintWriter getLogWriter() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return log.getParent();
	}

	@Override
	public final void setLoginTimeout(final int seconds) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void setLogWriter(final PrintWriter out) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(512);
		this.extract(extractor);
		return extractor.toString();
	}

	// <><><><><><><><><><><><><> Protected Methods <><><><><><><><><><><><><>

	@Override
	protected final Log getLog() {
		return log;
	}

	@Override
	protected final PoolObjectFactory<PooledConnection> getPooledConnectionFactory() {
		return new PooledConnectionFactory();
	}

} // End PooledDataSource
