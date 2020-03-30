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

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * Test the {@link MapBuilder} class.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class MapBuilderTest extends TestCase {

	private MapBuilder<String, String> builder;

	public MapBuilderTest() {
		super("MapBuilder");
	}

	@Override
	@Before
	public void setUp() throws Exception {
		this.builder = new MapBuilder<>();
	}

	@Test
	public void testConstructorCapacity() {
		MapBuilder<String, String> m = new MapBuilder<>(17);
		assertEquals(0, m.map.size);
		assertEquals(32, m.map.capacity);
		assertEquals(43, m.map.table.length);

		// Test minimum capacity of 8
		m = new MapBuilder<>(7);
		assertEquals(0, m.map.size);
		assertEquals(8, m.map.capacity);
		assertEquals(11, m.map.table.length);
	}

	@Test
	public void testConstructorDefault() {
		assertEquals(0, this.builder.map.size);
		assertEquals(8, this.builder.map.capacity);
		assertEquals(11, this.builder.map.table.length);
	}

	@Test
	public void testEquals() {
		try {
			this.builder.equals(null);
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot test for equals(Object) with a MapBuilder", e.getMessage());
		}
	}

	@Test
	public void testHashCode() {
		try {
			this.builder.hashCode();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot call hashCode() with a MapBuilder", e.getMessage());
		}
	}

	@Test
	public void testPut() {
		final MapHashed<String, String> m = this.builder.put("foo", "bar")
				.put("xyz", "123")
				.put("abc", "def")
				.put("ghi", "jkl")
				.put("mno", "pqr")
				.put("stu", "vwx")
				.put("yza", "bcd")
				.put("efg", "hij")
				.put("klm", "nop")
				.build();

		assertEquals(9, m.size);
		assertEquals(16, m.capacity);
		assertEquals(23, m.table.length);

		assertEquals("bar", m.get("foo"));
		assertEquals("123", m.get("xyz"));
		assertEquals("def", m.get("abc"));
		assertEquals("jkl", m.get("ghi"));
		assertEquals("pqr", m.get("mno"));
		assertEquals("vwx", m.get("stu"));
		assertEquals("bcd", m.get("yza"));
		assertEquals("hij", m.get("efg"));
		assertEquals("nop", m.get("klm"));
	}

	@Test
	public void testPutAll() {
		final Map<String, String> stringMap = new HashMap<>();
		stringMap.put("foo", "bar");
		stringMap.put("xyz", "123");

		final MapHashed<String, String> m = this.builder.putAll(stringMap).build();

		assertEquals(2, m.size);
		assertEquals("bar", m.get("foo"));
		assertEquals("123", m.get("xyz"));
	}

	public void testToString() {
		try {
			this.builder.toString();
			fail("Expected java.lang.UnsupportedOperationException was not thrown");
		} catch (final UnsupportedOperationException e) {
			assertEquals("Cannot call toString() with a MapBuilder", e.getMessage());
		}
	}

} // End MapBuilderTest
