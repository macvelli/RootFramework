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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
 * Test the {@link MapMultiValue} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class MapMultiValueTest extends TestCase {

	private MapMultiValue<String, String> map;

	public MapMultiValueTest() {
		super("MapMultiValue");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.map = new MapMultiValue<>();
	}

	@Test
	public void testClear() {
		assertEquals(0, this.map.size());

		this.map.put("foo", "bar");

		assertEquals(1, this.map.size());

		this.map.clear();

		assertEquals(0, this.map.size());
	}

	@Test
	public void testClone() {
		this.map.put("foo", "bar");
		this.map.put("foo", "ugh");
		this.map.put("xyz", "123");

		assertEquals(2, this.map.size());
		assertEquals(3, this.map.getNumValues());
		assertEquals("bar", this.map.get("foo", 0));
		assertEquals("ugh", this.map.get("foo", 1));
		assertEquals("123", this.map.get("xyz", 0));

		final MapMultiValue<String, String> m = this.map.clone();
		assertEquals(2, m.size());
		assertEquals(3, m.getNumValues());
		assertEquals("bar", m.get("foo", 0));
		assertEquals("ugh", m.get("foo", 1));
		assertEquals("123", m.get("xyz", 0));

		assertFalse(this.map == m);
		assertFalse(this.map.get("foo") == m.get("foo"));
		assertFalse(this.map.get("xyz") == m.get("xyz"));
	}

	@Test
	public void testConstructorCapacity() {
		MapMultiValue<String, String> m = new MapMultiValue<>(17);
		assertEquals(0, m.size());
		assertEquals(32, m.getCapacity());

		// Test minimum capacity of 8
		m = new MapMultiValue<>(7);
		assertEquals(0, m.size());
		assertEquals(8, m.getCapacity());
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

		final MapMultiValue<String, String> m = new MapMultiValue<>(stringMap);
		assertEquals(2, m.size());
		assertEquals(8, m.getCapacity());
		assertEquals("bar", m.get("foo", 0));
		assertEquals("123", m.get("xyz", 0));
	}

	@Test
	public void testContainsEntry() {
		assertFalse(this.map.containsEntry("foo", "bar"));

		this.map.put("foo", "bar");

		assertTrue(this.map.containsEntry("foo", "bar"));
		assertFalse(this.map.containsEntry("foo", "ugh"));

		this.map.put("foo", "ugh");

		assertTrue(this.map.containsEntry("foo", "bar"));
		assertTrue(this.map.containsEntry("foo", "ugh"));
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
	public void testContainsValueByKey() {
		assertFalse(this.map.containsValue("foo", "bar"));

		this.map.put("foo", "bar");
		this.map.put("xyz", "123");

		assertTrue(this.map.containsValue("foo", "bar"));
		assertFalse(this.map.containsValue("xyz", "bar"));
	}

	@Test
	public void testEntrySet() {
		Set<Entry<String, ListArray<String>>> entrySet = this.map.entrySet();
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
		final MapHashed<String, ListArray<String>> mapMultiValue = new MapHashed<>();

		assertTrue(this.map.equals(mapMultiValue));

		this.map.put("foo", "bar");

		assertFalse(this.map.equals(mapMultiValue));

		mapMultiValue.put("foo", new ListArray<>("bar"));

		assertTrue(this.map.equals(mapMultiValue));

		this.map.put("foo", "ugh");

		assertFalse(this.map.equals(mapMultiValue));

		mapMultiValue.get("foo").add("ugh");

		assertTrue(this.map.equals(mapMultiValue));

		this.map.clear();

		final HashMap<String, ArrayList<String>> hashMap = new HashMap<>();

		assertTrue(this.map.equals(hashMap));

		this.map.put("foo", "bar");

		assertFalse(this.map.equals(hashMap));

		final ArrayList<String> fooList = new ArrayList<>();
		hashMap.put("foo", fooList);
		fooList.add("bar");

		assertTrue(this.map.equals(hashMap));

		this.map.put("foo", "ugh");

		assertFalse(this.map.equals(hashMap));

		fooList.add("ugh");

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
		assertEquals("{foo=[bar]}", extractor.toString());

		this.map.put("xyz", "123");

		extractor.clear();
		this.map.extract(extractor);
		assertEquals("{foo=[bar],xyz=[123]}", extractor.toString());

		this.map.put("foo", "ugh");

		extractor.clear();
		this.map.extract(extractor);
		assertEquals("{foo=[bar,ugh],xyz=[123]}", extractor.toString());
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
	public void testGetNumValues() {
		assertEquals(0, this.map.getNumValues());

		this.map.put("foo", "bar");

		assertEquals(1, this.map.getNumValues());

		this.map.put("foo", "ugh");

		assertEquals(2, this.map.getNumValues());

		this.map.put("xyz", "123");

		assertEquals(3, this.map.getNumValues());
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

		assertEquals(new ListArray<>("bar"), this.map.get("foo"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testGetValueByClass() {
		try {
			this.map.get("foo", (Class<? extends ListArray<String>>) ListArray.class);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals(
					"This method is pointless for a MapMultiValue, and because of type erasure I cannot include a get(K, Class<V>) method...oh and good luck trying to call this method anyway",
					e.getMessage());
		}
	}

	@Test
	public void testGetValueByDefault() {
		final ListArray<String> list = new ListArray<>("xyz");

		assertEquals(list, this.map.get("foo", list));

		this.map.put("foo", "bar");

		assertEquals(new ListArray<>("bar"), this.map.get("foo", list));
	}

	@Test
	public void testGetValueByIndex() {
		assertNull(this.map.get("foo", 0));

		this.map.put("foo", "bar");

		assertEquals("bar", this.map.get("foo", 0));

		this.map.put("foo", "ugh");

		assertEquals("ugh", this.map.get("foo", 1));
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
		Itemizer<MapEntry<String, ListArray<String>>> itemizer = this.map.iterator();

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

		MapEntry<String, ListArray<String>> entry = itemizer.next();
		assertEquals("foo", entry.getKey());
		assertEquals(new ListArray<>("bar"), entry.getValue());
		assertEquals(0, itemizer.getIndex());
		assertTrue(itemizer.hasNext());

		entry = itemizer.next();
		assertEquals("xyz", entry.getKey());
		assertEquals(new ListArray<>("123"), entry.getValue());
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
		assertEquals(0, this.map.size());

		this.map.put("foo", new ListArray<>("bar"));
		assertEquals(1, this.map.size());
		assertEquals(1, this.map.getNumValues());
		assertEquals(new ListArray<>("bar"), this.map.get("foo"));

		this.map.put("xyz", new ListArray<>("123"));
		assertEquals(2, this.map.size());
		assertEquals(2, this.map.getNumValues());
		assertEquals(new ListArray<>("123"), this.map.get("xyz"));

		this.map.put("abc", new ListArray<>("def"));
		assertEquals(3, this.map.size());
		assertEquals(3, this.map.getNumValues());
		assertEquals(new ListArray<>("def"), this.map.get("abc"));

		this.map.put("ghi", new ListArray<>("jkl"));
		assertEquals(4, this.map.size());
		assertEquals(4, this.map.getNumValues());
		assertEquals(new ListArray<>("jkl"), this.map.get("ghi"));

		this.map.put("mno", new ListArray<>("pqr"));
		assertEquals(5, this.map.size());
		assertEquals(5, this.map.getNumValues());
		assertEquals(new ListArray<>("pqr"), this.map.get("mno"));

		this.map.put("stu", new ListArray<>("vwx"));
		assertEquals(6, this.map.size());
		assertEquals(6, this.map.getNumValues());
		assertEquals(new ListArray<>("vwx"), this.map.get("stu"));

		this.map.put("yza", new ListArray<>("bcd"));
		assertEquals(7, this.map.size());
		assertEquals(7, this.map.getNumValues());
		assertEquals(new ListArray<>("bcd"), this.map.get("yza"));

		this.map.put("efg", new ListArray<>("hij"));
		assertEquals(8, this.map.size());
		assertEquals(8, this.map.getNumValues());
		assertEquals(8, this.map.getCapacity());
		assertEquals(new ListArray<>("hij"), this.map.get("efg"));

		this.map.put("klm", new ListArray<>("nop"));
		assertEquals(9, this.map.size());
		assertEquals(9, this.map.getNumValues());
		assertEquals(16, this.map.getCapacity());
		assertEquals(new ListArray<>("nop"), this.map.get("klm"));

		this.map.put("foo", new ListArray<>("ugh"));
		assertEquals(9, this.map.size());
		assertEquals(9, this.map.getNumValues());
		assertEquals(new ListArray<>("ugh"), this.map.get("foo"));
	}

	@Test
	public void testPutAll() {
		final Map<String, ListArray<String>> stringMap = new HashMap<>();
		stringMap.put("foo", new ListArray<>("bar"));
		stringMap.put("xyz", new ListArray<>("123"));
		assertEquals(0, this.map.size());

		this.map.putAll(stringMap);
		assertEquals(2, this.map.size());

		assertEquals(new ListArray<>("bar"), this.map.get("foo"));
		assertEquals(new ListArray<>("123"), this.map.get("xyz"));

		this.map.putAll(stringMap);
		assertEquals(2, this.map.size());

		assertEquals(new ListArray<>("bar"), this.map.get("foo"));
		assertEquals(new ListArray<>("123"), this.map.get("xyz"));
	}

	@Test
	public void testPutAllCollection() {
		final Set<String> stringSet = new HashSet<>();
		stringSet.add("bar");
		stringSet.add("ugh");
		assertEquals(0, this.map.size());

		this.map.putAll("foo", stringSet);

		assertEquals(1, this.map.size());
		assertEquals(2, this.map.getNumValues());
		assertEquals("ugh", this.map.get("foo", 0));
		assertEquals("bar", this.map.get("foo", 1));
	}

	@Test
	public void testPutValue() {
		assertEquals(0, this.map.size());

		this.map.put("foo", "bar");
		assertEquals(1, this.map.size());
		assertEquals(1, this.map.getNumValues());
		assertEquals("bar", this.map.get("foo", 0));

		this.map.put("xyz", "123");
		assertEquals(2, this.map.size());
		assertEquals(2, this.map.getNumValues());
		assertEquals("123", this.map.get("xyz", 0));

		this.map.put("abc", "def");
		assertEquals(3, this.map.size());
		assertEquals(3, this.map.getNumValues());
		assertEquals("def", this.map.get("abc", 0));

		this.map.put("ghi", "jkl");
		assertEquals(4, this.map.size());
		assertEquals(4, this.map.getNumValues());
		assertEquals("jkl", this.map.get("ghi", 0));

		this.map.put("mno", "pqr");
		assertEquals(5, this.map.size());
		assertEquals(5, this.map.getNumValues());
		assertEquals("pqr", this.map.get("mno", 0));

		this.map.put("stu", "vwx");
		assertEquals(6, this.map.size());
		assertEquals(6, this.map.getNumValues());
		assertEquals("vwx", this.map.get("stu", 0));

		this.map.put("yza", "bcd");
		assertEquals(7, this.map.size());
		assertEquals(7, this.map.getNumValues());
		assertEquals("bcd", this.map.get("yza", 0));

		this.map.put("efg", "hij");
		assertEquals(8, this.map.size());
		assertEquals(8, this.map.getNumValues());
		assertEquals(8, this.map.getCapacity());
		assertEquals("hij", this.map.get("efg", 0));

		this.map.put("klm", "nop");
		assertEquals(9, this.map.size());
		assertEquals(9, this.map.getNumValues());
		assertEquals(16, this.map.getCapacity());
		assertEquals("nop", this.map.get("klm", 0));

		this.map.put("foo", "ugh");
		assertEquals(9, this.map.size());
		assertEquals(10, this.map.getNumValues());
		assertEquals("ugh", this.map.get("foo", 1));
	}

	@Test
	public void testRemove() {
		assertNull(this.map.remove("foo"));

		this.map.put("foo", "bar");
		this.map.put("foo", "ugh");

		assertEquals(1, this.map.size());
		assertEquals(2, this.map.getNumValues());

		final ListArray<String> stringList = this.map.remove("foo");

		assertEquals(0, this.map.size());
		assertEquals(0, this.map.getNumValues());

		assertEquals("bar", stringList.get(0));
		assertEquals("ugh", stringList.get(1));
	}

	@Test
	public void testRemoveValue() {
		assertFalse(this.map.remove("foo", "bar"));

		this.map.put("foo", "bar");
		this.map.put("foo", "ugh");

		assertEquals(1, this.map.size());
		assertEquals(2, this.map.getNumValues());
		assertEquals("bar", this.map.get("foo", 0));
		assertEquals("ugh", this.map.get("foo", 1));

		assertTrue(this.map.remove("foo", "bar"));
		assertEquals(1, this.map.size());
		assertEquals(1, this.map.getNumValues());
		assertEquals("ugh", this.map.get("foo", 0));
	}

	@Test
	public void testSize() {
		assertEquals(0, this.map.size());

		this.map.put("foo", "bar");

		assertEquals(1, this.map.size());
	}

	@Test
	public void testToImmutable() {
		MapImmutable<String, ListArray<String>> immutableMap = this.map.toImmutable();
		assertEquals(0, immutableMap.map.getSize());
		assertEquals(8, immutableMap.map.getCapacity());

		this.map.put("foo", "bar");
		this.map.put("xyz", "123");
		this.map.put("abc", "def");
		this.map.put("ghi", "jkl");

		immutableMap = this.map.toImmutable();
		assertEquals(4, immutableMap.map.getSize());
		assertEquals(8, immutableMap.map.getCapacity());
		assertEquals(new ListArray<>("bar"), immutableMap.get("foo"));
		assertEquals(new ListArray<>("123"), immutableMap.get("xyz"));
		assertEquals(new ListArray<>("def"), immutableMap.get("abc"));
		assertEquals(new ListArray<>("jkl"), immutableMap.get("ghi"));

		assertFalse(this.map == immutableMap.map);
		assertFalse(this.map.get("foo") == immutableMap.get("foo"));
	}

	public void testToString() {
		assertEquals("{}", this.map.toString());

		this.map.put("foo", "bar");

		assertEquals("{foo=[bar]}", this.map.toString());

		this.map.put("xyz", "123");

		assertEquals("{foo=[bar],xyz=[123]}", this.map.toString());

		this.map.put("foo", "ugh");

		assertEquals("{foo=[bar,ugh],xyz=[123]}", this.map.toString());

	}

	public void testValues() {
		Collection<ListArray<String>> valueCollection = this.map.values();

		assertEquals(0, valueCollection.size());
		assertFalse(valueCollection.contains(new ListArray<>("bar")));

		this.map.put("foo", "bar");
		this.map.put("xyz", "123");

		valueCollection = this.map.values();
		assertEquals(2, valueCollection.size());
		assertTrue(valueCollection.contains(new ListArray<>("bar")));
		assertTrue(valueCollection.contains(new ListArray<>("123")));
		assertFalse(valueCollection.contains(new ListArray<>("foo")));
		assertFalse(valueCollection.contains(new ListArray<>("xyz")));
	}

} // End MapMultiValueTest
