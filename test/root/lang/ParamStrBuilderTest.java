package root.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

class StringExtractorTest {

	@Test
	public void testStringExtractor() {
		StringExtractor paramBuilder = new StringExtractor();

		assertEquals(0, paramBuilder.getLength());
		assertEquals(16, paramBuilder.getCapacity());
	}

	@Test
	public void testStringExtractorInt() {
		StringExtractor paramBuilder = new StringExtractor(100);

		assertEquals(0, paramBuilder.getLength());
		assertEquals(100, paramBuilder.getCapacity());
	}

	@Test
	public void testStringExtractorString() {
		StringExtractor paramBuilder = new StringExtractor("Foo Bar");

		assertEquals(7, paramBuilder.getLength());
		assertEquals(23, paramBuilder.getCapacity());
	}

	@Test
	public void testStringExtractorCharSequence() {
		StringExtractor paramBuilder = new StringExtractor(new StringBuilder("Foo Bar"));

		assertEquals(7, paramBuilder.getLength());
		assertEquals(23, paramBuilder.getCapacity());
	}

	@Test
	public void testAppendBoolean() {
		StringExtractor paramBuilder = new StringExtractor();

		paramBuilder.append(true);
		assertEquals("true", paramBuilder.toString());
	}

	@Test
	public void testAppendChar() {
		StringExtractor paramBuilder = new StringExtractor();

		paramBuilder.append('K');
		assertEquals("K", paramBuilder.toString());
	}

	@Test
	public void testAppendCharArray() {
		StringExtractor paramBuilder = new StringExtractor();

		paramBuilder.append('F', 'o', 'o');
		assertEquals("Foo", paramBuilder.toString());
	}

	@Test
	public void testAppendCharArrayIntInt() {
		StringExtractor paramBuilder = new StringExtractor();

		paramBuilder.append(new char[] { 'F', 'o', 'o', ' ', 'B', 'a', 'r' }, 1, 5);
		assertEquals("oo Ba", paramBuilder.toString());
	}

	@Test
	public void testAppendCharSequence() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendCharSequenceIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendDouble() {
		StringExtractor paramBuilder = new StringExtractor();

		paramBuilder.append(1.25d);
		assertEquals("1.25", paramBuilder.toString());
	}

	@Test
	public void testAppendExtractor() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendExtractorArrayIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendFloat() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendCollectionOfQ() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendObjectArrayIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendParamStrObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendString() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendStringObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendStringBuffer() {
		fail("Not yet implemented");
	}

	@Test
	public void testAppendStringBuilder() {
		fail("Not yet implemented");
	}

	@Test
	public void testCharAt() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteCharAt() {
		fail("Not yet implemented");
	}

	@Test
	public void testLength() {
		fail("Not yet implemented");
	}

	@Test
	public void testReplace() {
		fail("Not yet implemented");
	}

	@Test
	public void testReverse() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetCharAt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubSequence() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubstringInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubstringIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testMain() {
		fail("Not yet implemented");
	}

}
