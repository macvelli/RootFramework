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

import root.lang.Extractable;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Parameters {

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	/**
	 * Allows variable {@code boolean} arguments to be used for any method parameter, though it would be pointless to use this for the last method
	 * parameter that is already defined as a vararg.
	 *
	 * @param args
	 *            The variable number of {@code boolean} values
	 * @return a {@code boolean} array containing the values
	 */
	public static final boolean[] toArray(final boolean... args) {
		return args;
	}

	/**
	 * Allows variable {@code char} arguments to be used for any method parameter, though it would be pointless to use this for the last method
	 * parameter that is already defined as a vararg.
	 *
	 * @param args
	 *            The variable number of {@code char} values
	 * @return a {@code char[]} array containing the values
	 */
	public static final char[] toArray(final char... args) {
		return args;
	}

	/**
	 * Allows variable {@link Extractable} arguments to be used for any method parameter, though it would be pointless to use this for the last method
	 * parameter that is already defined as a vararg.
	 *
	 * @param args
	 *            The variable number of {@link Extractable} values
	 * @return an {@code Extractable[]} array containing the values
	 */
	public static final Extractable[] toArray(final Extractable... extractableArray) {
		return extractableArray;
	}

	/**
	 * Allows variable {@code int} arguments to be used for any method parameter, though it would be pointless to use this for the last method
	 * parameter that is already defined as a vararg.
	 *
	 * @param args
	 *            The variable number of {@code int} values
	 * @return an {@code int[]} array containing the values
	 */
	public static final int[] toArray(final int... args) {
		return args;
	}

	/**
	 * Allows variable arguments to be used for any method parameter, though it would be pointless to use this for the last method parameter that is
	 * already defined as a vararg.
	 *
	 * @param args
	 *            The variable number of {@link Object} values
	 * @return an {@code Object[]} containing the values
	 */
	public static final Object[] toArray(final Object... args) {
		return args;
	}

} // End Parameters
