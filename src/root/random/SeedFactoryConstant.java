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
 * This {@link SeedFactory} is really only useful for unit testing pseudorandom number generators.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class SeedFactoryConstant implements SeedFactory {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final int[] intArray;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public SeedFactoryConstant(final int... intArray) {
		this.intArray = intArray;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final Seed create(final int numBytes) {
		final byte[] seed = new byte[numBytes];
		int index = 0, offset = 0;
		int randomInt;

		do {
			randomInt = this.intArray[index++];

			seed[offset++] = (byte) randomInt;

			if (offset == numBytes) {
				break;
			}

			randomInt >>>= 8;
			seed[offset++] = (byte) randomInt;

			if (offset == numBytes) {
				break;
			}

			randomInt >>>= 8;
			seed[offset++] = (byte) randomInt;

			if (offset == numBytes) {
				break;
			}

			randomInt >>>= 8;
			seed[offset++] = (byte) randomInt;
		} while (offset < numBytes);

		return new Seed(seed);
	}

} // End SeedFactoryConstant
