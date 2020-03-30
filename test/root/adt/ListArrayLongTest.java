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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import root.lang.StringExtractor;
import root.random.RNG;
import root.random.RNGKiss;
import root.random.SeedFactoryConstant;

/**
 * Test the {@link ListArrayLong} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ListArrayLongTest extends TestCase {

	private ListArrayLong list;

	public ListArrayLongTest() {
		super("ListArrayLong");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.list = new ListArrayLong();
	}

	@Test
	public void testAdd() {
		final long foo = "foo".hashCode();

		assertEquals(0, this.list.size);

		this.list.add(foo);
		assertEquals(1, this.list.size);
		assertEquals(foo, this.list.get(0));

		this.list.add(foo);
		assertEquals(2, this.list.size);
		assertEquals(foo, this.list.get(1));

		this.list.add(foo);
		assertEquals(3, this.list.size);
		assertEquals(foo, this.list.get(2));

		this.list.add(foo);
		assertEquals(4, this.list.size);
		assertEquals(foo, this.list.get(3));

		this.list.add(foo);
		assertEquals(5, this.list.size);
		assertEquals(foo, this.list.get(4));

		this.list.add(foo);
		assertEquals(6, this.list.size);
		assertEquals(foo, this.list.get(5));

		this.list.add(foo);
		assertEquals(7, this.list.size);
		assertEquals(foo, this.list.get(6));

		this.list.add(foo);
		assertEquals(8, this.list.size);
		assertEquals(8, this.list.values.length);
		assertEquals(foo, this.list.get(7));

		this.list.add(foo);
		assertEquals(9, this.list.size);
		assertEquals(16, this.list.values.length);
		assertEquals(foo, this.list.get(8));
	}

	@Test
	public void testAddAllArray() {
		final long[] array = { "foo".hashCode(), "bar".hashCode() };
		assertEquals(0, this.list.size);

		this.list.addAll(array, 0, 2);
		assertEquals(2, this.list.size);

		assertEquals(101574L, this.list.get(0));
		assertEquals(97299L, this.list.get(1));

		this.list.addAll(array, 0, 1);
		assertEquals(3, this.list.size);

		assertEquals(101574L, this.list.get(0));
		assertEquals(97299L, this.list.get(1));
		assertEquals(101574L, this.list.get(2));

		this.list.addAll(array, 1, 1);
		assertEquals(4, this.list.size);

		assertEquals(101574L, this.list.get(0));
		assertEquals(97299L, this.list.get(1));
		assertEquals(101574L, this.list.get(2));
		assertEquals(97299L, this.list.get(3));
	}

	@Test
	public void testAddAllCollection() {
		final List<Long> longList = new ArrayList<>();
		longList.add(new Long("foo".hashCode()));
		longList.add(new Long("bar".hashCode()));
		assertEquals(0, this.list.size);

		this.list.addAll(longList);
		assertEquals(2, this.list.size);

		assertEquals(101574L, this.list.get(0));
		assertEquals(97299L, this.list.get(1));

		this.list.addAll(longList);
		assertEquals(4, this.list.size);

		assertEquals(101574L, this.list.get(0));
		assertEquals(97299L, this.list.get(1));
		assertEquals(101574L, this.list.get(2));
		assertEquals(97299L, this.list.get(3));
	}

	@Test
	public void testAddAllCollectionWithIndex() {
		assertEquals(0, this.list.size);

		this.list.addAll(new long[] { "foo".hashCode(), "bar".hashCode() }, 0, 2);
		assertEquals(2, this.list.size);

		final List<Long> longList = new ArrayList<>();
		longList.add(new Long("xyz".hashCode()));
		longList.add(new Long("123".hashCode()));
		this.list.addAll(1, longList);
		assertEquals(4, this.list.size);

		assertEquals(101574L, this.list.get(0));
		assertEquals(119193L, this.list.get(1));
		assertEquals(48690L, this.list.get(2));
		assertEquals(97299L, this.list.get(3));
	}

	@Test
	public void testAddWithIndex() {
		assertEquals(0, this.list.size);

		this.list.addAll(new long[] { "foo".hashCode(), "bar".hashCode() }, 0, 2);
		assertEquals(2, this.list.size);

		this.list.add(1, "xyz".hashCode());
		assertEquals(3, this.list.size);

		assertEquals(101574L, this.list.get(0));
		assertEquals(119193L, this.list.get(1));
		assertEquals(97299L, this.list.get(2));
	}

	@Test
	public void testClear() {
		assertEquals(0, this.list.size);
		this.list.add("foo".hashCode());
		assertEquals(1, this.list.size);
		this.list.clear();
		assertEquals(0, this.list.size);
	}

	@Test
	public void testClone() {
		this.list.addAll(new long[] { "foo".hashCode(), "bar".hashCode() }, 0, 2);
		assertEquals(2, this.list.size);
		assertEquals(101574L, this.list.get(0));
		assertEquals(97299L, this.list.get(1));

		final ListArrayLong l = this.list.clone();
		assertEquals(2, l.size);
		assertEquals(101574L, l.get(0));
		assertEquals(97299L, l.get(1));
		assertFalse(this.list == l);
	}

	@Test
	public void testConstructorArray() {
		final ListArrayLong l = new ListArrayLong("foo".hashCode(), "bar".hashCode());
		assertEquals(2, l.size);
		assertEquals(101574L, l.get(0));
		assertEquals(97299L, l.get(1));
	}

	@Test
	public void testConstructorCapacity() {
		ListArrayLong l = new ListArrayLong(15);
		assertEquals(0, l.size);
		assertEquals(15, l.values.length);

		// Test minimum capacity of 8
		l = new ListArrayLong(7);
		assertEquals(0, l.size);
		assertEquals(8, l.values.length);
	}

	@Test
	public void testConstructorCollection() {
		final List<Long> longList = new ArrayList<>();
		longList.add(new Long("foo".hashCode()));
		longList.add(new Long("bar".hashCode()));

		final ListArrayLong l = new ListArrayLong(longList);
		assertEquals(2, l.size);
		assertEquals(8, l.values.length);
		assertEquals(101574L, l.get(0));
		assertEquals(97299L, l.get(1));
	}

	@Test
	public void testConstructorDefault() {
		assertEquals(0, this.list.size);
		assertEquals(8, this.list.values.length);
	}

	@Test
	public void testContains() {
		assertFalse(this.list.contains(101574L));
		this.list.add("foo".hashCode());
		assertTrue(this.list.contains(101574L));
	}

	@Test
	public void testContainsAll() {
		final List<Long> longList = new ArrayList<>();
		longList.add(new Long("foo".hashCode()));
		longList.add(new Long("bar".hashCode()));

		assertFalse(this.list.containsAll(longList));

		this.list.add("foo".hashCode());
		assertFalse(this.list.containsAll(longList));

		this.list.add("bar".hashCode());
		assertTrue(this.list.containsAll(longList));
	}

	@Test
	public void testContainsAny() {
		final List<Long> longList = new ArrayList<>();
		longList.add(new Long("foo".hashCode()));
		longList.add(new Long("bar".hashCode()));

		assertFalse(this.list.containsAny(longList));

		this.list.add("bar".hashCode());
		assertTrue(this.list.containsAny(longList));
	}

	public void testEcho() {
		final long l = this.list.echo("foo".hashCode());

		assertEquals(1, this.list.size);
		assertEquals(101574L, this.list.get(0));
		assertEquals(101574L, l);
	}

	@Test
	public void testEquals() {
		// Test equals() to a ListArrayLong
		final ListArrayLong testListA = new ListArrayLong();
		assertTrue(this.list.equals(testListA));

		testListA.add("foo".hashCode());
		assertFalse(this.list.equals(testListA));

		this.list.add("foo".hashCode());
		assertTrue(this.list.equals(testListA));

		testListA.add("bar".hashCode());
		assertFalse(this.list.equals(testListA));

		this.list.add("bar".hashCode());
		assertTrue(this.list.equals(testListA));

		this.list.clear();

		// Test equals() to a Collection<? extends Number>
		final List<Integer> testListB = new ArrayList<>();
		assertTrue(this.list.equals(testListB));

		testListB.add("foo".hashCode());
		assertFalse(this.list.equals(testListB));

		this.list.add("foo".hashCode());
		assertTrue(this.list.equals(testListB));

		testListB.add("bar".hashCode());
		assertFalse(this.list.equals(testListB));

		this.list.add("bar".hashCode());
		assertTrue(this.list.equals(testListB));
	}

	@Test
	public void testExtract() {
		final StringExtractor extractor = new StringExtractor();
		this.list.extract(extractor);
		assertEquals("[]", extractor.toString());

		this.list.add("foo".hashCode());

		extractor.clear();
		this.list.extract(extractor);
		assertEquals("[101574]", extractor.toString());

		this.list.add("bar".hashCode());

		extractor.clear();
		this.list.extract(extractor);
		assertEquals("[101574,97299]", extractor.toString());
	}

	@Test
	public void testGet() {
		assertEquals(0, this.list.get(0));
		this.list.add("foo".hashCode());
		assertEquals(101574L, this.list.get(0));

		try {
			this.list.get(8);
			fail("Expected java.lang.ArrayIndexOutOfBoundsException was not thrown");
		} catch (final ArrayIndexOutOfBoundsException e) {
		}
	}

	@Test
	public void testGetCapacity() {
		assertEquals(8, this.list.getCapacity());
		assertEquals(this.list.values.length, this.list.getCapacity());
		this.list.addAll(new long[] { "foo".hashCode(), "bar".hashCode(), "xyz".hashCode(), "123".hashCode(), "foo".hashCode(), "bar".hashCode(),
				"xyz".hashCode(), "123".hashCode(), "foo".hashCode() }, 0, 9);
		assertEquals(13, this.list.getCapacity());
		assertEquals(this.list.values.length, this.list.getCapacity());
	}

	@Test
	public void testGetSize() {
		assertEquals(0, this.list.getSize());
		this.list.add("foo".hashCode());
		assertEquals(1, this.list.getSize());
	}

	@Test
	public void testGetValues() {
		final long[] longArray = this.list.getValues();
		assertTrue(longArray == this.list.values);
	}

	@Test
	public void testHashCode() {
		assertEquals(0, this.list.hashCode());

		this.list.add("foo".hashCode());
		assertEquals(101572, this.list.hashCode());

		this.list.add("bar".hashCode());
		assertEquals(157079, this.list.hashCode());
	}

	@Test
	public void testIndexOf() {
		assertEquals(-1, this.list.indexOf("bar".hashCode()));

		this.list.add("foo".hashCode());
		assertEquals(-1, this.list.indexOf("bar".hashCode()));

		this.list.add("bar".hashCode());
		assertEquals(1, this.list.indexOf("bar".hashCode()));
	}

	@Test
	public void testInsert() {
		assertEquals(0, this.list.size);

		this.list.addAll(new long[] { "foo".hashCode(), "bar".hashCode() }, 0, 2);
		assertEquals(2, this.list.size);

		this.list.insert(1, "xyz".hashCode());
		assertEquals(3, this.list.size);

		assertEquals(101574L, this.list.get(0));
		assertEquals(119193L, this.list.get(1));
		assertEquals(97299L, this.list.get(2));
	}

	@Test
	public void testInsertAll() {
		assertEquals(0, this.list.size);

		this.list.addAll(new long[] { "foo".hashCode(), "bar".hashCode() }, 0, 2);
		assertEquals(2, this.list.size);

		final List<Long> longList = new ArrayList<>();
		longList.add(new Long("xyz".hashCode()));
		longList.add(new Long("123".hashCode()));
		this.list.insertAll(1, longList);
		assertEquals(4, this.list.size);

		assertEquals(101574L, this.list.get(0));
		assertEquals(119193L, this.list.get(1));
		assertEquals(48690L, this.list.get(2));
		assertEquals(97299L, this.list.get(3));
	}

	@Test
	public void testIsEmpty() {
		assertTrue(this.list.isEmpty());

		this.list.add("foo".hashCode());
		assertFalse(this.list.isEmpty());
	}

	@Test
	public void testLast() {
		assertEquals(0L, this.list.last());

		this.list.add("foo".hashCode());
		assertEquals(101574L, this.list.last());

		this.list.add("bar".hashCode());
		assertEquals(97299L, this.list.last());
	}

	@Test
	public void testLastIndexOf() {
		assertEquals(-1, this.list.lastIndexOf("foo".hashCode()));

		this.list.add("foo".hashCode());
		assertEquals(0, this.list.lastIndexOf("foo".hashCode()));

		this.list.add("bar".hashCode());
		assertEquals(0, this.list.lastIndexOf("foo".hashCode()));

		this.list.add("foo".hashCode());
		assertEquals(2, this.list.lastIndexOf("foo".hashCode()));
	}

	@Test
	public void testRandom() {
		final RNG rng = new RNGKiss(new SeedFactoryConstant(857435, 2768, 984598, 3027694, 104));

		assertEquals(0L, this.list.random(rng));

		this.list.add("foo".hashCode());
		this.list.add("bar".hashCode());
		this.list.add("xyz".hashCode());
		this.list.add("123".hashCode());

		assertEquals(119193L, this.list.random(rng));
	}

	@Test
	public void testRemoveAll() {
		final List<Long> longList = new ArrayList<>();
		longList.add(new Long("xyz".hashCode()));
		longList.add(new Long("123".hashCode()));

		assertFalse(this.list.removeAll(longList));

		this.list.add("foo".hashCode());
		this.list.add("bar".hashCode());

		assertFalse(this.list.removeAll(longList));

		longList.add(new Long("foo".hashCode()));
		assertTrue(this.list.removeAll(longList));
		assertEquals(1, this.list.size);
		assertEquals(97299L, this.list.get(0));
	}

	@Test
	public void testRemoveIndex() {
		try {
			this.list.remove(0);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 0, Size: 0", e.getMessage());
		}

		this.list.add("foo".hashCode());
		this.list.add("bar".hashCode());

		final long l = this.list.remove(0);
		assertEquals(101574L, l);
		assertEquals(1, this.list.size);
		assertEquals(97299L, this.list.get(0));
	}

	@Test
	public void testRemoveValue() {
		assertFalse(this.list.removeValue("foo".hashCode()));

		this.list.add("foo".hashCode());
		this.list.add("bar".hashCode());

		assertFalse(this.list.removeValue("xyz".hashCode()));
		assertTrue(this.list.removeValue("foo".hashCode()));
		assertEquals(1, this.list.size);
		assertEquals(97299L, this.list.get(0));
	}

	@Test
	public void testReplace() {
		assertFalse(this.list.replace("xyz".hashCode(), "123".hashCode()));

		this.list.add("foo".hashCode());
		this.list.add("bar".hashCode());

		assertFalse(this.list.replace("xyz".hashCode(), "123".hashCode()));
		assertTrue(this.list.replace("foo".hashCode(), "xyz".hashCode()));
		assertEquals(2, this.list.size);
		assertEquals(119193L, this.list.get(0));
		assertEquals(97299L, this.list.get(1));
	}

	@Test
	public void testRetainAll() {
		final List<Long> longList = new ArrayList<>();
		longList.add(new Long("foo".hashCode()));
		longList.add(new Long("bar".hashCode()));

		assertFalse(this.list.retainAll(longList));

		this.list.add("foo".hashCode());
		this.list.add("bar".hashCode());

		assertFalse(this.list.retainAll(longList));

		longList.set(0, new Long("xyz".hashCode()));
		assertTrue(this.list.retainAll(longList));
		assertEquals(1, this.list.size);
		assertEquals(97299L, this.list.get(0));
		assertEquals(0L, this.list.get(1));
	}

	@Test
	public void testSet() {
		try {
			this.list.set(0, "xyz".hashCode());
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 0, Size: 0", e.getMessage());
		}

		this.list.add("foo".hashCode());
		this.list.add("bar".hashCode());

		final long l = this.list.set(1, "xyz".hashCode());
		assertEquals(97299L, l);
		assertEquals(2, this.list.size);
		assertEquals(101574L, this.list.get(0));
		assertEquals(119193L, this.list.get(1));
	}

	@Test
	public void testShuffle() {
		final RNG rng = new RNGKiss(new SeedFactoryConstant(37544, 2768, 7346, 3027694, 104));

		this.list.add("foo".hashCode());
		this.list.add("bar".hashCode());
		this.list.add("xyz".hashCode());
		this.list.add("123".hashCode());

		this.list.shuffle(rng);

		assertEquals(101574L, this.list.get(0));
		assertEquals(48690L, this.list.get(1));
		assertEquals(97299L, this.list.get(2));
		assertEquals(119193L, this.list.get(3));
	}

	@Test
	public void testSize() {
		assertEquals(0, this.list.size());
		this.list.add("foo".hashCode());
		assertEquals(1, this.list.size());
	}

	@Test
	public void testSubListFromIndex() {
		try {
			this.list.subList(1);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 1, Size: 0", e.getMessage());
		}

		this.list.add("foo".hashCode());
		this.list.add("bar".hashCode());
		this.list.add("xyz".hashCode());
		this.list.add("123".hashCode());

		final ListArrayLong subList = this.list.subList(1);
		assertEquals(3, subList.size);
		assertEquals(97299L, subList.get(0));
		assertEquals(119193L, subList.get(1));
		assertEquals(48690L, subList.get(2));
	}

	@Test
	public void testSubListFromIndexToIndex() {
		try {
			this.list.subList(1, 3);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 3, Size: 0", e.getMessage());
		}

		this.list.add("foo".hashCode());
		this.list.add("bar".hashCode());
		this.list.add("xyz".hashCode());
		this.list.add("123".hashCode());

		final ListArrayLong subList = this.list.subList(1, 3);
		assertEquals(2, subList.size);
		assertEquals(97299L, subList.get(0));
		assertEquals(119193L, subList.get(1));
	}

	@Test
	public void testToArray() {
		long[] array = this.list.toArray();
		assertNotNull(array);
		assertEquals(0, array.length);

		this.list.add("foo".hashCode());
		this.list.add("bar".hashCode());
		array = this.list.toArray();

		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals(101574L, array[0]);
		assertEquals(97299L, array[1]);
	}

	@Test
	public void testToString() {
		assertEquals("[]", this.list.toString());

		this.list.add("foo".hashCode());
		assertEquals("[101574]", this.list.toString());

		this.list.add("bar".hashCode());
		assertEquals("[101574,97299]", this.list.toString());
	}

} // End ListArrayLongTest
