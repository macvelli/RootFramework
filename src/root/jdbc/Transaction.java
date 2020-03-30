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
import java.sql.SQLException;

import javax.sql.DataSource;

import root.adt.MapHashed;
import root.annotation.Todo;
import root.log.Log;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Todo({ "Should there be something that can be called at the very end of a process to see whether or not an uncommitted transaction still exists, and if so throw an exception?",
		"How about nested transactions? Savepoints?" })
public final class Transaction {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Log log = new Log(Transaction.class);

	private static final MapHashed<Thread, TransactionLocalScope> txnMap = new MapHashed<>();

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Begins a transaction on the current thread if one is not already in progress with the default transaction isolation level.
	 */
	public static final void begin() {
		Transaction.begin(TransactionIsolationLevel.DEFAULT);
	}

	/**
	 * Begins a transaction on the current thread if one is not already in progress. The new transaction is created with the specified isolation
	 * level.
	 *
	 * @param isolationLevel
	 *            The isolation level to use for the transaction
	 */
	public static final void begin(final TransactionIsolationLevel isoLevel) {
		if (txnMap.get(Thread.currentThread()) == null) {
			txnMap.put(Thread.currentThread(), new TransactionLocalScope(isoLevel));
		}
	}

	/**
	 * Commits the {@link Connection} obtained from the {@link DataSource} used during the transaction. If the {@link Connection} throws an
	 * {@link SQLException} while attempting to commit, it is repackaged into a {@link DatabaseException} and immediately rethrown.
	 * <p>
	 * A {@link DatabaseException} is thrown if <code>Transaction.begin()</code> has not been called before this method.
	 */
	public static final void commit() {
		final TransactionLocalScope txnScope = txnMap.get(Thread.currentThread());

		if (txnScope == null) {
			log.error("Attempting to commit a transaction that is inactive");
			throw new DatabaseException("Attempting to commit a transaction that is inactive");
		}

		try {
			log.debug("Committing transaction");
			txnScope.commit();
		} catch (final SQLException e) {
			log.error("Exception occurred while committing transaction", e);
			throw new DatabaseException("Exception occurred while committing transaction", e);
		} finally {
			txnMap.remove(Thread.currentThread());
		}
	}

	/**
	 * Returns {@code true} if the current {@link Thread} has an active transaction, {@code false} otherwise.
	 *
	 * @return {@code true} if the current {@link Thread} has an active transaction
	 */
	public static final boolean isActive() {
		return txnMap.containsKey(Thread.currentThread());
	}

	/**
	 * Rolls back the currently in-scoped transaction.
	 *
	 * @throws DatabaseException
	 *             if there is no active transaction
	 */
	public static final void rollback() {
		final TransactionLocalScope txnScope = txnMap.get(Thread.currentThread());

		if (txnScope == null) {
			log.error("Attempting to rollback a transaction that is inactive");
			throw new DatabaseException("Attempting to rollback a transaction that is inactive");
		}

		try {
			log.debug("Rolling back transaction");
			txnScope.rollback();
		} catch (final SQLException e) {
			// No-op...the connection is hosed
		} finally {
			txnMap.remove(Thread.currentThread());
		}
	}

	// <><><><><><><><><><><><><>< Package Methods ><><><><><><><><><><><><><>

	/**
	 * Returns the {@link TransactionLocalScope} associated with the current thread, or {@code null} if a transaction is not in progress.
	 */
	static final TransactionLocalScope getLocalScope() {
		return txnMap.get(Thread.currentThread());
	}

} // End Transaction
