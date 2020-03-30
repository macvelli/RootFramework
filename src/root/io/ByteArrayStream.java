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
package root.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import root.lang.Constants;
import root.lang.reflect.ByteArrayField;

/**
 * http://java.sun.com/developer/technicalArticles/Streams/WritingIOSC/index.html http://mindprod.com/jgloss/unsigned.html
 *
 * TODO: Good example of a) extend from multiple classes b) parent class checked exception erasure<br>
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ByteArrayStream implements InputOutputStream<byte[]> {

	// <><><><><><><><><><><><><><> Inner Classes <><><><><><><><><><><><><><>

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	public final class ByteArrayInput extends InputStream {

		// <><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><>

		private int loc;
		private int mark;

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private ByteArrayInput() {
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final int available() {
			return ByteArrayStream.this.size - this.loc;
		}

		@Override
		public final void close() {
		}

		@Override
		public final void mark(final int readlimit) {
			this.mark = this.loc;
		}

		@Override
		public final boolean markSupported() {
			return true;
		}

		@Override
		public final int read() {
			return this.loc == ByteArrayStream.this.size ? -1 : ByteArrayStream.this.bytes[this.loc++] & 0xff;
		}

		@Override
		public final int read(final byte[] b) {
			return this.read(b, 0, b.length);
		}

		@Override
		public final int read(final byte[] b, final int off, int len) {
			if (this.loc == ByteArrayStream.this.size) {
				return -1;
			}

			if (this.loc + len > ByteArrayStream.this.size) {
				len = ByteArrayStream.this.size - this.loc;
			}

			System.arraycopy(ByteArrayStream.this.bytes, this.loc, b, off, len);
			this.loc += len;

			return len;
		}

		@Override
		public final void reset() {
			this.loc = this.mark;
		}

		@Override
		public final long skip(long len) {
			if (this.loc + len > ByteArrayStream.this.size) {
				len = ByteArrayStream.this.size - this.loc;
			}

			this.loc += len;

			return len;
		}

	} // End ByteArrayInput

	/**
	 *
	 * @author Edward Smith
	 * @version 0.5
	 * @since 0.5
	 */
	public final class ByteArrayOutput extends OutputStream {

		// <><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><>

		private ByteArrayOutput() {
		}

		// <><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><>

		@Override
		public final void close() {
		}

		@Override
		public final void flush() {
		}

		@Override
		public String toString() {
			return ByteArrayStream.this.toString();
		}

		@Override
		public final void write(final byte[] b) {
			final int newSize = ByteArrayStream.this.size + b.length;

			if (newSize > ByteArrayStream.this.bytes.length) {
				this.resize(newSize << 1);
			}

			System.arraycopy(b, 0, ByteArrayStream.this.bytes, ByteArrayStream.this.size, b.length);
			ByteArrayStream.this.size = newSize;
		}

		@Override
		public final void write(final byte[] b, final int offset, final int len) {
			final int newSize = ByteArrayStream.this.size + len;

			if (newSize > ByteArrayStream.this.bytes.length) {
				this.resize(newSize << 1);
			}

			System.arraycopy(b, offset, ByteArrayStream.this.bytes, ByteArrayStream.this.size, len);
			ByteArrayStream.this.size = newSize;
		}

		@Override
		public final void write(final int b) {
			if (ByteArrayStream.this.size == ByteArrayStream.this.bytes.length) {
				this.resize(ByteArrayStream.this.size << 1);
			}

			ByteArrayStream.this.bytes[ByteArrayStream.this.size++] = (byte) b;
		}

		// <><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><>

		private void resize(final int capacity) {
			final byte[] b = new byte[capacity];
			System.arraycopy(ByteArrayStream.this.bytes, 0, b, 0, ByteArrayStream.this.size);
			ByteArrayStream.this.bytes = b;
		}

	} // End ByteArrayOutput

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final ByteArrayField<ByteArrayOutputStream> bufField = new ByteArrayField<>(ByteArrayOutputStream.class, "buf");

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int size;
	private byte[] bytes;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public ByteArrayStream() {
		this.bytes = new byte[32];
	}

	public ByteArrayStream(final byte[] bytes) {
		this.bytes = bytes;
		this.size = bytes.length;
	}

	public ByteArrayStream(final ByteArrayOutputStream os) {
		this.bytes = bufField.getField(os);
		this.size = os.size();
	}

	public ByteArrayStream(final InputStream is) throws IOException {
		// TODO: What if the InputStream is buffered?
		this.bytes = new byte[is.available()];
		is.read(this.bytes, 0, this.bytes.length);
		this.size = this.bytes.length;
	}

	public ByteArrayStream(final int size) {
		this.bytes = new byte[size];
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final ByteArrayInput getInputStream() {
		return new ByteArrayInput();
	}

	@Override
	public final ByteArrayOutput getOutputStream() {
		return new ByteArrayOutput();
	}

	@Override
	public final int getSize() {
		return this.size;
	}

	@Override
	public final byte[] getValues() {
		final byte b[] = new byte[this.size];
		System.arraycopy(this.bytes, 0, b, 0, this.size);
		return b;
	}

	@Override
	public final void reset() {
		this.size = 0;
	}

	@Override
	public final String toString() {
		return new String(this.bytes, 0, this.size, Constants.CHARSET_UTF8);
	}

	@Override
	public final void writeTo(final OutputStream os) throws IOException {
		os.write(this.bytes, 0, this.size);
	}

} // End ByteArrayStream
