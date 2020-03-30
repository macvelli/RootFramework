package root.adt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import root.adt.StackLinked;

class StackLinkedTest {

	private StackLinked<String> s;

	@Before
	public void setUp() throws Exception {
		s = new StackLinked<String>();
	}

	@Test
	public void testClear() {
		s.push("Foo");
		assertEquals(1, s.getSize());
		s.clear();
		assertEquals(0, s.getSize());
		assertTrue(s.isEmpty());
	}

	@Test
	public void testEquals() {
		StackLinked<String> d = new StackLinked<>();
		d.push("Foo");
		s.push("Foo");
		assertTrue(s.equals(d));
		d.push("Bar");
		assertFalse(s.equals(d));
		d.pop();
		assertTrue(s.equals(d));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testHashCode() {
		s.hashCode();
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
	}

	@Test
	public void testSize() {
		assertEquals(0, s.getSize());
	}

	@Test
	public void testToString() {
		s.push("Foo");
		s.push("Bar");
		assertEquals("[Bar, Foo]", s.toString());
	}

}
