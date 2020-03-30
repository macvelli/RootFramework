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

import root.adt.QueueBounded;
import root.lang.Extractable;
import root.lang.FastString;
import root.lang.ParamString;
import root.log.Log;
import root.util.Parameters;

/**
 * A generic resource pool that coordinates with a {@link PoolObjectFactory} to manage the creation, validation, and destruction of pooled objects.
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements managed by the pool
 */
public abstract class Pool<T> {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	protected static final ParamString acquiringObjectFromPool = new ParamString("Acquiring a {P} from the pool");
	protected static final ParamString returningObjectToPool = new ParamString("Returning a {P} to the pool");

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	/** The size of the pool */
	protected int size;

	/** <code>true</code> if the {@link #close()} method has been called */
	protected boolean closed;

	/** Caches the parameters used for {@link Log#debug(String, Extractable...)} */
	protected final Extractable[] debugParams;

	/** The {@link PoolObjectFactory} implementation for the pool */
	protected final PoolObjectFactory<T> factory;

	/** The bounded queue that stores idle objects in the pool */
	protected final QueueBounded<T> queue;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 * Creates an empty pool with a fixed capacity. Object lifecycles are managed by the provided {@link PoolObjectFactory} implementation.
	 *
	 * @param capacity
	 *            the capacity of the pool
	 * @param factory
	 *            the {@link PoolObjectFactory} implementation to use
	 */
	protected Pool(final int capacity, final PoolObjectFactory<T> factory) {
		this.closed = false;

		this.factory = factory;
		this.debugParams = Parameters.toArray(new FastString(factory.getObjectClassName()));
		this.queue = new QueueBounded<>(capacity);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Returns the abandoned object to the pool making it immediately available for acquisition.
	 *
	 * @param t
	 *            the object to return to the pool
	 */
	public abstract void abandon(final T t);

	/**
	 * Acquires an object from the pool.
	 *
	 * @return an instance of the pooled object
	 * @throws InterruptedException
	 */
	public abstract T acquire() throws InterruptedException;

	/**
	 * Closes the pool and destroys every pooled object.
	 *
	 * @throws InterruptedException
	 */
	public abstract void close() throws InterruptedException;

	/**
	 * Returns the capacity of this pool.
	 *
	 * @return the capacity of this pool
	 */
	public final int getCapacity() {
		return this.queue.getCapacity();
	}

	/**
	 * Removes the specified object from the pool.
	 *
	 * @param t
	 *            the object to remove from the pool
	 */
	public final void remove(final T t) {
		this.factory.destroy(t);
		this.size--;
	}

} // End Pool
