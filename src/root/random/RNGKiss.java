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

import root.annotation.Todo;

/**
 * The KISS (Keep It Simple Stupid) random number generator by George Marsaglia. This is the version posted by George Marsaglia to
 * {@code comp.lang.fortran} on June 23, 2007.
 * <p>
 * Combines:
 *
 * <ol>
 * <li>Simple additive generator x(n)=x(n-1)+545925293</li>
 * <li>A 3-shift XOR-Shift generator</li>
 * <li>A basic Add-With-Carry generator</li>
 * </ol>
 *
 * The output will be a sequence of 32-bit integers with period greater than 2^121 or 10^36.
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class RNGKiss implements RNG {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int x;
	private int y;
	private int z;
	private int w;

	private int c;

	private final Seed seed;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public RNGKiss() {
		this(new SeedFactorySecure());
	}

	public RNGKiss(final SeedFactory seedFactory) {
		this.seed = seedFactory.create(17);

		this.x = this.seed.getInt(0);
		this.y = this.seed.getInt(4);

		if (this.y == 0) {
			this.y++;
		}

		this.z = this.seed.getInt(8) & 0x7FFFFFFF;
		this.w = this.seed.getInt(12) & 0x7FFFFFFF;
		this.c = this.seed.getByte(16) & 0x01;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 * Allows clients to save the seed state of this pseudorandom number generator so that it can be restarted reproducibly.
	 *
	 * @return the {@code byte[]} used to seed this pseudorandom number generator
	 */
	@Override
	@Todo("Should this return a Seed object instead of its underlying byte[] array?")
	public final byte[] getSeed() {
		return this.seed.getBytes();
	}

	/**
	 * Returns the next pseudorandom {@code boolean}.
	 *
	 * @return the next pseudorandom {@code boolean}
	 */
	@Override
	public final boolean nextBoolean() {
		return (this.nextInt() & 0x01) == 1;
	}

	/**
	 * Populates a sequence of pseudorandom bytes into {@code byteArray}.
	 *
	 * @param byteArray
	 *            the {@code byte[]} to populate
	 * @param offset
	 *            the offset to begin byte population
	 * @param length
	 *            the number of bytes to populate
	 */
	@Override
	public final void nextBytes(final byte[] byteArray, int offset, final int length) {
		final int endLoop = offset + length;
		int randomInt;

		do {
			randomInt = this.nextInt();

			byteArray[offset++] = (byte) randomInt;

			if (offset == endLoop) {
				break;
			}

			randomInt >>>= 8;
			byteArray[offset++] = (byte) randomInt;

			if (offset == endLoop) {
				break;
			}

			randomInt >>>= 8;
			byteArray[offset++] = (byte) randomInt;

			if (offset == endLoop) {
				break;
			}

			randomInt >>>= 8;
			byteArray[offset++] = (byte) randomInt;
		} while (offset < endLoop);
	}

	/**
	 * Returns a pseudorandom {@code double} in the range of 0 <= d < 1.
	 *
	 * @return a pseudorandom {@code double} in the range of 0 <= d < 1
	 */
	@Override
	public final double nextDouble() {
		long l = this.nextInt() & 0x3FFFFFF; // Last 26 bits
		l <<= 27;
		l |= this.nextInt() & 0x7FFFFFF; // Last 27 bits

		return l / 9007199254740992.0d;
	}

	/**
	 * Returns a pseudorandom {@code float} in the range of 0 <= f < 1.
	 *
	 * @return a pseudorandom {@code float} in the range of 0 <= f < 1
	 */
	@Override
	public final float nextFloat() {
		return (this.nextInt() & 0x7FFFFF) / 16777216.0f;
	}

	/**
	 * Returns a pseudorandom {@code int} that can be used as an index where {@code 0 <= index < size}.
	 *
	 * @param size
	 *            the size to index by
	 * @return a pseudorandom {@code int} that can be used as an index where {@code 0 <= index < size}
	 */
	@Override
	public final int nextIndex(final int size) {
		return (this.nextInt() & 0x7FFFFFFF) % size;
	}

	/**
	 * Returns the next pseudorandom {@code int}.
	 *
	 * @return the next pseudorandom {@code int}
	 */
	@Override
	public final int nextInt() {
		// Simple Additive Generator
		this.x += 545925293;

		// Three-pass XOR-Shift Generator
		this.y ^= this.y << 13;
		this.y ^= this.y >>> 17;
		this.y ^= this.y << 5;

		// Add-With-Carry Generator
		final int t = this.z + this.w + this.c;
		this.z = this.w;
		this.c = t & 0x80000000;
		this.w = t & 0x7FFFFFFF;

		return this.x + this.y + this.w;
	}

	/**
	 * Returns the next pseudorandom {@code long}.
	 *
	 * @return the next pseudorandom {@code long}
	 */
	@Override
	public final long nextLong() {
		final int rand = this.nextInt();
		long l = rand; // Will be negative if rand is negative

		l <<= rand & 0x1F; // Left shift 0-31 bits
		return l | this.nextInt(); // Inclusive OR the next int
	}

	/**
	 * Returns a pseudorandom {@code int} that is defined within the range {@code from <= int < to}.
	 *
	 * @param size
	 *            the size to index by
	 * @return a pseudorandom {@code int} that is defined within the range {@code from <= int < to}
	 */
	@Override
	public final int nextRange(final int from, final int to) {
		return (this.nextInt() & 0x7FFFFFFF) % (to - from) + from;
	}

} // End RNGKiss
