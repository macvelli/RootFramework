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
 * Test the {@link MapHashed} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class MapHashedTest extends TestCase {

	private MapHashed<String, String> map;

	public MapHashedTest() {
		super("MapHashed");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.map = new MapHashed<>();
	}

	@Test
	public void testClear() {
		assertEquals(0, this.map.size);
		this.map.put("foo", "bar");
		assertEquals(1, this.map.size);
		this.map.clear();
		assertEquals(0, this.map.size);
	}

	@Test
	public void testClone() {
		this.map.put("foo", "bar");
		this.map.put("xyz", "123");

		assertEquals(2, this.map.size);
		assertEquals("bar", this.map.get("foo"));
		assertEquals("123", this.map.get("xyz"));

		final MapHashed<String, String> m = this.map.clone();
		assertEquals(2, m.size);
		assertEquals("bar", m.get("foo"));
		assertEquals("123", m.get("xyz"));
		assertFalse(this.map == m);
	}

	@Test
	public void testConstructorCapacity() {
		MapHashed<String, String> m = new MapHashed<>(17);
		assertEquals(0, m.size);
		assertEquals(32, m.capacity);
		assertEquals(43, m.table.length);

		// Test minimum capacity of 8
		m = new MapHashed<>(7);
		assertEquals(0, m.size);
		assertEquals(8, m.capacity);
		assertEquals(11, m.table.length);
	}

	@Test
	public void testConstructorDefault() {
		assertEquals(0, this.map.size);
		assertEquals(8, this.map.capacity);
		assertEquals(11, this.map.table.length);
	}

	@Test
	public void testConstructorMap() {
		final Map<String, String> stringMap = new HashMap<>();
		stringMap.put("foo", "bar");
		stringMap.put("xyz", "123");

		final MapHashed<String, String> m = new MapHashed<>(stringMap);
		assertEquals(2, m.size);
		assertEquals(8, m.capacity);
		assertEquals(11, m.table.length);
		assertEquals("bar", m.get("foo"));
		assertEquals("123", m.get("xyz"));
	}

	@Test
	public void testContainsEntry() {
		assertFalse(this.map.containsEntry("foo", "bar"));
		this.map.put("foo", "bar");
		assertTrue(this.map.containsEntry("foo", "bar"));
		assertFalse(this.map.containsEntry("foo", "xyz"));
	}

	@Test
	public void testContainsKey() {
		assertFalse(this.map.containsKey("foo"));
		this.map.put("foo", "bar");
		assertTrue(this.map.containsKey("foo"));
		assertFalse(this.map.containsKey("bar"));
	}

	@Test
	public void testContainsValue() {
		assertFalse(this.map.containsValue("bar"));
		this.map.put("foo", "bar");
		assertTrue(this.map.containsValue("bar"));
		assertFalse(this.map.containsValue("foo"));
	}

	@Test
	public void testEntrySet() {
		Set<Entry<String, String>> entrySet = this.map.entrySet();
		final MapEntry<String, String> entry = new MapEntry<>("foo", "bar");

		assertEquals(0, entrySet.size());
		assertFalse(entrySet.contains(entry));

		this.map.put("foo", "bar");
		this.map.put("xyz", "123");

		entrySet = this.map.entrySet();
		assertEquals(2, entrySet.size());
		assertTrue(entrySet.contains(entry));
		assertTrue(entrySet.contains(new MapEntry<String, String>("xyz", "123")));
		assertFalse(entrySet.contains(new MapEntry<String, String>("bar", "foo")));
		assertFalse(entrySet.contains(new MapEntry<String, String>("123", "xyz")));
	}

	@Test
	public void testEquals() {
		final MapHashed<String, String> mapHashed = new MapHashed<>();

		assertTrue(this.map.equals(mapHashed));

		this.map.put("foo", "bar");

		assertFalse(this.map.equals(mapHashed));

		mapHashed.put("foo", "bar");

		assertTrue(this.map.equals(mapHashed));

		this.map.clear();

		final HashMap<String, String> hashMap = new HashMap<>();

		assertTrue(this.map.equals(hashMap));

		this.map.put("foo", "bar");

		assertFalse(this.map.equals(hashMap));

		hashMap.put("foo", "bar");

		assertTrue(this.map.equals(hashMap));
	}

	@Test
	public void testExtract() {
		final StringExtractor extractor = new StringExtractor();
		this.map.extract(extractor);
		assertEquals("{}", extractor.toString());

		this.map.put("foo", "bar");

		extractor.clear();
		this.map.extract(extractor);
		assertEquals("{foo=bar}", extractor.toString());

		this.map.put("xyz", "123");

		extractor.clear();
		this.map.extract(extractor);
		assertEquals("{foo=bar,xyz=123}", extractor.toString());
	}

	@Test
	public void testGetCapacity() {
		assertEquals(8, this.map.getCapacity());

		this.map.put("foo", "bar");
		this.map.put("xyz", "123");
		this.map.put("abc", "def");
		this.map.put("ghi", "jkl");
		this.map.put("mno", "pqr");
		this.map.put("stu", "vwx");
		this.map.put("yza", "bcd");
		this.map.put("efg", "hij");
		this.map.put("klm", "nop");

		assertEquals(16, this.map.getCapacity());
	}

	@Test
	public void testGetSize() {
		assertEquals(0, this.map.getSize());
		this.map.put("foo", "bar");
		assertEquals(1, this.map.getSize());
	}

	@Test
	public void testGetValue() {
		assertNull(this.map.get("foo"));
		this.map.put("foo", "bar");
		assertEquals("bar", this.map.get("foo"));
	}

	@Test
	public void testGetValueByClass() {
		String str = this.map.get("foo", String.class);
		assertNotNull(str);
		assertEquals(0, str.length());

		this.map.put("xyz", "123");
		str = this.map.get("xyz", String.class);
		assertEquals("123", str);
	}

	@Test
	public void testGetValueByDefault() {
		assertEquals("xyz", this.map.get("foo", "xyz"));

		this.map.put("foo", "bar");
		assertEquals("bar", this.map.get("foo", "xyz"));
	}

	@Test
	public void testHashCode() {
		assertEquals(0, this.map.hashCode());

		this.map.put("foo", "bar");
		assertEquals(101572, this.map.hashCode());

		this.map.put("xyz", "123");
		assertEquals(182301, this.map.hashCode());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(this.map.isEmpty());
		this.map.put("foo", "bar");
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
		}

		this.map.put("foo", "bar");
		this.map.put("xyz", "123");
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

		this.map.put("foo", "bar");
		this.map.put("xyz", "123");

		keySet = this.map.keySet();
		assertEquals(2, keySet.size());
		assertTrue(keySet.contains("foo"));
		assertTrue(keySet.contains("xyz"));
		assertFalse(keySet.contains("bar"));
		assertFalse(keySet.contains("123"));
	}

	@Test
	public void testPut() {
		assertEquals(0, this.map.size);

		this.map.put("foo", "bar");
		assertEquals(1, this.map.size);
		assertEquals("bar", this.map.get("foo"));

		this.map.put("xyz", "123");
		assertEquals(2, this.map.size);
		assertEquals("123", this.map.get("xyz"));

		this.map.put("abc", "def");
		assertEquals(3, this.map.size);
		assertEquals("def", this.map.get("abc"));

		this.map.put("ghi", "jkl");
		assertEquals(4, this.map.size);
		assertEquals("jkl", this.map.get("ghi"));

		this.map.put("mno", "pqr");
		assertEquals(5, this.map.size);
		assertEquals("pqr", this.map.get("mno"));

		this.map.put("stu", "vwx");
		assertEquals(6, this.map.size);
		assertEquals("vwx", this.map.get("stu"));

		this.map.put("yza", "bcd");
		assertEquals(7, this.map.size);
		assertEquals("bcd", this.map.get("yza"));

		this.map.put("efg", "hij");
		assertEquals(8, this.map.size);
		assertEquals(8, this.map.capacity);
		assertEquals(11, this.map.table.length);
		assertEquals("hij", this.map.get("efg"));

		this.map.put("klm", "nop");
		assertEquals(9, this.map.size);
		assertEquals(16, this.map.capacity);
		assertEquals(23, this.map.table.length);
		assertEquals("nop", this.map.get("klm"));

		this.map.put("foo", "ugh");
		assertEquals(9, this.map.size);
		assertEquals("ugh", this.map.get("foo"));
	}

	@Test
	public void testPutAll() {
		final Map<String, String> stringMap = new HashMap<>();
		stringMap.put("foo", "bar");
		stringMap.put("xyz", "123");
		assertEquals(0, this.map.size);

		this.map.putAll(stringMap);
		assertEquals(2, this.map.size);

		assertEquals("bar", this.map.get("foo"));
		assertEquals("123", this.map.get("xyz"));

		this.map.putAll(stringMap);
		assertEquals(2, this.map.size);

		assertEquals("bar", this.map.get("foo"));
		assertEquals("123", this.map.get("xyz"));
	}

	@Test
	public void testRemove() {
		assertNull(this.map.remove("foo"));

		this.map.put("foo", "bar");
		assertEquals(1, this.map.size);

		assertEquals("bar", this.map.remove("foo"));
		assertEquals(0, this.map.size);
	}

	@Test
	public void testSize() {
		assertEquals(0, this.map.size());
		this.map.put("foo", "bar");
		assertEquals(1, this.map.size());
	}

	@Test
	public void testToImmutable() {
		MapImmutable<String, String> immutableMap = this.map.toImmutable();
		assertEquals(0, immutableMap.map.getSize());
		assertEquals(8, immutableMap.map.getCapacity());

		this.map.put("foo", "bar");
		this.map.put("xyz", "123");
		this.map.put("abc", "def");
		this.map.put("ghi", "jkl");

		immutableMap = this.map.toImmutable();
		assertEquals(4, immutableMap.map.getSize());
		assertEquals(8, immutableMap.map.getCapacity());
		assertEquals("bar", immutableMap.get("foo"));
		assertEquals("123", immutableMap.get("xyz"));
		assertEquals("def", immutableMap.get("abc"));
		assertEquals("jkl", immutableMap.get("ghi"));

		assertFalse(this.map == immutableMap.map);
	}

	public void testToString() {
		assertEquals("{}", this.map.toString());

		this.map.put("foo", "bar");

		assertEquals("{foo=bar}", this.map.toString());

		this.map.put("xyz", "123");

		assertEquals("{foo=bar,xyz=123}", this.map.toString());
	}

	public void testValues() {
		Collection<String> valueCollection = this.map.values();

		assertEquals(0, valueCollection.size());
		assertFalse(valueCollection.contains("bar"));

		this.map.put("foo", "bar");
		this.map.put("xyz", "123");

		valueCollection = this.map.values();
		assertEquals(2, valueCollection.size());
		assertTrue(valueCollection.contains("bar"));
		assertTrue(valueCollection.contains("123"));
		assertFalse(valueCollection.contains("foo"));
		assertFalse(valueCollection.contains("xyz"));
	}

} // End MapHashedTest
