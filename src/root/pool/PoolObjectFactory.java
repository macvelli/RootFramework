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

/**
 * An object lifecycle management interface specifically designed to work with {@link PoolConcurrent}. An implementation of this interface must exist
 * in order for the pool to properly manage object instances.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of elements managed by this {@link PoolObjectFactory}
 */
public interface PoolObjectFactory<T> {

	/**
	 * Creates a new object instance for the pool.
	 *
	 * @return a new object instance for the pool
	 */
	T create();

	/**
	 * Destroys a pooled object. Normally called by the {@link PoolConcurrent} when a <code>validate()</code> method call returns <code>false</code>.
	 *
	 * @param o
	 *            the pooled object to destroy
	 */
	void destroy(T o);

	/**
	 * Returns the fully-qualified class name of the object managed by this {@link PoolObjectFactory}.
	 *
	 * @return the fully-qualified class name of the object managed by this {@link PoolObjectFactory}
	 */
	String getObjectClassName();

	/**
	 * Validates a pooled object to ensure it is still in an acceptable state before allowing the {@link PoolConcurrent} to release it for use.
	 *
	 * @param t
	 *            the pooled object to validate
	 * @return <code>true</code> if the object is valid, <code>false</code> otherwise.
	 */
	boolean validate(T t);

} // End PoolObjectFactory
