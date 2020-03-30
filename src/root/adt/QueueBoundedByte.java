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
package root.adt;

import root.util.Root;
import root.validation.IndexOutOfBoundsException;

/**
 * This is a byte-specific implementation of the {@link QueueBounded} class. While <code>QueueBoundedByte</code> implements most of the methods from
 * the {@link RootQueue} interface, it does not extend {@link RootQueue} so that all methods deal with primitives instead of the {@link Number}
 * classes.
 * <p>
 * TODO: Convert every method in this class to use the primitive byte instead of the object Byte<br>
 * TODO: Everything needs to implement Externalizable where possible<br>
 * TODO: Take the incHead() and incTail() methods to all other array-based data structures that use inc()<br>
 * TODO: Put braces around all one-line statements<br>
 * TODO: Add isFull() method to other existing bounded implementations<br>
 * TODO: Convert other bounded implementations to use new exception classes that are uses in here <br>
 * TODO: Add enqueue() and dequeue() methods to RootQueue that take arrays with offsets and lengths
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class QueueBoundedByte {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int head;
	private int tail;
	private int size;

	private final byte[] queue;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public QueueBoundedByte(final int capacity) {
		this.queue = new byte[capacity];
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	/**
	 *
	 */
	public final void clear() {
		this.head = 0;
		this.tail = 0;
		this.size = 0;
	}

	/**
	 *
	 * @return
	 * @throws root.adt.DataStructureEmptyException
	 */
	public final byte dequeue() {
		if (this.size == 0) {
			throw new DataStructureEmptyException();
		}

		final byte b = this.queue[this.head];
		this.incHead();
		this.size--;

		return b;
	}

	/**
	 *
	 * @param byteArray
	 * @param offset
	 * @param length
	 * @return
	 * @throws root.validation.IndexOutOfBoundsException
	 */
	public final int dequeue(final byte[] byteArray, final int offset, final int length) {
		if (offset + length > byteArray.length) {
			throw new IndexOutOfBoundsException(offset + length, byteArray.length);
		}

		final int numBytesToDequeue = Root.min(this.size, length);

		if (numBytesToDequeue > 0) {
			int newHead = this.head + numBytesToDequeue;

			if (newHead < this.queue.length) {
				System.arraycopy(this.queue, this.head, byteArray, offset, numBytesToDequeue);
			} else {
				final int firstCopyLen = this.queue.length - this.head;
				newHead = numBytesToDequeue - firstCopyLen;

				System.arraycopy(this.queue, this.head, byteArray, offset, firstCopyLen);
				System.arraycopy(this.queue, 0, byteArray, offset + firstCopyLen, newHead);
			}

			this.head = newHead;
			this.size -= numBytesToDequeue;
		}

		return numBytesToDequeue;
	}

	/**
	 *
	 * @param b
	 * @throws DataStructureFullException
	 */
	public final void enqueue(final byte b) {
		if (this.size == this.queue.length) {
			throw new DataStructureFullException();
		}

		this.queue[this.tail] = b;
		this.incTail();
		this.size++;
	}

	/**
	 *
	 * @param byteArray
	 * @param offset
	 * @param length
	 * @throws IndexOutOfBoundsException
	 * @throws InsufficientCapacityException
	 */
	public final void enqueue(final byte[] byteArray, final int offset, final int length) {
		if (offset + length > byteArray.length) {
			throw new IndexOutOfBoundsException(offset + length, byteArray.length);
		}

		final int numBytesToEnqueue = Root.min(byteArray.length, length);

		if (numBytesToEnqueue > this.getCapacity()) {
			throw new InsufficientCapacityException(numBytesToEnqueue, this.getCapacity());
		}

		if (numBytesToEnqueue > 0) {
			int newTail = this.tail + numBytesToEnqueue;

			if (newTail < this.queue.length) {
				System.arraycopy(byteArray, offset, this.queue, this.tail, numBytesToEnqueue);
			} else {
				final int firstCopyLen = this.queue.length - this.tail;
				newTail = numBytesToEnqueue - firstCopyLen;

				System.arraycopy(byteArray, offset, this.queue, this.tail, firstCopyLen);
				System.arraycopy(byteArray, offset + firstCopyLen, this.queue, 0, newTail);
			}

			this.tail = newTail;
			this.size += numBytesToEnqueue;
		}
	}

	/**
	 *
	 * @param i
	 * @throws InsufficientCapacityException
	 */
	public final void enqueue(final int i) {
		if (4 > this.getCapacity()) {
			throw new InsufficientCapacityException(4, this.getCapacity());
		}

		if (this.queue.length - this.tail < 4) {
			this.queue[this.tail] = (byte) (i >>> 24);
			this.incTail();
			this.queue[this.tail] = (byte) (i >>> 16);
			this.incTail();
			this.queue[this.tail] = (byte) (i >>> 8);
			this.incTail();
		} else {
			this.queue[this.tail++] = (byte) (i >>> 24);
			this.queue[this.tail++] = (byte) (i >>> 16);
			this.queue[this.tail++] = (byte) (i >>> 8);
		}

		// Can always increment tail directly for last byte
		this.queue[this.tail++] = (byte) i;

		this.size += 4;
	}

	/**
	 *
	 * @param intArray
	 * @param offset
	 * @param length
	 * @throws root.validation.IndexOutOfBoundsException
	 * @throws root.adt.InsufficientCapacityException
	 */
	public final void enqueue(final int[] intArray, final int offset, final int length) {
		if (offset + length > intArray.length) {
			throw new IndexOutOfBoundsException(offset + length, intArray.length);
		}

		final int numBytesToEnqueue = Root.min(intArray.length, length) << 2;

		if (numBytesToEnqueue > this.getCapacity()) {
			throw new InsufficientCapacityException(numBytesToEnqueue, this.getCapacity());
		}

		if (numBytesToEnqueue > 0) {
			final int lastIntIndex = offset + length;
			for (int index = offset; index < lastIntIndex; index++) {
				final int i = intArray[index];

				if (this.queue.length - this.tail < 4) {
					this.queue[this.tail] = (byte) (i >>> 24);
					this.incTail();
					this.queue[this.tail] = (byte) (i >>> 16);
					this.incTail();
					this.queue[this.tail] = (byte) (i >>> 8);
					this.incTail();
				} else {
					this.queue[this.tail++] = (byte) (i >>> 24);
					this.queue[this.tail++] = (byte) (i >>> 16);
					this.queue[this.tail++] = (byte) (i >>> 8);
				}

				// Can always increment tail directly for last byte
				this.queue[this.tail++] = (byte) i;
			}

			this.size += numBytesToEnqueue;
		}
	}

	public final int getCapacity() {
		return this.queue.length - this.size;
	}

	public final int getSize() {
		return this.size;
	}

	public final boolean isEmpty() {
		return this.size == 0;
	}

	public final boolean isFull() {
		return this.size == this.queue.length;
	}

	/**
	 * Retrieves, but does not remove, the head of this queue. If the queue is empty, this method throws a {@link DataStructureEmptyException}.
	 *
	 * @return the head of this queue
	 * @throws DataStructureEmptyException
	 *             if the queue is empty
	 */
	public final byte peek() {
		if (this.size == 0) {
			throw new DataStructureEmptyException();
		}

		return this.queue[this.head];
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private void incHead() {
		if (++this.head == this.queue.length) {
			this.head = 0;
		}
	}

	private void incTail() {
		if (++this.tail == this.queue.length) {
			this.tail = 0;
		}
	}

} // End QueueBoundedByte
