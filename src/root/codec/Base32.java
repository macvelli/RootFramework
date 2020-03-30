/*
 * Copyright 2006-2016 Edward Smith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package root.codec;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import root.lang.Constants;
import root.util.Root;

/**
 * In Java 8 it can be done as
 *
 * Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8)) Here is a short,
 * self-contained complete example
 *
 * import java.nio.charset.StandardCharsets; import java.util.Base64;
 *
 * public class Temp { public static void main(String... args) throws Exception { final String s =
 * "old crow medicine show"; final byte[] authBytes = s.getBytes(StandardCharsets.UTF_8); final
 * String encoded = Base64.getEncoder().encodeToString(authBytes); System.out.println(s + " => " +
 * encoded); } }
 *
 * gives output
 *
 * old crow medicine show => b2xkIGNyb3cgbWVkaWNpbmUgc2hvdw==
 *
 * <h1>RFC 4648 Base32 Implementation</h1>
 *
 * <h2>Advantages</h2>
 *
 * <p>
 * Base32 has number of advantages over Base64:
 * </p>
 *
 * <ol>
 * <li>The resulting character set is all one case (usually represented as uppercase), which can
 * often be beneficial when using a case-insensitive filesystem, spoken language, or human memory.
 * </li>
 * <li>The result can be used as a file name because it can not possibly contain the '/' symbol,
 * which is the Unix path separator.</li>
 * <li>The alphabet can be selected to avoid similar-looking pairs of different symbols, so the
 * strings can be accurately transcribed by hand. (For example, the RFC 4648 symbol set omits the
 * digits for one, eight and zero, since they could be confused with the letters 'I', 'B', and 'O'.)
 * </li>
 * <li>A result excluding padding can be included in a URL without encoding any characters.</li>
 * </ol>
 *
 * <p>
 * Base32 has these advantages over hexadecimal/Base16:
 * </p>
 *
 * <ol>
 * <li>Base32 representation takes roughly 20% less space. (1000 bits takes 200 characters, compared
 * to 250 for Base16)</li>
 * </ol>
 *
 * <h2>Disadvantages</h2>
 *
 * <ol>
 * <li>Base32 representation takes roughly 20% more space than Base64. Also, because it encodes 5
 * bytes to 8 characters (rather than 3 bytes to 4 characters), padding to an 8-character boundary
 * is a greater burden on short messages.</li>
 * </ol>
 *
 * <p>
 * Length of Base64 and Base32 notations as percentage of binary data
 * </p>
 *
 * <table>
 * <tr>
 * <th>&nbsp;</th>
 * <th>Base64</th>
 * <th>Base32</th>
 * </tr>
 * <tr>
 * <td>8-bit</td>
 * <td>133%</td>
 * <td>160%</td>
 * </tr>
 * <tr>
 * <td>7-bit</td>
 * <td>117%</td>
 * <td>140%</td>
 * </tr>
 * </table>
 *
 * Implementations MUST include appropriate pad characters at the end of encoded data unless the
 * specification referring to this document explicitly states otherwise.
 *
 * Implementations MUST reject the encoded data if it contains characters outside the base alphabet
 * when interpreting base-encoded data, unless the specification referring to this document
 * explicitly states otherwise. Such specifications may instead state, as MIME does, that characters
 * outside the base encoding alphabet should simply be ignored when interpreting data ("be liberal
 * in what you accept"). Note that this means that any adjacent carriage return/ line feed (CRLF)
 * characters constitute "non-alphabet characters" and are ignored. Furthermore, such specifications
 * MAY ignore the pad character, "=", treating it as non-alphabet data, if it is present before the
 * end of the encoded data. If more than the allowed number of pad characters is found at the end of
 * the string (e.g., a base 64 string terminated with "==="), the excess pad characters MAY also be
 * ignored.
 *
 * - The Base 32 encoding is designed to represent arbitrary sequences of octets in a form that
 * needs to be case insensitive but that need not be human readable.
 *
 * A 33-character subset of US-ASCII is used, enabling 5 bits to be represented per printable
 * character. (The extra 33rd character, "=", is used to signify a special processing function.)
 *
 * The encoding process represents 40-bit groups of input bits as output strings of 8 encoded
 * characters. Proceeding from left to right, a 40-bit input group is formed by concatenating 5 8bit
 * input groups. These 40 bits are then treated as 8 concatenated 5-bit groups, each of which is
 * translated into a single character in the base 32 alphabet. When a bit stream is encoded via the
 * base 32 encoding, the bit stream must be presumed to be ordered with the most-significant- bit
 * first. That is, the first bit in the stream will be the high- order bit in the first 8bit byte,
 * the eighth bit will be the low- order bit in the first 8bit byte, and so on.
 *
 * Each 5-bit group is used as an index into an array of 32 printable characters. The character
 * referenced by the index is placed in the output string. These characters, identified in Table 3,
 * below, are selected from US-ASCII digits and uppercase letters.
 *
 * Table 3: The Base 32 Alphabet
 *
 * Value Encoding Value Encoding Value Encoding Value Encoding 0 A 9 J 18 S 27 3 1 B 10 K 19 T 28 4
 * 2 C 11 L 20 U 29 5 3 D 12 M 21 V 30 6 4 E 13 N 22 W 31 7 5 F 14 O 23 X 6 G 15 P 24 Y (pad) = 7 H
 * 16 Q 25 Z 8 I 17 R 26 2
 *
 * Special processing is performed if fewer than 40 bits are available at the end of the data being
 * encoded. A full encoding quantum is always completed at the end of a body. When fewer than 40
 * input bits are available in an input group, bits with value zero are added (on the right) to form
 * an integral number of 5-bit groups. Padding at the end of the data is performed using the "="
 * character. Since all base 32 input is an integral number of octets, only the following cases can
 * arise:
 *
 * (1) The final quantum of encoding input is an integral multiple of 40 bits; here, the final unit
 * of encoded output will be an integral multiple of 8 characters with no "=" padding.
 *
 * (2) The final quantum of encoding input is exactly 8 bits; here, the final unit of encoded output
 * will be two characters followed by six "=" padding characters.
 *
 * (3) The final quantum of encoding input is exactly 16 bits; here, the final unit of encoded
 * output will be four characters followed by four "=" padding characters.
 *
 * (4) The final quantum of encoding input is exactly 24 bits; here, the final unit of encoded
 * output will be five characters followed by three "=" padding characters.
 *
 * (5) The final quantum of encoding input is exactly 32 bits; here, the final unit of encoded
 * output will be seven characters followed by one "=" padding character.
 *
 * The case for base 32 is shown in the following figure, borrowed from [7]. Each successive
 * character in a base-32 value represents 5 successive bits of the underlying octet sequence. Thus,
 * each group of 8 characters represents a sequence of 5 octets (40 bits).
 *
 * 1 2 3 01234567 89012345 67890123 45678901 23456789 +--------+--------+--------+--------+--------+
 * |< 1 >< 2| >< 3 ><|.4 >< 5.|>< 6 ><.|7 >< 8 >| +--------+--------+--------+--------+--------+
 * <===> 8th character <====> 7th character <===> 6th character <====> 5th character <====> 4th
 * character <===> 3rd character <====> 2nd character <===> 1st character
 *
 * 10. Test Vectors
 *
 * BASE32("") = ""
 *
 * BASE32("f") = "MY======"
 *
 * BASE32("fo") = "MZXQ===="
 *
 * BASE32("foo") = "MZXW6==="
 *
 * BASE32("foob") = "MZXW6YQ="
 *
 * BASE32("fooba") = "MZXW6YTB"
 *
 * BASE32("foobar") = "MZXW6YTBOI======"
 *
 * 12. Security Considerations
 *
 * When base encoding and decoding is implemented, care should be taken not to introduce
 * vulnerabilities to buffer overflow attacks, or other attacks on the implementation. A decoder
 * should not break on invalid input including, e.g., embedded NUL characters (ASCII 0).
 *
 * If non-alphabet characters are ignored, instead of causing rejection of the entire encoding (as
 * recommended), a covert channel that can be used to "leak" information is made possible. The
 * ignored characters could also be used for other nefarious purposes, such as to avoid a string
 * equality comparison or to trigger implementation bugs. The implications of ignoring non-alphabet
 * characters should be understood in applications that do not follow the recommended practice.
 * Similarly, when the base 16 and base 32 alphabets are handled case insensitively, alteration of
 * case can be used to leak information or make string equality comparisons fail.
 *
 * When padding is used, there are some non-significant bits that warrant security concerns, as they
 * may be abused to leak information or used to bypass string equality comparisons or to trigger
 * implementation problems.
 *
 * Base encoding visually hides otherwise easily recognized information, such as passwords, but does
 * not provide any computational confidentiality. This has been known to cause security incidents
 * when, e.g., a user reports details of a network protocol exchange (perhaps to illustrate some
 * other problem) and accidentally reveals the password because she is unaware that the base
 * encoding does not protect the password.
 *
 * Base encoding adds no entropy to the plaintext, but it does increase the amount of plaintext
 * available and provide a signature for cryptanalysis in the form of a characteristic probability
 * distribution.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class Base32 {

	// <><><><><><><><><><><><><><><> Constants ><><><><><><><><><><><><><><><>

	private static final char[]	base32Encoding = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7' };

	private static final byte[]	base32Decoding = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11,
			0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x02, 0x03,
			0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15,
			0x16, 0x17, 0x18, 0x19, 0x00, 0x00, 0x00, 0x00, 0x00 };

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	/**
	 *
	 * @param base32Str
	 * @return
	 */
	public static final String decode(final String base32Str) {
		// No padding character is used
		// During decoding, differences in case are ignored; however, any other
		// character from outside the alphabet is treated as an unrecoverable error.
		if (base32Str == null) {
			return null;
		}

		if (base32Str.length() == 0) {
			return Constants.EMPTY_STRING;
		}

		final char[] charArray = Root.toCharArray(base32Str);
		int charArrayIndex = -1, decodedIndex = 0, decodeValue;
		int charsToDecode = charArray.length;
		final byte[] decoded = new byte[(charsToDecode >>> 3) * 5 + 5];
		// TODO: Replace StringBuilder with faster Root implementation

		do {
			// Decoding the first character
			byte b = getByte(charArray, ++charArrayIndex, false);

			decodeValue = b << 3;
			charsToDecode--;

			if (charsToDecode == 0) {
				throw new CodecDecodeException(base32Str);
			}

			// Decoding the second character
			b = getByte(charArray, ++charArrayIndex, false);

			decodeValue |= (b & 0x1c) >>> 2;
			decoded[decodedIndex++] = (byte) decodeValue;
			decodeValue = (b & 0x03) << 6;
			charsToDecode--;

			if (charsToDecode == 0) {
				if (decodeValue != 0) {
					throw new CodecDecodeException(base32Str);
				}

				break;
			}

			// Decoding the third character
			b = getByte(charArray, ++charArrayIndex, true);

			if (charArray[charArrayIndex] == '=') {
				if (!isPaddingValid(charArray, charArrayIndex, 6) || decodeValue != 0) {
					throw new CodecDecodeException(base32Str);
				}

				break;
			}

			decodeValue |= b << 1;
			charsToDecode--;

			if (charsToDecode == 0) {
				throw new CodecDecodeException(base32Str);
			}

			// Decoding the fourth character
			b = getByte(charArray, ++charArrayIndex, false);

			decodeValue |= (b & 0x10) >>> 4;
			decoded[decodedIndex++] = (byte) decodeValue;
			decodeValue = (b & 0x0f) << 4;
			charsToDecode--;

			if (charsToDecode == 0) {
				if (decodeValue != 0) {
					throw new CodecDecodeException(base32Str);
				}

				break;
			}

			// Decoding the fifth character
			b = getByte(charArray, ++charArrayIndex, true);

			if (charArray[charArrayIndex] == '=') {
				if (!isPaddingValid(charArray, charArrayIndex, 4) || decodeValue != 0) {
					throw new CodecDecodeException(base32Str);
				}

				break;
			}

			decodeValue |= (b & 0x1e) >>> 1;
			decoded[decodedIndex++] = (byte) decodeValue;
			decodeValue = (b & 0x01) << 7;
			charsToDecode--;

			if (charsToDecode == 0) {
				if (decodeValue != 0) {
					throw new CodecDecodeException(base32Str);
				}

				break;
			}

			// Decoding the sixth character
			b = getByte(charArray, ++charArrayIndex, true);

			if (charArray[charArrayIndex] == '=') {
				if (!isPaddingValid(charArray, charArrayIndex, 3) || decodeValue != 0) {
					throw new CodecDecodeException(base32Str);
				}

				break;
			}

			decodeValue |= b << 2;
			charsToDecode--;

			if (charsToDecode == 0) {
				throw new CodecDecodeException(base32Str);
			}

			// Decoding the seventh character
			b = getByte(charArray, ++charArrayIndex, false);

			decodeValue |= (b & 0x18) >>> 3;
			decoded[decodedIndex++] = (byte) decodeValue;
			decodeValue = (b & 0x07) << 5;
			charsToDecode--;

			if (charsToDecode == 0) {
				if (decodeValue != 0) {
					throw new CodecDecodeException(base32Str);
				}

				break;
			}

			// Decoding the eighth character
			b = getByte(charArray, ++charArrayIndex, true);

			if (charArray[charArrayIndex] == '=') {
				if (!isPaddingValid(charArray, charArrayIndex, 1) || decodeValue != 0) {
					throw new CodecDecodeException(base32Str);
				}

				break;
			}

			decodeValue |= b;
			decoded[decodedIndex++] = (byte) decodeValue;
			charsToDecode--;

		} while (charsToDecode > 0);

		return new String(decoded, 0, decodedIndex, StandardCharsets.UTF_8);
	}

	/**
	 *
	 * @param byteArray
	 * @return
	 */
	public static final String encode(final byte[] byteArray) {
		if (byteArray == null) {
			return null;
		}

		if (byteArray.length == 0) {
			return Constants.EMPTY_STRING;
		}

		int byteArrayIndex = -1, encodingIndex;
		int bytesToEncode = byteArray.length;
		final StringBuilder encoded = new StringBuilder((bytesToEncode / 5 << 3) + 8);
		// TODO: Replace StringBuilder with faster Root implementation

		do {
			// Encoding the first byte
			byte b = byteArray[++byteArrayIndex];
			encoded.append(base32Encoding[(b & 0xF8) >>> 3]);

			// TODO: printf '%x\n' "$((2#11000000))" <-- Works from the bash command line to covert
			// binary to hex
			encodingIndex = (b & 0x07) << 2;
			bytesToEncode--;

			if (bytesToEncode == 0) {
				encoded.append(base32Encoding[encodingIndex]);
				break;
			}

			// Encoding the second byte
			b = byteArray[++byteArrayIndex];
			encoded.append(base32Encoding[encodingIndex | (b & 0xc0) >>> 6]);
			encoded.append(base32Encoding[(b & 0x3e) >>> 1]);

			encodingIndex = (b & 0x01) << 4;
			bytesToEncode--;

			if (bytesToEncode == 0) {
				encoded.append(base32Encoding[encodingIndex]);
				break;
			}

			// Encoding the third byte
			b = byteArray[++byteArrayIndex];
			encoded.append(base32Encoding[encodingIndex | (b & 0xf0) >>> 4]);

			encodingIndex = (b & 0x0f) << 1;
			bytesToEncode--;

			if (bytesToEncode == 0) {
				encoded.append(base32Encoding[encodingIndex]);
				break;
			}

			// Encoding the fourth byte
			b = byteArray[++byteArrayIndex];
			encoded.append(base32Encoding[encodingIndex | (b & 0x80) >>> 7]);
			encoded.append(base32Encoding[(b & 0x7c) >>> 2]);

			encodingIndex = (b & 0x03) << 3;
			bytesToEncode--;

			if (bytesToEncode == 0) {
				encoded.append(base32Encoding[encodingIndex]);
				break;
			}

			// Encoding the fifth byte
			b = byteArray[++byteArrayIndex];
			encoded.append(base32Encoding[encodingIndex | (b & 0xe0) >>> 5]);
			encoded.append(base32Encoding[b & 0x1f]);
			bytesToEncode--;

		} while (bytesToEncode > 0);

		return encoded.toString();
	}

	/**
	 *
	 * @param str
	 * @param charset
	 * @return
	 */
	public static final String encode(final String str, final Charset charset) {
		if (str == null) {
			return null;
		}

		if (str.length() == 0) {
			return Constants.EMPTY_STRING;
		}

		return encode(str.getBytes(charset));
	}

	/**
	 *
	 * @param charArray
	 * @param index
	 * @param isEqualsValid
	 * @return
	 */
	private static byte getByte(final char[] charArray, final int index, final boolean isEqualsValid) {
		final char c = charArray[index];

		if (c > 0x7f) {
			throw new CodecDecodeException(c, index);
		}

		final byte b = base32Decoding[c];

		if (b == 0 && c != 'A' && c != 'a' && (!isEqualsValid || c != '=')) {
			throw new CodecDecodeException(c, index);
		}

		return b;
	}

	/**
	 *
	 *
	 * @param charArray
	 * @param index
	 * @param len
	 * @return
	 */
	private static boolean isPaddingValid(final char[] charArray, int index, final int len) {
		final int end = index + len;

		if (charArray.length != end) {
			return false;
		}

		for (; index < end; index++) {
			if (charArray[index] != '=') {
				return false;
			}
		}

		return true;
	}

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	private Base32() {
	}

} // End Base32
