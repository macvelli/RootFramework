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
package root.random;

import java.security.SecureRandom;

import root.annotation.Delegate;

/**
 * A {@link Delegate} implementation of the {@link SecureRandom} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Delegate
public final class RNGSecure implements RNG {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final byte[] oneByteArray;
	private final byte[] fourByteArray;
	private final byte[] eightByteArray;
	private final SecureRandom delegate;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public RNGSecure() {
		this.oneByteArray = new byte[1];
		this.fourByteArray = new byte[4];
		this.eightByteArray = new byte[8];
		this.delegate = new SecureRandom();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Returns {@code null} as {@link SecureRandom} does not publish the seed it uses to initialize itself.
	 *
	 * @return {@code null}
	 */
	@Override
	public final byte[] getSeed() {
		return null;
	}

	/**
	 * Returns the next random {@code boolean}.
	 *
	 * @return the next random {@code boolean}
	 */
	@Override
	public final boolean nextBoolean() {
		this.delegate.nextBytes(this.oneByteArray);

		return (this.oneByteArray[0] & 0x01) == 1;
	}

	/**
	 * Populates a sequence of random bytes into {@code byteArray}.
	 *
	 * @param byteArray
	 *            the {@code byte[]} to populate
	 * @param offset
	 *            the offset to begin byte population
	 * @param length
	 *            the number of bytes to populate
	 */
	@Override
	public final void nextBytes(final byte[] byteArray, final int offset, final int length) {
		final byte[] nextByteArray = new byte[length];
		this.delegate.nextBytes(nextByteArray);
		System.arraycopy(nextByteArray, 0, byteArray, offset, length);
	}

	/**
	 * Returns a random {@code double} in the range of 0 <= d < 1.
	 *
	 * @return a random {@code double} in the range of 0 <= d < 1
	 */
	@Override
	public final double nextDouble() {
		this.delegate.nextBytes(this.eightByteArray);

		// Take the first 53 bits out of the byte array
		long l = this.eightByteArray[0] & 0x1F;
		l = (l << 8) + this.eightByteArray[1] & 0xFF;
		l = (l << 8) + this.eightByteArray[2] & 0xFF;
		l = (l << 8) + this.eightByteArray[3] & 0xFF;
		l = (l << 8) + this.eightByteArray[4] & 0xFF;
		l = (l << 8) + this.eightByteArray[5] & 0xFF;
		l = (l << 8) + this.eightByteArray[6] & 0xFF;

		return l / 9007199254740992.0d;
	}

	/**
	 * Returns a random {@code float} in the range of 0 <= f < 1.
	 *
	 * @return a random {@code float} in the range of 0 <= f < 1
	 */
	@Override
	public final float nextFloat() {
		this.delegate.nextBytes(this.fourByteArray);

		// Take the first 23 bits out of the byte array
		int i = this.fourByteArray[0] & 0x7F;
		i = (i << 8) + this.fourByteArray[1] & 0xFF;
		i = (i << 8) + this.fourByteArray[2] & 0xFF;

		return i / 16777216.0f;
	}

	/**
	 * Returns a random {@code int} that can be used as an index where {@code 0 <= index < size}.
	 *
	 * @param size
	 *            the size to index by
	 * @return a random {@code int} that can be used as an index where {@code 0 <= index < size}
	 */
	@Override
	public final int nextIndex(final int size) {
		int i;

		if (size < 256) {
			this.delegate.nextBytes(this.oneByteArray);

			i = this.oneByteArray[0] & 0xFF;
		} else {
			this.delegate.nextBytes(this.fourByteArray);

			i = this.fourByteArray[0] & 0x7F;
			i = (i << 8) + this.fourByteArray[1] & 0xFF;
			i = (i << 8) + this.fourByteArray[2] & 0xFF;
			i = (i << 8) + this.fourByteArray[3] & 0xFF;
		}

		return i % size;
	}

	/**
	 * Returns the next random {@code int}.
	 *
	 * @return the next random {@code int}
	 */
	@Override
	public final int nextInt() {
		this.delegate.nextBytes(this.fourByteArray);

		int i = this.fourByteArray[0] & 0xFF;
		i = (i << 8) + this.fourByteArray[1] & 0xFF;
		i = (i << 8) + this.fourByteArray[2] & 0xFF;
		i = (i << 8) + this.fourByteArray[3] & 0xFF;

		return i;
	}

	/**
	 * Returns the next random {@code long}.
	 *
	 * @return the next random {@code long}
	 */
	@Override
	public final long nextLong() {
		this.delegate.nextBytes(this.eightByteArray);

		long l = this.eightByteArray[0] & 0xFF;
		l = (l << 8) + this.eightByteArray[1] & 0xFF;
		l = (l << 8) + this.eightByteArray[2] & 0xFF;
		l = (l << 8) + this.eightByteArray[3] & 0xFF;
		l = (l << 8) + this.eightByteArray[4] & 0xFF;
		l = (l << 8) + this.eightByteArray[5] & 0xFF;
		l = (l << 8) + this.eightByteArray[6] & 0xFF;
		l = (l << 8) + this.eightByteArray[7] & 0xFF;

		return l;
	}

	/**
	 * Returns a random {@code int} that is defined within the range {@code from <= int < to}.
	 *
	 * @param size
	 *            the size to index by
	 * @return a random {@code int} that is defined within the range {@code from <= int < to}
	 */
	@Override
	public final int nextRange(final int from, final int to) {
		final int size = to - from;
		int i;

		if (size < 256) {
			this.delegate.nextBytes(this.oneByteArray);

			i = this.oneByteArray[0] & 0xFF;
		} else {
			this.delegate.nextBytes(this.fourByteArray);

			i = this.fourByteArray[0] & 0x7F;
			i = (i << 8) + this.fourByteArray[1] & 0xFF;
			i = (i << 8) + this.fourByteArray[2] & 0xFF;
			i = (i << 8) + this.fourByteArray[3] & 0xFF;
		}

		return i % size + from;
	}

} // End RNGSecure
