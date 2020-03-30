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

import root.annotation.Todo;
import root.log.Log;

/**
 * A simple, non-thread-safe version of the generic resource {@link Pool}.
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements managed by the pool
 */
public final class PoolSimple<T> extends Pool<T> {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Log log = new Log(PoolSimple.class);

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Creates an empty pool with a fixed capacity. Object lifecycles are managed by the provided {@link PoolObjectFactory} implementation.
	 *
	 * @param capacity
	 *            The capacity of the pool
	 * @param factory
	 *            The {@link PoolObjectFactory} implementation to use
	 */
	public PoolSimple(final int capacity, final PoolObjectFactory<T> factory) {
		super(capacity, factory);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Returns the abandoned object to the pool making it immediately available for acquisition.
	 *
	 * @param t
	 *            the object to return to the pool
	 */
	@Override
	public final void abandon(final T t) {
		log.debug(returningObjectToPool, this.debugParams);

		if (this.closed) {
			this.remove(t);
		} else {
			this.queue.enqueue(t);
		}
	}

	/**
	 * Acquires an object from the pool according to the following algorithm:
	 * <ul>
	 * <li>If the size of the pool is at capacity and no objects are present then wait up to <code>maxWait</code> for an object to become available.
	 * <li>Any pooled objects are validated by the {@link PoolObjectFactory}. Invalid objects are destroyed and removed from the pool until either the
	 * pool is purged or a valid object is found and returned.
	 * <li>Otherwise if the pool is empty, a new object is created from the {@link PoolObjectFactory} and returned, which increments the size of the
	 * pool.
	 * </ul>
	 *
	 * @return An instance of a pooled object.
	 * @throws Exception
	 *             Occurs for various reasons.
	 */
	@Override
	public final T acquire() {
		// 1. If the pool is full but the queue is empty, throw an exception
		if (this.size == this.queue.getCapacity() && this.queue.isEmpty()) {
			log.throwException(new IllegalStateException("PoolSimple is full but queue is empty"));
		}

		log.debug(acquiringObjectFromPool, this.debugParams);
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

	/**
	 * Closes the pool and destroys every pooled object.
	 */
	@Override
	@Todo("What about active objects that are outside the pool?")
	public final void close() {
		while (!this.queue.isEmpty()) {
			this.remove(this.queue.dequeue());
		}
	}

} // End PoolSimple
