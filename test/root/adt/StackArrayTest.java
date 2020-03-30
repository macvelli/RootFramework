package root.adt;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import root.adt.StackArray;

class StackArrayTest extends TestCase {

	private StackArray<String> s;

	@Override
	@Before
	public void setUp() throws Exception {
		s = new StackArray<>();
	}

	@Test
	public void testClear() {
		s.push("Foo");
		assertEquals(1, s.getSize());
		assertFalse(s.isEmpty());
		s.clear();
		assertEquals(0, s.getSize());
		assertTrue(s.isEmpty());
	}

	@Test
	public void testEquals() {
		ArrayList<String> list = new ArrayList<>();

		s.push("Foo");
		assertFalse(s.equals(list));
		list.add("Foo");
		assertTrue(s.equals(list));
		list.add(0, "Bar");
		assertFalse(s.equals(list));
		s.push("Bar");
		assertTrue(s.equals(list));
	}

	@Test
	public void testHashCode() {
		s.push("Foo");
		s.push("Bar");
		assertEquals(403080, s.hashCode());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(s.isEmpty());
	}

	@Test
	public void testIterator() {
		s.push("Foo");
		s.push("Bar");
		Iterator<String> i = s.iterator();
		assertTrue(i.hasNext());
		assertEquals("Bar", i.next());
		assertTrue(i.hasNext());
		assertEquals("Foo", i.next());
		assertFalse(i.hasNext());
	}

	@Test
	public void testPeek() {
		s.push("Foo");
		assertEquals("Foo", s.peek());
		assertEquals(1, s.getSize());
		assertFalse(s.isEmpty());
	}

	@Test
	public void testPop() {
		s.push("Foo");
		assertEquals("Foo", s.pop());
		assertEquals(0, s.getSize());
		assertTrue(s.isEmpty());
	}

	@Test
	public void testPush() {
		assertEquals(0, s.getSize());
		assertTrue(s.isEmpty());
		s.push("Foo");
		assertEquals(1, s.getSize());
		assertFalse(s.isEmpty());
	}

	@Test
	public void testSize() {
		assertEquals(0, s.getSize());
		s.push("Foo");
		assertEquals(1, s.getSize());
		s.pop();
		assertEquals(0, s.getSize());
	}

	@Test
	public void testToString() {
		s.push("Foo");
		s.push("Bar");
		assertEquals("[Bar, Foo]", s.toString());
	}

}
