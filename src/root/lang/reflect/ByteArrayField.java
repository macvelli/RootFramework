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
 *            The type having the <code>byte[]</code> field
 */
public final class ByteArrayField<T> {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Unsafe unsafe = Root.getUnsafe();

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final long fieldOffset;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public ByteArrayField(final Class<T> clazz, final String fieldName) {
		try {
			final Field field = clazz.getDeclaredField(fieldName);

			if (Modifier.isStatic(field.getModifiers())) {
				throw new IllegalArgumentException("Use StaticByteArrayField for static byte[] fields");
			}

			this.fieldOffset = ByteArrayField.unsafe.objectFieldOffset(field);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final byte[] getField(final T obj) {
		return (byte[]) unsafe.getObject(obj, this.fieldOffset);
	}

	public final void setField(final T obj, final byte[] value) {
		unsafe.putObject(obj, this.fieldOffset, value);
	}

} // End ByteArrayField
