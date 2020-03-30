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
package root.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Sync {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final Condition cond;
	private final ReentrantLock lock;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Sync() {
		this.lock = new ReentrantLock();
		this.cond = this.lock.newCondition();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void await() throws InterruptedException {
		this.cond.await();
	}

	public final long awaitNanos(final long timeout) throws InterruptedException {
		return this.cond.awaitNanos(timeout);
	}

	/**
	 * Acquires the lock for the current thread.
	 */
	public final void lock() {
		this.lock.lock();
	}

	/**
	 * Wakes up one thread waiting on the underlying {@link Condition}.
	 */
	public final void signal() {
		this.cond.signal();
	}

	/**
	 * Wakes up all threads waiting on the underlying {@link Condition}.
	 */
	public final void signalAll() {
		this.cond.signalAll();
	}

	/**
	 * Releases the lock held by the current thread.
	 */
	public final void unlock() {
		this.lock.unlock();
	}

} // End Sync
