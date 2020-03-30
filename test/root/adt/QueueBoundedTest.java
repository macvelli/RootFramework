package root.adt;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import root.adt.QueueBounded;

class QueueBoundedTest extends TestCase {

	private QueueBounded<String> q;

	@Override
	@Before
	public void setUp() throws Exception {
		q = new QueueBounded<>(2);
	}

	@Test
	public void testClear() {
		q.enqueue("Foo");
		assertEquals(1, q.getSize());
		q.clear();
		assertEquals(0, q.getSize());
		assertTrue(q.isEmpty());
	}

	@Test
	public void testDequeue() {
		q.enqueue("Foo");
		assertEquals("Foo", q.dequeue());
		assertEquals(0, q.getSize());
		assertTrue(q.isEmpty());
	}

	@Test
	public void testEnqueue() {
		assertEquals(0, q.getSize());
		assertTrue(q.isEmpty());
		q.enqueue("Foo");
		assertEquals(1, q.getSize());
	}

	@Test
	public void testEquals() {
		QueueBounded<String> d = new QueueBounded<String>(2);
		d.enqueue("Foo");
		q.enqueue("Foo");
		assertTrue(q.equals(d));
		d.enqueue("Bar");
		assertFalse(q.equals(d));
		d.dequeue();
		assertFalse(q.equals(d));
	}

	@Test
	public void testHashCode() {
		q.enqueue("Foo");
		q.enqueue("Bar");
		assertEquals(207543, q.hashCode());
	}

	@Test
	public void testIsEmpty() {
		assertTrue(q.isEmpty());
	}

	@Test
	public void testIterator() {
		q.enqueue("Foo");
		q.enqueue("Bar");
		Iterator<String> i = q.iterator();
		assertTrue(i.hasNext());
		assertEquals("Foo", i.next());
		assertTrue(i.hasNext());
		assertEquals("Bar", i.next());
		assertFalse(i.hasNext());
	}

	@Test
	public void testPeek() {
		q.enqueue("Foo");
		assertEquals("Foo", q.peek());
		assertEquals(1, q.getSize());
		assertFalse(q.isEmpty());
	}

	@Test
	public void testSize() {
		assertEquals(0, q.getSize());
	}

	@Test
	public void testToString() {
		q.enqueue("Foo");
		q.enqueue("Bar");
		assertEquals("[Foo, Bar]", q.toString());
	}

}
