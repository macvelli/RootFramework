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
package root.util;

import java.util.concurrent.locks.ReentrantLock;

import root.random.RNG;
import root.random.RNGKiss;

/**
 * This is a thread-safe version of RNGKiss where all methods defined on the {@link RNG} interface are implemented here as static methods with a
 * {@link ReentrantLock} used to make each method thread-safe.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Random {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final RNGKiss rng = new RNGKiss();
	private static final ReentrantLock randomLock = new ReentrantLock();

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	public static final byte[] getSeed() {
		return rng.getSeed();
	}

	public static final boolean nextBoolean() {
		Random.randomLock.lock();

		try {
			return rng.nextBoolean();
		} finally {
			Random.randomLock.unlock();
		}
	}

	public static final void nextBytes(final byte[] byteArray, final int offset, final int length) {
		Random.randomLock.lock();

		try {
			rng.nextBytes(byteArray, offset, length);
		} finally {
			Random.randomLock.unlock();
		}
	}

	public static final double nextDouble() {
		Random.randomLock.lock();

		try {
			return rng.nextDouble();
		} finally {
			Random.randomLock.unlock();
		}
	}

	public static final float nextFloat() {
		Random.randomLock.lock();

		try {
			return rng.nextFloat();
		} finally {
			Random.randomLock.unlock();
		}
	}

	public static final int nextIndex(final int size) {
		Random.randomLock.lock();

		try {
			return rng.nextIndex(size);
		} finally {
			Random.randomLock.unlock();
		}
	}

	public static final int nextInt() {
		Random.randomLock.lock();

		try {
			return rng.nextInt();
		} finally {
			Random.randomLock.unlock();
		}
	}

	public static final long nextLong() {
		Random.randomLock.lock();

		try {
			return rng.nextLong();
		} finally {
			Random.randomLock.unlock();
		}
	}

	public static final int nextRange(final int from, final int to) {
		Random.randomLock.lock();

		try {
			return rng.nextRange(from, to);
		} finally {
			Random.randomLock.unlock();
		}
	}

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private Random() {
	}

} // End Random
