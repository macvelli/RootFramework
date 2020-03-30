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
import root.lang.FastString;
import root.lang.Itemizer;
import root.lang.StringExtractor;

/**
 * Test the {@link MapExtractable} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class MapExtractableTest extends TestCase {

	private MapExtractable<FastString, FastString> map;

	private final FastString foo = new FastString("foo");
	private final FastString bar = new FastString("bar");
	private final FastString xyz = new FastString("xyz");
	private final FastString oneTwoThree = new FastString("123");

	public MapExtractableTest() {
		super("MapExtractable");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.map = new MapExtractable<>();
	}

	@Test
	public void testClear() {
		assertEquals(0, this.map.size());

		this.map.put(this.foo, this.bar);

		assertEquals(1, this.map.size());

		this.map.clear();

		assertEquals(0, this.map.size());
	}

	@Test
	public void testClone() {
		this.map.put(this.foo, this.bar);
		this.map.put(this.xyz, this.oneTwoThree);

		assertEquals(2, this.map.size());
		assertEquals(this.bar, this.map.get(this.foo));
		assertEquals(this.oneTwoThree, this.map.get(this.xyz));

		final MapExtractable<FastString, FastString> m = this.map.clone();
		assertEquals(2, m.size());
		assertEquals(this.bar, m.get(this.foo));
		assertEquals(this.oneTwoThree, m.get(this.xyz));
		assertFalse(this.map == m);
	}

	@Test
	public void testConstructorCapacity() {
		MapExtractable<FastString, FastString> m = new MapExtractable<>(17);
		assertEquals(0, m.size());
		assertEquals(32, m.getCapacity());

		// Test minimum capacity of 8
		m = new MapExtractable<>(7);
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
		final Map<FastString, FastString> stringMap = new HashMap<>();
		stringMap.put(this.foo, this.bar);
		stringMap.put(this.xyz, this.oneTwoThree);

		final MapExtractable<FastString, FastString> m = new MapExtractable<>(stringMap);
		assertEquals(2, m.size());
		assertEquals(8, m.getCapacity());
		assertEquals(this.bar, m.get(this.foo));
		assertEquals(this.oneTwoThree, m.get(this.xyz));
	}

	@Test
	public void testContainsEntry() {
		assertFalse(this.map.containsEntry(this.foo, this.bar));

		this.map.put(this.foo, this.bar);

		assertTrue(this.map.containsEntry(this.foo, this.bar));
		assertFalse(this.map.containsEntry(this.foo, this.xyz));
	}

	@Test
	public void testContainsKey() {
		assertFalse(this.map.containsKey(this.foo));

		this.map.put(this.foo, this.bar);

		assertTrue(this.map.containsKey(this.foo));
		assertFalse(this.map.containsKey(this.bar));
	}

	@Test
	public void testContainsValue() {
		assertFalse(this.map.containsValue(this.bar));

		this.map.put(this.foo, this.bar);

		assertTrue(this.map.containsValue(this.bar));
		assertFalse(this.map.containsValue(this.foo));
	}

	@Test
	public void testEntrySet() {
		Set<Entry<FastString, FastString>> entrySet = this.map.entrySet();
		final MapEntry<FastString, FastString> entry = new MapEntry<>(this.foo, this.bar);

		assertEquals(0, entrySet.size());
		assertFalse(entrySet.contains(entry));

		this.map.put(this.foo, this.bar);
		this.map.put(this.xyz, this.oneTwoThree);

		entrySet = this.map.entrySet();
		assertEquals(2, entrySet.size());
		assertTrue(entrySet.contains(entry));
		assertTrue(entrySet.contains(new MapEntry<FastString, FastString>(this.xyz, this.oneTwoThree)));
		assertFalse(entrySet.contains(new MapEntry<FastString, FastString>(this.bar, this.foo)));
		assertFalse(entrySet.contains(new MapEntry<FastString, FastString>(this.oneTwoThree, this.xyz)));
	}

	@Test
	public void testEquals() {
		final MapHashed<FastString, FastString> mapHashed = new MapHashed<>();

		assertTrue(this.map.equals(mapHashed));

		this.map.put(this.foo, this.bar);

		assertFalse(this.map.equals(mapHashed));

		mapHashed.put(this.foo, this.bar);

		assertTrue(this.map.equals(mapHashed));

		this.map.clear();

		final HashMap<FastString, FastString> hashMap = new HashMap<>();

		assertTrue(this.map.equals(hashMap));

		this.map.put(this.foo, this.bar);

		assertFalse(this.map.equals(hashMap));

		hashMap.put(this.foo, this.bar);

		assertTrue(this.map.equals(hashMap));
	}

	@Test
	public void testExtract() {
		final StringExtractor extractor = new StringExtractor();
		this.map.extract(extractor);
		assertEquals("{}", extractor.toString());

		this.map.put(this.foo, this.bar);

		extractor.clear();
		this.map.extract(extractor);
		assertEquals("{foo=bar}", extractor.toString());

		this.map.put(this.xyz, this.oneTwoThree);

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

		this.map.put(this.foo, this.bar);

		assertEquals(1, this.map.getSize());
	}

	@Test
	public void testGetValue() {
		assertNull(this.map.get("foo"));

		this.map.put(this.foo, this.bar);

		assertEquals(this.bar, this.map.get(this.foo));
	}

	@Test
	public void testGetValueByClass() {
		this.map.put(this.xyz, this.oneTwoThree);

		final FastString str = this.map.get(this.xyz, FastString.class);

		assertEquals(this.oneTwoThree, str);
	}

	@Test
	public void testGetValueByDefault() {
		assertEquals(this.xyz, this.map.get(this.foo, this.xyz));

		this.map.put(this.foo, this.bar);

		assertEquals(this.bar, this.map.get(this.foo, this.xyz));
	}

	@Test
	public void testHashCode() {
		assertEquals(0, this.map.hashCode());

		this.map.put(this.foo, this.bar);
		assertEquals(101572, this.map.hashCode());

		this.map.put(this.xyz, this.oneTwoThree);
		assertEquals(182301, this.map.hashCode());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(this.map.isEmpty());

		this.map.put(this.foo, this.bar);

		assertFalse(this.map.isEmpty());
	}

	@Test
	public void testIterator() {
		Itemizer<MapEntry<FastString, FastString>> itemizer = this.map.iterator();

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

		this.map.put(this.foo, this.bar);
		this.map.put(this.xyz, this.oneTwoThree);
		itemizer = this.map.iterator();

		assertEquals(-1, itemizer.getIndex());
		assertEquals(2, itemizer.getSize());
		assertTrue(itemizer.hasNext());

		MapEntry<FastString, FastString> entry = itemizer.next();
		assertEquals(this.foo, entry.getKey());
		assertEquals(this.bar, entry.getValue());
		assertEquals(0, itemizer.getIndex());
		assertTrue(itemizer.hasNext());

		entry = itemizer.next();
		assertEquals(this.xyz, entry.getKey());
		assertEquals(this.oneTwoThree, entry.getValue());
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
		Set<FastString> keySet = this.map.keySet();

		assertEquals(0, keySet.size());
		assertFalse(keySet.contains(this.foo));

		this.map.put(this.foo, this.bar);
		this.map.put(this.xyz, this.oneTwoThree);

		keySet = this.map.keySet();
		assertEquals(2, keySet.size());
		assertTrue(keySet.contains(this.foo));
		assertTrue(keySet.contains(this.xyz));
		assertFalse(keySet.contains(this.bar));
		assertFalse(keySet.contains(this.oneTwoThree));
	}

	@Test
	public void testPut() {
		assertEquals(0, this.map.size());

		this.map.put(this.foo, this.bar);
		assertEquals(1, this.map.size());
		assertEquals(this.bar, this.map.get(this.foo));

		this.map.put(this.xyz, this.oneTwoThree);
		assertEquals(2, this.map.size());
		assertEquals(this.oneTwoThree, this.map.get(this.xyz));

		final FastString ugh = new FastString("ugh");

		this.map.put(this.foo, ugh);
		assertEquals(2, this.map.size());
		assertEquals(ugh, this.map.get(this.foo));
	}

	@Test
	public void testPutAll() {
		final Map<FastString, FastString> stringMap = new HashMap<>();
		stringMap.put(this.foo, this.bar);
		stringMap.put(this.xyz, this.oneTwoThree);
		assertEquals(0, this.map.size());

		this.map.putAll(stringMap);
		assertEquals(2, this.map.size());

		assertEquals(this.bar, this.map.get(this.foo));
		assertEquals(this.oneTwoThree, this.map.get(this.xyz));

		this.map.putAll(stringMap);
		assertEquals(2, this.map.size());

		assertEquals(this.bar, this.map.get(this.foo));
		assertEquals(this.oneTwoThree, this.map.get(this.xyz));
	}

	@Test
	public void testRemove() {
		assertNull(this.map.remove(this.foo));

		this.map.put(this.foo, this.bar);
		assertEquals(1, this.map.size());

		assertEquals(this.bar, this.map.remove(this.foo));
		assertEquals(0, this.map.size());
	}

	@Test
	public void testSize() {
		assertEquals(0, this.map.size());

		this.map.put(this.foo, this.bar);

		assertEquals(1, this.map.size());
	}

	@Test
	public void testToImmutable() {
		MapImmutable<FastString, FastString> immutableMap = this.map.toImmutable();
		assertEquals(0, immutableMap.map.getSize());
		assertEquals(8, immutableMap.map.getCapacity());

		final FastString abc = new FastString("abc");
		final FastString def = new FastString("def");
		final FastString ghi = new FastString("ghi");
		final FastString jkl = new FastString("jkl");

		this.map.put(this.foo, this.bar);
		this.map.put(this.xyz, this.oneTwoThree);
		this.map.put(abc, def);
		this.map.put(ghi, jkl);

		immutableMap = this.map.toImmutable();
		assertEquals(4, immutableMap.map.getSize());
		assertEquals(8, immutableMap.map.getCapacity());
		assertEquals(this.bar, immutableMap.get(this.foo));
		assertEquals(this.oneTwoThree, immutableMap.get(this.xyz));
		assertEquals(def, immutableMap.get(abc));
		assertEquals(jkl, immutableMap.get(ghi));

		assertFalse(this.map == immutableMap.map);
	}

	public void testToString() {
		assertEquals("{}", this.map.toString());

		this.map.put(this.foo, this.bar);

		assertEquals("{foo=bar}", this.map.toString());

		this.map.put(this.xyz, this.oneTwoThree);

		assertEquals("{foo=bar,xyz=123}", this.map.toString());
	}

	public void testValues() {
		Collection<FastString> valueCollection = this.map.values();

		assertEquals(0, valueCollection.size());
		assertFalse(valueCollection.contains(this.bar));

		this.map.put(this.foo, this.bar);
		this.map.put(this.xyz, this.oneTwoThree);

		valueCollection = this.map.values();
		assertEquals(2, valueCollection.size());
		assertTrue(valueCollection.contains(this.bar));
		assertTrue(valueCollection.contains(this.oneTwoThree));
		assertFalse(valueCollection.contains(this.foo));
		assertFalse(valueCollection.contains(this.xyz));
	}

} // End MapExtractableTest
