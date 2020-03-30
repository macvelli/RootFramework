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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import root.adt.CollectorCharArray;
import root.annotation.Todo;
import root.clock.CurrentTime;
import root.lang.Constants;
import root.lang.ParamString;
import root.lang.StringExtractor;
import root.lang.reflect.CharArrayField;
import root.lang.reflect.ObjectConstructor;
import root.validation.InvalidParameterException;
import sun.misc.Unsafe;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Root {

	// <><><><><><><><><><><><><><><> Constants ><><><><><><><><><><><><><><><>

	public static final CurrentTime systemTimePerSecond = new CurrentTime(1);

	@Todo("One of these days this won't be necessary for String objects")
	private static final CharArrayField<String> strValue = new CharArrayField<>(String.class, "value");

	@Todo("One of these days this won't be necessary")
	private static final ObjectConstructor<String> fastNewString = new ObjectConstructor<>(String.class, char[].class, boolean.class);

	private static final int[] hashTableSizeArray = { 11, 23, 43, 67, 89, 107, 131, 149, 173, 193, 223, 239, 257, 277, 307, 331, 347, 367, 389, 409,
			431, 449, 479, 491, 521, 541, 557, 577, 599, 619, 641, 661, 683, 709, 727, 751, 769, 797, 811, 839, 853, 877, 907, 919, 941, 967, 983,
			1009, 1031, 1049, 1069, 1091, 1109, 1151, 1153, 1181, 1201, 1217, 1237, 1259, 1283, 1301, 1327, 1361, 1367, 1399, 1409, 1429, 1451, 1481,
			1493, 1523, 1543, 1559, 1579, 1601, 1621, 1657, 1667, 1693, 1709, 1733, 1753, 1777, 1801, 1823, 1847, 1861, 1877, 1901, 1931, 1949, 1973,
			1987, 2011, 2027, 2053, 2069, 2099, 2113, 2137, 2161, 2179, 2203, 2221, 2243, 2267, 2287, 2309, 2333, 2347, 2371, 2389, 2411, 2437, 2459,
			2477, 2503, 2521, 2539, 2579, 2591, 2609, 2633, 2647, 2671, 2689, 2711, 2731, 2753, 2777, 2797, 2819, 2837, 2861, 2887, 2903, 2927, 2953,
			2969, 2999, 3011, 3037, 3061, 3079, 3109, 3119, 3137, 3163, 3181, 3203, 3221, 3251, 3271, 3299, 3307, 3329, 3359, 3371, 3407, 3413, 3449,
			3457, 3491, 3499, 3527, 3541, 3571, 3593, 3607, 3631, 3659, 3671, 3691, 3719, 3733, 3761, 3779, 3797, 3821, 3847, 3863, 3889, 3907, 3929,
			3947, 3989, 3989, 4013, 4049, 4057, 4079, 4099, 4127, 4139, 4177, 4201, 4211, 4229, 4253, 4271, 4289, 4327, 4337, 4357, 4373, 4397, 4421,
			4441, 4463, 4481, 4507, 4523, 4547, 4567, 4591, 4621, 4637, 4651, 4673, 4703, 4721, 4751, 4759, 4783, 4801, 4831, 4861, 4871, 4889, 4909,
			4931, 4951, 4973, 4993, 5021, 5039, 5059, 5077, 5099, 5147, 5147, 5167, 5189, 5209, 5227, 5261, 5273, 5297, 5323, 5333, 5381, 5381, 5399,
			5419, 5441, 5471 };

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	/**
	 * Returns the <code>int[]</code> parameter with each <code>int</code> converted to its absolute value.
	 *
	 * @param ints
	 *            The <code>int[]</code> of values.
	 * @return the <code>int[]</code> absolute values.
	 */
	public static final int[] abs(final int... ints) {
		int j = 0;

		for (final int i : ints) {
			if (i < 0) {
				ints[j] = -i;
			}
			j++;
		}

		return ints;
	}

	/**
	 * Returns the substring of the {@link String} parameter found between the open and close characters.
	 * <p>
	 * A {@code null} is returned if either the open or close characters cannot be found.
	 *
	 * @param str
	 *            The {@link String} to parse for the substring
	 * @param open
	 *            The opening character
	 * @param close
	 *            The closing character
	 * @return The {@link String} found between the open and close characters
	 */
	public static final String between(final String str, final char open, final char close) {
		if (str != null && str.length() > 1) {
			final char[] value = Root.toCharArray(str);

			for (int begin = 0; begin < value.length; begin++) {
				if (value[begin] == open) {
					begin++;

					for (int end = begin; end < value.length; end++) {
						if (value[end] == close) {
							final int count = end - begin;

							return count == 0 ? Constants.EMPTY_STRING : new String(value, begin, count);
						}
					}

					break;
				}
			}
		}

		return null;
	}

	/**
	 * Safely extract a <code>boolean</code> value from a {@link Boolean} object instance. If the {@link Boolean} instance is <code>null</code>,
	 * <code>false</code> is returned.
	 *
	 * @param n
	 *            The {@link Boolean} object instance.
	 * @return a <code>boolean</code> value, <code>false</code> if <code>null</code>.
	 */
	public static final boolean booleanValue(final Boolean b) {
		return b != null && b.booleanValue();
	}

	/**
	 * Safely extract a <code>byte</code> value from a {@link Number} object instance. If the {@link Number} instance is <code>null</code>, zero is
	 * returned.
	 *
	 * @param n
	 *            The {@link Number} object instance.
	 * @return a <code>byte</code> value, or zero if <code>null</code>.
	 */
	public static final byte byteValue(final Number n) {
		return (byte) (n == null ? 0 : n.intValue());
	}

	/**
	 * Calculates the capacity of a hash table in multiples of 16.
	 *
	 * @param requestedCapacity
	 *            the requested capacity of the hash table
	 * @return the calculated hash table capacity, as a multiple of 16
	 */
	public static final int calculateHashTableCapacity(final int requestedCapacity) {
		if (requestedCapacity <= 8) {
			return 8;
		}

		int capacity = requestedCapacity >> 4;

		if (requestedCapacity % 16 != 0) {
			capacity++;
		}

		return capacity << 4;
	}

	/**
	 * Returns the size of a hash table based upon the given capacity. For capacity values less than or equal to 4096, a predetermined prime number
	 * will be returned with a load factor around 75%. Capacity values greater than 4096 will return an odd number hash table size approximating a 75%
	 * load factor.
	 *
	 * @param capacity
	 *            The capacity of the hash table
	 * @return the size of the hash table based upon the given capacity
	 */
	public static final int calculateHashTableSize(final int capacity) {
		if (capacity <= 4096) {
			return hashTableSizeArray[capacity >> 4];
		}

		return capacity + (capacity >> 2) + (capacity >> 4) + 1;
	}

	/**
	 * Casts the {@link Object} to the data type defined by generic type parameter {@code T}.
	 *
	 * @param obj
	 *            the object to cast
	 * @return the {@link Object} casted to the generic type parameter {@code T}
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T cast(final Object obj) {
		return (T) obj;
	}

	/**
	 * Closes an <code>InputStream</code> while catching and rethrowing any <code>IOException</code> s as <code>RuntimeException</code>s.
	 *
	 * @param in
	 *            the <code>InputStream</code> to close.
	 */
	public static final void close(final InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Returns <code>true</code> if the {@link Object} array contains the specified {@link Object}, <code>false</code> otherwise.
	 *
	 * @param array
	 *            The {@link Object} array.
	 * @param obj
	 *            The {@link Object} to find.
	 * @return <code>true</code> if the {@link Object} array contains the specified {@link Object}.
	 */
	public static final boolean contains(final Object[] array, final Object obj) {
		// TODO: Do the same for Comparable[], Comparable as well as primitives
		// (see java.util.Arrays)
		if (array != null) {
			for (final Object p : array) {
				if (obj == p || obj != null && obj.equals(p)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns <code>a</code> divided by <code>b</code> as a <code>double</code>.
	 *
	 * @param a
	 *            the numerator
	 * @param b
	 *            the denominator
	 * @return <code>a</code> divided by <code>b</code> as a <code>double</code>
	 */
	public static final double divideDouble(final double a, final double b) {
		return a / b;
	}

	/**
	 * Returns <code>a</code> divided by <code>b</code> as a <code>float</code>.
	 *
	 * @param a
	 *            the numerator
	 * @param b
	 *            the denominator
	 * @return <code>a</code> divided by <code>b</code> as a <code>float</code>
	 */
	public static final float divideFloat(final float a, final float b) {
		return a / b;
	}

	/**
	 * Safely extract a <code>double</code> value from a {@link Number} object instance. If the {@link Number} instance is <code>null</code>, zero is
	 * returned.
	 *
	 * @param n
	 *            The {@link Number} object instance.
	 * @return a <code>double</code> value, or zero if <code>null</code>.
	 */
	public static final double doubleValue(final Number n) {
		return n == null ? 0.0d : n.doubleValue();
	}

	/**
	 * Safely determines whether the two {@code char[]} objects are equal to one another.
	 *
	 * @param oneCharArray
	 *            the first {@code char[]} array
	 * @param twoCharArray
	 *            the second {@code char[]} array
	 * @return {@code true} if the two {@code char[]} arrays are equal, {@code false} otherwise
	 */
	public static final boolean equals(final char[] oneCharArray, final char[] twoCharArray) {
		if (oneCharArray == twoCharArray) {
			return true;
		}

		if (oneCharArray == null || twoCharArray == null || oneCharArray.length != twoCharArray.length) {
			return false;
		}

		for (int i = 0; i < oneCharArray.length; i++) {
			if (oneCharArray[i] != twoCharArray[i]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Safely determines whether the two objects are equal to one another according to the semantics of <code>Object.equals()</code> while gracefully
	 * handling <code>null</code> reference values.
	 *
	 * @param o
	 *            the first object to compare for equality.
	 * @param p
	 *            the second object to company for equality.
	 * @return <code>true</code> if <code>o.equals(p)</code>; <code>false</code> otherwise.
	 */
	public static final boolean equals(final Object o, final Object p) {
		return o == p || o != null && o.equals(p);
	}

	/**
	 * Performs a deep dive to determine whether two arrays are equal to each another. Both arrays must be the same length and each element is safely
	 * compared according to the semantics of <code>Object.equals()</code> while gracefully handling <code>null</code> reference values.
	 *
	 * @param a
	 *            the first array to compare for equality.
	 * @param b
	 *            the second array to company for equality.
	 * @return <code>true</code> if both arrays are the same length and contain the same elements in the exact same order; <code>false</code>
	 *         otherwise.
	 */
	public static final boolean equals(final Object[] a, final Object[] b) {
		if (a == b) {
			return true;
		}

		if (a == null || b == null || a.length != b.length) {
			return false;
		}

		Object c, d;
		for (int i = 0; i < a.length; i++) {
			c = a[i];
			d = b[i];

			if (c != d && (c == null || !c.equals(d))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Safely determines whether an {@link Object}'s {@link Class} type is <i>equal</i> to the {@link Class} parameter while gracefully handling
	 * <code>null</code> reference values.
	 *
	 * @param obj
	 *            The {@link Object} to check it's {@link Class} type
	 * @param clazz
	 *            The {@link Class} type to check for
	 * @return {@code true} if {@code obj != null && obj.getClass() == clazz}
	 */
	public static final boolean equalToClass(final Object obj, final Class<? extends Object> clazz) {
		return obj != null && obj.getClass() == clazz;
	}

	/**
	 * Returns <code>true</code> if the <code>int</code> is an even number, <code>false</code> if it is odd.
	 *
	 * @param i
	 *            The <code>int</code> value.
	 * @return <code>true</code> if the <code>int</code> is an even number.
	 */
	public static final boolean even(final int i) {
		return (i & 1) == 0;
	}

	/**
	 * Extracts a string representation of the specified {@code char[]} array.
	 * <ul>
	 * <li>If the array is {@code null}, then extract "null"</li>
	 * <li>If the array is empty, then extract "[]"</li>
	 * <li>If the array is {'F', 'o', 'o'}, then extract "[F, o, o]"
	 * </ul>
	 *
	 * @param extractor
	 *            the extractor to populate
	 * @param array
	 *            the {@code char[]} array to extract
	 */
	public static final void extract(final StringExtractor extractor, final char[] array) {
		Arrays.toString(array);

		if (array == null) {
			extractor.append(Constants.nullCharArray);
		} else {
			extractor.append('[');

			if (array.length > 0) {
				extractor.append(array[0]);

				for (int i = 1; i < array.length; i++) {
					extractor.append(',').append(' ').append(array[i]);
				}
			}

			extractor.append(']');
		}
	}

	/**
	 * TODO: 20140516 Test this new implementation out. Combination of Integer class implementation and the cppformat library's fmt::FormatInt method
	 * found at https://github.com/cppformat/cppformat.
	 *
	 * TODO: Shouldn't I just move this into StringExtractor? Where else am I going to use this? And I want people to call builder.append(i) not
	 * Fast.extract(builder, i)
	 *
	 * @param chars
	 * @param i
	 */
	public static final void extract(final StringExtractor chars, final float f) {
		if (f == Float.NaN) {
			chars.append(Constants.notANumber);
			return;
		}

		if (f == Float.POSITIVE_INFINITY) {
			chars.append(Constants.posInfinity);
			return;
		}

		if (f == Float.NEGATIVE_INFINITY) {
			chars.append(Constants.negInfinity);
			return;
		}

		final int floatBits = Float.floatToRawIntBits(f);
		if (floatBits == 0) {
			chars.append(Constants.posZero);
			return;
		}

		if (floatBits == 0x80000000) {
			chars.append(Constants.negZero);
			return;
		}

		// if (floatBits == 0x4b800000) {
		// chars.append('1', '6', '7', '7', '7', '2', '1', '6', '.', '0');
		// return;
		// }
		//
		// if (floatBits == 0xcb800000) {
		// chars.append('-', '1', '6', '7', '7', '7', '2', '1', '6', '.', '0');
		// return;
		// }

		final boolean sign = (floatBits & 0x80000000) != 0;
		final int exponent = ((floatBits & 0x7f800000) >> 23) - 127;
		final int significand = floatBits & 0x007fffff | 0x00800000;

		int fraction = 0, numDigitsOfPrecision = 8;

		int charPos = chars.getLength();
		final char[] charArr = chars.getCharArray(15);

		if (exponent >= 0) {
			// We have an integer component to process

			if (exponent < 24) {
				final long longSignificand = (long) significand << exponent;
				int integer = (int) (longSignificand >> 23);
				fraction = (int) (longSignificand - ((long) integer << 23));
				int q, r, decimalPos;

				if (sign) {
					charArr[charPos++] = '-';
				}

				decimalPos = charPos + intFormatSize(integer);
				charPos = decimalPos;

				while (integer >= 100) {
					q = integer / 100;
					r = integer - (q << 6) - (q << 5) - (q << 2);
					integer = q;
					charArr[--charPos] = Constants.digitOnes[r];
					charArr[--charPos] = Constants.digitTens[r];
					numDigitsOfPrecision -= 2;
				}

				if (integer < 10) {
					charArr[--charPos] = Constants.digits[integer];
					numDigitsOfPrecision--;
				} else {
					charArr[--charPos] = Constants.digitOnes[integer];
					charArr[--charPos] = Constants.digitTens[integer];
					numDigitsOfPrecision -= 2;
				}

				charPos = decimalPos;
				charArr[charPos++] = '.';

				if (numDigitsOfPrecision == 0 || fraction == 0) {
					charArr[charPos++] = '0';
				} else {
					while (numDigitsOfPrecision >= 2 && fraction > 0) {
						fraction = (fraction << 6) + (fraction << 5) + (fraction << 2);
						q = fraction >> 23;
						fraction -= q << 23;

						charArr[charPos++] = Constants.digitTens[q];
						charArr[charPos++] = Constants.digitOnes[q];
						numDigitsOfPrecision -= 2;
					}

					if (numDigitsOfPrecision == 1 && fraction > 0) {
						fraction = (fraction << 3) + (fraction << 1);
						q = fraction >> 23;

						charArr[charPos++] = Constants.digits[q];
					}

					if (fraction >= 4194304) {
						// Round half up
						while (charArr[--charPos] == '9') {
							;
						}

						charArr[charPos] = (char) (charArr[charPos++] + 1);
					}
				}

				// System.out.println("Integer: " + integer + ", fraction: " + fraction + ", q: " +
				// q + ", r: " + r);
			}
		} else {
			// We are dealing with just the fractional part
			int q;

			// System.out.println("floatBits: " + Integer.toHexString(floatBits) + ", Sign: " + sign
			// + ", exponent: " + exponent + ", significand: " + significand);

			if (exponent >= -15) {
				long longFraction = significand;
				final int shift = 23 - exponent;

				if (sign) {
					charArr[charPos++] = '-';
				}

				charArr[charPos++] = '0';
				charArr[charPos++] = '.';

				do {
					longFraction = (longFraction << 6) + (longFraction << 5) + (longFraction << 2);
					q = (int) (longFraction >> shift);
					if (q > 0) {
						longFraction -= (long) q << shift;
					}

					charArr[charPos++] = Constants.digitTens[q];
					charArr[charPos++] = Constants.digitOnes[q];
					numDigitsOfPrecision -= 2;
				} while (numDigitsOfPrecision >= 2 && longFraction > 0);

				if (numDigitsOfPrecision == 1 && longFraction > 0) {
					longFraction = (longFraction << 3) + (longFraction << 1);
					q = (int) (longFraction >> shift);

					charArr[charPos++] = Constants.digits[q];
				}

				if (longFraction >= 1L << shift - 1) {
					// Round half up
					while (charArr[--charPos] == '9') {
						;
					}

					charArr[charPos] = (char) (charArr[charPos++] + 1);
				}
			}
		}

		chars.reduceLength(charPos);
	}

	/**
	 * TODO: 20140516 Test this new implementation out. Combination of Integer class implementation and the cppformat library's fmt::FormatInt method
	 * found at https://github.com/cppformat/cppformat.
	 *
	 * TODO: Shouldn't I just move this into StringExtractor? Where else am I going to use this? And I want people to call builder.append(i) not
	 * Fast.extract(builder, i)
	 *
	 * @param chars
	 * @param i
	 */
	public static final void extract(final StringExtractor chars, int i) {
		if (i == Integer.MIN_VALUE) {
			chars.append(Constants.intMinValue, 0, 11);
			return;
		}

		final int charsSize = chars.getLength();
		int charPos = stringSize(i);
		final char[] buf = chars.getCharArray(charPos);
		final boolean sign = i < 0;
		charPos += charsSize;

		i = sign ? -i : i;
		int q, r;

		while (i >= 100) {
			q = i / 100;
			r = i - (q << 6) - (q << 5) - (q << 2);
			i = q;
			buf[--charPos] = Constants.digitOnes[r];
			buf[--charPos] = Constants.digitTens[r];
		}

		if (i < 10) {
			buf[--charPos] = Constants.digits[i];
		} else {
			buf[--charPos] = Constants.digitOnes[i];
			buf[--charPos] = Constants.digitTens[i];
		}

		if (sign) {
			buf[--charPos] = '-';
		}
	}

	/**
	 * Safely extract a <code>float</code> value from a {@link Number} object instance. If the {@link Number} instance is <code>null</code>, zero is
	 * returned.
	 *
	 * @param n
	 *            The {@link Number} object instance.
	 * @return a <code>float</code> value, or zero if <code>null</code>.
	 */
	public static final float floatValue(final Number n) {
		return n == null ? 0.0f : n.floatValue();
	}

	/**
	 * Takes an {@link InputStream} and returns a {@link BufferedReader}.
	 *
	 * @param is
	 *            the {@link InputStream}
	 * @return a {@link BufferedReader} that wraps the {@link InputStream}
	 */
	public static final BufferedReader getBufferedReader(final InputStream is) {
		return new BufferedReader(new InputStreamReader(is));
	}

	/**
	 * Attempts to load a resource identified by {@code resourceName} using the following algorithm:
	 * <ol>
	 * <li>Check if the {@code resourceName} actually refers to an environment variable, and use it if it does</li>
	 * <li>Attempt to load the resource from the {@code clazz}</li>
	 * <li>If not found, attempt to load the resource from the system {@link ClassLoader}</li>
	 * <li>If still not found, attempt to load the resource from the file system</li>
	 * </ol>
	 * When the resource is located successfully, an {@link InputStream} is created and then wrapped with a {@link BufferedInputStream} for
	 * performance purposes.
	 *
	 * @param clazz
	 *            the class to load the resource from
	 * @param resourceName
	 *            the name of the resource to load
	 * @return a {@link BufferedInputStream} of the resource
	 */
	public static final BufferedInputStream getResourceAsStream(final Class<?> clazz, String resourceName) {
		InputStream is = null;

		// Check to see if the resourceName is actually an environment variable
		if (System.getenv(resourceName) != null) {
			resourceName = System.getenv(resourceName);
		}

		// 1. Try to load the resourceName from the clazz's ClassLoader
		if (clazz != null) {
			is = clazz.getResourceAsStream(resourceName);
		}

		// 2. If Step 1 failed, try to load the resourceName from the System ClassLoader
		if (is == null) {
			is = ClassLoader.getSystemResourceAsStream(resourceName);
		}

		// 3. If Steps 1 & 2 failed, try to load the resourceName from the file system
		if (is == null) {
			try {
				is = new FileInputStream(resourceName);
			} catch (final FileNotFoundException e) {
				// No-op
			}
		}

		// Error - Cannot find resource by resourceName
		if (is == null) {
			throw new InvalidParameterException("getResourceAsStream", String.class, "resourceName",
					ParamString.formatMsg("Cannot find resource from Class {P} and resource name {P}", clazz, resourceName));
		}

		return new BufferedInputStream(is);
	}

	/**
	 * TODO: Document!
	 *
	 * @param t
	 * @return
	 */
	public static final String getStackTraceAsString(final Throwable t) {
		final StringWriter sw = new StringWriter(500);

		t.printStackTrace(new PrintWriter(sw));

		return sw.toString();
	}

	/**
	 * Returns an instance of {@link sun.misc.Unsafe}.
	 *
	 * @return an instance of {@link sun.misc.Unsafe}
	 */
	public static final Unsafe getUnsafe() {
		try {
			final Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			return (Unsafe) field.get(null);
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			throw new RuntimeException("Cannot get instance of sun.misc.Unsafe", e);
		}
	}

	/**
	 * Calculates the hash code of a {@code char[]}.
	 *
	 * @param charArray
	 *            the {@code char[]} to calculate the hash value
	 * @return the hash code of a {@code char[]}
	 */
	public static final int hashCode(final char[] charArray) {
		if (charArray == null || charArray.length == 0) {
			return 0;
		}

		int hash = charArray.length;
		for (final char element : charArray) {
			hash = (hash << 5 ^ hash) + element;
		}

		return hash & 0x7FFFFFFF;
	}

	/**
	 * Ensures the <code>int</code> value parameter will be a positive value for use as a hash code value.
	 *
	 * @param integer
	 *            the <code>int</code> value to use for the hash code
	 * @return a hash code value greater than or equal to zero for the integer
	 */
	public static final int hashCode(final int integer) {
		return integer & 0x7FFFFFFF;
	}

	/**
	 * Calculates a hash code value from the {@code long} values in the array starting from index zero to {@code size - 1}.
	 *
	 * @param array
	 *            the array whose hash code to calculate
	 * @param size
	 *            the size of the array to use for the calculation
	 * @return a hash code value of the array
	 */
	public static final int hashCode(final long[] array, final int size) {
		if (array == null) {
			return 0;
		}

		long h = size;
		for (int i = 0; i < size; i++) {
			h <<= 1;

			h ^= array[i];
		}

		return (int) h;
	}

	/**
	 * Defaults <code>null</code> object reference hash code values to zero while ensuring non-null references always return a positive hash code
	 * value.
	 *
	 * @param obj
	 *            the object to calculate its hash code.
	 * @return a hash code value greater than or equal to zero for the object.
	 */
	public static final int hashCode(final Object obj) {
		return obj == null ? 0 : obj.hashCode() & 0x7FFFFFFF;
	}

	/**
	 * TODO: Document!
	 *
	 * @param array
	 * @return
	 */
	public static final int hashCode(final Object[] array) {
		if (array == null) {
			return 0;
		}

		int h = array.length;
		for (final Object o : array) {
			if (o != null) {
				h ^= o.hashCode();
			}
			h <<= 1;
		}

		return h & 0x7FFFFFFF;
	}

	/**
	 * Performs a deep dive to calculate an <code>Object[]</code> hash code value. Each array element hash code value is used to calculate the
	 * aggregate hash code value. This method always returns a positive value.
	 *
	 * @param array
	 *            The {@link Object} array
	 * @param size
	 *            The size of the {@link Object} array
	 * @return a hash code value greater than or equal to zero.
	 */
	public static final int hashCode(final Object[] array, final int size) {
		if (array == null) {
			return 0;
		}

		int h = size;
		for (int i = 0; i < size; i++) {
			h <<= 1;

			if (array[i] != null) {
				h ^= array[i].hashCode();
			}
		}

		return h;
	}

	/**
	 * Safely extract an <code>int</code> value from a {@link Number} object instance. If the {@link Number} instance is <code>null</code>, zero is
	 * returned.
	 *
	 * @param n
	 *            The {@link Number} object instance.
	 * @return an <code>int</code> value, or zero if <code>null</code>.
	 */
	public static final int intValue(final Number n) {
		return n == null ? 0 : n.intValue();
	}

	/**
	 * TODO: Document!
	 *
	 * @param obj
	 * @return
	 */
	public static final boolean isArray(final Object obj) {
		return obj != null && obj.getClass().isArray();
	}

	// <><><><><><><><><><><><><>< Private Methods <><><><><><><><><><><><><><>

	/**
	 * Returns <code>true</code> if the specified string is either <code>null</code> or its length is equal to zero.
	 *
	 * @param value
	 *            The string to evaluate.
	 * @return <code>true</code> if the string is empty.
	 */
	public static final boolean isEmpty(final String value) {
		return value == null || value.length() == 0;
	}

	/**
	 * Returns {@code true} if the specified integer is a prime number.
	 *
	 * @param number
	 *            The {@code int} to evaluate.
	 * @return {@code true} if the string is empty.
	 */
	public static boolean isPrime(final int number) {
		if ((number & 0x01) == 0) {
			return false;
		}

		final int half = number / 2;

		for (int i = 3; i <= half; i += 2) {
			if (number % i == 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns the largest index where the specified fragment is located within the <code>char[]</code>, or -1 if not found.
	 *
	 * @param c
	 *            The <code>char[]</code> to search.
	 * @param fgmt
	 *            The fragment to look for.
	 * @return the largest index where the fragment is located, -1 if not found.
	 */
	public static final int lastIndexOf(final char[] c, final char... fgmt) {
		// TODO: Pass in the length of char[] c
		// TODO: Make one of these to just find a single character so that I
		// don't have to slow down this implementation that expects the char
		// fragment length > 1
		if (c != null && fgmt != null && c.length >= fgmt.length) {
			final int end = fgmt.length - 2;
			final char fgmtLastChar = fgmt[end + 1];
			int j, k;

			search: for (int i = c.length - 1; i > end;) {
				if (c[i--] == fgmtLastChar) {
					final int l = i - end;
					for (j = i, k = end; j >= l;) {
						if (c[j--] != fgmt[k--]) {
							continue search;
						}
					}

					return l;
				}
			}
		}

		return -1;
	}

	/**
	 * Returns the largest index where the specified fragment is located within the {@link String}, or -1 if not found.
	 *
	 * @param s
	 *            The {@link String} to search.
	 * @param fgmt
	 *            The fragment to look for.
	 * @return the largest index where the fragment is located, -1 if not found.
	 */
	public static final int lastIndexOf(final String s, final String fgmt) {
		return s == null ? -1 : s.lastIndexOf(fgmt);
	}

	/**
	 * Safely returns the {@link String} length (zero if <code>null</code>).
	 *
	 * @param s
	 *            The {@link String}
	 * @return the {@link String} length, or zero if <code>null</code>.
	 */
	public static final int length(final String s) {
		return s == null ? 0 : s.length();
	}

	/**
	 * Safely calculates and returns the aggregate length of all {@link String} instances in the array.
	 *
	 * @param strs
	 *            the {@link String} array
	 * @return the aggregate length of the {@link String} array.
	 */
	public static final int length(final String... strs) {
		int len = 0;

		if (strs != null) {
			for (final String s : strs) {
				if (s != null) {
					len += s.length();
				}
			}
		}

		return len;
	}

	/**
	 * Safely extract a <code>long</code> value from a {@link Number} object instance. If the {@link Number} instance is <code>null</code>, zero is
	 * returned.
	 *
	 * @param n
	 *            The {@link Number} object instance.
	 * @return a <code>long</code> value, or zero if <code>null</code>.
	 */
	public static final long longValue(final Number n) {
		return n == null ? 0 : n.longValue();
	}

	/**
	 * Returns the maximum <code>int</code> value from an <code>int[]</code> of values.
	 *
	 * @param ints
	 *            The <code>int[]</code> of values.
	 * @return the maximum <code>int</code> value.
	 */
	public static final int max(final int... ints) {
		int max = Integer.MIN_VALUE;

		for (final int i : ints) {
			if (i > max) {
				max = i;
			}
		}

		return max;
	}

	/**
	 * Returns the maximum <code>int</code> value between the two parameters.
	 *
	 * @param a
	 *            The first <code>int</code>.
	 * @param b
	 *            The second <code>int</code>.
	 * @return the maximum <code>int</code> value.
	 */
	public static final int max(final int a, final int b) {
		return a > b ? a : b;
	}

	/**
	 * Ensures the length of a {@link String} does not exceed the maximum length value provided as input. A substring is returned equal to the max
	 * length if the {@link String} length is greater than max length.
	 *
	 * @param str
	 *            The {@link String} to evaluate.
	 * @param maxLen
	 *            The maximum length of the {@link String}.
	 * @return A substring(0, maxLen) if {@link String} length > maxLen
	 */
	public static final String maxLength(final String str, final int maxLen) {
		return str != null && str.length() > maxLen ? str.substring(0, maxLen) : str;
	}

	/**
	 * Returns the minimum <code>int</code> value from an <code>int[]</code> of values.
	 *
	 * @param ints
	 *            The <code>int[]</code> of values.
	 * @return the minimum <code>int</code> value.
	 */
	public static final int min(final int... ints) {
		int min = ints[0] < ints[1] ? ints[0] : ints[1];

		for (int i = 2; i < ints.length; i++) {
			if (ints[i] < min) {
				min = ints[i];
			}
		}

		return min;
	}

	/**
	 * Returns the minimum <code>int</code> value between the two parameters.
	 *
	 * @param a
	 *            The first <code>int</code>.
	 * @param b
	 *            The second <code>int</code>.
	 * @return the minimum <code>int</code> value.
	 */
	public static final int min(final int a, final int b) {
		return a < b ? a : b;
	}

	/**
	 * Returns a generic type array defined by the generic type parameter {@code T}.
	 *
	 * @param size
	 *            the size of the generic type array
	 * @returns generic type array defined by the generic type parameter {@code T}
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T[] newArray(final int size) {
		return (T[]) new Object[size];
	}

	/**
	 * Returns the generic type array parameter if its {@code length} is greater than or equal to the requested {@code size}. Otherwise, a new generic
	 * type array is reflectively created and returned with {@code length} equal to {@code size}.
	 *
	 * @param array
	 *            The generic type array
	 * @param size
	 *            The requested size
	 * @return a generic type array of the requested size
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T[] newArray(final T[] array, final int size) {
		if (array.length < size) {
			return (T[]) Array.newInstance(array.getClass().getComponentType(), size);
		} else if (array.length > size) {
			// Null out the rest of the array if array.length > size
			for (int i = size; i < array.length; i++) {
				array[i] = null;
			}
		}

		return array;
	}

	/**
	 * Returns a new {@link String} object that reuses the {@code charArray} for fastest object creation.
	 *
	 * @param charArray
	 *            the {@code char[]} to reuse
	 * @return a new {@link String} object that reuses the {@code charArray}
	 */
	public static final String newInstance(final char[] charArray) {
		return fastNewString.newInstance(charArray, Boolean.TRUE);
	}

	/**
	 * Returns a new instance of {@code T} represented by the {@link Class} object. The instance is reflectively created using its default
	 * constructor.
	 *
	 * @param clazz
	 *            the {@link Class} object of the {@code T} instance to create
	 * @return a new instance of {@code T}
	 */
	public static final <T> T newInstance(final Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns a new instance of the class represented by its binary name in the {@code className} parameter. The specified {@link ClassLoader} is
	 * used to load the class specified by {@code className}. The instance is reflectively created using its default constructor.
	 *
	 * <h4>Binary names</h4>
	 * <p>
	 * Any class name provided as a {@link String} parameter to methods in <tt>ClassLoader</tt> must be a binary name as defined by <cite>The
	 * Java&trade; Language Specification</cite>.
	 * <p>
	 * Examples of valid class names include:
	 *
	 * <blockquote>
	 *
	 * <pre>
	 *   "java.lang.String"
	 *   "javax.swing.JSpinner$DefaultEditor"
	 *   "java.security.KeyStore$Builder$FileBuilder$1"
	 *   "java.net.URLClassLoader$3$1"
	 * </pre>
	 *
	 * </blockquote>
	 *
	 * @param classLoader
	 *            the {@link ClassLoader} to use to load the class
	 * @param className
	 *            the binary name of the class to instantiate
	 * @return a new instance of the class represented by its binary name in the {@code className} parameter
	 */
	public static final Object newInstance(final ClassLoader classLoader, final String className) {
		try {
			final Class<?> clazz = classLoader.loadClass(className);

			return clazz.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns <code>true</code> if the <code>char[]</code> is not <code>null</code> and its length is greater than zero.
	 *
	 * @param c
	 *            The char[] to check for not empty condition.
	 * @return <code>true</code> if char[] is not empty, <code>false</code> otherwise.
	 */
	public static final boolean notEmpty(final char[] c) {
		return c != null && c.length > 0;
	}

	/**
	 * Returns <code>true</code> if the {@link CharSequence} is not <code>null</code> and its length is greater than zero.
	 *
	 * @param s
	 *            The {@link CharSequence} to check for not empty condition.
	 * @return <code>true</code> if {@link CharSequence} is not empty, <code>false</code> otherwise.
	 */
	public static final boolean notEmpty(final CharSequence chSeq) {
		return chSeq != null && chSeq.length() > 0;
	}

	/**
	 * Returns {@code true} if the two specified arrays of chars are <i>not equal</i> to one another. Two arrays are not equal if they do not contain
	 * the same elements in the same order, or are not the same length, or one array reference is {@code null} while the other is not.
	 *
	 * @param o
	 *            the first {@code char[]} to compare for inequality
	 * @param p
	 *            the second {@code char[]} to compare for inequality
	 * @return {@code true} if the two arrays are not equal
	 */
	public static final boolean notEqual(final char[] o, final char[] p) {
		if (o != p) {
			if (o == null || p == null || o.length != p.length) {
				return true;
			}

			for (int i = 0; i < o.length; i++) {
				if (o[i] != p[i]) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Safely determines whether the two objects are <i>not equal</i> to one another according to the semantics of <code>Object.equals()</code> while
	 * gracefully handling <code>null</code> reference values.
	 *
	 * @param o
	 *            the first object to compare for inequality.
	 * @param p
	 *            the second object to company for inequality.
	 * @return <code>true</code> if <code>!o.equals(p)</code>; <code>false</code> otherwise.
	 */
	public static final boolean notEqual(final Object o, final Object p) {
		return o != p && (o == null || !o.equals(p));
	}

	/**
	 * Returns <code>true</code> if the <code>int</code> is an odd number, <code>false</code> if it is even.
	 *
	 * @param i
	 *            The <code>int</code> value.
	 * @return <code>true</code> if the <code>int</code> is an odd number.
	 */
	public static final boolean odd(final int i) {
		return (i & 1) == 1;
	}

	/**
	 * Returns the percentage difference between <code>a</code> and <code>b</code>.
	 * <p>
	 * The numerator (a) <b><i>must</i></b> be greater than the denominator (b).
	 *
	 * @param a
	 *            the numerator
	 * @param b
	 *            the denominator
	 * @return the percentage difference between <code>a</code> and <code>b</code>
	 */
	public static final double percentDiff(final double a, final double b) {
		return a / b - 1;
	}

	/**
	 * Generates a JDK proxy instance to the interface class backed by the provided invocation handler. Very useful for aspect-oriented programming.
	 *
	 * @param clazz
	 *            the interface to proxy
	 * @param h
	 *            the proxy invocation handler
	 * @return a new proxy instance of the interface backed by the invocation handler
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T proxy(final Class<T> clazz, final InvocationHandler h) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, h);
	}

	/**
	 * Safely extract a <code>short</code> value from a {@link Number} object instance. If the {@link Number} instance is <code>null</code>, zero is
	 * returned.
	 *
	 * @param n
	 *            The {@link Number} object instance.
	 * @return a <code>short</code> value, or zero if <code>null</code>.
	 */
	public static final short shortValue(final Number n) {
		return (short) (n == null ? 0 : n.intValue());
	}

	/**
	 * Returns a {@code char[][]} of {@link String} segments split by {@code delimiter}.
	 *
	 * @param string
	 *            the {@link String} to split
	 * @param delimiter
	 *            the {@code char} to split by
	 * @return a {@code char[][]} of {@link String} segments split by {@code delimiter}
	 */
	@Todo("This needs to use Substring once I put the Substring type into Java")
	public static char[][] split(final String string, final char delimiter) {
		final CollectorCharArray splitCollector = new CollectorCharArray();
		final char[] stringCharArray = strValue.getField(string);
		char[] splitCharArray;
		int begin = 0, end = 0;

		for (; end < stringCharArray.length; end++) {
			if (stringCharArray[end] == delimiter) {
				splitCharArray = new char[end - begin];
				System.arraycopy(stringCharArray, begin, splitCharArray, 0, splitCharArray.length);
				splitCollector.add(splitCharArray);
				begin = end + 1;
			}
		}

		// Return the entire char[] of the String if no splits detected
		if (splitCollector.isEmpty()) {
			return new char[][] { stringCharArray };
		}

		// Add the last split segment
		splitCharArray = new char[end - begin];
		System.arraycopy(stringCharArray, begin, splitCharArray, 0, splitCharArray.length);
		splitCollector.add(splitCharArray);

		return splitCollector.toArray();
	}

	/**
	 * Safely calls <code>startsWith(String prefix)</code> on a {@link String} instance. Returns <code>false</code> if the {@link String} reference is
	 * <code>null</code>.
	 *
	 * @param s
	 *            The {@link String} instance.
	 * @param prefix
	 *            The prefix.
	 * @return <code>true</code> if the {@link String} starts with the prefix.
	 */
	public static final boolean startsWith(final String s, final String prefix) {
		return s != null && s.startsWith(prefix);
	}

	/**
	 * Safely returns a substring of the {@link String} parameter starting at <code>beginIndex</code>.
	 *
	 * @param s
	 *            The {@link String} to substring.
	 * @param beginIndex
	 *            The beginning index.
	 * @return The specified substring, or <code>null</code>.
	 */
	public static final String substring(final String s, final int beginIndex) {
		return s == null ? null : s.substring(beginIndex);
	}

	/**
	 * Safely returns a substring of the {@link String} parameter starting at <code>beginIndex</code> and ends at <code>endIndex - 1</code>.
	 *
	 * @param s
	 *            The {@link String} to substring.
	 * @param beginIndex
	 *            The beginning index.
	 * @param endIndex
	 *            The ending index.
	 * @return The specified substring, or <code>null</code>.
	 */
	public static final String substring(final String s, final int beginIndex, final int endIndex) {
		return s == null ? null : s.substring(beginIndex, endIndex);
	}

	/**
	 * Returns the reference to the actual <code>char[]</code> that is encapsulated within the {@link String}.
	 *
	 * @param str
	 *            The {@link String} to get the <code>char[]</code> from
	 * @return the reference to the actual <code>char[]</code> (<b>not a copy</b>)
	 */
	public static final char[] toCharArray(final String str) {
		return strValue.getField(str);
	}

	/**
	 * Defaults <code>null</code> object references to the string value "null" (without quotes) while calling <code>Object.toString()</code> on
	 * non-null references.
	 *
	 * @param obj
	 *            the object to represent as a string.
	 * @return a string representation of the object.
	 */
	public static final String toString(final Object obj) {
		return obj == null ? Constants.NULL_STRING : obj.toString();
	}

	/**
	 * Generates a string representation of an object array. If an array contains elements 'A', 'B', and 'C' then the generated string will be
	 * "[A,B,C]".
	 *
	 * @param objs
	 *            the array to represent as a string.
	 * @return a string representation of the array.
	 */
	public static final String toString(final Object[] objs) {
		// TODO: How does this match up with Array.join() or whatever? And how
		// about an Extractable version (doesn't make sense on single
		// Extractable instance)?
		if (objs == null || objs.length == 0) {
			return "[]";
		}

		// 1. Calculate the string length
		int strLen = objs.length + 1;
		final String[] strs = new String[objs.length];
		for (int i = 0; i < objs.length; i++) {
			strs[i] = objs[i] == null ? Constants.NULL_STRING : objs[i].toString();
			strLen += strs[i].length();
		}

		// 2. Create the character array
		final char[] c = new char[strLen];

		// 3. Get characters from first array element
		c[0] = '[';
		strLen = strs[0].length();
		strs[0].getChars(0, strLen, c, 1);

		// 4. Append characters from remaining elements
		int j = strLen + 1;
		for (int i = 1; i < objs.length; i++) {
			c[j++] = ',';
			strLen = strs[i].length();
			strs[i].getChars(0, strLen, c, j);
			j += strLen;
		}
		c[j] = ']';

		return new String(c);
	}

	/**
	 * TODO: Document!
	 *
	 * @param objs
	 * @param len
	 * @return
	 */
	public static final String toString(final Object[] objs, final int len) {
		// TODO: How does this match up with Array.join() or whatever? And how
		// about an Extractable version (doesn't make sense on single
		// Extractable instance)?
		if (objs == null || len == 0) {
			return "[]";
		}

		int strLen = len + 1;
		final String[] strs = new String[len];
		for (int i = 0; i < len; i++) {
			strs[i] = objs[i] == null ? Constants.NULL_STRING : objs[i].toString();
			strLen += strs[i].length();
		}

		final char[] c = new char[strLen];
		strLen = strs[0].length();

		c[0] = '[';
		strs[0].getChars(0, strLen, c, 1);
		int j = strLen + 1;
		for (int i = 1; i < len; i++) {
			c[j++] = ',';
			strLen = strs[i].length();
			strs[i].getChars(0, strLen, c, j);
			j += strLen;
		}
		c[j] = ']';

		return new String(c);
	}

	/**
	 * Safely trims the {@link String} value by checking for a <code>null</code> reference before trimming.
	 *
	 * @param value
	 *            the string to trim.
	 * @return the trimmed string.
	 */
	public static final String trim(final String value) {
		return value == null ? null : value.trim();
	}

	/**
	 * This is needed because casting a negative <code>byte</code> value to an <code>int</code> results in the same negative value.
	 *
	 * @param b
	 * @return
	 */
	public static final int unsignedInt(final byte b) {
		// TODO: unsignedInt(short), unsignedLong(int), what else?
		return b & 0xff;
	}

	/**
	 * Returns the string array representation of the object array argument.<br>
	 * TODO: Change this to Safe.toString(Object[] objArray)
	 *
	 * @param objs
	 *            the object array.
	 * @return the string array representation of the object array.
	 */
	public static final String[] valueOf(final Object[] objs) {
		final String[] s = new String[objs.length];

		for (int i = 0; i < objs.length; i++) {
			s[i] = objs[i] == null ? Constants.NULL_STRING : objs[i] instanceof Object[] ? toString((Object[]) objs[i]) : objs[i].toString();
		}

		return s;
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private static final int intFormatSize(final int i) {
		if (i < 10) {
			return 1; // 9
		}
		if (i < 100) {
			return 2; // 99
		}
		if (i < 1000) {
			return 3; // 999
		}
		if (i < 10000) {
			return 4; // 9999
		}
		if (i < 100000) {
			return 5; // 99999
		}
		if (i < 1000000) {
			return 6; // 999999
		}
		if (i < 10000000) {
			return 7; // 9999999
		}

		return 8; // 99999999
	}

	private static final int stringSize(final int i) {
		if (i < 0) {
			if (i > -10) {
				return 2;
			}
			if (i > -100) {
				return 3;
			}
			if (i > -1000) {
				return 4;
			}
			if (i > -10000) {
				return 5;
			}
			if (i > -100000) {
				return 6;
			}
			if (i > -1000000) {
				return 7;
			}
			if (i > -10000000) {
				return 8;
			}
			if (i > -100000000) {
				return 9;
			}
			if (i > -1000000000) {
				return 10;
			}

			return 11;
		}

		if (i < 10) {
			return 1;
		}
		if (i < 100) {
			return 2;
		}
		if (i < 1000) {
			return 3;
		}
		if (i < 10000) {
			return 4;
		}
		if (i < 100000) {
			return 5;
		}
		if (i < 1000000) {
			return 6;
		}
		if (i < 10000000) {
			return 7;
		}
		if (i < 100000000) {
			return 8;
		}
		if (i < 1000000000) {
			return 9;
		}

		return 10;
	}

} // End Root
