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

/**
 * Contains the seed for an {@link RNG} which is used to initialized the random number generator.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Seed {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final byte[] seed;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Seed(final byte[] seed) {
		this.seed = seed;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final byte getByte(final int offset) {
		return this.seed[offset];
	}

	public final byte[] getBytes() {
		return this.seed;
	}

	public final int getInt(final int offset) {
		return this.seed[offset] << 24 | (this.seed[offset + 1] & 0xFF) << 16 | (this.seed[offset + 2] & 0xFF) << 8 | this.seed[offset + 3] & 0xFF;
	}

} // End Seed
