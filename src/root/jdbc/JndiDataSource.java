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
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import root.annotation.Delegate;
import root.annotation.Inheritance;
import root.lang.StringExtractor;
import root.log.Log;
import root.pool.PoolObjectFactory;

/**
 * This class wraps a JNDI {@link DataSource}.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Delegate
@Inheritance
public final class JndiDataSource extends RootDataSource {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private class JndiPooledConnectionFactory implements PoolObjectFactory<PooledConnection> {

		@Override
		public final PooledConnection create() {
			try {
				return new PooledConnection(JndiDataSource.this, JndiDataSource.this.dataSource.getConnection());
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

	} // End JndiPooledConnectionFactory

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Log log = new Log(JndiDataSource.class);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	/** The JNDI {@link DataSource} to delegate operations to */
	private final DataSource dataSource;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public JndiDataSource(final String jndiName, final int capacity) {
		super(capacity);

		log.debug("Creating JndiDataSource from JNDI name [{P}]", jndiName);

		try {
			final InitialContext ctx = new InitialContext();
			this.dataSource = (DataSource) ctx.lookup("java:/comp/env/" + jndiName);
		} catch (final Exception e) {
			log.error("JNDI lookup failed for DataSource named {P}", e, jndiName);
			throw new DatabaseException("JNDI lookup failed for DataSource named {P}", e, jndiName);
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append("JndiDataSource [");
		extractor.append("dataSource=").append(this.dataSource.getClass().getName());
		extractor.append(", maxIdleTime=").append(this.maxIdleTime);
		extractor.append(", maxWait=").append(this.pool.getMaxWait());
		extractor.append(", poolCapacity=").append(this.pool.getCapacity());
		extractor.append(']');
	}

	@Override
	public final int getLoginTimeout() throws SQLException {
		return this.dataSource.getLoginTimeout();
	}

	@Override
	public final PrintWriter getLogWriter() throws SQLException {
		return this.dataSource.getLogWriter();
	}

	@Override
	public final Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return this.dataSource.getParentLogger();
	}

	@Override
	public final void setLoginTimeout(final int seconds) throws SQLException {
		this.dataSource.setLoginTimeout(seconds);
	}

	@Override
	public final void setLogWriter(final PrintWriter out) throws SQLException {
		this.setLogWriter(out);
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
		return new JndiPooledConnectionFactory();
	}

} // End JndiDataSource
