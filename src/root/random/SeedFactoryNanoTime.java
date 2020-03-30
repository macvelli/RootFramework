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
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class SeedFactoryNanoTime implements SeedFactory {

	@Override
	public final Seed create(final int numBytes) {
		final byte[] seed = new byte[numBytes];
		long nanoTime = System.nanoTime();

		for (int i = 0; i < numBytes; nanoTime >>>= 8) {
			if (nanoTime == 0) {
				nanoTime = System.nanoTime();
			}

			seed[i++] = (byte) nanoTime;
		}

		return new Seed(seed);
	}

} // End SeedFactoryNanoTime
