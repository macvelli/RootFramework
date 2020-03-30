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
package root.lang.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Takes a {@link Class} and prints out its declared fields, constructors, and methods. This is a really good tool for seeing "hidden" things declared
 * in a class that don't show up in its JavaDoc.
 * <p>
 * Usage:
 *
 * <pre>
 * <code>ClassInfo.printDeclarations(String.class);</code>
 * </pre>
 *
 * TODO: Figure out how to XML the output so an XSLT can show it in a browser
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ClassInfo {

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	public static void printConstructors(final Class<?> clazz) {
		final Constructor<?>[] constructors = clazz.getDeclaredConstructors();

		System.out.println(clazz.getName() + " Constructors");

		final int numDashes = clazz.getName().length() + 13;
		for (int i = 0; i < numDashes; i++) {
			System.out.print('-');
		}
		System.out.println();

		for (final Constructor<?> c : constructors) {
			System.out.println(c);
		}
	}

	public static void printDeclarations(final Class<?> clazz) {
		ClassInfo.printFields(clazz);

		System.out.println();

		ClassInfo.printConstructors(clazz);

		System.out.println();

		ClassInfo.printMethods(clazz);
	}

	public static void printFields(final Class<?> clazz) {
		final Field[] fields = clazz.getDeclaredFields();

		System.out.println(clazz.getName() + " Fields");

		final int numDashes = clazz.getName().length() + 7;
		for (int i = 0; i < numDashes; i++) {
			System.out.print('-');
		}
		System.out.println();

		for (final Field f : fields) {
			System.out.println(f);
		}
	}

	public static void printMethods(final Class<?> clazz) {
		final Method[] methods = clazz.getDeclaredMethods();

		System.out.println(clazz.getName() + " Methods");

		final int numDashes = clazz.getName().length() + 8;
		for (int i = 0; i < numDashes; i++) {
			System.out.print('-');
		}
		System.out.println();

		for (final Method m : methods) {
			System.out.println(m);
		}
	}

} // End ClassInfo
