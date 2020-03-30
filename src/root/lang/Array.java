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
package root.lang;

import root.metrics.Stopwatch;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Array {

	public static final void copy(final char[] source, int sourcePos, final char[] dest, int destPos, final int length) {
		if (length < 17) {
			for (int i = 0; i < length; i++) {
				dest[destPos++] = source[sourcePos++];
			}
		} else {
			System.arraycopy(source, sourcePos, dest, destPos, length);
		}
	}

	public static void main(final String[] args) {
		char[] arrayA = { 'F', 'o', 'o', 'b', 'a', 'r', '0', '1' };
		char[] arrayB = new char[8];

		final Stopwatch stopwatch = new Stopwatch();

		for (int j = 0; j < 10; j++) {
			stopwatch.start("System Call");
			for (int i = 0; i < 10000000; i++) {
				System.arraycopy(arrayA, 0, arrayB, 0, arrayA.length);
			}
			stopwatch.stop();

			stopwatch.start("Method Call");
			for (int i = 0; i < 10000000; i++) {
				Array.copy(arrayA, 0, arrayB, 0, arrayA.length);
			}
			stopwatch.stop();

			System.out.println("------------------------------------------------");
			System.out.println(arrayA);
			System.out.println(stopwatch.getExecutionRunReport());

			arrayB = new char[arrayA.length << 1];
			System.arraycopy(arrayA, 0, arrayB, 0, arrayA.length);
			System.arraycopy(arrayA, 0, arrayB, arrayA.length, arrayA.length);
			arrayA = arrayB;
		}

		System.out.println("******************* End ************************");
		System.out.println(stopwatch);

		System.exit(0);
	}

} // End Array
