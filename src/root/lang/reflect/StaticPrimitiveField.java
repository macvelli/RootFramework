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
public final class StaticPrimitiveField<C> {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Unsafe unsafe = Root.getUnsafe();

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final long fieldOffset;
	private final Object objectBase;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public StaticPrimitiveField(final Class<C> clazz, final String fieldName) {
		try {
			final Field f = clazz.getDeclaredField(fieldName);

			if (!Modifier.isStatic(f.getModifiers())) {
				throw new RuntimeException("StaticPrimitiveField does not support instance fields");
			}

			this.fieldOffset = unsafe.staticFieldOffset(f);
			this.objectBase = unsafe.staticFieldBase(f);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final boolean getBoolean() {
		return unsafe.getBoolean(this.objectBase, this.fieldOffset);
	}

	public final byte getByte() {
		return unsafe.getByte(this.objectBase, this.fieldOffset);
	}

	public final char getChar() {
		return unsafe.getChar(this.objectBase, this.fieldOffset);
	}

	public final double getDouble() {
		return unsafe.getDouble(this.objectBase, this.fieldOffset);
	}

	public final float getFloat() {
		return unsafe.getFloat(this.objectBase, this.fieldOffset);
	}

	public final int getInt() {
		return unsafe.getInt(this.objectBase, this.fieldOffset);
	}

	public final long getLong() {
		return unsafe.getLong(this.objectBase, this.fieldOffset);
	}

	public final short getShort() {
		return unsafe.getShort(this.objectBase, this.fieldOffset);
	}

	public final void setBoolean(final boolean value) {
		unsafe.putBoolean(this.objectBase, this.fieldOffset, value);
	}

	public final void setByte(final byte value) {
		unsafe.putByte(this.objectBase, this.fieldOffset, value);
	}

	public final void setChar(final char value) {
		unsafe.putChar(this.objectBase, this.fieldOffset, value);
	}

	public final void setDouble(final double value) {
		unsafe.putDouble(this.objectBase, this.fieldOffset, value);
	}

	public final void setFloat(final float value) {
		unsafe.putFloat(this.objectBase, this.fieldOffset, value);
	}

	public final void setInt(final int value) {
		unsafe.putInt(this.objectBase, this.fieldOffset, value);
	}

	public final void setLong(final long value) {
		unsafe.putLong(this.objectBase, this.fieldOffset, value);
	}

	public final void setShort(final short value) {
		unsafe.putShort(this.objectBase, this.fieldOffset, value);
	}

} // End StaticPrimitiveField
