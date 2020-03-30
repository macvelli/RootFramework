package root.adt;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import root.adt.ListArray;
import root.adt.SetHashed;

class SetHashedTest extends TestCase {

	private SetHashed<String> s;

	@Override
	@Before
	public void setUp() throws Exception {
		s = new SetHashed<String>();
	}

	@Test
	public void testAdd() {
		assertTrue(s.add("Foo"));
		assertTrue(s.add("Bar"));
		assertFalse(s.add("Foo"));
		assertEquals(2, s.getSize());
	}

	@Test
	public void testAffixArray() {
		s.addAll("Foo", "Bar", "Foo");
		assertEquals(2, s.getSize());
	}

	@Test
	public void testAddIterable() {
		ListArray<String> a = new ListArray<String>();
		a.addAll("Foo", "Bar", "Foo");
		s.addAll(a);
		assertEquals(2, s.getSize());
	}

	@Test
	public void testClear() {
		s.addAll("Foo", "Bar");
		s.clear();
		assertEquals(0, s.getSize());
		assertTrue(s.isEmpty());
	}

	@Test
	public void testContains() {
		s.addAll("Foo", "Bar");
		assertTrue(s.contains("Foo"));
		assertFalse(s.contains("XYZ"));
	}

	@Test
	public void testContainsAllArray() {
		s.addAll("Foo", "Bar");
		assertTrue(s.containsAll("Bar", "Foo"));
		assertFalse(s.containsAll("Foo", "Bar", "ABC"));
	}

	@Test
	public void testContainsAllIterable() {
		ListArray<String> a = new ListArray<String>();
		a.addAll("Foo", "Bar");
		s.addAll("Foo", "Bar");
		// assertTrue(s.containsAll(a));
		a.add("ABC");
		// assertFalse(s.containsAll(a));
		a.remove(0);
		// assertFalse(s.containsAll(a));
	}

	@Test
	public void testContainsAnyArray() {
		s.addAll("Foo", "Bar");
		assertTrue(s.containsAny("Bar", "ABC"));
		assertFalse(s.containsAny("ABC", "XYZ", "123"));
	}

	@Test
	public void testContainsAnyIterable() {
		ListArray<String> a = new ListArray<String>();
		a.addAll("Foo", "XYZ");
		s.addAll("Foo", "Bar");
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
		s.addAll("Foo", "Bar");
		assertTrue(s.equals(h));
	}

	@Test
	public void testHashCode() {
		s.addAll("Foo", "Bar");
		assertEquals(137371, s.hashCode());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(s.isEmpty());
		s.add("Foo");
		assertFalse(s.isEmpty());
	}

	@Test
	public void testIterator() {
		s.addAll("Foo", "Bar");
		Iterator<String> i = s.iterator();
		assertTrue(i.hasNext());
		assertEquals("Foo", i.next());
		assertTrue(i.hasNext());
		assertEquals("Bar", i.next());
		assertFalse(i.hasNext());
	}

	@Test
	public void testRemove() {
		s.addAll("Foo", "Bar");
		assertTrue(s.remove("Foo"));
		assertEquals(1, s.getSize());
		assertFalse(s.remove("ABC"));
		assertEquals(1, s.getSize());
	}

	@Test
	public void testReplace() {
		s.addAll("Foo", "Bar");
		assertTrue(s.replace("Foo", "XYZ"));
		assertEquals(2, s.getSize());
		assertFalse(s.replace("Foo", "Goo"));
		assertFalse(s.replace("XYZ", "Bar"));
		assertEquals(1, s.getSize());
	}

	@Test
	public void testSize() {
		assertEquals(0, s.getSize());
		s.add("Foo");
		assertEquals(1, s.getSize());
	}

	@Test
	public void testToArray() {
		s.addAll("Foo", "Bar");
		String[] t = s.toArray(new String[s.size]);
		assertEquals(2, t.length);
		assertEquals("Foo", t[0]);
		assertEquals("Bar", t[1]);
	}

	@Test
	public void testToString() {
		s.addAll("Foo", "Bar");
		assertEquals("[Foo, Bar]", s.toString());
	}

} // End HeapSetTest
