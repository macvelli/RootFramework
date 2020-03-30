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
package root.pool;

import java.util.NoSuchElementException;

import root.annotation.Todo;
import root.lang.ParamString;
import root.log.Log;
import root.thread.Sync;

/**
 * A thread-safe version of the generic resource {@link Pool}.
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements managed by the pool
 */
@Todo("See about keeping track of the maximum number of threads that have waited on this pool and log the value like once a day")
public final class PoolConcurrent<T> extends Pool<T> {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Log log = new Log(PoolConcurrent.class);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	/** The maximum number of nanoseconds to wait for an object to return to the pool */
	private long maxWait;

	/** Manages synchronization efforts between all {@link Thread}s that use the pool */
	private final Sync sync;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Creates an empty pool with a fixed capacity. Object lifecycles are managed by the provided {@link PoolObjectFactory} implementation.
	 *
	 * @param capacity
	 *            The capacity of the pool
	 * @param factory
	 *            The {@link PoolObjectFactory} implementation to use
	 */
	public PoolConcurrent(final int capacity, final PoolObjectFactory<T> factory) {
		super(capacity, factory);
		this.maxWait = Long.MAX_VALUE;
		this.sync = new Sync();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Returns the abandoned object to the pool making it immediately available for acquisition.
	 * <p>
	 * One thread waiting to acquire an object from the pool will be notified about the availability of this object.
	 *
	 * @param t
	 *            the object to return to the pool
	 */
	@Override
	public final void abandon(final T t) {
		log.debug(returningObjectToPool, this.debugParams);

		this.sync.lock();
		try {
			if (this.closed) {
				this.remove(t);
			} else {
				this.queue.enqueue(t);
			}

			this.sync.signal();
		} finally {
			this.sync.unlock();
		}
	}

	/**
	 * Acquires an object from the pool according to the following algorithm:
	 * <ul>
	 * <li>If the size of the pool is at capacity and no objects are present then wait up to <code>maxWait</code> for an object to become available.
	 * <li>Any pooled objects are validated by the {@link PoolObjectFactory}. Invalid objects are destroyed and removed from the pool until either the
	 * pool is purged or a valid object is found and returned.
	 * <li>Otherwise if the pool is empty, a new object is created from the {@link PoolObjectFactory} and returned, which also increments the size of
	 * the pool.
	 * </ul>
	 *
	 * @return an instance of the pooled object
	 * @throws InterruptedException
	 */
	@Override
	public final T acquire() throws InterruptedException {
		long timeout = this.maxWait;

		log.debug(acquiringObjectFromPool, this.debugParams);
		this.sync.lock();
		try {
			if (!this.closed) {
				while (!this.closed && this.size == this.queue.getCapacity() && this.queue.isEmpty()) {
					if (timeout > 0) {
						timeout = this.sync.awaitNanos(timeout);
					} else {
						log.throwException(new NoSuchElementException(ParamString.formatMsg("Cannot acquire a {P} from the pool", this.debugParams)));
					}
				}

				if (!this.closed) {
					while (!this.queue.isEmpty() && !this.factory.validate(this.queue.peek())) {
						log.debug("Removing an invalid {P} from the pool", this.debugParams);
						this.factory.destroy(this.queue.dequeue());
						this.size--;
					}

					if (this.queue.isEmpty()) {
						log.debug("Creating a new {P} from the factory", this.debugParams);
						this.size++;
						return this.factory.create();
					}

					return this.queue.dequeue();
				}
			}
		} finally {
			this.sync.unlock();
		}

		throw new ClosedForBusinessException(this.debugParams);
	}

	/**
	 * Closes the pool and destroys every pooled object.
	 *
	 * @throws InterruptedException
	 */
	@Override
	public final void close() throws InterruptedException {
		this.sync.lock();
		this.closed = true;

		try {
			// Destroy all idle objects in the queue
			while (!this.queue.isEmpty()) {
				this.remove(this.queue.dequeue());
			}

			// Signal all waiting threads
			this.sync.signalAll();

			// Make sure all objects are destroyed before exiting the method
			while (this.size > 0) {
				this.sync.awaitNanos(this.maxWait);
			}
		} finally {
			this.sync.unlock();
		}
	}

	/**
	 * Returns the maximum wait time in milliseconds for an object to become available in the pool before throwing a {@link NoSuchElementException}.
	 *
	 * @return the maximum wait time for an object to become available
	 */
	public final long getMaxWait() {
		return this.maxWait / 1000000;
	}

	/**
	 * Sets the maximum wait time in milliseconds for an object to become available in the pool.
	 *
	 * @param maxWait
	 *            The maximum millisecond wait time value to set
	 */
	public final void setMaxWait(final int maxWait) {
		this.maxWait = maxWait * 1000000L;
	}

} // End PoolConcurrent
