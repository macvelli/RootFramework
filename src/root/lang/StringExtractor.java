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

import java.util.Iterator;

import root.annotation.Todo;
import root.util.Root;

/**
 * A {@link StringExtractor} replaces both {@link StringBuffer} and {@link StringBuilder} as it works directly with {@link Extractable} objects. See
 * {@link Extractable} for why <b>you want to do this</b>.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Todo({ "Need Extractable implementations for double, float, and long",
		"At least for char[] arrays, System.arraycopy() is much slower at small array lengths than just copying the damn array elements in Java" })
public class StringExtractor implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	/** The number of characters in this {@link Extractor} */
	int size;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/** The array of characters held within this {@link Extractor} */
	char[] chars;

	public StringExtractor() {
		this.chars = new char[64];
	}

	public StringExtractor(final boolean lazyInit) {
		// Nothing to do
	}

	public StringExtractor(final CharSequence chSeq) {
		this.size = chSeq.length();
		this.chars = new char[this.size + 64];

		for (int i = 0; i < this.size; i++) {
			this.chars[i] = chSeq.charAt(i);
		}
	}

	public StringExtractor(final int capacity) {
		this.chars = new char[capacity + 64];
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public StringExtractor(final String str) {
		this.size = str.length();
		this.chars = new char[this.size + 64];

		str.getChars(0, this.size, this.chars, 0);
	}

	public final StringExtractor addSeparator() {
		if (this.size == this.chars.length) {
			this.resize(this.size << 1);
		}

		this.chars[this.size++] = ',';

		return this;
	}

	public final StringExtractor addSeparator(final char ch) {
		if (this.size == this.chars.length) {
			this.resize(this.size << 1);
		}

		this.chars[this.size++] = ch;

		return this;
	}

	public final StringExtractor append(final boolean b) {
		return b ? this.append(Constants.trueCharArray, 0, 4) : this.append(Constants.falseCharArray, 0, 5);
	}

	public final StringExtractor append(final boolean condition, final char ch) {
		if (condition) {
			if (this.size == this.chars.length) {
				this.resize(this.size << 1);
			}

			this.chars[this.size++] = ch;
		}

		return this;
	}

	public final StringExtractor append(final char ch) {
		if (this.size == this.chars.length) {
			this.resize(this.size << 1);
		}

		this.chars[this.size++] = ch;
		return this;
	}

	public final StringExtractor append(final char[] charArray) {
		return this.append(charArray, 0, charArray.length);
	}

	public final StringExtractor append(final char[] charArray, final int offset, final int length) {
		if (this.chars == null) {
			if (offset == 0) {
				this.chars = charArray;
				this.size = length;
			} else {
				this.chars = new char[length - offset];
				System.arraycopy(charArray, offset, this.chars, 0, length);
			}
		} else {
			final int newSize = this.size + length;

			if (newSize > this.chars.length) {
				this.resize(newSize + (newSize >> 1));
			}

			System.arraycopy(charArray, offset, this.chars, this.size, length);
			this.size = newSize;
		}

		return this;
	}

	public final StringExtractor append(final CharSequence csq) {
		return this.append(csq, 0, csq.length());
	}

	public final StringExtractor append(final CharSequence csq, final int start, final int end) {
		final int newSize = this.size + end - start;

		if (newSize > this.chars.length) {
			this.resize(newSize + (newSize >> 1));
		}

		for (int i = start, j = this.size; i < end; i++, j++) {
			this.chars[j] = csq.charAt(i);
		}

		this.size = newSize;

		return this;
	}

	@Todo("I know I can do better than this")
	public final StringExtractor append(final double d) {
		return this.append(Double.toString(d));
	}

	public final StringExtractor append(final Extractable ext) {
		if (ext == null) {
			return this.append(Constants.nullCharArray, 0, 4);
		}

		ext.extract(this);

		return this;
	}

	public final StringExtractor append(final Extractable[] extArray, final int offset, final int length) {
		if (extArray == null) {
			return this.append(Constants.nullCharArray, 0, 4);
		}

		if (length == 0) {
			return this.appendEmptyArray();
		}

		this.append('[');
		this.append(extArray[offset]);

		if (length > 1) {
			final int endpoint = offset + length;

			for (int i = offset + 1; i < endpoint; i++) {
				this.addSeparator();
				this.append(extArray[i]);
			}
		}

		return this.append(']');
	}

	@Todo("I know I can do better than this")
	public final StringExtractor append(final float f) {
		return this.append(Float.toString(f));
	}

	public final StringExtractor append(final int i) {
		Root.extract(this, i);
		return this;
	}

	public final StringExtractor append(final int i, final Formatter formatter) {
		formatter.format(i, this);
		return this;
	}

	public final StringExtractor append(final int[] intArray, final int offset, final int length) {
		if (intArray == null) {
			return this.append(Constants.nullCharArray, 0, 4);
		}

		if (length == 0) {
			return this.appendEmptyArray();
		}

		this.append('[');
		Root.extract(this, intArray[offset]);

		if (length > 1) {
			final int endpoint = offset + length;

			for (int i = offset + 1; i < endpoint; i++) {
				this.addSeparator();
				Root.extract(this, intArray[i]);
			}
		}

		return this.append(']');
	}

	public final StringExtractor append(final Iterable<?> iterable) {
		if (iterable == null) {
			return this.append(Constants.nullCharArray, 0, 4);
		}

		final Iterator<?> itr = iterable.iterator();

		if (!itr.hasNext()) {
			return this.appendEmptyArray();
		}

		this.append('[');
		this.append(Root.toString(itr.next()));

		while (itr.hasNext()) {
			this.addSeparator();
			this.append(Root.toString(itr.next()));
		}

		return this.append(']');
	}

	@Todo("I know I can do better than this")
	public final StringExtractor append(final long l) {
		return this.append(Long.toString(l));
	}

	public final StringExtractor append(final long l, final Formatter formatter) {
		formatter.format(l, this);
		return this;
	}

	@Todo("I know I can do better than this")
	public final StringExtractor append(final long[] longArray, final int offset, final int length) {
		if (longArray == null) {
			return this.append(Constants.nullCharArray, 0, 4);
		}

		if (length == 0) {
			return this.appendEmptyArray();
		}

		this.append('[');
		this.append(longArray[offset]);

		if (length > 1) {
			final int endpoint = offset + length;

			for (int i = offset + 1; i < endpoint; i++) {
				this.addSeparator();
				this.append(longArray[i]);
			}
		}

		return this.append(']');
	}

	public final StringExtractor append(final Object obj) {
		return this.append(Root.toString(obj));
	}

	public final StringExtractor append(final Object[] objArray, final int offset, final int length) {
		if (objArray == null) {
			return this.append(Constants.nullCharArray, 0, 4);
		}

		if (length == 0) {
			return this.appendEmptyArray();
		}

		this.append('[');
		this.append(objArray[offset]);

		if (length > 1) {
			final int endpoint = offset + length;

			for (int i = offset + 1; i < endpoint; i++) {
				this.addSeparator();
				this.append(Root.toString(objArray[i]));
			}
		}

		return this.append(']');
	}

	public final StringExtractor append(final ParamString paramStr, final Extractable... extArray) {
		paramStr.format(this, extArray);

		return this;
	}

	public final StringExtractor append(final ParamString paramStr, final Object... objArray) {
		paramStr.format(this, objArray);

		return this;
	}

	public final StringExtractor append(String str) {
		if (str == null) {
			str = Constants.NULL_STRING;
		}

		if (str.length() > 0) {
			final int newSize = this.size + str.length();

			if (newSize > this.chars.length) {
				this.resize(newSize + (newSize >> 1));
			}

			str.getChars(0, str.length(), this.chars, this.size);
			this.size = newSize;
		}

		return this;
	}

	public final StringExtractor append(final String message, final Extractable... extArray) {
		if (extArray.length == 0) {
			return this.append(message);
		} else {
			this.ensureCapacity(this.size + message.length() + extArray.length << 5);
			final char[] msgCharArray = Root.toCharArray(message);

			// Build the formatted message
			int i = 0, j = 0, srcPos = 0, segmentLength;
			while (true) {
				if (msgCharArray[i++] == '{' && msgCharArray[i] == 'P' && msgCharArray[++i] == '}') {
					segmentLength = i - srcPos - 2;

					if (segmentLength > 0) {
						this.append(msgCharArray, srcPos, segmentLength);
					}

					this.append(extArray[j++]);
					srcPos = ++i;

					if (j == extArray.length) {
						if (srcPos < msgCharArray.length) {
							this.append(msgCharArray, srcPos, msgCharArray.length - srcPos);
						}

						return this;
					}
				}
			}
		}
	}

	public final StringExtractor append(final String message, final Object... objArray) {
		if (objArray.length == 0) {
			return this.append(message);
		} else {
			// Convert all of the Objects into Strings and calculate the total String length
			final char[] msgCharArray = Root.toCharArray(message);
			final String[] strArray = new String[objArray.length];
			int newSize = this.size + message.length();

			for (int i = 0; i < objArray.length; i++) {
				strArray[i] = Root.toString(objArray[i]);
				newSize += strArray[i].length();
			}

			if (newSize > this.chars.length) {
				this.resize(newSize + (newSize >> 1));
			}

			String str;

			// Build the formatted message
			int i = 0, j = 0, srcPos = 0, segmentLength;
			while (true) {
				if (msgCharArray[i++] == '{' && msgCharArray[i] == 'P' && msgCharArray[++i] == '}') {
					segmentLength = i - srcPos - 2;

					if (segmentLength > 0) {
						System.arraycopy(msgCharArray, srcPos, this.chars, this.size, segmentLength);
						this.size += segmentLength;
					}

					str = strArray[j++];
					str.getChars(0, str.length(), this.chars, this.size);
					this.size += str.length();
					srcPos = ++i;

					if (j == strArray.length) {
						if (srcPos < msgCharArray.length) {
							System.arraycopy(msgCharArray, srcPos, this.chars, this.size, msgCharArray.length - srcPos);
						}

						return this;
					}
				}
			}
		}
	}

	public final StringExtractor append(final StringBuffer buffer) {
		final int newSize = this.size + buffer.length();

		if (newSize > this.chars.length) {
			this.resize(newSize + (newSize >> 1));
		}

		buffer.getChars(0, buffer.length(), this.chars, this.size);
		this.size = newSize;

		return this;
	}

	public final StringExtractor append(final StringBuilder builder) {
		final int newSize = this.size + builder.length();

		if (newSize > this.chars.length) {
			this.resize(newSize + (newSize >> 1));
		}

		builder.getChars(0, builder.length(), this.chars, this.size);
		this.size = newSize;

		return this;
	}

	/**
	 * Copies the {@code char[]} array in Java instead of using {@link System#arraycopy(Object, int, Object, int, int)}. This method should be used
	 * when the length of the {@code char[]} array is 16 or less.
	 *
	 * @param array
	 *            the {@code char[]} array to append
	 * @return this {@link StringExtractor}
	 */
	public final StringExtractor appendArray(final char[] array) {
		final int length = array.length;
		final int newSize = this.size + length;

		if (newSize > this.chars.length) {
			this.resize(newSize + (newSize >> 1));
		}

		// Do the copy in Java instead of calling System.arraycopy()
		for (int i = 0; i < length; i++) {
			this.chars[this.size++] = array[i];
		}

		return this;
	}

	public final char charAt(final int index) {
		return this.chars[index];
	}

	public final void clear() {
		this.size = 0;
	}

	public final StringExtractor delete(final int start, final int end) {
		final int len = end - start;

		if (len > 0) {
			System.arraycopy(this.chars, end, this.chars, start, this.size - end);
			this.size -= len;
		}

		return this;
	}

	public final StringExtractor deleteCharAt(final int index) {
		System.arraycopy(this.chars, index + 1, this.chars, index, this.size - index - 1);
		this.size--;
		return this;
	}

	public final void ensureCapacity(final int minCapacity) {
		if (minCapacity > this.chars.length) {
			this.resize(minCapacity + (minCapacity >> 1));
		}
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append(this.chars, 0, this.size);
	}

	public final int getCapacity() {
		return this.chars.length;
	}

	public final char[] getCharArray(final int numCharsToAdd) {
		if (numCharsToAdd > 0) {
			if (this.chars == null) {
				this.chars = new char[numCharsToAdd];
				this.size = numCharsToAdd;
			} else {
				final int newSize = this.size + numCharsToAdd;

				if (newSize > this.chars.length) {
					this.resize(newSize + (newSize >> 1));
				}

				this.size = newSize;
			}
		}

		return this.chars;
	}

	public final void getChars(final int srcStart, final int srcEnd, final char[] destCharArray, final int destStart) {
		System.arraycopy(this.chars, srcStart, destCharArray, destStart, srcEnd - srcStart);
	}

	public final int getLength() {
		return this.size;
	}

	public final StringExtractor insert(final int index, final boolean b) {
		return b ? this.insert(index, Constants.trueCharArray, 0, 4) : this.insert(index, Constants.falseCharArray, 0, 5);
	}

	public final StringExtractor insert(final int index, final char ch) {
		if (this.size == this.chars.length) {
			this.resize(this.size << 1);
		}

		System.arraycopy(this.chars, index, this.chars, index + 1, this.size - index);
		this.chars[index] = ch;
		this.size++;
		return this;
	}

	public final StringExtractor insert(final int index, final char[] charArray) {
		return this.insert(index, charArray, 0, charArray.length);
	}

	public final StringExtractor insert(final int index, final char[] charArray, final int offset, final int length) {
		final int newSize = this.size + length;

		if (newSize > this.chars.length) {
			this.resize(newSize + (newSize >> 1));
		}

		System.arraycopy(this.chars, index, this.chars, index + length, this.size - index);
		System.arraycopy(charArray, offset, this.chars, index, length);

		this.size = newSize;

		return this;
	}

	public final StringExtractor insert(final int index, final CharSequence csq) {
		return this.insert(index, csq, 0, csq.length());
	}

	public final StringExtractor insert(final int index, final CharSequence csq, final int start, final int end) {
		final int length = end - start;
		final int newSize = this.size + length;

		if (newSize > this.chars.length) {
			this.resize(newSize + (newSize >> 1));
		}

		System.arraycopy(this.chars, index, this.chars, index + length, this.size - index);

		for (int i = start, j = index; i < end; i++, j++) {
			this.chars[j] = csq.charAt(i);
		}

		this.size = newSize;

		return this;
	}

	@Todo("I know I can do better than this")
	public final StringExtractor insert(final int index, final double d) {
		return this.insert(index, Double.toString(d));
	}

	@Todo("What the hell is this? It doesn't take advantage of using an Extractable at all")
	public final StringExtractor insert(final int index, final Extractable ext) {
		if (ext == null) {
			return this.insert(index, Constants.nullCharArray, 0, 4);
		}

		return this.insert(index, ext.toString());
	}

	public final StringExtractor insert(final int index, final Extractable[] extArray, final int offset, final int length) {
		// Save off everything from index to current size and then set size equal to index
		final char[] savedChars = new char[this.size - index];
		System.arraycopy(this.chars, index, savedChars, 0, savedChars.length);
		this.size = index;

		if (extArray == null) {
			this.append(Constants.nullCharArray, 0, 4);
		} else {
			if (length == 0) {
				this.appendEmptyArray();
			} else {
				this.append('[');
				this.append(extArray[offset]);

				if (length > 1) {
					final int endpoint = offset + length;

					for (int i = offset + 1; i < endpoint; i++) {
						this.addSeparator();
						this.append(extArray[i]);
					}
				}

				this.append(']');
			}
		}

		// Append saved characters back onto this Extractor
		return this.append(savedChars, 0, savedChars.length);
	}

	@Todo("I know I can do better than this")
	public final StringExtractor insert(final int index, final float f) {
		return this.insert(index, Float.toString(f));
	}

	@Todo("I know I can do better than this")
	public final StringExtractor insert(final int index, final int i) {
		return this.insert(index, Integer.toString(i));
	}

	@Todo("I know I can do better than this")
	public final StringExtractor insert(final int index, final long l) {
		return this.insert(index, Long.toString(l));
	}

	public final StringExtractor insert(final int index, final Object obj) {
		if (obj == null) {
			return this.insert(index, Constants.nullCharArray, 0, 4);
		}

		return this.insert(index, obj.toString());
	}

	public final StringExtractor insert(final int index, final Object[] objArray, final int offset, final int length) {
		// Save off everything from index to current size and then set size equal to index
		final char[] savedChars = new char[this.size - index];
		System.arraycopy(this.chars, index, savedChars, 0, savedChars.length);
		this.size = index;

		if (objArray == null) {
			this.append(Constants.nullCharArray, 0, 4);
		} else {
			if (length == 0) {
				this.appendEmptyArray();
			} else {
				this.append('[');
				this.append(objArray[offset]);

				if (length > 1) {
					final int endpoint = offset + length;

					for (int i = offset + 1; i < endpoint; i++) {
						this.addSeparator();
						this.append(objArray[i]);
					}
				}

				this.append(']');
			}
		}

		// Append saved characters back onto this Extractor
		return this.append(savedChars, 0, savedChars.length);
	}

	public final StringExtractor insert(final int index, final String str) {
		final int newSize = this.size + str.length();

		if (newSize > this.chars.length) {
			this.resize(newSize + (newSize >> 1));
		}

		System.arraycopy(this.chars, index, this.chars, index + str.length(), this.size - index);
		str.getChars(0, str.length(), this.chars, index);
		this.size = newSize;

		return this;
	}

	public final StringExtractor insert(final int index, final StringBuffer buffer) {
		final int newSize = this.size + buffer.length();

		if (newSize > this.chars.length) {
			this.resize(newSize + (newSize >> 1));
		}

		System.arraycopy(this.chars, index, this.chars, index + buffer.length(), this.size - index);
		buffer.getChars(0, buffer.length(), this.chars, index);
		this.size = newSize;

		return this;
	}

	public final StringExtractor insert(final int index, final StringBuilder builder) {
		final int newSize = this.size + builder.length();

		if (newSize > this.chars.length) {
			this.resize(newSize + (newSize >> 1));
		}

		System.arraycopy(this.chars, index, this.chars, index + builder.length(), this.size - index);
		builder.getChars(0, builder.length(), this.chars, index);
		this.size = newSize;

		return this;
	}

	public final int length() {
		return this.size;
	}

	public final void reduceLength(final int newLength) {
		if (newLength < this.size) {
			this.size = newLength;
		}
	}

	public final StringExtractor replace(final int start, final int end, final String str) {
		final int newSize = this.size + str.length() - (end - start);

		if (newSize > this.chars.length) {
			this.resize(newSize + (newSize >> 1));
		}

		System.arraycopy(this.chars, end, this.chars, start + str.length(), this.size - end);
		str.getChars(0, str.length(), this.chars, start);
		this.size = newSize;

		return this;
	}

	public final void setCharAt(final int index, final char ch) {
		this.chars[index] = ch;
	}

	public final String subSequence(final int start, final int end) {
		return new String(this.chars, start, end - start);
	}

	public final String substring(final int start) {
		return this.substring(start, this.size);
	}

	public final String substring(final int start, final int end) {
		return new String(this.chars, start, end - start);
	}

	// <><><><><><><><><><><><><>< Private Methods <><><><><><><><><><><><><><>

	public final char[] toArray() {
		final char[] array = new char[this.size];

		System.arraycopy(this.chars, 0, array, 0, this.size);

		return array;
	}

	@Override
	public final String toString() {
		return this.size == this.chars.length ? Root.newInstance(this.chars) : new String(this.chars, 0, this.size);
	}

	private StringExtractor appendEmptyArray() {
		final int newSize = this.size + 2;

		if (newSize > this.chars.length) {
			this.resize(newSize + (newSize >> 1));
		}

		this.chars[this.size++] = '[';
		this.chars[this.size++] = ']';

		return this;
	}

	private void resize(final int capacity) {
		final char[] c = new char[capacity];
		System.arraycopy(this.chars, 0, c, 0, this.size);
		this.chars = c;
	}

} // End StringExtractor
