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
 * Test the {@link ListArraySorted} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ListArraySortedTest extends TestCase {

	private ListArraySorted<String> list;

	public ListArraySortedTest() {
		super("ListArraySorted");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.list = new ListArraySorted<>();
	}

	@Test
	public void testAdd() {
		final String foo = "foo";
		final String bar = "bar";
		final String cat = "cat";
		final String xyz = "xyz";
		final String jet = "jet";
		final String dog = "dog";
		final String log = "log";

		assertEquals(0, this.list.size);

		this.list.add(foo);
		assertEquals(1, this.list.size);
		assertEquals(foo, this.list.get(0));

		this.list.add(bar);
		assertEquals(2, this.list.size);
		assertEquals(bar, this.list.get(0));
		assertEquals(foo, this.list.get(1));

		this.list.add(cat);
		assertEquals(3, this.list.size);
		assertEquals(bar, this.list.get(0));
		assertEquals(cat, this.list.get(1));
		assertEquals(foo, this.list.get(2));

		this.list.add(xyz);
		assertEquals(4, this.list.size);
		assertEquals(bar, this.list.get(0));
		assertEquals(cat, this.list.get(1));
		assertEquals(foo, this.list.get(2));
		assertEquals(xyz, this.list.get(3));

		this.list.add(jet);
		assertEquals(5, this.list.size);
		assertEquals(bar, this.list.get(0));
		assertEquals(cat, this.list.get(1));
		assertEquals(foo, this.list.get(2));
		assertEquals(jet, this.list.get(3));
		assertEquals(xyz, this.list.get(4));

		this.list.add(foo);
		assertEquals(6, this.list.size);
		assertEquals(bar, this.list.get(0));
		assertEquals(cat, this.list.get(1));
		assertEquals(foo, this.list.get(2));
		assertEquals(foo, this.list.get(3));
		assertEquals(jet, this.list.get(4));
		assertEquals(xyz, this.list.get(5));

		this.list.add(dog);
		assertEquals(7, this.list.size);
		assertEquals(bar, this.list.get(0));
		assertEquals(cat, this.list.get(1));
		assertEquals(dog, this.list.get(2));
		assertEquals(foo, this.list.get(3));
		assertEquals(foo, this.list.get(4));
		assertEquals(jet, this.list.get(5));
		assertEquals(xyz, this.list.get(6));

		this.list.add(log);
		assertEquals(8, this.list.size);
		assertEquals(8, ((Object[]) this.list.values).length);
		assertEquals(bar, this.list.get(0));
		assertEquals(cat, this.list.get(1));
		assertEquals(dog, this.list.get(2));
		assertEquals(foo, this.list.get(3));
		assertEquals(foo, this.list.get(4));
		assertEquals(jet, this.list.get(5));
		assertEquals(log, this.list.get(6));
		assertEquals(xyz, this.list.get(7));

		this.list.add(jet);
		assertEquals(9, this.list.size);
		assertEquals(16, ((Object[]) this.list.values).length);
		assertEquals(bar, this.list.get(0));
		assertEquals(cat, this.list.get(1));
		assertEquals(dog, this.list.get(2));
		assertEquals(foo, this.list.get(3));
		assertEquals(foo, this.list.get(4));
		assertEquals(jet, this.list.get(5));
		assertEquals(jet, this.list.get(6));
		assertEquals(log, this.list.get(7));
		assertEquals(xyz, this.list.get(8));
	}

	@Test
	public void testAddAllArray() {
		String[] array = { "foo", "bar" };
		assertEquals(0, this.list.size);

		this.list.addAll(array, 0, 2);
		assertEquals(2, this.list.size);

		assertEquals("bar", this.list.get(0));
		assertEquals("foo", this.list.get(1));

		array = new String[] { "xyz", "123" };
		this.list.addAll(array, 0, 1);
		assertEquals(3, this.list.size);

		assertEquals("bar", this.list.get(0));
		assertEquals("foo", this.list.get(1));
		assertEquals("xyz", this.list.get(2));

		this.list.addAll(array, 1, 1);
		assertEquals(4, this.list.size);

		assertEquals("123", this.list.get(0));
		assertEquals("bar", this.list.get(1));
		assertEquals("foo", this.list.get(2));
		assertEquals("xyz", this.list.get(3));
	}

	@Test
	public void testAddAllCollection() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");
		assertEquals(0, this.list.size);

		this.list.addAll(stringList);
		assertEquals(2, this.list.size);

		assertEquals("bar", this.list.get(0));
		assertEquals("foo", this.list.get(1));

		stringList.set(0, "xyz");
		stringList.set(1, "123");
		this.list.addAll(stringList);
		assertEquals(4, this.list.size);

		assertEquals("123", this.list.get(0));
		assertEquals("bar", this.list.get(1));
		assertEquals("foo", this.list.get(2));
		assertEquals("xyz", this.list.get(3));
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
			assertEquals("Use the addAll(Collection) function to insert elements into a ListArraySorted", e.getMessage());
		}
	}

	@Test
	public void testAddWithIndex() {
		assertEquals(0, this.list.size);

		this.list.addAll(new String[] { "foo", "bar" }, 0, 2);
		assertEquals(2, this.list.size);

		try {
			this.list.add(1, "xyz");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Use the add(T) function to insert an element into a ListArraySorted", e.getMessage());
		}
	}

	@Test
	public void testClear() {
		assertEquals(0, this.list.size);
		this.list.add("foo");
		assertEquals(1, this.list.size);
		this.list.clear();
		assertEquals(0, this.list.size);
	}

	@Test
	public void testClone() {
		this.list.addAll(new String[] { "foo", "bar" }, 0, 2);
		assertEquals(2, this.list.size);
		assertEquals("bar", this.list.get(0));
		assertEquals("foo", this.list.get(1));

		final ListArraySorted<String> l = this.list.clone();
		assertEquals(2, l.size);
		assertEquals("bar", l.get(0));
		assertEquals("foo", l.get(1));
		assertFalse(this.list == l);
	}

	@Test
	public void testConstructorArray() {
		final ListArraySorted<String> l = new ListArraySorted<>("foo", "bar");
		assertEquals(2, l.size);
		assertEquals(2, ((Object[]) l.values).length);
		assertEquals("bar", l.get(0));
		assertEquals("foo", l.get(1));
	}

	@Test
	public void testConstructorCapacity() {
		ListArraySorted<String> l = new ListArraySorted<>(15);
		assertEquals(0, l.size);
		assertEquals(15, ((Comparable[]) l.values).length);

		// Test minimum capacity of 8
		l = new ListArraySorted<>(7);
		assertEquals(0, l.size);
		assertEquals(8, ((Comparable[]) l.values).length);
	}

	@Test
	public void testConstructorCollection() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");

		final ListArraySorted<String> l = new ListArraySorted<>(stringList);
		assertEquals(2, l.size);
		assertEquals(8, ((Comparable[]) l.values).length);
		assertEquals("bar", l.get(0));
		assertEquals("foo", l.get(1));
	}

	@Test
	public void testConstructorDefault() {
		assertEquals(0, this.list.size);
		assertEquals(8, ((Comparable[]) this.list.values).length);
	}

	@Test
	public void testContains() {
		assertFalse(this.list.contains("foo"));
		this.list.add("foo");
		assertTrue(this.list.contains("foo"));
	}

	@Test
	public void testContainsAll() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");

		assertFalse(this.list.containsAll(stringList));

		this.list.add("foo");
		assertFalse(this.list.containsAll(stringList));

		this.list.add("bar");
		assertTrue(this.list.containsAll(stringList));
	}

	@Test
	public void testContainsAny() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");

		assertFalse(this.list.containsAny(stringList));

		this.list.add("bar");
		assertTrue(this.list.containsAny(stringList));
	}

	public void testEcho() {
		final String str = this.list.echo("foo");

		assertEquals(1, this.list.size);
		assertEquals("foo", this.list.get(0));
		assertEquals("foo", str);
	}

	@Test
	public void testEquals() {
		final List<String> testList = new ArrayList<>();
		assertTrue(this.list.equals(testList));

		testList.add("foo");
		assertFalse(this.list.equals(testList));

		this.list.add("foo");
		assertTrue(this.list.equals(testList));

		testList.add(0, "bar");
		assertFalse(this.list.equals(testList));

		this.list.add("bar");
		assertTrue(this.list.equals(testList));
	}

	@Test
	public void testExtract() {
		final StringExtractor extractor = new StringExtractor();
		this.list.extract(extractor);
		assertEquals("[]", extractor.toString());

		this.list.add("foo");

		extractor.clear();
		this.list.extract(extractor);
		assertEquals("[foo]", extractor.toString());

		this.list.add("bar");

		extractor.clear();
		this.list.extract(extractor);
		assertEquals("[bar,foo]", extractor.toString());
	}

	@Test
	public void testGet() {
		assertNull(this.list.get(0));
		this.list.add("foo");
		assertEquals("foo", this.list.get(0));

		try {
			this.list.get(8);
			fail("Expected java.lang.ArrayIndexOutOfBoundsException was not thrown");
		} catch (final ArrayIndexOutOfBoundsException e) {
		}
	}

	@Test
	public void testGetCapacity() {
		assertEquals(8, this.list.getCapacity());
		assertEquals(((Comparable[]) this.list.values).length, this.list.getCapacity());
		this.list.addAll(new String[] { "foo", "bar", "xyz", "123", "foo", "bar", "xyz", "123", "foo" }, 0, 9);
		assertEquals(13, this.list.getCapacity());
		assertEquals(((Comparable[]) this.list.values).length, this.list.getCapacity());
	}

	@Test
	public void testGetSize() {
		assertEquals(0, this.list.getSize());
		this.list.add("foo");
		assertEquals(1, this.list.getSize());
	}

	@Test
	public void testHashCode() {
		assertEquals(0, this.list.hashCode());

		this.list.add("foo");
		assertEquals(101572, this.list.hashCode());

		this.list.add("bar");
		assertEquals(226536, this.list.hashCode());
	}

	@Test
	public void testIndexOf() {
		assertEquals(-1, this.list.indexOf("foo"));
		assertEquals(-1, this.list.indexOf("bar"));

		this.list.add("foo");
		assertEquals(0, this.list.indexOf("foo"));
		assertEquals(-1, this.list.indexOf("bar"));

		this.list.add("bar");
		assertEquals(0, this.list.indexOf("bar"));
		assertEquals(1, this.list.indexOf("foo"));

		this.list.add("xyz");
		this.list.add("123");
		assertEquals(1, this.list.indexOf("bar"));
		assertEquals(2, this.list.indexOf("foo"));

		this.list.add("foo");
		assertEquals(2, this.list.indexOf("foo"));

		this.list.add("dog");
		assertEquals(3, this.list.indexOf("foo"));

		this.list.add("jet");
		assertEquals(3, this.list.indexOf("foo"));
	}

	@Test
	public void testInsert() {
		assertEquals(0, this.list.size);

		this.list.addAll(new String[] { "foo", "bar" }, 0, 2);
		assertEquals(2, this.list.size);

		try {
			this.list.insert(1, "xyz");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Use the add(T) function to insert an element into a ListArraySorted", e.getMessage());
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
			assertEquals("Use the addAll(Collection) function to insert elements into a ListArraySorted", e.getMessage());
		}
	}

	@Test
	public void testIsEmpty() {
		assertTrue(this.list.isEmpty());

		this.list.add("foo");
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
		}

		this.list.add("foo");
		this.list.add("bar");
		itemizer = this.list.iterator();

		assertEquals(-1, itemizer.getIndex());
		assertEquals(2, itemizer.getSize());
		assertTrue(itemizer.hasNext());

		String str = itemizer.next();
		assertEquals("bar", str);
		assertEquals(0, itemizer.getIndex());
		assertTrue(itemizer.hasNext());

		str = itemizer.next();
		assertEquals("foo", str);
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

		this.list.add("foo");
		assertEquals("foo", this.list.last());

		this.list.add("bar");
		assertEquals("foo", this.list.last());
	}

	@Test
	public void testLastIndexOf() {
		assertEquals(-1, this.list.lastIndexOf("foo"));

		this.list.add("foo");
		assertEquals(0, this.list.lastIndexOf("foo"));

		this.list.add("bar");
		assertEquals(1, this.list.lastIndexOf("foo"));

		this.list.add("foo");
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

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		assertEquals("foo", this.list.random(rng));
	}

	@Test
	public void testRemoveAll() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("xyz");
		stringList.add("123");

		assertFalse(this.list.removeAll(stringList));

		this.list.add("foo");
		this.list.add("bar");

		assertFalse(this.list.removeAll(stringList));

		stringList.add("bar");
		assertTrue(this.list.removeAll(stringList));
		assertEquals(1, this.list.size);
		assertEquals("foo", this.list.get(0));
	}

	@Test
	public void testRemoveIndex() {
		try {
			this.list.remove(0);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 0, Size: 0", e.getMessage());
		}

		this.list.add("foo");
		this.list.add("bar");

		final String str = this.list.remove(0);
		assertEquals("bar", str);
		assertEquals(1, this.list.size);
		assertEquals("foo", this.list.get(0));
	}

	@Test
	public void testRemoveObject() {
		assertFalse(this.list.remove("foo"));

		this.list.add("foo");
		this.list.add("bar");

		assertFalse(this.list.remove("xyz"));
		assertTrue(this.list.remove("bar"));
		assertEquals(1, this.list.size);
		assertEquals("foo", this.list.get(0));
	}

	@Test
	public void testReplace() {
		assertFalse(this.list.replace("xyz", "123"));

		this.list.add("foo");
		this.list.add("bar");

		assertFalse(this.list.replace("xyz", "123"));
		assertTrue(this.list.replace("foo", "xyz"));
		assertEquals(2, this.list.size);
		assertEquals("bar", this.list.get(0));
		assertEquals("xyz", this.list.get(1));
	}

	@Test
	public void testRetainAll() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");

		assertFalse(this.list.retainAll(stringList));

		this.list.add("foo");
		this.list.add("bar");

		assertFalse(this.list.retainAll(stringList));

		stringList.set(1, "xyz");
		assertTrue(this.list.retainAll(stringList));
		assertEquals(1, this.list.size);
		assertEquals("foo", this.list.get(0));
		assertNull(this.list.get(1));
	}

	@Test
	public void testSet() {
		try {
			this.list.set(1, "xyz");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Use the add(T) function to put an element into a ListArraySorted", e.getMessage());
		}
	}

	@Test
	public void testShuffle() {
		final RNG rng = new RNGKiss(new SeedFactoryConstant(37544, 2768, 7346, 3027694, 104));

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		try {
			this.list.shuffle(rng);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot shuffle the elements of a ListArraySorted", e.getMessage());
		}
	}

	@Test
	public void testSize() {
		assertEquals(0, this.list.size());
		this.list.add("foo");
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

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		final ListArraySorted<String> subList = this.list.subList(1);
		assertEquals(3, subList.size);
		assertEquals("bar", subList.get(0));
		assertEquals("foo", subList.get(1));
		assertEquals("xyz", subList.get(2));
	}

	@Test
	public void testSubListFromIndexToIndex() {
		try {
			this.list.subList(1, 3);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 3, Size: 0", e.getMessage());
		}

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		final ListArraySorted<String> subList = this.list.subList(1, 3);
		assertEquals(2, subList.size);
		assertEquals("bar", subList.get(0));
		assertEquals("foo", subList.get(1));
	}

	@Test
	public void testSubSetFromIndex() {
		try {
			this.list.subset(1);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 1, Size: 0", e.getMessage());
		}

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		final SetHashed<String> subset = this.list.subset(1);
		assertEquals(3, subset.size);
		assertFalse(subset.contains("123"));
		assertTrue(subset.contains("bar"));
		assertTrue(subset.contains("foo"));
		assertTrue(subset.contains("xyz"));
	}

	@Test
	public void testSubSetFromIndexToIndex() {
		try {
			this.list.subset(1, 3);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 3, Size: 0", e.getMessage());
		}

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		final SetHashed<String> subset = this.list.subset(1, 3);
		assertEquals(2, subset.size);
		assertFalse(subset.contains("123"));
		assertTrue(subset.contains("bar"));
		assertTrue(subset.contains("foo"));
		assertFalse(subset.contains("xyz"));
	}

	@Test
	public void testToArray() {
		@SuppressWarnings("rawtypes")
		Comparable[] array = this.list.toArray();
		assertNotNull(array);
		assertEquals(0, array.length);

		this.list.add("foo");
		this.list.add("bar");
		array = this.list.toArray();

		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals("bar", array[0]);
		assertEquals("foo", array[1]);
	}

	@Test
	public void testToArrayWithParameter() {
		String[] array = this.list.toArray(new String[1]);
		assertNotNull(array);
		assertEquals(1, array.length);

		this.list.add("foo");
		this.list.add("bar");
		array = this.list.toArray(new String[2]);

		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals("bar", array[0]);
		assertEquals("foo", array[1]);
	}

	@Test
	public void testToImmutable() {
		ListImmutable<String> immutableList = this.list.toImmutable();
		assertEquals(0, immutableList.list.getSize());
		assertEquals(0, immutableList.list.getCapacity());

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		immutableList = this.list.toImmutable();
		assertEquals(4, immutableList.list.getSize());
		assertEquals(4, immutableList.list.getCapacity());
		assertEquals("123", immutableList.get(0));
		assertEquals("bar", immutableList.get(1));
		assertEquals("foo", immutableList.get(2));
		assertEquals("xyz", immutableList.get(3));

		assertFalse(this.list == immutableList.list);
	}

	@Test
	public void testToSet() {
		SetHashed<String> set = this.list.toSet();
		assertEquals(0, set.size);

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		set = this.list.toSet();
		assertEquals(4, set.size);
		assertTrue(set.contains("foo"));
		assertTrue(set.contains("bar"));
		assertTrue(set.contains("xyz"));
		assertTrue(set.contains("123"));
	}

	@Test
	public void testToString() {
		assertEquals("[]", this.list.toString());

		this.list.add("foo");
		assertEquals("[foo]", this.list.toString());

		this.list.add("bar");
		assertEquals("[bar,foo]", this.list.toString());
	}

} // End ListArrayTest
