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
 * Test the {@link ListLinked} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ListLinkedTest extends TestCase {

	private ListLinked<String> list;

	public ListLinkedTest() {
		super("ListLinked");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.list = new ListLinked<>();
	}

	@Test
	public void testAdd() {
		assertEquals(0, this.list.size);
		assertNull(this.list.head);
		assertNull(this.list.tail);

		this.list.add("foo");
		assertNotNull(this.list.head);
		assertNotNull(this.list.tail);
		assertEquals(1, this.list.size);
		assertEquals("foo", this.list.get(0));

		this.list.add("bar");
		assertEquals(2, this.list.size);
		assertEquals("bar", this.list.get(1));
	}

	@Test
	public void testAddAllArray() {
		final String[] array = { "foo", "bar" };
		assertEquals(0, this.list.size);
		assertNull(this.list.head);
		assertNull(this.list.tail);

		this.list.addAll(array, 0, 2);
		assertNotNull(this.list.head);
		assertNotNull(this.list.tail);
		assertEquals(2, this.list.size);

		assertEquals("foo", this.list.get(0));
		assertEquals("bar", this.list.get(1));

		this.list.addAll(array, 0, 1);
		assertEquals(3, this.list.size);

		assertEquals("foo", this.list.get(0));
		assertEquals("bar", this.list.get(1));
		assertEquals("foo", this.list.get(2));

		this.list.addAll(array, 1, 1);
		assertEquals(4, this.list.size);

		assertEquals("foo", this.list.get(0));
		assertEquals("bar", this.list.get(1));
		assertEquals("foo", this.list.get(2));
		assertEquals("bar", this.list.get(3));
	}

	@Test
	public void testAddAllCollection() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");

		assertEquals(0, this.list.size);
		assertNull(this.list.head);
		assertNull(this.list.tail);

		this.list.addAll(stringList);
		assertEquals(2, this.list.size);
		assertNotNull(this.list.head);
		assertNotNull(this.list.tail);

		assertEquals("foo", this.list.get(0));
		assertEquals("bar", this.list.get(1));

		this.list.addAll(stringList);
		assertEquals(4, this.list.size);

		assertEquals("foo", this.list.get(0));
		assertEquals("bar", this.list.get(1));
		assertEquals("foo", this.list.get(2));
		assertEquals("bar", this.list.get(3));
	}

	@Test
	public void testAddAllCollectionWithIndex() {
		assertEquals(0, this.list.size);
		assertNull(this.list.head);
		assertNull(this.list.tail);

		this.list.addAll(new String[] { "foo", "bar" }, 0, 2);
		assertEquals(2, this.list.size);

		final List<String> stringList = new ArrayList<>();
		stringList.add("xyz");
		stringList.add("123");
		this.list.addAll(1, stringList);
		assertEquals(4, this.list.size);

		assertEquals("foo", this.list.get(0));
		assertEquals("xyz", this.list.get(1));
		assertEquals("123", this.list.get(2));
		assertEquals("bar", this.list.get(3));
	}

	@Test
	public void testAddWithIndex() {
		assertEquals(0, this.list.size);
		assertNull(this.list.head);
		assertNull(this.list.tail);

		this.list.addAll(new String[] { "foo", "bar" }, 0, 2);
		assertEquals(2, this.list.size);

		this.list.add(1, "xyz");
		assertEquals(3, this.list.size);

		assertEquals("foo", this.list.get(0));
		assertEquals("xyz", this.list.get(1));
		assertEquals("bar", this.list.get(2));
	}

	@Test
	public void testClear() {
		assertEquals(0, this.list.size);
		assertNull(this.list.head);
		assertNull(this.list.tail);

		this.list.add("foo");
		assertEquals(1, this.list.size);
		assertNotNull(this.list.head);
		assertNotNull(this.list.tail);

		this.list.clear();
		assertEquals(0, this.list.size);
		assertNull(this.list.head);
		assertNull(this.list.tail);
	}

	@Test
	public void testClone() {
		this.list.addAll(new String[] { "foo", "bar" }, 0, 2);
		assertEquals(2, this.list.size);
		assertEquals("foo", this.list.get(0));
		assertEquals("bar", this.list.get(1));

		final ListLinked<String> l = this.list.clone();
		assertEquals(2, l.size);
		assertEquals("foo", l.get(0));
		assertEquals("bar", l.get(1));
		assertFalse(this.list == l);
	}

	@Test
	public void testConstructorArray() {
		final ListLinked<String> l = new ListLinked<>("foo", "bar");
		assertEquals(2, l.size);
		assertEquals("foo", l.get(0));
		assertEquals("bar", l.get(1));
	}

	@Test
	public void testConstructorCollection() {
		final List<String> stringList = new ArrayList<>();
		stringList.add("foo");
		stringList.add("bar");

		final ListLinked<String> l = new ListLinked<>(stringList);
		assertEquals(2, l.size);
		assertEquals("foo", l.get(0));
		assertEquals("bar", l.get(1));
	}

	@Test
	public void testConstructorDefault() {
		assertEquals(0, this.list.size);
		assertNull(this.list.head);
		assertNull(this.list.tail);
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
		assertEquals("[foo,bar]", extractor.toString());
	}

	@Test
	public void testGet() {
		try {
			this.list.get(0);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 0, Size: 0", e.getMessage());
		}

		assertNull(this.list.head);
		assertNull(this.list.tail);

		this.list.add("foo");
		assertEquals("foo", this.list.get(0));

		try {
			this.list.get(1);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 1, Size: 1", e.getMessage());
		}
	}

	@Test
	public void testGetCapacity() {
		assertEquals(Integer.MAX_VALUE, this.list.getCapacity());
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
		assertEquals(157079, this.list.hashCode());
	}

	@Test
	public void testIndexOf() {
		assertEquals(-1, this.list.indexOf("bar"));

		this.list.add("foo");
		assertEquals(-1, this.list.indexOf("bar"));

		this.list.add("bar");
		assertEquals(1, this.list.indexOf("bar"));
	}

	@Test
	public void testInsert() {
		assertEquals(0, this.list.size);

		this.list.addAll(new String[] { "foo", "bar" }, 0, 2);
		assertEquals(2, this.list.size);

		this.list.insert(1, "xyz");
		assertEquals(3, this.list.size);

		assertEquals("foo", this.list.get(0));
		assertEquals("xyz", this.list.get(1));
		assertEquals("bar", this.list.get(2));
	}

	@Test
	public void testInsertAll() {
		assertEquals(0, this.list.size);

		this.list.addAll(new String[] { "foo", "bar" }, 0, 2);
		assertEquals(2, this.list.size);

		final List<String> stringList = new ArrayList<>();
		stringList.add("xyz");
		stringList.add("123");
		this.list.insertAll(1, stringList);
		assertEquals(4, this.list.size);

		assertEquals("foo", this.list.get(0));
		assertEquals("xyz", this.list.get(1));
		assertEquals("123", this.list.get(2));
		assertEquals("bar", this.list.get(3));
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

		this.list.add("foo");
		assertEquals("foo", this.list.last());

		this.list.add("bar");
		assertEquals("bar", this.list.last());
	}

	@Test
	public void testLastIndexOf() {
		assertEquals(-1, this.list.lastIndexOf("foo"));

		this.list.add("foo");
		assertEquals(0, this.list.lastIndexOf("foo"));

		this.list.add("bar");
		assertEquals(0, this.list.lastIndexOf("foo"));

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

		assertEquals("xyz", this.list.random(rng));
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

		stringList.add("foo");
		assertTrue(this.list.removeAll(stringList));
		assertEquals(1, this.list.size);
		assertEquals("bar", this.list.get(0));
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
		this.list.add("xyz");
		this.list.add("123");

		String str = this.list.remove(0);
		assertEquals("foo", str);
		assertEquals(3, this.list.size);
		assertEquals("bar", this.list.get(0));
		assertEquals("xyz", this.list.get(1));
		assertEquals("123", this.list.get(2));

		str = this.list.remove(1);
		assertEquals("xyz", str);
		assertEquals(2, this.list.size);
		assertEquals("bar", this.list.get(0));
		assertEquals("123", this.list.get(1));

		str = this.list.remove(1);
		assertEquals("123", str);
		assertEquals(1, this.list.size);
		assertEquals("bar", this.list.get(0));

		str = this.list.remove(0);
		assertEquals("bar", str);
		assertEquals(0, this.list.size);
		assertNull(this.list.head);
		assertNull(this.list.tail);
	}

	@Test
	public void testRemoveObject() {
		assertFalse(this.list.remove("foo"));

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		assertTrue(this.list.remove("foo"));
		assertEquals(3, this.list.size);
		assertEquals("bar", this.list.get(0));
		assertEquals("xyz", this.list.get(1));
		assertEquals("123", this.list.get(2));

		assertTrue(this.list.remove("xyz"));
		assertEquals(2, this.list.size);
		assertEquals("bar", this.list.get(0));
		assertEquals("123", this.list.get(1));

		assertTrue(this.list.remove("123"));
		assertEquals(1, this.list.size);
		assertEquals("bar", this.list.get(0));

		assertTrue(this.list.remove("bar"));
		assertEquals(0, this.list.size);
		assertNull(this.list.head);
		assertNull(this.list.tail);
	}

	@Test
	public void testReplace() {
		assertFalse(this.list.replace("xyz", "123"));

		this.list.add("foo");
		this.list.add("bar");

		assertFalse(this.list.replace("xyz", "123"));
		assertTrue(this.list.replace("foo", "xyz"));
		assertEquals(2, this.list.size);
		assertEquals("xyz", this.list.get(0));
		assertEquals("bar", this.list.get(1));
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

		stringList.set(0, "xyz");
		assertTrue(this.list.retainAll(stringList));
		assertEquals(1, this.list.size);
		assertEquals("bar", this.list.get(0));
	}

	@Test
	public void testSet() {
		try {
			this.list.set(0, "xyz");
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 0, Size: 0", e.getMessage());
		}

		this.list.add("foo");
		this.list.add("bar");

		final String str = this.list.set(1, "xyz");
		assertEquals("bar", str);
		assertEquals(2, this.list.size);
		assertEquals("foo", this.list.get(0));
		assertEquals("xyz", this.list.get(1));
	}

	@Test
	public void testShuffle() {
		final RNG rng = new RNGKiss(new SeedFactoryConstant(37544, 2768, 7346, 3027694, 104));

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		this.list.shuffle(rng);

		assertEquals("foo", this.list.get(0));
		assertEquals("123", this.list.get(1));
		assertEquals("bar", this.list.get(2));
		assertEquals("xyz", this.list.get(3));
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

		final ListLinked<String> subList = this.list.subList(1);
		assertEquals(3, subList.size);
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

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		final ListLinked<String> subList = this.list.subList(1, 3);
		assertEquals(2, subList.size);
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

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		final SetHashed<String> subset = this.list.subset(1);
		assertEquals(3, subset.size);
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

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		final SetHashed<String> subset = this.list.subset(1, 3);
		assertEquals(2, subset.size);
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

		this.list.add("foo");
		this.list.add("bar");
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

		this.list.add("foo");
		this.list.add("bar");
		array = this.list.toArray(new String[2]);

		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals("foo", array[0]);
		assertEquals("bar", array[1]);
	}

	@Test
	public void testToImmutable() {
		ListImmutable<String> immutableList = this.list.toImmutable();
		assertEquals(0, immutableList.list.getSize());
		assertEquals(Integer.MAX_VALUE, immutableList.list.getCapacity());

		this.list.add("foo");
		this.list.add("bar");
		this.list.add("xyz");
		this.list.add("123");

		immutableList = this.list.toImmutable();
		assertEquals(4, immutableList.list.getSize());
		assertEquals(Integer.MAX_VALUE, immutableList.list.getCapacity());
		assertEquals("foo", immutableList.get(0));
		assertEquals("bar", immutableList.get(1));
		assertEquals("xyz", immutableList.get(2));
		assertEquals("123", immutableList.get(3));

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
		assertEquals("[foo,bar]", this.list.toString());
	}

} // End ListLinkedTest
