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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import root.lang.Itemizer;
import root.lang.StringExtractor;

/**
 * Test the {@link MapImmutable} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class MapImmutableTest extends TestCase {

	private MapImmutable<String, String> map;

	public MapImmutableTest() {
		super("MapImmutable");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.map = new MapHashed<String, String>().toImmutable();
	}

	@Test
	public void testClear() {
		try {
			this.map.clear();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot clear items from a MapImmutable", e.getMessage());
		}
	}

	@Test
	public void testClone() {
		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		assertEquals(1, this.map.size());
		assertEquals("bar", this.map.get("foo"));

		final MapImmutable<String, String> clone = this.map.clone();
		assertEquals(1, clone.size());
		assertEquals("bar", clone.get("foo"));
		assertTrue(this.map == clone);
	}

	@Test
	public void testConstructorDefault() {
		assertEquals(0, this.map.size());
		assertEquals(8, this.map.getCapacity());
	}

	@Test
	public void testConstructorMap() {
		final Map<String, String> stringMap = new HashMap<>();
		stringMap.put("foo", "bar");
		stringMap.put("xyz", "123");

		final MapImmutable<String, String> m = new MapImmutable<>(stringMap);
		assertEquals(2, m.size());
		assertEquals(8, m.getCapacity());
		assertEquals("bar", m.get("foo"));
		assertEquals("123", m.get("xyz"));
	}

	@Test
	public void testContainsEntry() {
		assertFalse(this.map.containsEntry("foo", "bar"));

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		assertTrue(this.map.containsEntry("foo", "bar"));
		assertFalse(this.map.containsEntry("foo", "xyz"));
	}

	@Test
	public void testContainsKey() {
		assertFalse(this.map.containsKey("foo"));

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		assertTrue(this.map.containsKey("foo"));
		assertFalse(this.map.containsKey("bar"));
	}

	@Test
	public void testContainsValue() {
		assertFalse(this.map.containsValue("bar"));

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		assertTrue(this.map.containsValue("bar"));
		assertFalse(this.map.containsValue("foo"));
	}

	@Test
	public void testEntrySet() {
		Set<Entry<String, String>> entrySet = this.map.entrySet();
		final MapEntry<String, String> entry = new MapEntry<>("foo", "bar");

		assertEquals(0, entrySet.size());
		assertFalse(entrySet.contains(entry));

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");
		mapHashed.put("xyz", "123");

		this.map = new MapImmutable<>(mapHashed);

		entrySet = this.map.entrySet();
		assertEquals(2, entrySet.size());
		assertTrue(entrySet.contains(entry));
		assertTrue(entrySet.contains(new MapEntry<String, String>("xyz", "123")));
		assertFalse(entrySet.contains(new MapEntry<String, String>("bar", "foo")));
		assertFalse(entrySet.contains(new MapEntry<String, String>("123", "xyz")));

		// Make sure the entry set cannot modify the immutable map
		try {
			entrySet.add(new MapEntry<>("xyz", "123"));
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		final List<MapEntry<String, String>> mapEntryList = new ListArray<>();
		mapEntryList.add(new MapEntry<>("xyz", "123"));

		try {
			entrySet.addAll(mapEntryList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			entrySet.clear();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		final Iterator<Map.Entry<String, String>> itr = entrySet.iterator();

		try {
			itr.remove();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			entrySet.remove(entry);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			entrySet.removeAll(mapEntryList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			entrySet.retainAll(mapEntryList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}
	}

	@Test
	public void testEquals() {
		final MapHashed<String, String> mapHashed = new MapHashed<>();

		assertTrue(this.map.equals(mapHashed));

		mapHashed.put("foo", "bar");

		assertFalse(this.map.equals(mapHashed));

		this.map = new MapImmutable<>(mapHashed);

		assertTrue(this.map.equals(mapHashed));

		this.map = new MapImmutable<>(new MapHashed<String, String>());

		final HashMap<String, String> hashMap = new HashMap<>();

		assertTrue(this.map.equals(hashMap));

		hashMap.put("foo", "bar");

		assertFalse(this.map.equals(hashMap));

		this.map = new MapImmutable<>(mapHashed);

		assertTrue(this.map.equals(hashMap));
	}

	@Test
	public void testExtract() {
		final StringExtractor extractor = new StringExtractor();
		this.map.extract(extractor);
		assertEquals("{}", extractor.toString());

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		extractor.clear();
		this.map.extract(extractor);
		assertEquals("{foo=bar}", extractor.toString());

		mapHashed.put("xyz", "123");

		this.map = new MapImmutable<>(mapHashed);

		extractor.clear();
		this.map.extract(extractor);
		assertEquals("{foo=bar,xyz=123}", extractor.toString());
	}

	@Test
	public void testGetCapacity() {
		assertEquals(8, this.map.getCapacity());
	}

	@Test
	public void testGetSize() {
		assertEquals(0, this.map.getSize());

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		assertEquals(1, this.map.getSize());
	}

	@Test
	public void testGetValue() {
		assertNull(this.map.get("foo"));

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		assertEquals("bar", this.map.get("foo"));
	}

	@Test
	public void testGetValueByClass() {
		try {
			this.map.get("foo", String.class);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot create new values from a MapImmutable", e.getMessage());
		}
	}

	@Test
	public void testGetValueByDefault() {
		assertEquals("xyz", this.map.get("foo", "xyz"));

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		assertEquals("bar", this.map.get("foo", "xyz"));
	}

	@Test
	public void testHashCode() {
		assertEquals(0, this.map.hashCode());

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		assertEquals(101572, this.map.hashCode());

		mapHashed.put("xyz", "123");

		this.map = new MapImmutable<>(mapHashed);

		assertEquals(182301, this.map.hashCode());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(this.map.isEmpty());

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		assertFalse(this.map.isEmpty());
	}

	@Test
	public void testIterator() {
		Itemizer<MapEntry<String, String>> itemizer = this.map.iterator();

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

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");
		mapHashed.put("xyz", "123");

		this.map = new MapImmutable<>(mapHashed);

		itemizer = this.map.iterator();

		assertEquals(-1, itemizer.getIndex());
		assertEquals(2, itemizer.getSize());
		assertTrue(itemizer.hasNext());

		MapEntry<String, String> entry = itemizer.next();
		assertEquals("foo", entry.getKey());
		assertEquals("bar", entry.getValue());
		assertEquals(0, itemizer.getIndex());
		assertTrue(itemizer.hasNext());

		entry = itemizer.next();
		assertEquals("xyz", entry.getKey());
		assertEquals("123", entry.getValue());
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
	public void testKeySet() {
		Set<String> keySet = this.map.keySet();

		assertEquals(0, keySet.size());
		assertFalse(keySet.contains("foo"));

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");
		mapHashed.put("xyz", "123");

		this.map = new MapImmutable<>(mapHashed);

		keySet = this.map.keySet();
		assertEquals(2, keySet.size());
		assertTrue(keySet.contains("foo"));
		assertTrue(keySet.contains("xyz"));
		assertFalse(keySet.contains("bar"));
		assertFalse(keySet.contains("123"));

		// Make sure the key set cannot modify the immutable map
		try {
			keySet.add("xyz");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		final List<String> keyList = new ListArray<>("xyz");

		try {
			keySet.addAll(keyList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			keySet.clear();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		final Iterator<String> itr = keySet.iterator();

		try {
			itr.remove();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			keySet.remove("foo");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			keySet.removeAll(keyList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			keySet.retainAll(keyList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}
	}

	@Test
	public void testPut() {
		try {
			this.map.put("foo", "bar");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot put items into a MapImmutable", e.getMessage());
		}
	}

	@Test
	public void testPutAll() {
		final Map<String, String> stringMap = new HashMap<>();
		stringMap.put("foo", "bar");
		stringMap.put("xyz", "123");

		try {
			this.map.putAll(stringMap);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot put items into a MapImmutable", e.getMessage());
		}
	}

	@Test
	public void testRemove() {
		try {
			this.map.remove("foo");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot remove items from a MapImmutable", e.getMessage());
		}
	}

	@Test
	public void testSize() {
		assertEquals(0, this.map.size());

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		assertEquals(1, this.map.size());
	}

	@Test
	public void testToImmutable() {
		assertEquals(0, this.map.getSize());
		assertEquals(8, this.map.getCapacity());

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");
		mapHashed.put("xyz", "123");
		mapHashed.put("abc", "def");
		mapHashed.put("ghi", "jkl");

		this.map = new MapImmutable<>(mapHashed);

		final MapImmutable<String, String> immutableMap = this.map.toImmutable();
		assertEquals(4, immutableMap.map.getSize());
		assertEquals(8, immutableMap.map.getCapacity());
		assertEquals("bar", immutableMap.get("foo"));
		assertEquals("123", immutableMap.get("xyz"));
		assertEquals("def", immutableMap.get("abc"));
		assertEquals("jkl", immutableMap.get("ghi"));

		assertTrue(this.map == immutableMap);
	}

	public void testToString() {
		assertEquals("{}", this.map.toString());

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");

		this.map = new MapImmutable<>(mapHashed);

		assertEquals("{foo=bar}", this.map.toString());

		mapHashed.put("xyz", "123");

		this.map = new MapImmutable<>(mapHashed);

		assertEquals("{foo=bar,xyz=123}", this.map.toString());
	}

	public void testValues() {
		Collection<String> valueCollection = this.map.values();

		assertEquals(0, valueCollection.size());
		assertFalse(valueCollection.contains("bar"));

		final MapHashed<String, String> mapHashed = new MapHashed<>();
		mapHashed.put("foo", "bar");
		mapHashed.put("xyz", "123");

		this.map = new MapImmutable<>(mapHashed);

		valueCollection = this.map.values();
		assertEquals(2, valueCollection.size());
		assertTrue(valueCollection.contains("bar"));
		assertTrue(valueCollection.contains("123"));
		assertFalse(valueCollection.contains("foo"));
		assertFalse(valueCollection.contains("xyz"));

		// Make sure the values collection cannot modify the immutable map
		try {
			valueCollection.add("xyz");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		final List<String> valueList = new ListArray<>("xyz");

		try {
			valueCollection.addAll(valueList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			valueCollection.clear();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		final Iterator<String> itr = valueCollection.iterator();

		try {
			itr.remove();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			valueCollection.remove("foo");
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			valueCollection.removeAll(valueList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}

		try {
			valueCollection.retainAll(valueList);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
		}
	}

} // End MapImmutableTest
