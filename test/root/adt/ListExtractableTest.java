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
import root.lang.Extractable;
import root.lang.FastString;
import root.lang.Itemizer;
import root.lang.StringExtractor;
import root.random.RNG;
import root.random.RNGKiss;
import root.random.SeedFactoryConstant;

/**
 * Test the {@link ListExtractable} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ListExtractableTest extends TestCase {

	private ListExtractable<FastString> list;

	public ListExtractableTest() {
		super("ListExtractable");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.list = new ListExtractable<>();
	}

	@Test
	public void testAdd() {
		final FastString foo = new FastString("foo");

		assertEquals(0, this.list.size());

		this.list.add(foo);
		assertEquals(1, this.list.size());
		assertEquals(foo, this.list.get(0));

		this.list.add(foo);
		assertEquals(2, this.list.size());
		assertEquals(foo, this.list.get(1));

		this.list.add(foo);
		assertEquals(3, this.list.size());
		assertEquals(foo, this.list.get(2));

		this.list.add(foo);
		assertEquals(4, this.list.size());
		assertEquals(foo, this.list.get(3));

		this.list.add(foo);
		assertEquals(5, this.list.size());
		assertEquals(foo, this.list.get(4));

		this.list.add(foo);
		assertEquals(6, this.list.size());
		assertEquals(foo, this.list.get(5));

		this.list.add(foo);
		assertEquals(7, this.list.size());
		assertEquals(foo, this.list.get(6));

		this.list.add(foo);
		assertEquals(8, this.list.size());
		assertEquals(8, this.list.getCapacity());
		assertEquals(foo, this.list.get(7));

		this.list.add(foo);
		assertEquals(9, this.list.size());
		assertEquals(16, this.list.getCapacity());
		assertEquals(foo, this.list.get(8));
	}

	@Test
	public void testAddAllArray() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString[] array = { foo, bar };
		assertEquals(0, this.list.size());

		this.list.addAll(array, 0, 2);
		assertEquals(2, this.list.size());

		assertEquals(foo, this.list.get(0));
		assertEquals(bar, this.list.get(1));

		this.list.addAll(array, 0, 1);
		assertEquals(3, this.list.size());

		assertEquals(foo, this.list.get(0));
		assertEquals(bar, this.list.get(1));
		assertEquals(foo, this.list.get(2));

		this.list.addAll(array, 1, 1);
		assertEquals(4, this.list.size());

		assertEquals(foo, this.list.get(0));
		assertEquals(bar, this.list.get(1));
		assertEquals(foo, this.list.get(2));
		assertEquals(bar, this.list.get(3));
	}

	@Test
	public void testAddAllCollection() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final List<FastString> stringList = new ArrayList<>();
		stringList.add(foo);
		stringList.add(bar);
		assertEquals(0, this.list.size());

		this.list.addAll(stringList);
		assertEquals(2, this.list.size());

		assertEquals(foo, this.list.get(0));
		assertEquals(bar, this.list.get(1));

		this.list.addAll(stringList);
		assertEquals(4, this.list.size());

		assertEquals(foo, this.list.get(0));
		assertEquals(bar, this.list.get(1));
		assertEquals(foo, this.list.get(2));
		assertEquals(bar, this.list.get(3));
	}

	@Test
	public void testAddAllCollectionWithIndex() {
		assertEquals(0, this.list.size());

		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		this.list.addAll(new FastString[] { foo, bar }, 0, 2);
		assertEquals(2, this.list.size());

		final List<FastString> stringList = new ArrayList<>();
		stringList.add(xyz);
		stringList.add(oneTwoThree);
		this.list.addAll(1, stringList);
		assertEquals(4, this.list.size());

		assertEquals(foo, this.list.get(0));
		assertEquals(xyz, this.list.get(1));
		assertEquals(oneTwoThree, this.list.get(2));
		assertEquals(bar, this.list.get(3));
	}

	@Test
	public void testAddWithIndex() {
		assertEquals(0, this.list.size());

		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");

		this.list.addAll(new FastString[] { foo, bar }, 0, 2);
		assertEquals(2, this.list.size());

		this.list.add(1, xyz);
		assertEquals(3, this.list.size());

		assertEquals(foo, this.list.get(0));
		assertEquals(xyz, this.list.get(1));
		assertEquals(bar, this.list.get(2));
	}

	@Test
	public void testClear() {
		assertEquals(0, this.list.size());
		this.list.add(new FastString("foo"));
		assertEquals(1, this.list.size());
		this.list.clear();
		assertEquals(0, this.list.size());
	}

	@Test
	public void testClone() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		this.list.addAll(new FastString[] { foo, bar }, 0, 2);
		assertEquals(2, this.list.size());
		assertEquals(foo, this.list.get(0));
		assertEquals(bar, this.list.get(1));

		final ListExtractable<FastString> l = this.list.clone();
		assertEquals(2, l.size());
		assertEquals(foo, l.get(0));
		assertEquals(bar, l.get(1));
		assertFalse(this.list == l);
		assertFalse(this.list.list == l.list);
	}

	@Test
	public void testConstructorArray() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		final ListExtractable<FastString> l = new ListExtractable<>(foo, bar);
		assertEquals(2, l.size());
		assertEquals(2, l.getCapacity());
		assertEquals(foo, l.get(0));
		assertEquals(bar, l.get(1));
	}

	@Test
	public void testConstructorCapacity() {
		ListExtractable<FastString> l = new ListExtractable<>(15);
		assertEquals(0, l.size());
		assertEquals(15, l.getCapacity());

		// Test minimum capacity of 8
		l = new ListExtractable<>(7);
		assertEquals(0, l.size());
		assertEquals(8, l.getCapacity());
	}

	@Test
	public void testConstructorCollection() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		final List<FastString> stringList = new ArrayList<>();
		stringList.add(foo);
		stringList.add(bar);

		final ListExtractable<FastString> l = new ListExtractable<>(stringList);
		assertEquals(2, l.size());
		assertEquals(8, l.getCapacity());
		assertEquals(foo, l.get(0));
		assertEquals(bar, l.get(1));
	}

	@Test
	public void testConstructorDefault() {
		assertEquals(0, this.list.size());
		assertEquals(8, this.list.getCapacity());
	}

	@Test
	public void testConstructorRootList() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		final RootList<FastString> rootList = new ListLinked<>();
		rootList.add(foo);
		rootList.add(bar);

		final ListExtractable<FastString> l = new ListExtractable<>(rootList);
		assertEquals(2, l.size());
		assertEquals(Integer.MAX_VALUE, l.getCapacity());
		assertEquals(foo, l.get(0));
		assertEquals(bar, l.get(1));
		assertTrue(l.list == rootList);
	}

	@Test
	public void testContains() {
		final FastString foo = new FastString("foo");

		assertFalse(this.list.contains(foo));
		this.list.add(foo);
		assertTrue(this.list.contains(foo));
	}

	@Test
	public void testContainsAll() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		final List<FastString> stringList = new ArrayList<>();
		stringList.add(foo);
		stringList.add(bar);

		assertFalse(this.list.containsAll(stringList));

		this.list.add(foo);
		assertFalse(this.list.containsAll(stringList));

		this.list.add(bar);
		assertTrue(this.list.containsAll(stringList));
	}

	@Test
	public void testContainsAny() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		final List<FastString> stringList = new ArrayList<>();
		stringList.add(foo);
		stringList.add(bar);

		assertFalse(this.list.containsAny(stringList));

		this.list.add(bar);
		assertTrue(this.list.containsAny(stringList));
	}

	public void testEcho() {
		final FastString foo = this.list.echo(new FastString("foo"));

		assertEquals(1, this.list.size());
		assertEquals(foo, this.list.get(0));
		assertEquals(new FastString("foo"), foo);
	}

	@Test
	public void testEquals() {
		final FastString foo = new FastString("foo");

		final List<FastString> testList = new ArrayList<>();
		assertTrue(this.list.equals(testList));

		testList.add(foo);
		assertFalse(this.list.equals(testList));

		this.list.add(foo);
		assertTrue(this.list.equals(testList));
	}

	@Test
	public void testExtract() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		final StringExtractor extractor = new StringExtractor();
		this.list.extract(extractor);
		assertEquals("[]", extractor.toString());

		this.list.add(foo);

		extractor.clear();
		this.list.extract(extractor);
		assertEquals("[foo]", extractor.toString());

		this.list.add(bar);

		extractor.clear();
		this.list.extract(extractor);
		assertEquals("[foo,bar]", extractor.toString());
	}

	@Test
	public void testGet() {
		final FastString foo = new FastString("foo");

		assertNull(this.list.get(0));
		this.list.add(foo);
		assertEquals(foo, this.list.get(0));

		try {
			this.list.get(8);
			fail("Expected java.lang.ArrayIndexOutOfBoundsException was not thrown");
		} catch (final ArrayIndexOutOfBoundsException e) {
		}
	}

	@Test
	public void testGetCapacity() {
		final FastString foo = new FastString("foo");

		assertEquals(8, this.list.getCapacity());
		assertEquals(this.list.list.getCapacity(), this.list.getCapacity());

		this.list.addAll(new FastString[] { foo, foo, foo, foo, foo, foo, foo, foo, foo }, 0, 9);

		assertEquals(13, this.list.getCapacity());
		assertEquals(this.list.list.getCapacity(), this.list.getCapacity());
	}

	@Test
	public void testGetSize() {
		assertEquals(0, this.list.getSize());
		this.list.add(new FastString("foo"));
		assertEquals(1, this.list.getSize());
	}

	@Test
	public void testHashCode() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		assertEquals(0, this.list.hashCode());

		this.list.add(foo);
		assertEquals(101572, this.list.hashCode());

		this.list.add(bar);
		assertEquals(157079, this.list.hashCode());
	}

	@Test
	public void testIndexOf() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		assertEquals(-1, this.list.indexOf(bar));

		this.list.add(foo);
		assertEquals(-1, this.list.indexOf(bar));

		this.list.add(bar);
		assertEquals(1, this.list.indexOf(bar));
	}

	@Test
	public void testInsert() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");

		assertEquals(0, this.list.size());

		this.list.addAll(new FastString[] { foo, bar }, 0, 2);
		assertEquals(2, this.list.size());

		this.list.insert(1, xyz);
		assertEquals(3, this.list.size());

		assertEquals(foo, this.list.get(0));
		assertEquals(xyz, this.list.get(1));
		assertEquals(bar, this.list.get(2));
	}

	@Test
	public void testInsertAll() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		assertEquals(0, this.list.size());

		this.list.addAll(new FastString[] { foo, bar }, 0, 2);
		assertEquals(2, this.list.size());

		final List<FastString> stringList = new ArrayList<>();
		stringList.add(xyz);
		stringList.add(oneTwoThree);
		this.list.insertAll(1, stringList);
		assertEquals(4, this.list.size());

		assertEquals(foo, this.list.get(0));
		assertEquals(xyz, this.list.get(1));
		assertEquals(oneTwoThree, this.list.get(2));
		assertEquals(bar, this.list.get(3));
	}

	@Test
	public void testIsEmpty() {
		assertTrue(this.list.isEmpty());

		this.list.add(new FastString("foo"));
		assertFalse(this.list.isEmpty());
	}

	@Test
	public void testIterator() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		Itemizer<FastString> itemizer = this.list.iterator();

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

		this.list.add(foo);
		this.list.add(bar);
		itemizer = this.list.iterator();

		assertEquals(-1, itemizer.getIndex());
		assertEquals(2, itemizer.getSize());
		assertTrue(itemizer.hasNext());

		FastString str = itemizer.next();
		assertEquals(foo, str);
		assertEquals(0, itemizer.getIndex());
		assertTrue(itemizer.hasNext());

		str = itemizer.next();
		assertEquals(bar, str);
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
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		assertNull(this.list.last());

		this.list.add(foo);
		assertEquals(foo, this.list.last());

		this.list.add(bar);
		assertEquals(bar, this.list.last());
	}

	@Test
	public void testLastIndexOf() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		assertEquals(-1, this.list.lastIndexOf(foo));

		this.list.add(foo);
		assertEquals(0, this.list.lastIndexOf(foo));

		this.list.add(bar);
		assertEquals(0, this.list.lastIndexOf(foo));

		this.list.add(foo);
		assertEquals(2, this.list.lastIndexOf(foo));
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
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		final RNG rng = new RNGKiss(new SeedFactoryConstant(857435, 2768, 984598, 3027694, 104));

		assertNull(this.list.random(rng));

		this.list.add(foo);
		this.list.add(bar);
		this.list.add(xyz);
		this.list.add(oneTwoThree);

		assertEquals(xyz, this.list.random(rng));
	}

	@Test
	public void testRemoveAll() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		final List<FastString> stringList = new ArrayList<>();
		stringList.add(xyz);
		stringList.add(oneTwoThree);

		assertFalse(this.list.removeAll(stringList));

		this.list.add(foo);
		this.list.add(bar);

		assertFalse(this.list.removeAll(stringList));

		stringList.add(foo);
		assertTrue(this.list.removeAll(stringList));
		assertEquals(1, this.list.size());
		assertEquals(bar, this.list.get(0));
	}

	@Test
	public void testRemoveIndex() {
		try {
			this.list.remove(0);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 0, Size: 0", e.getMessage());
		}

		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		this.list.add(foo);
		this.list.add(bar);

		final FastString str = this.list.remove(0);
		assertEquals(foo, str);
		assertEquals(1, this.list.size());
		assertEquals(bar, this.list.get(0));
	}

	@Test
	public void testRemoveObject() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");

		assertFalse(this.list.remove(foo));

		this.list.add(foo);
		this.list.add(bar);

		assertFalse(this.list.remove(xyz));
		assertTrue(this.list.remove(foo));
		assertEquals(1, this.list.size());
		assertEquals(bar, this.list.get(0));
	}

	@Test
	public void testReplace() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		assertFalse(this.list.replace(xyz, oneTwoThree));

		this.list.add(foo);
		this.list.add(bar);

		assertFalse(this.list.replace(xyz, oneTwoThree));
		assertTrue(this.list.replace(foo, xyz));
		assertEquals(2, this.list.size());
		assertEquals(xyz, this.list.get(0));
		assertEquals(bar, this.list.get(1));
	}

	@Test
	public void testRetainAll() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");

		final List<FastString> stringList = new ArrayList<>();
		stringList.add(foo);
		stringList.add(bar);

		assertFalse(this.list.retainAll(stringList));

		this.list.add(foo);
		this.list.add(bar);

		assertFalse(this.list.retainAll(stringList));

		stringList.set(0, xyz);
		assertTrue(this.list.retainAll(stringList));
		assertEquals(1, this.list.size());
		assertEquals(bar, this.list.get(0));
		assertNull(this.list.get(1));
	}

	@Test
	public void testSet() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");

		try {
			this.list.set(0, xyz);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 0, Size: 0", e.getMessage());
		}

		this.list.add(foo);
		this.list.add(bar);

		final FastString str = this.list.set(1, xyz);
		assertEquals(bar, str);
		assertEquals(2, this.list.size());
		assertEquals(foo, this.list.get(0));
		assertEquals(xyz, this.list.get(1));
	}

	@Test
	public void testShuffle() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		final RNG rng = new RNGKiss(new SeedFactoryConstant(37544, 2768, 7346, 3027694, 104));

		this.list.add(foo);
		this.list.add(bar);
		this.list.add(xyz);
		this.list.add(oneTwoThree);

		this.list.shuffle(rng);

		assertEquals(foo, this.list.get(0));
		assertEquals(oneTwoThree, this.list.get(1));
		assertEquals(bar, this.list.get(2));
		assertEquals(xyz, this.list.get(3));
	}

	@Test
	public void testSize() {
		assertEquals(0, this.list.size());
		this.list.add(new FastString("foo"));
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

		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		this.list.add(foo);
		this.list.add(bar);
		this.list.add(xyz);
		this.list.add(oneTwoThree);

		final ListExtractable<FastString> subList = this.list.subList(1);
		assertEquals(3, subList.size());
		assertEquals(bar, subList.get(0));
		assertEquals(xyz, subList.get(1));
		assertEquals(oneTwoThree, subList.get(2));
	}

	@Test
	public void testSubListFromIndexToIndex() {
		try {
			this.list.subList(1, 3);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 3, Size: 0", e.getMessage());
		}

		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		this.list.add(foo);
		this.list.add(bar);
		this.list.add(xyz);
		this.list.add(oneTwoThree);

		final ListExtractable<FastString> subList = this.list.subList(1, 3);
		assertEquals(2, subList.size());
		assertEquals(bar, subList.get(0));
		assertEquals(xyz, subList.get(1));
	}

	@Test
	public void testSubSetFromIndex() {
		try {
			this.list.subset(1);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 1, Size: 0", e.getMessage());
		}

		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		this.list.add(foo);
		this.list.add(bar);
		this.list.add(xyz);
		this.list.add(oneTwoThree);

		final SetExtractable<FastString> subset = this.list.subset(1);
		assertEquals(3, subset.size());
		assertFalse(subset.contains(foo));
		assertTrue(subset.contains(bar));
		assertTrue(subset.contains(xyz));
		assertTrue(subset.contains(oneTwoThree));
	}

	@Test
	public void testSubSetFromIndexToIndex() {
		try {
			this.list.subset(1, 3);
			fail("Expected root.validation.IndexOutOfBoundsException was not thrown");
		} catch (final root.validation.IndexOutOfBoundsException e) {
			assertEquals("Index: 3, Size: 0", e.getMessage());
		}

		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		this.list.add(foo);
		this.list.add(bar);
		this.list.add(xyz);
		this.list.add(oneTwoThree);

		final SetExtractable<FastString> subset = this.list.subset(1, 3);
		assertEquals(2, subset.size());
		assertFalse(subset.contains(foo));
		assertTrue(subset.contains(bar));
		assertTrue(subset.contains(xyz));
		assertFalse(subset.contains(oneTwoThree));
	}

	@Test
	public void testToArray() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		Extractable[] array = this.list.toArray();
		assertNotNull(array);
		assertEquals(0, array.length);

		this.list.add(foo);
		this.list.add(bar);
		array = this.list.toArray();

		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals(foo, array[0]);
		assertEquals(bar, array[1]);
	}

	@Test
	public void testToArrayWithParameter() {
		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");

		FastString[] array = this.list.toArray(new FastString[2]);
		assertNotNull(array);
		assertEquals(2, array.length);

		this.list.add(foo);
		this.list.add(bar);
		array = this.list.toArray(new FastString[2]);

		assertNotNull(array);
		assertEquals(2, array.length);
		assertEquals(foo, array[0]);
		assertEquals(bar, array[1]);
	}

	@Test
	public void testToImmutable() {
		ListImmutable<FastString> immutableList = this.list.toImmutable();
		assertEquals(0, immutableList.list.getSize());
		assertEquals(0, immutableList.list.getCapacity());

		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		this.list.add(foo);
		this.list.add(bar);
		this.list.add(xyz);
		this.list.add(oneTwoThree);

		immutableList = this.list.toImmutable();
		assertEquals(4, immutableList.list.getSize());
		assertEquals(4, immutableList.list.getCapacity());
		assertEquals(foo, immutableList.get(0));
		assertEquals(bar, immutableList.get(1));
		assertEquals(xyz, immutableList.get(2));
		assertEquals(oneTwoThree, immutableList.get(3));

		assertFalse(this.list == immutableList.list);
	}

	@Test
	public void testToSet() {
		SetExtractable<FastString> set = this.list.toSet();
		assertEquals(0, set.size());

		final FastString foo = new FastString("foo");
		final FastString bar = new FastString("bar");
		final FastString xyz = new FastString("xyz");
		final FastString oneTwoThree = new FastString("123");

		this.list.add(foo);
		this.list.add(bar);
		this.list.add(xyz);
		this.list.add(oneTwoThree);

		set = this.list.toSet();
		assertEquals(4, set.size());
		assertTrue(set.contains(foo));
		assertTrue(set.contains(bar));
		assertTrue(set.contains(xyz));
		assertTrue(set.contains(oneTwoThree));
	}

	@Test
	public void testToString() {
		assertEquals("[]", this.list.toString());

		this.list.add(new FastString("foo"));
		assertEquals("[foo]", this.list.toString());

		this.list.add(new FastString("bar"));
		assertEquals("[foo,bar]", this.list.toString());
	}

} // End ListExtractableTest
