package root.adt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import root.adt.ListArray;
import root.adt.SetHashed;
import root.adt.SetImmutable;
import root.util.Parameters;

class SetImmutableTest {

	private SetImmutable<String> s;

	@Before
	public void setUp() throws Exception {
		s = new SetImmutable<String>(Parameters.toArray("Foo", "Bar"));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAdd() {
		s.add("ABC");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddAllArray() {
		s.addAll("ABC", "123", "Foo");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testAddIterable() {
		s.addAll(new ListArray<String>());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testClear() {
		s.clear();
	}

	@Test
	public void testContains() {
		assertTrue(s.contains("Foo"));
		assertFalse(s.contains("XYZ"));
	}

	@Test
	public void testContainsAllArray() {
		assertTrue(s.containsAll("Bar", "Foo"));
		assertFalse(s.containsAll("Foo", "Bar", "ABC"));
	}

	@Test
	public void testContainsAllIterable() {
		ListArray<String> a = new ListArray<String>();
		a.addAll("Foo", "Bar");
		// assertTrue(s.containsAll(a));
		a.add("ABC");
		// assertFalse(s.containsAll(a));
		a.remove(0);
		// assertFalse(s.containsAll(a));
	}

	@Test
	public void testContainsAnyArray() {
		assertTrue(s.containsAny("Bar", "ABC"));
		assertFalse(s.containsAny("ABC", "XYZ", "123"));
	}

	@Test
	public void testContainsAnyIterable() {
		ListArray<String> a = new ListArray<String>();
		a.addAll("Foo", "XYZ");
		assertTrue(s.containsAny(a));
		a.add("ABC");
		assertTrue(s.containsAny(a));
		a.remove(0);
		assertFalse(s.containsAny(a));
	}

	@Test
	public void testEquals() {
		SetHashed<String> h = new SetHashed<String>();
		h.addAll("Bar", "Foo");
		assertTrue(s.equals(h));
	}

	@Test
	public void testHashCode() {
		assertEquals(338240, s.hashCode());
	}

	@Test
	public void testIsEmpty() {
		assertFalse(s.isEmpty());
	}

	@Test
	public void testIterator() {
		Iterator<String> i = s.iterator();
		assertTrue(i.hasNext());
		assertEquals("Bar", i.next());
		assertTrue(i.hasNext());
		assertEquals("Foo", i.next());
		assertFalse(i.hasNext());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemove() {
		s.remove("Foo");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testReplace() {
		s.replace("Foo", "XYZ");
	}

	@Test
	public void testSize() {
		assertEquals(2, s.getSize());
	}

	@Test
	public void testToArray() {
		String[] t = s.toArray();
		assertEquals(2, t.length);
		assertEquals("Bar", t[0]);
		assertEquals("Foo", t[1]);
	}

	@Test
	public void testToString() {
		assertEquals("[Bar, Foo]", s.toString());
	}

} // End SetImmutableTest
