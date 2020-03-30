/*
 * Base32Test.java
 *
 * Last Modified: 03/04/2016
 */
package root.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import root.lang.Constants;

/**
 * BASE32(null) = null<br>
 * BASE32("") = ""<br>
 * BASE32("f") = "MY"<br>
 * BASE32("fo") = "MZXQ"<br>
 * BASE32("foo") = "MZXW6"<br>
 * BASE32("foob") = "MZXW6YQ"<br>
 * BASE32("fooba") = "MZXW6YTB"<br>
 * BASE32("foobar") = "MZXW6YTBOI"<br>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
class Base32Test {

	@Test
	public void testDecodeEmptyString() {
		assertEquals(Constants.EMPTY_STRING, Base32.decode(Constants.EMPTY_STRING));
	}

	@Test
	public void testDecodemyPaddingString() {
		assertEquals("f", Base32.decode("my======"));
	}

	@Test
	public void testDecodeMYPaddingString() {
		assertEquals("f", Base32.decode("MY======"));
	}

	@Test
	public void testDecodemyString() {
		assertEquals("f", Base32.decode("my"));
	}

	@Test
	public void testDecodeMYString() {
		assertEquals("f", Base32.decode("MY"));
	}

	@Test
	public void testDecodemzxqPaddingString() {
		assertEquals("fo", Base32.decode("mzxq===="));
	}

	@Test
	public void testDecodeMZXQPaddingString() {
		assertEquals("fo", Base32.decode("MZXQ===="));
	}

	@Test
	public void testDecodemzxqString() {
		assertEquals("fo", Base32.decode("mzxq"));
	}

	@Test
	public void testDecodeMZXQString() {
		assertEquals("fo", Base32.decode("MZXQ"));
	}

	@Test
	public void testDecodemzxw6PaddingString() {
		assertEquals("foo", Base32.decode("mzxw6==="));
	}

	@Test
	public void testDecodeMZXW6PaddingString() {
		assertEquals("foo", Base32.decode("MZXW6==="));
	}

	@Test
	public void testDecodemzxw6String() {
		assertEquals("foo", Base32.decode("mzxw6"));
	}

	@Test
	public void testDecodeMZXW6String() {
		assertEquals("foo", Base32.decode("MZXW6"));
	}

	@Test
	public void testDecodemzxw6yqPaddingString() {
		assertEquals("foob", Base32.decode("mzxw6yq="));
	}

	@Test
	public void testDecodeMZXW6YQPaddingString() {
		assertEquals("foob", Base32.decode("MZXW6YQ="));
	}

	@Test
	public void testDecodemzxw6yqString() {
		assertEquals("foob", Base32.decode("mzxw6yq"));
	}

	@Test
	public void testDecodeMZXW6YQString() {
		assertEquals("foob", Base32.decode("MZXW6YQ"));
	}

	@Test
	public void testDecodemzxw6ytboiPaddingString() {
		assertEquals("foobar", Base32.decode("mzxw6ytboi======"));
	}

	@Test
	public void testDecodeMZXW6YTBOIPaddingString() {
		assertEquals("foobar", Base32.decode("MZXW6YTBOI======"));
	}

	@Test
	public void testDecodemzxw6ytboiString() {
		assertEquals("foobar", Base32.decode("mzxw6ytboi"));
	}

	@Test
	public void testDecodeMZXW6YTBOIString() {
		assertEquals("foobar", Base32.decode("MZXW6YTBOI"));
	}

	@Test
	public void testDecodemzxw6ytbString() {
		assertEquals("fooba", Base32.decode("mzxw6ytb"));
	}

	@Test
	public void testDecodeMZXW6YTBString() {
		assertEquals("fooba", Base32.decode("MZXW6YTB"));
	}

	@Test
	public void testDecodeNullString() {
		assertNull(Base32.decode(null));
	}

	@Test
	public void testEncodeEmptyByteArray() {
		assertEquals(Constants.EMPTY_STRING, Base32.encode(new byte[] {}));
	}

	@Test
	public void testEncodefoobarString() {
		assertEquals("MZXW6YTBOI", Base32.encode("foobar"));
	}

	@Test
	public void testEncodefoobaString() {
		assertEquals("MZXW6YTB", Base32.encode("fooba"));
	}

	@Test
	public void testEncodefoobString() {
		assertEquals("MZXW6YQ", Base32.encode("foob"));
	}

	@Test
	public void testEncodefooString() {
		assertEquals("MZXW6", Base32.encode("foo"));
	}

	@Test
	public void testEncodefoString() {
		assertEquals("MZXQ", Base32.encode("fo"));
	}

	@Test
	public void testEncodefString() {
		assertEquals("MY", Base32.encode("f"));
	}

	@Test
	public void testEncodeNullByteArray() {
		assertNull(Base32.encode((byte[]) null));
	}

} // End Base32Test
