package root.adt;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import root.adt.ListArray;
import root.adt.SetMultiKey;
import root.util.Parameters;

class SetMultiKeyTest extends TestCase {

	private SetMultiKey<String> s;

	@Override
	@Before
	public void setUp() throws Exception {
		s = new SetMultiKey<String>();
	}

	@Test
	public void testAdd() {
		assertTrue(s.add(Parameters.toArray("Foo")));
		assertTrue(s.add(Parameters.toArray("Bar")));
		assertFalse(s.add(Parameters.toArray("Foo")));
		assertEquals(2, s.getSize());
	}

	@Test
	public void testAffixArray() {
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"), Parameters.toArray("Foo"));
		assertEquals(2, s.getSize());
	}

	@Test
	public void testAddIterable() {
		ListArray<String[]> a = new ListArray<String[]>();
		a.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"), Parameters.toArray("Foo"));
		s.addAll(a);
		assertEquals(2, s.getSize());
	}

	@Test
	public void testClear() {
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		s.clear();
		assertEquals(0, s.getSize());
		assertTrue(s.isEmpty());
	}

	@Test
	public void testContains() {
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		assertTrue(s.contains(Parameters.toArray("Foo")));
		assertFalse(s.contains(Parameters.toArray("XYZ")));
	}

	@Test
	public void testContainsAllArray() {
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		assertTrue(s.containsAll(Parameters.toArray("Bar"), Parameters.toArray("Foo")));
		assertFalse(s.containsAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"), Parameters.toArray("ABC")));
	}

	@Test
	public void testContainsAllIterable() {
		ListArray<String[]> a = new ListArray<String[]>();
		a.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		// assertTrue(s.containsAll(a));
		a.add(Parameters.toArray("ABC"));
		// assertFalse(s.containsAll(a));
		a.remove(0);
		// assertFalse(s.containsAll(a));
	}

	@Test
	public void testContainsAnyArray() {
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		assertTrue(s.containsAny(Parameters.toArray("Bar"), Parameters.toArray("ABC")));
		assertFalse(s.containsAny(Parameters.toArray("ABC"), Parameters.toArray("XYZ"), Parameters.toArray("123")));
	}

	@Test
	public void testContainsAnyIterable() {
		ListArray<String[]> a = new ListArray<String[]>();
		a.addAll(Parameters.toArray("Foo"), Parameters.toArray("XYZ"));
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		assertTrue(s.containsAny(a));
		a.add(Parameters.toArray("ABC"));
		assertTrue(s.containsAny(a));
		a.remove(0);
		assertFalse(s.containsAny(a));
	}

	@Test
	public void testEquals() {
		SetMultiKey<String> h = new SetMultiKey<String>();
		h.addAll(Parameters.toArray("Bar"), Parameters.toArray("Foo"));
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		assertTrue(s.equals(h));
	}

	@Test
	public void testHashCode() {
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		assertEquals(137371, s.hashCode());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(s.isEmpty());
		s.add(Parameters.toArray("Foo"));
		assertFalse(s.isEmpty());
	}

	@Test
	public void testIterator() {
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		Iterator<String[]> i = s.iterator();
		assertTrue(i.hasNext());
		assertEquals("Foo", i.next()[0]);
		assertTrue(i.hasNext());
		assertEquals("Bar", i.next()[0]);
		assertFalse(i.hasNext());
	}

	@Test
	public void testRemove() {
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		assertTrue(s.remove(Parameters.toArray("Foo")));
		assertEquals(1, s.getSize());
		assertFalse(s.remove(Parameters.toArray("ABC")));
		assertEquals(1, s.getSize());
	}

	@Test
	public void testReplace() {
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		assertTrue(s.replace(Parameters.toArray("Foo"), Parameters.toArray("XYZ")));
		assertEquals(2, s.getSize());
		assertFalse(s.replace(Parameters.toArray("Foo"), Parameters.toArray("Goo")));
		assertFalse(s.replace(Parameters.toArray("XYZ"), Parameters.toArray("Bar")));
		assertEquals(1, s.getSize());
	}

	@Test
	public void testSize() {
		assertEquals(0, s.getSize());
		s.add(Parameters.toArray("Foo"));
		assertEquals(1, s.getSize());
	}

	@Test
	public void testToArray() {
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		String[][] t = s.toArray();
		assertEquals(2, t.length);
		assertEquals("Foo", t[0][0]);
		assertEquals("Bar", t[1][0]);
	}

	@Test
	public void testToString() {
		s.addAll(Parameters.toArray("Foo"), Parameters.toArray("Bar"));
		assertEquals("[[Foo], [Bar]]", s.toString());
	}

} // End MultiKeySetTest
