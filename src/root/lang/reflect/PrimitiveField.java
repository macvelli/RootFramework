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
 */
public final class PrimitiveField<C> {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Unsafe unsafe = Root.getUnsafe();

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final long fieldOffset;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public PrimitiveField(final Class<C> clazz, final String fieldName) {
		try {
			final Field f = clazz.getDeclaredField(fieldName);

			if (Modifier.isStatic(f.getModifiers())) {
				throw new RuntimeException("PrimitiveField does not support static fields");
			}

			this.fieldOffset = unsafe.objectFieldOffset(f);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final boolean getBoolean(final C c) {
		return unsafe.getBoolean(c, this.fieldOffset);
	}

	public final byte getByte(final C c) {
		return unsafe.getByte(c, this.fieldOffset);
	}

	public final char getChar(final C c) {
		return unsafe.getChar(c, this.fieldOffset);
	}

	public final double getDouble(final C c) {
		return unsafe.getDouble(c, this.fieldOffset);
	}

	public final float getFloat(final C c) {
		return unsafe.getFloat(c, this.fieldOffset);
	}

	public final int getInt(final C c) {
		return unsafe.getInt(c, this.fieldOffset);
	}

	public final long getLong(final C c) {
		return unsafe.getLong(c, this.fieldOffset);
	}

	public final short getShort(final C c) {
		return unsafe.getShort(c, this.fieldOffset);
	}

	public final void setBoolean(final C c, final boolean value) {
		unsafe.putBoolean(c, this.fieldOffset, value);
	}

	public final void setByte(final C c, final byte value) {
		unsafe.putByte(c, this.fieldOffset, value);
	}

	public final void setChar(final C c, final char value) {
		unsafe.putChar(c, this.fieldOffset, value);
	}

	public final void setDouble(final C c, final double value) {
		unsafe.putDouble(c, this.fieldOffset, value);
	}

	public final void setFloat(final C c, final float value) {
		unsafe.putFloat(c, this.fieldOffset, value);
	}

	public final void setInt(final C c, final int value) {
		unsafe.putInt(c, this.fieldOffset, value);
	}

	public final void setLong(final C c, final long value) {
		unsafe.putLong(c, this.fieldOffset, value);
	}

	public final void setShort(final C c, final short value) {
		unsafe.putShort(c, this.fieldOffset, value);
	}

} // End PrimitiveField
