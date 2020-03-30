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
 * @param <T>
 *            The type having the <code>char[]</code> field
 */
public class CharArrayField<T> {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Unsafe unsafe = Root.getUnsafe();

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final long fieldOffset;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public CharArrayField(final Class<T> clazz, final String fieldName) {
		try {
			final Field field = clazz.getDeclaredField(fieldName);

			if (Modifier.isStatic(field.getModifiers())) {
				throw new IllegalArgumentException("Use StaticCharArrayField for static char[] fields");
			}

			this.fieldOffset = CharArrayField.unsafe.objectFieldOffset(field);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final char[] getField(final T obj) {
		return (char[]) unsafe.getObject(obj, this.fieldOffset);
	}

	public final void setField(final T obj, final char[] value) {
		unsafe.putObject(obj, this.fieldOffset, value);
	}

} // End CharArrayField
