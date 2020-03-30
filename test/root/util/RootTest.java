package root.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

class RootTest {

	@Test
	public void testBetween() {
		String s = "(Foo) {Bar} []";

		assertEquals("Foo", Root.between(s, '(', ')'));
		assertEquals("Bar", Root.between(s, '{', '}'));
		assertEquals("Foo) {Bar", Root.between(s, '(', '}'));
		assertNull(Root.between(s, '[', ']'));

		s = null;
		assertNull(Root.between(s, '(', ')'));
	}

} // End RootTest
