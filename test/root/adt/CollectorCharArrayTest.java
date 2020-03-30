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

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import root.lang.Itemizer;
import root.lang.StringExtractor;

/**
 * Test the {@link CollectorCharArray} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class CollectorCharArrayTest extends TestCase {

	private CollectorCharArray collector;

	public CollectorCharArrayTest() {
		super("CollectorCharArray");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.collector = new CollectorCharArray();
	}

	@Test
	public void testAddCharArray() {
		final char[] foo = { 'F', 'o', 'o' };

		this.collector.add(foo);
		assertEquals(1, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[0]));

		this.collector.add(foo);
		assertEquals(2, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[1]));

		this.collector.add(foo);
		assertEquals(3, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[2]));

		this.collector.add(foo);
		assertEquals(4, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[3]));

		this.collector.add(foo);
		assertEquals(5, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[4]));

		this.collector.add(foo);
		assertEquals(6, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[5]));

		this.collector.add(foo);
		assertEquals(7, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[6]));

		this.collector.add(foo);
		assertEquals(8, this.collector.size);
		assertEquals(8, this.collector.values.length);
		assertTrue(Arrays.equals(foo, this.collector.values[7]));

		this.collector.add(foo);
		assertEquals(9, this.collector.size);
		assertEquals(16, this.collector.values.length);
		assertTrue(Arrays.equals(foo, this.collector.values[8]));
	}

	@Test
	public void testAddCharSequence() {
		final StringBuilder builder = new StringBuilder("Foo");
		final char[] foo = { 'F', 'o', 'o' };

		this.collector.add(builder);
		assertEquals(1, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[0]));

		this.collector.add(builder);
		assertEquals(2, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[1]));

		this.collector.add(builder);
		assertEquals(3, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[2]));

		this.collector.add(builder);
		assertEquals(4, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[3]));

		this.collector.add(builder);
		assertEquals(5, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[4]));

		this.collector.add(builder);
		assertEquals(6, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[5]));

		this.collector.add(builder);
		assertEquals(7, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[6]));

		this.collector.add(builder);
		assertEquals(8, this.collector.size);
		assertEquals(8, this.collector.values.length);
		assertTrue(Arrays.equals(foo, this.collector.values[7]));

		this.collector.add(builder);
		assertEquals(9, this.collector.size);
		assertEquals(16, this.collector.values.length);
		assertTrue(Arrays.equals(foo, this.collector.values[8]));
	}

	@Test
	public void testAddString() {
		final String fooString = "Foo";
		final char[] foo = { 'F', 'o', 'o' };

		this.collector.add(fooString);
		assertEquals(1, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[0]));

		this.collector.add(fooString);
		assertEquals(2, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[1]));

		this.collector.add(fooString);
		assertEquals(3, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[2]));

		this.collector.add(fooString);
		assertEquals(4, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[3]));

		this.collector.add(fooString);
		assertEquals(5, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[4]));

		this.collector.add(fooString);
		assertEquals(6, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[5]));

		this.collector.add(fooString);
		assertEquals(7, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[6]));

		this.collector.add(fooString);
		assertEquals(8, this.collector.size);
		assertEquals(8, this.collector.values.length);
		assertTrue(Arrays.equals(foo, this.collector.values[7]));

		this.collector.add(fooString);
		assertEquals(9, this.collector.size);
		assertEquals(16, this.collector.values.length);
		assertTrue(Arrays.equals(foo, this.collector.values[8]));
	}

	@Test
	public void testAddStringExtractor() {
		final StringExtractor extractor = new StringExtractor("Foo");
		final char[] foo = { 'F', 'o', 'o' };

		this.collector.add(extractor);
		assertEquals(1, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[0]));

		this.collector.add(extractor);
		assertEquals(2, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[1]));

		this.collector.add(extractor);
		assertEquals(3, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[2]));

		this.collector.add(extractor);
		assertEquals(4, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[3]));

		this.collector.add(extractor);
		assertEquals(5, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[4]));

		this.collector.add(extractor);
		assertEquals(6, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[5]));

		this.collector.add(extractor);
		assertEquals(7, this.collector.size);
		assertTrue(Arrays.equals(foo, this.collector.values[6]));

		this.collector.add(extractor);
		assertEquals(8, this.collector.size);
		assertEquals(8, this.collector.values.length);
		assertTrue(Arrays.equals(foo, this.collector.values[7]));

		this.collector.add(extractor);
		assertEquals(9, this.collector.size);
		assertEquals(16, this.collector.values.length);
		assertTrue(Arrays.equals(foo, this.collector.values[8]));
	}

	@Test
	public void testClear() {
		assertEquals(0, this.collector.size);
		this.collector.add("Foo");
		assertEquals(1, this.collector.size);
		this.collector.clear();
		assertEquals(0, this.collector.size);
	}

	@Test
	public void testConstructorCapacity() {
		CollectorCharArray a = new CollectorCharArray(15);
		assertEquals(0, a.size);
		assertEquals(15, a.values.length);

		// Test minimum capacity of 8
		a = new CollectorCharArray(7);
		assertEquals(0, a.size);
		assertEquals(8, a.values.length);
	}

	@Test
	public void testConstructorDefault() {
		assertEquals(0, this.collector.size);
		assertEquals(8, this.collector.values.length);
	}

	@Test
	public void testEquals() {
		final CollectorCharArray testCollector = new CollectorCharArray();
		assertTrue(this.collector.equals(testCollector));

		testCollector.add("Foo");
		assertFalse(this.collector.equals(testCollector));

		this.collector.add("Foo");
		assertTrue(this.collector.equals(testCollector));
	}

	@Test
	public void testExtract() {
		final StringExtractor extractor = new StringExtractor();
		this.collector.extract(extractor);
		assertEquals("[]", extractor.toString());

		this.collector.add("Foo");

		extractor.clear();
		this.collector.extract(extractor);
		assertEquals("[[F, o, o]]", extractor.toString());

		this.collector.add("Bar");

		extractor.clear();
		this.collector.extract(extractor);
		assertEquals("[[F, o, o], [B, a, r]]", extractor.toString());
	}

	@Test
	public void testGet() {
		assertNull(this.collector.get(0));
		this.collector.add("Foo");
		assertTrue(Arrays.equals(new char[] { 'F', 'o', 'o' }, this.collector.get(0)));

		try {
			this.collector.get(8);
			fail("Expected java.lang.ArrayIndexOutOfBoundsException was not thrown");
		} catch (final ArrayIndexOutOfBoundsException e) {
		}
	}

	@Test
	public void testGetSize() {
		assertEquals(0, this.collector.getSize());
		this.collector.add("Foo");
		assertEquals(1, this.collector.getSize());
	}

	@Test
	public void testHashCode() {
		assertEquals(0, this.collector.hashCode());

		this.collector.add("Foo");
		assertEquals(515, this.collector.hashCode());

		this.collector.add("Bar");
		assertEquals(191, this.collector.hashCode());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(this.collector.isEmpty());

		this.collector.add("Foo");
		assertFalse(this.collector.isEmpty());
	}

	@Test
	public void testIterator() {
		Itemizer<char[]> itemizer = this.collector.iterator();

		// Test empty collector
		assertEquals(-1, itemizer.getIndex());
		assertEquals(0, itemizer.getSize());
		assertFalse(itemizer.hasNext());
		assertTrue(itemizer == itemizer.iterator());

		try {
			itemizer.next();
			fail("Expected java.util.NoSuchElementException was not thrown");
		} catch (final NoSuchElementException e) {
		}

		try {
			itemizer.remove();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		this.collector.add("Foo");
		this.collector.add("Bar");
		itemizer = this.collector.iterator();

		assertEquals(-1, itemizer.getIndex());
		assertEquals(2, itemizer.getSize());
		assertTrue(itemizer.hasNext());

		char[] charArray = itemizer.next();
		assertTrue(Arrays.equals(new char[] { 'F', 'o', 'o' }, charArray));
		assertEquals(0, itemizer.getIndex());
		assertTrue(itemizer.hasNext());

		charArray = itemizer.next();
		assertTrue(Arrays.equals(new char[] { 'B', 'a', 'r' }, charArray));
		assertEquals(1, itemizer.getIndex());
		assertFalse(itemizer.hasNext());

		itemizer.reset();
		assertEquals(-1, itemizer.getIndex());
		assertTrue(itemizer.hasNext());
		itemizer.next();
		assertTrue(itemizer.hasNext());
		itemizer.next();
		assertFalse(itemizer.hasNext());
	}

	@Test
	public void testToArray() {
		char[][] charArray = this.collector.toArray();
		assertNotNull(charArray);
		assertEquals(0, charArray.length);

		this.collector.add("Foo");
		this.collector.add("Bar");
		charArray = this.collector.toArray();

		assertNotNull(charArray);
		assertEquals(2, charArray.length);
		assertTrue(Arrays.equals(new char[] { 'F', 'o', 'o' }, charArray[0]));
		assertTrue(Arrays.equals(new char[] { 'B', 'a', 'r' }, charArray[1]));
	}

	@Test
	public void testToString() {
		assertEquals("[]", this.collector.toString());

		this.collector.add("Foo");
		assertEquals("[[F, o, o]]", this.collector.toString());

		this.collector.add("Bar");
		assertEquals("[[F, o, o], [B, a, r]]", this.collector.toString());
	}

} // End CollectorCharArrayTest
