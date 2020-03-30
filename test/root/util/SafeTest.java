package root.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import root.lang.Constants;

class SafeTest {

	@Test
	public void testNotEqual() {
		assertTrue(Root.notEqual("A", "B"));
		assertTrue(Root.notEqual(null, "B"));
		assertTrue(Safe.notEqualToClass("A", null));
		assertFalse(Root.notEqual("A", "A"));
		assertFalse(Safe.notEqualToClass(null, null));
	}

	@Test
	public void testEqualsObjectObject() {
		assertTrue(Root.equals("A", "A"));
		assertTrue(Root.equals(null, null));
		assertFalse(Root.equals("A", "B"));
		assertFalse(Root.equals(null, "B"));
		assertFalse(Root.equals("A", null));
	}

	@Test
	public void testEqualsObjectArrayObjectArray() {
		String[] strsA = null, strsB = null;

		assertTrue(Root.equals(strsA, strsB));

		strsA = Parameters.toArray("A", "B");
		assertFalse(Root.equals(strsA, strsB));
		assertFalse(Root.equals(strsB, strsA));

		strsB = Parameters.toArray("A");
		assertFalse(Root.equals(strsA, strsB));

		strsB = Parameters.toArray("B", "A");
		assertFalse(Root.equals(strsA, strsB));

		strsB = Parameters.toArray("A", "B");
		assertTrue(Root.equals(strsA, strsB));
	}

	@Test
	public void testHashCodeObject() {
		String s = null;
		assertEquals(0, Root.hashCode(s));
		assertEquals(70822, Root.hashCode("Foo"));
		assertEquals(463543527, Root.hashCode("Barfield"));
	}

	@Test
	public void testHashCodeObjectArray() {
		String[] strs = null;

		assertEquals(0, Root.hashCode(strs));
		assertEquals(463472707, Root.hashCode(Parameters.toArray("Foo", "Barfield")));
	}

	@Test
	public void testToStringObject() {
		Integer i = null;
		assertEquals("null", Root.toString(i));
		assertTrue(Constants.NULL_STRING == Root.toString(i));
		i = 3;
		assertEquals("3", Root.toString(i));
	}

	@Test
	public void testToStringObjectArray() {
		final Object[] objs = null;
		assertEquals("[]", Root.toString(objs));
		assertEquals("[]", Root.toString(new Object[0]));
		String[] is = Parameters.toArray("one", "two", "three");
		assertEquals("[one,two,three]", Root.toString(is));
	}

	@Test
	public void testToStringObjectArrayLength() {
		assertEquals("[]", Root.toString(null, 2));
		assertEquals("[]", Root.toString(new Object[0], 0));
		String[] is = Parameters.toArray("one", "two", "three");
		assertEquals("[one,two]", Root.toString(is, 2));
	}

	@Test
	public void testMaxLen() {
		String s = "Maximum Length";

		assertEquals("Maximum Length", Root.maxLength(s, 50));
		assertEquals("Maximum Le", Root.maxLength(s, 10));
	}

	@Test
	public void testIsEmpty() {
		String s = null;
		assertTrue(Root.isEmpty(s));
		assertTrue(Root.isEmpty(""));
		assertFalse(Root.isEmpty(" "));

	}

	@Test
	public void testLength() {
		assertEquals(15, Root.length("Foo", "Bar", "XYZ", "123", "ABC"));
	}

	@Test
	public void testTrim() {
		String s = null;
		assertNull(Root.trim(s));
		assertEquals("Foo", Root.trim(" Foo "));
	}

	@Test
	public void testValueOf() {
		String[] s = Root.valueOf(new Object[] { null, "Foo", 123 });
		assertEquals(3, s.length);
		assertEquals("null", s[0]);
		assertEquals("Foo", s[1]);
		assertEquals("123", s[2]);
	}

} // End SafeTest
