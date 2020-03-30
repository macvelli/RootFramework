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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import root.util.Root;
import sun.misc.Unsafe;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <C>
 *            The class type of the {@link Object}
 * @param <F>
 *            The field type
 */
public final class StaticObjectField<C, F> {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Unsafe unsafe = Root.getUnsafe();

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final long fieldOffset;
	private final Object objectBase;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public StaticObjectField(final Class<C> clazz, final String fieldName) {
		try {
			final Field f = clazz.getDeclaredField(fieldName);

			if (!Modifier.isStatic(f.getModifiers())) {
				throw new RuntimeException("StaticObjectField does not support instance fields");
			}

			this.fieldOffset = unsafe.staticFieldOffset(f);
			this.objectBase = unsafe.staticFieldBase(f);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final F getField() {
		return Root.cast(unsafe.getObject(this.objectBase, this.fieldOffset));
	}

	public final void setField(final F value) {
		unsafe.putObject(this.objectBase, this.fieldOffset, value);
	}

} // End StaticObjectField
