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

import root.annotation.Todo;
import root.lang.FastInteger;
import root.lang.StringExtractor;
import root.log.Log;
import root.pool.PoolConcurrent;
import root.pool.PoolObjectFactory;
import root.validation.InvalidParameterException;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ThreadPool {

	// <><><><><><><><><><><><><>< Private Classes ><><><><><><><><><><><><><>

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private final class PooledThread extends Thread {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private boolean running;
		private Runnable activity;
		private final Sync sync;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private PooledThread(final int threadCount) {
			super(new StringExtractor(ThreadPool.this.threadPoolName).append('-').append(threadCount < 10, '0').append(threadCount).toString());
			this.sync = new Sync();
			this.running = true;
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void run() {
			this.sync.lock();

			while (this.running) {
				try {
					while (this.activity == null) {
						this.sync.await();
					}

					this.activity.run();
				} catch (final InterruptedException e) {
				} catch (final Throwable t) {
					pooledThreadLog.error("[{P}]: Exception occurred while running activity", t, this.getName());
				} finally {
					this.activity = null;
					ThreadPool.this.pool.abandon(this);
				}
			}

			this.sync.unlock();
		}

	} // End PooledThread

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	private final class PooledThreadFactory implements PoolObjectFactory<PooledThread> {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private int threadCount;
		private final int priority;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private PooledThreadFactory(final int priority) {
			this.priority = priority;
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final PooledThread create() {
			final PooledThread thread = new PooledThread(this.threadCount++);
			thread.setPriority(this.priority);
			thread.start();

			return thread;
		}

		@Override
		public final void destroy(final PooledThread thread) {
			thread.activity = null;
			thread.running = false;
		}

		@Override
		public final String getObjectClassName() {
			return PooledThread.class.getName();
		}

		@Override
		public final boolean validate(final PooledThread thread) {
			return true;
		}

	} // End PooledThreadFactory

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Log pooledThreadLog = new Log(PooledThread.class);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final String threadPoolName;
	private final PoolConcurrent<PooledThread> pool;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	@Todo("I don't like this but can't come up with anything else at the moment")
	public ThreadPool(final int capacity, final int priority, final String threadPoolName) {
		if (priority > Thread.MAX_PRIORITY || priority < Thread.MIN_PRIORITY) {
			throw new InvalidParameterException(this.getClass().getName(), int.class, "priority", "Invalid thread priority: {P}",
					FastInteger.valueOf(priority));
		}

		this.threadPoolName = threadPoolName;
		this.pool = new PoolConcurrent<>(capacity, new PooledThreadFactory(priority));
	}

	public ThreadPool(final int capacity, final String threadPoolName) {
		this(capacity, Thread.NORM_PRIORITY, threadPoolName);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Closes the {@link ThreadPool}.
	 */
	public final void close() {
		try {
			this.pool.setMaxWait(10000);
			this.pool.close();
		} catch (final InterruptedException e) {
			// Nothing to do
		}
	}

	/**
	 * Execute each {@link Runnable} in the {@code runnableIterable} against the {@link ThreadPool}.
	 *
	 * @param runnableIterable
	 *            the {@link Iterable} of {@link Runnable} to execute
	 */
	public final void execute(final Iterable<? extends Runnable> runnableIterable) {
		for (final Runnable r : runnableIterable) {
			this.execute(r);
		}
	}

	/**
	 * Execute a single {@link Runnable} against the {@link ThreadPool}.
	 *
	 * @param r
	 *            the {@link Runnable} to execute
	 */
	public final void execute(final Runnable r) {
		final PooledThread t;

		try {
			t = this.pool.acquire();
		} catch (final Exception e) {
			throw new IllegalStateException(e);
		}

		t.sync.lock();
		try {
			t.activity = r;
			t.sync.signal();
		} finally {
			t.sync.unlock();
		}
	}

	/**
	 * Execute each {@link Runnable} in the {@code runnableArray} against the {@link ThreadPool}.
	 *
	 * @param runnableArray
	 *            the array of {@link Runnable} to execute
	 */
	public final void execute(final Runnable[] runnableArray) {
		for (final Runnable r : runnableArray) {
			this.execute(r);
		}
	}

	/**
	 * Returns the capacity of this {@link ThreadPool}.
	 *
	 * @return the capacity of this {@link ThreadPool}
	 */
	public final int getCapacity() {
		return this.pool.getCapacity();
	}

	/**
	 * Returns the maximum amount of time in milliseconds the pool will wait for a task to free up before erroring out.
	 *
	 * @return the maximum amount of time the pool will wait for a task.
	 */
	public final long getMaxWait() {
		return this.pool.getMaxWait();
	}

	/**
	 * Sets the maximum amount of time in milliseconds the pool will wait for a task to free up before erroring out. A value less than or equal to
	 * zero is ignored and the pool will wait indefinitely.
	 *
	 * @param maxWait
	 *            The maximum amount of time the pool will wait for a task.
	 */
	public final void setMaxWait(final int maxWait) {
		this.pool.setMaxWait(maxWait);
	}

} // End ThreadPool
