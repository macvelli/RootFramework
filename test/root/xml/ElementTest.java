package root.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

class ElementTest {

	private Element parent;

	@Before
	public void setUp() {
		parent = new Element("Foo", 0);
	}

	@Test
	public void testAddChildName() {
		parent.addChild("Bar");

		assertEquals("<Foo>\n\t<Bar/>\n</Foo>\n", parent.toString());
	}

	@Test
	public void testAddChildNameValue() {
		parent.addChild("Bar", "XYZ");

		assertEquals("<Foo>\n\t<Bar>XYZ</Bar>\n</Foo>\n", parent.toString());
	}

	@Test
	public void testGetName() {
		assertEquals("Foo", parent.getName());
	}

	@Test
	public void testGetValue() {
		assertNull(parent.getValue());
	}

	@Test
	public void testToString() {
		assertEquals("<Foo/>\n", parent.toString());
	}

} // End ElementTest
