package root.lang;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

class ParamStrTest {

	@Test
	public void testBuild() {
		ParamString p = new ParamString("{P} goes the weasel");
		assertEquals("Pop goes the weasel", p.formatMsg("Pop"));

		p = new ParamString("Twinkle, {P} little star");
		assertEquals("Twinkle, twinkle little star", p.formatMsg("twinkle"));

		p = new ParamString("A B C {P} {P} F G");
		assertEquals("A B C D E F G", p.format('D', 'E'));

		p = new ParamString("Mary had a little {P}");
		assertEquals("Mary had a little lamb", p.formatMsg("lamb"));

		p = new ParamString("Row, row, row your boat");
		assertEquals("Row, row, row your boat", p.format());

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void failBuild() {
		ParamString p = new ParamString("Index: {P}, Size: {P} is going to fail");
		p.format(4, 3, 2);
	}

	@Test
	public void testStaticFormat() {
		String empty = null;

		assertEquals("Index: 4, Size: 3", ParamString.formatMsg("Index: {P}, Size: {P}", 4, 3));
		assertEquals("Passed one less {P} than I should have",
				ParamString.formatMsg("Passed {P} less {P} than I should have", "one"));
		assertEquals("true or false", ParamString.formatMsg("{P} or {P}", true, false));
		assertEquals("What happens when null is a parameter?",
				ParamString.formatMsg("What happens when {P} is a parameter?", empty));
		assertEquals("nothing to format", ParamString.formatMsg("nothing to format"));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void failStaticFormat() {
		ParamString.formatMsg("Index: {P}, Size: {P} is going to fail", 4, 3, 2);
	}

} // End ParamStrTest
