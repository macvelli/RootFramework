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

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import root.lang.StringExtractor;

/**
 * Test the {@link BitSet} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class BitSetTest extends TestCase {

	private BitSet flags;

	public BitSetTest() {
		super("BitSet");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.flags = new BitSet();
		this.flags.set(1);
	}

	@Test
	public void testClearAll() {
		this.flags.set(0);
		assertTrue(this.flags.get(0));
		assertTrue(this.flags.get(1));
		this.flags.clear();
		assertFalse(this.flags.get(0));
		assertFalse(this.flags.get(1));
	}

	@Test
	public void testClearBit() {
		assertTrue(this.flags.get(1));
		this.flags.clear(1);
		assertFalse(this.flags.get(1));
	}

	@Test
	public void testContains() {
		this.flags.set(2);

		// Test contains(BitSet)
		assertTrue(this.flags.contains(new BitSet(2)));
		assertTrue(this.flags.contains(new BitSet(4)));
		assertTrue(this.flags.contains(new BitSet(6)));
		assertFalse(this.flags.contains(new BitSet(14)));

		// Test contains(int)
		assertTrue(this.flags.contains(2));
		assertTrue(this.flags.contains(4));
		assertTrue(this.flags.contains(6));
		assertFalse(this.flags.contains(14));
	}

	@Test
	public void testContainsAny() {
		// Test containsAny(BitSet)
		assertTrue(this.flags.containsAny(new BitSet(2)));
		assertTrue(this.flags.containsAny(new BitSet(6)));
		assertTrue(this.flags.containsAny(new BitSet(14)));
		assertFalse(this.flags.contains(new BitSet(4)));

		// Test containsAny(int)
		assertTrue(this.flags.containsAny(2));
		assertTrue(this.flags.containsAny(6));
		assertTrue(this.flags.containsAny(14));
		assertFalse(this.flags.contains(4));
	}

	@Test
	public void testEqualsInt() {
		assertTrue(this.flags.equals(2));
		assertFalse(this.flags.equals(1));
	}

	@Test
	public void testEqualsObject() {
		final BitSet b = new BitSet();
		b.set(1);
		assertTrue(this.flags.equals(b));
		b.clear(1);
		assertFalse(this.flags.equals(b));
	}

	@Test
	public void testExtract() {
		final StringExtractor chars = new StringExtractor();
		this.flags.extract(chars);
		assertEquals("00000000000000000000000000000010", chars.toString());
	}

	@Test
	public void testGet() {
		assertTrue(this.flags.get(1));
		assertFalse(this.flags.get(0));
	}

	@Test
	public void testGetFlags() {
		assertEquals(2, this.flags.getFlags());
	}

	@Test
	public void testHashCode() {
		assertEquals(2, this.flags.hashCode());
	}

	@Test
	public void testIntersect() {
		final BitSet b = new BitSet(2);
		assertEquals(2, this.flags.intersect(b).getFlags());
		b.clear();
		assertEquals(0, this.flags.intersect(b).getFlags());
	}

	@Test
	public void testIsEmpty() {
		assertFalse(this.flags.isEmpty());
		this.flags.clear();
		assertTrue(this.flags.isEmpty());
	}

	@Test
	public void testSetInt() {
		assertFalse(this.flags.get(4));
		this.flags.set(4);
		assertTrue(this.flags.get(4));
	}

	@Test
	public void testSetIntBoolean() {
		assertTrue(this.flags.get(1));
		this.flags.set(1, false);
		assertFalse(this.flags.get(1));
	}

	@Test
	public void testToString() {
		assertEquals("00000000000000000000000000000010", this.flags.toString());
	}

	@Test
	public void testUnion() {
		final BitSet b = new BitSet(4);
		assertEquals(6, this.flags.union(b).getFlags());
		b.clear();
		assertEquals(2, this.flags.union(b).getFlags());
	}

} // End BitSetTest
