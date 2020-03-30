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
import java.lang.reflect.InvocationTargetException;

import root.util.Root;
import sun.misc.Unsafe;
import sun.reflect.ConstructorAccessor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type with the constructor to access reflectively
 */
public class ObjectConstructor<T> {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Unsafe unsafe = Root.getUnsafe();

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final ConstructorAccessor constructorAccessor;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public ObjectConstructor(final Class<T> clazz, final Class<?>... parameterTypes) {

		try {
			final Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
			final Field field = constructor.getClass().getDeclaredField("constructorAccessor");
			constructor.setAccessible(true);

			try {
				constructor.newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// Have to call newInstance() once in order to lazy load the ConstructorAccessor
			}

			final long fieldOffset = ObjectConstructor.unsafe.objectFieldOffset(field);
			this.constructorAccessor = (ConstructorAccessor) unsafe.getObject(constructor, fieldOffset);
		} catch (NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@SuppressWarnings("unchecked")
	public final T newInstance(final Object... args) {
		try {
			return (T) this.constructorAccessor.newInstance(args);
		} catch (InstantiationException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

} // End ObjectConstructor
