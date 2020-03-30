package root.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

class FastTest {

	@Test
	public void testVarg() {
		String[] strs = Parameters.toArray("A", "B");
		assertEquals(2, strs.length);
		assertEquals("A", strs[0]);
		assertEquals("B", strs[1]);
	}

} // End FastTest
