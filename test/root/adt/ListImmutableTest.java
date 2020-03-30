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
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.random.RNG;
import root.random.RNGKiss;
import root.random.SeedFactoryConstant;

/**
 * Test the {@link ListImmutable} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ListImmutableTest extends TestCase {

	private ListImmutable<String> list;

	public ListImmutableTest() {
		super("ListImmutable");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.list = new ListArray<String>().toImmutable();
	}

	@Test
	public void testAdd() {
		try {
			this.list.add("foo");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot add items to a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testAddAllArray() {
		final String[] array = { "foo", "bar" };

		try {
			this.list.addAll(array, 0, 2);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot add items to a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testAddAllCollection() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");

		try {
			this.list.addAll(stringList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot add items to a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testAddAllCollectionWithIndex() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("xyz");
		stringList.add("123");

		try {
			this.list.addAll(1, stringList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot add items to a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testAddWithIndex() {
		try {
			this.list.add(1, "xyz");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot add items to a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testClear() {
		try {
			this.list.clear();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot clear items from a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testClone() {
		this.list = new ListImmutable<>("foo", "bar");

		assertEquals(2, this.list.size());
		assertEquals("foo", this.list.get(0));
		assertEquals("bar", this.list.get(1));

		final ListImmutable<String> clone = this.list.clone();
		assertEquals(2, clone.size());
		assertEquals("foo", clone.get(0));
		assertEquals("bar", clone.get(1));
		assertTrue(this.list == clone);
	}

	@Test
	public void testConstructorArray() {
		this.list = new ListImmutable<>("foo", "bar");

		assertEquals(2, this.list.size());
		assertEquals(2, this.list.getCapacity());
		assertEquals("foo", this.list.get(0));
		assertEquals("bar", this.list.get(1));
	}

	@Test
	public void testConstructorCollection() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");

		this.list = new ListImmutable<>(stringList);
		assertEquals(2, this.list.size());
		assertEquals(2, this.list.getCapacity());
		assertEquals("foo", this.list.get(0));
		assertEquals("bar", this.list.get(1));
	}

	@Test
	public void testConstructorRootList() {
		final RootList<String> rootList = new ListLinked<>();
		rootList.add("foo");
		rootList.add("bar");

		this.list = new ListImmutable<>(rootList);
		assertEquals(2, this.list.size());
		assertEquals(Integer.MAX_VALUE, this.list.getCapacity());
		assertEquals("foo", this.list.get(0));
		assertEquals("bar", this.list.get(1));
		assertFalse(this.list.list == rootList);
	}

	@Test
	public void testContains() {
		assertFalse(this.list.contains("foo"));
		this.list = new ListImmutable<>("foo", "bar");
		assertTrue(this.list.contains("foo"));
	}

	@Test
	public void testContainsAll() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");

		assertFalse(this.list.containsAll(stringList));

		this.list = new ListImmutable<>("foo");
		assertFalse(this.list.containsAll(stringList));

		this.list = new ListImmutable<>("foo", "bar");
		assertTrue(this.list.containsAll(stringList));
	}

	@Test
	public void testContainsAny() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");

		assertFalse(this.list.containsAny(stringList));

		this.list = new ListImmutable<>("bar");
		assertTrue(this.list.containsAny(stringList));
	}

	public void testEcho() {
		try {
			this.list.echo("foo");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot add items to a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testEquals() {
		final List<String> testList = new ArrayList<>();
		assertTrue(this.list.equals(testList));

		testList.add("foo");
		assertFalse(this.list.equals(testList));

		this.list = new ListImmutable<>("foo");
		assertTrue(this.list.equals(testList));
	}

	@Test
	public void testExtract() {
		final StringExtractor extractor = new StringExtractor();
		this.list.extract(extractor);
		assertEquals("[]", extractor.toString());

		this.list = new ListImmutable<>("foo");

		extractor.clear();
		this.list.extract(extractor);
		assertEquals("[foo]", extractor.toString());

		this.list = new ListImmutable<>("foo", "bar");

		extractor.clear();
		this.list.extract(extractor);
		assertEquals("[foo,bar]", extractor.toString());
	}

	@Test
	public void testGet() {
		try {
			this.list.get(0);
			fail("Expected java.lang.ArrayIndexOutOfBoundsException was not thrown");
		} catch (final ArrayIndexOutOfBoundsException e) {
		}

		this.list = new ListImmutable<>("foo");
		assertEquals("foo", this.list.get(0));

		try {
			this.list.get(1);
			fail("Expected java.lang.ArrayIndexOutOfBoundsException was not thrown");
		} catch (final ArrayIndexOutOfBoundsException e) {
		}
	}

	@Test
	public void testGetCapacity() {
		assertEquals(0, this.list.getCapacity());
		this.list = new ListImmutable<>("foo", "bar", "xyz", "123", "foo", "bar", "xyz", "123", "foo");
		assertEquals(9, this.list.getCapacity());
	}

	@Test
	public void testGetSize() {
		assertEquals(0, this.list.getSize());
		this.list = new ListImmutable<>("foo");
		assertEquals(1, this.list.getSize());
	}

	@Test
	public void testHashCode() {
		assertEquals(0, this.list.hashCode());

		this.list = new ListImmutable<>("foo");
		assertEquals(101572, this.list.hashCode());

		this.list = new ListImmutable<>("foo", "bar");
		assertEquals(157079, this.list.hashCode());
	}

	@Test
	public void testIndexOf() {
		assertEquals(-1, this.list.indexOf("bar"));

		this.list = new ListImmutable<>("foo");
		assertEquals(-1, this.list.indexOf("bar"));

		this.list = new ListImmutable<>("foo", "bar");
		assertEquals(1, this.list.indexOf("bar"));
	}

	@Test
	public void testInsert() {
		try {
			this.list.insert(1, "xyz");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot add items to a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testInsertAll() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("xyz");
		stringList.add("123");

		try {
			this.list.insertAll(1, stringList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot add items to a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testIsEmpty() {
		assertTrue(this.list.isEmpty());

		this.list = new ListImmutable<>("foo");
		assertFalse(this.list.isEmpty());
	}

	@Test
	public void testIterator() {
		Itemizer<String> itemizer = this.list.iterator();

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
			assertEquals("Cannot remove items from an ImmutableItemizer", e.getMessage());
		}

		this.list = new ListImmutable<>("foo", "bar");
		itemizer = this.list.iterator();

		assertEquals(-1, itemizer.getIndex());
		assertEquals(2, itemizer.getSize());
		assertTrue(itemizer.hasNext());

		String str = itemizer.next();
		assertEquals("foo", str);
		assertEquals(0, itemizer.getIndex());
		assertTrue(itemizer.hasNext());

		str = itemizer.next();
		assertEquals("bar", str);
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
	public void testLast() {
		assertNull(this.list.last());

		this.list = new ListImmutable<>("foo");
		assertEquals("foo", this.list.last());

		this.list = new ListImmutable<>("foo", "bar");
		assertEquals("bar", this.list.last());
	}

	@Test
	public void testLastIndexOf() {
		assertEquals(-1, this.list.lastIndexOf("foo"));

		this.list = new ListImmutable<>("foo");
		assertEquals(0, this.list.lastIndexOf("foo"));

		this.list = new ListImmutable<>("foo", "bar");
		assertEquals(0, this.list.lastIndexOf("foo"));

		this.list = new ListImmutable<>("foo", "bar", "foo");
		assertEquals(2, this.list.lastIndexOf("foo"));
	}

	@Test
	public void testListIterator() {
		try {
			this.list.listIterator();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}
	}

	@Test
	public void testListIteratorWithIndex() {
		try {
			this.list.listIterator(2);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}
	}

	@Test
	public void testRandom() {
		final RNG rng = new RNGKiss(new SeedFactoryConstant(857435, 2768, 984598, 3027694, 104));

		assertNull(this.list.random(rng));

		this.list = new ListImmutable<>("foo", "bar", "xyz", "123");

		assertEquals("xyz", this.list.random(rng));
	}

	@Test
	public void testRemoveAll() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("xyz");
		stringList.add("123");

		try {
			this.list.removeAll(stringList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot remove items from a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testRemoveIndex() {
		try {
			this.list.remove(0);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot remove items from a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testRemoveObject() {
		try {
			this.list.remove("xyz");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot remove items from a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testReplace() {
		try {
			this.list.replace("xyz", "123");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot modify a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testRetainAll() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");

		try {
			this.list.retainAll(stringList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot modify a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testSet() {
		try {
			this.list.set(1, "xyz");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot modify a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testShuffle() {
		final RNG rng = new RNGKiss(new SeedFactoryConstant(37544, 2768, 7346, 3027694, 104));

		try {
			this.list.shuffle(rng);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot modify a ListImmutable", e.getMessage());
		}
	}

	@Test
	public void testSize() {
		assertEquals(0, this.list.size());
		this.list = new ListImmutable<>("foo");
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

		this.list = new ListImmutable<>("foo", "bar", "xyz", "123");

		final RootList<String> subList = this.list.subList(1);
		assertEquals(3, subList.size());
		assertEquals("bar", subList.get(0));
		assertEquals("xyz", subList.get(1));
		assertEquals("123", subList.get(2));
	}

	@Test
	public void testSubListFromIndexToIndex() {
		try {
			this.list.subList(1, 3);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 3, Size: 0", e.getMessage());
		}

		this.list = new ListImmutable<>("foo", "bar", "xyz", "123");

		final RootList<String> subList = this.list.subList(1, 3);
		assertEquals(2, subList.size());
		assertEquals("bar", subList.get(0));
		assertEquals("xyz", subList.get(1));
	}

	@Test
	public void testSubSetFromIndex() {
		try {
			this.list.subset(1);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 1, Size: 0", e.getMessage());
		}

		this.list = new ListImmutable<>("foo", "bar", "xyz", "123");

		final RootSet<String> subset = this.list.subset(1);
		assertEquals(3, subset.size());
		assertFalse(subset.contains("foo"));
		assertTrue(subset.contains("bar"));
		assertTrue(subset.contains("xyz"));
		assertTrue(subset.contains("123"));
	}

	@Test
	public void testSubSetFromIndexToIndex() {
		try {
			this.list.subset(1, 3);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 3, Size: 0", e.getMessage());
		}

		this.list = new ListImmutable<>("foo", "bar", "xyz", "123");

		final RootSet<String> subset = this.list.subset(1, 3);
		assertEquals(2, subset.size());
		assertFalse(subset.contains("foo"));
		assertTrue(subset.contains("bar"));
		assertTrue(subset.contains("xyz"));
		assertFalse(subset.contains("123"));
	}

	@Test
	public void testToArray() {
		Object[] array = this.list.toArray();
		assertNotNull(array);
		assertEquals(0, array.length);

		this.list = new ListImmutable<>("foo", "bar");
		array = this.list.toArray();

		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals("foo", array[0]);
		assertEquals("bar", array[1]);
	}

	@Test
	public void testToArrayWithParameter() {
		String[] array = this.list.toArray(new String[2]);
		assertNotNull(array);
		assertEquals(2, array.length);

		this.list = new ListImmutable<>("foo", "bar");
		array = this.list.toArray(new String[2]);

		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals("foo", array[0]);
		assertEquals("bar", array[1]);
	}

	@Test
	public void testToImmutable() {
		assertEquals(0, this.list.getSize());
		assertEquals(0, this.list.getCapacity());

		this.list = new ListImmutable<>("foo", "bar", "xyz", "123");

		final ListImmutable<String> immutableList = this.list.toImmutable();
		assertEquals(4, immutableList.list.getSize());
		assertEquals(4, immutableList.list.getCapacity());
		assertEquals("foo", immutableList.get(0));
		assertEquals("bar", immutableList.get(1));
		assertEquals("xyz", immutableList.get(2));
		assertEquals("123", immutableList.get(3));

		assertTrue(this.list == immutableList);
	}

	@Test
	public void testToSet() {
		RootSet<String> set = this.list.toSet();
		assertEquals(0, set.size());

		this.list = new ListImmutable<>("foo", "bar", "xyz", "123");

		set = this.list.toSet();
		assertEquals(4, set.size());
		assertTrue(set.contains("foo"));
		assertTrue(set.contains("bar"));
		assertTrue(set.contains("xyz"));
		assertTrue(set.contains("123"));
	}

	@Test
	public void testToString() {
		assertEquals("[]", this.list.toString());

		this.list = new ListImmutable<>("foo");
		assertEquals("[foo]", this.list.toString());

		this.list = new ListImmutable<>("foo", "bar");
		assertEquals("[foo,bar]", this.list.toString());
	}

} // End ListImmutableTest
