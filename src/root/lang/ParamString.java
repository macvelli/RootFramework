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
package root.lang;

import java.text.MessageFormat;

import root.adt.CollectorCharArray;
import root.util.Root;

/**
 * {@link ParamString} leverages the simple <code>{P}</code> mnemonic to denote where a parameter is to be populated within a parameterized
 * {@link String}. Why not use <code>{0}</code> and <code>{1}</code> etc, like {@link MessageFormat} does? Because {@link ParamString} leverages the
 * <b>order</b> in which the parameters are passed to determine which values populate which parameters.
 * <p>
 * There are two ways in which {@link ParamString} can be utilized in your code:
 * <ol>
 * <li>As as static method call: {@link #formatMsg(String, Object [])} and {@link #formatMsg(String, Extractable [])}</li>
 * <li>As a <b>reusable compiled</b> instance: {@link #format(Object [])}, {@link #format(Extractable [])},
 * {@link #format(Extractor, StringExtractor)}, and {@link #format(Extractor, StringExtractor)}</li>
 * </ol>
 * <p>
 *
 * Here is an example on how to use ParamString in the wild:
 *
 * <pre>
 * <code>String s = "The {P} brown fox {P} over the {P} dog"
 * ParamString.formatMsg(s, "quick", "jumped", "lazy");
 *
 * ParamString paramStr = new ParamString(s);
 * paramStr.format("quick", "jumped", "lazy");</code>
 * </pre>
 *
 * Obviously, using the precompiled version will be faster at the expense of some memory allocation.
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ParamString {

	// <><><><><><><><><><><><><><> Static Methods <><><><><><><><><><><><><><>

	public static final String formatMsg(final String message, final Extractable... extArray) {
		if (extArray.length == 0) {
			return message;
		} else {
			final StringExtractor extractor = new StringExtractor(message.length() + extArray.length << 5);
			final char[] msgCharArray = Root.toCharArray(message);

			// Build the formatted message
			int i = 0, j = 0, srcPos = 0, segmentLength;
			while (true) {
				if (msgCharArray[i++] == '{' && msgCharArray[i] == 'P' && msgCharArray[++i] == '}') {
					segmentLength = i - srcPos - 2;

					if (segmentLength > 0) {
						extractor.append(msgCharArray, srcPos, segmentLength);
					}

					extractor.append(extArray[j++]);
					srcPos = ++i;

					if (j == extArray.length) {
						if (srcPos < msgCharArray.length) {
							extractor.append(msgCharArray, srcPos, msgCharArray.length - srcPos);
						}

						return extractor.toString();
					}
				}
			}
		}
	}

	public static final String formatMsg(final String message, final Object... objArray) {
		if (objArray.length == 0) {
			return message;
		} else {
			// Convert all of the Objects into Strings and calculate the total String length
			final char[] msgCharArray = Root.toCharArray(message);
			final String[] strArray = new String[objArray.length];
			int totalStrLen = 0;

			for (int i = 0; i < objArray.length; i++) {
				strArray[i] = Root.toString(objArray[i]);
				totalStrLen += strArray[i].length();
			}

			final char[] charArray = new char[msgCharArray.length + totalStrLen - 3 * objArray.length];
			String str;

			// Build the formatted message
			int i = 0, j = 0, srcPos = 0, destPos = 0, segmentLength;
			while (true) {
				if (msgCharArray[i++] == '{' && msgCharArray[i] == 'P' && msgCharArray[++i] == '}') {
					segmentLength = i - srcPos - 2;

					if (segmentLength > 0) {
						System.arraycopy(msgCharArray, srcPos, charArray, destPos, segmentLength);
						destPos += segmentLength;
					}

					str = strArray[j++];
					str.getChars(0, str.length(), charArray, destPos);
					destPos += str.length();
					srcPos = ++i;

					if (j == strArray.length) {
						if (srcPos < msgCharArray.length) {
							System.arraycopy(msgCharArray, srcPos, charArray, destPos, msgCharArray.length - srcPos);
						}

						return new String(charArray);
					}
				}
			}
		}
	}

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final int totalSize;
	private final char[][] msgSegments;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public ParamString(final String message) {
		final char[] msgCharArray = Root.toCharArray(message);
		final CollectorCharArray segments = new CollectorCharArray();

		char[] msgSegment;
		int i = 0, srcPos = 0, size = 0, segmentLength;
		final int end = msgCharArray.length - 2;
		while (i < end) {
			if (msgCharArray[i++] == '{' && msgCharArray[i] == 'P' && msgCharArray[++i] == '}') {
				segmentLength = i - srcPos - 2;

				if (segmentLength == 0) {
					segments.add(Constants.emptyCharArray);
				} else {
					msgSegment = new char[segmentLength];
					System.arraycopy(msgCharArray, srcPos, msgSegment, 0, segmentLength);
					segments.add(msgSegment);
					size += segmentLength;
				}

				srcPos = ++i;
			}
		}

		segmentLength = msgCharArray.length - srcPos;

		if (segmentLength == 0) {
			segments.add(Constants.emptyCharArray);
		} else {
			msgSegment = new char[segmentLength];
			System.arraycopy(msgCharArray, srcPos, msgSegment, 0, segmentLength);
			segments.add(msgSegment);
		}

		this.totalSize = size + segmentLength;
		this.msgSegments = segments.toArray();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final String format(final Extractable... extArray) {
		final StringExtractor extractor = new StringExtractor(this.totalSize + extArray.length << 5);
		char[] msgSegment;
		int i = 0;

		while (true) {
			msgSegment = this.msgSegments[i];

			if (msgSegment.length > 0) {
				extractor.append(msgSegment, 0, msgSegment.length);
			}

			if (i == extArray.length) {
				return extractor.toString();
			}

			extractor.append(extArray[i++]);
		}
	}

	public final String format(final Object... objArray) {
		final String[] strArray = new String[objArray.length];
		int totalStrLen = 0;

		// Convert all of the Objects into Strings and calculate the total String length
		for (int i = 0; i < objArray.length; i++) {
			strArray[i] = Root.toString(objArray[i]);
			totalStrLen += strArray[i].length();
		}

		final char[] charArray = new char[this.totalSize + totalStrLen];
		String str;
		char[] msgSegment;
		int i = 0, destPos = 0;

		while (true) {
			msgSegment = this.msgSegments[i];

			if (msgSegment.length > 0) {
				System.arraycopy(msgSegment, 0, charArray, destPos, msgSegment.length);
				destPos += msgSegment.length;
			}

			if (i == objArray.length) {
				return new String(charArray);
			}

			str = strArray[i++];
			str.getChars(0, str.length(), charArray, destPos);
			destPos += str.length();
		}
	}

	public final void format(final StringExtractor extractor, final Extractable... extArray) {
		extractor.ensureCapacity(extractor.length() + this.totalSize + extArray.length << 5);
		char[] msgSegment;
		int i = 0;

		while (true) {
			msgSegment = this.msgSegments[i];

			if (msgSegment.length > 0) {
				extractor.append(msgSegment, 0, msgSegment.length);
			}

			if (i == extArray.length) {
				break;
			}

			extractor.append(extArray[i++]);
		}
	}

	public final void format(final StringExtractor extractor, final Object... objArray) {
		final String[] strArray = new String[objArray.length];
		int totalStrLen = 0;

		// Convert all of the Objects into Strings and calculate the total String length
		for (int i = 0; i < objArray.length; i++) {
			strArray[i] = Root.toString(objArray[i]);
			totalStrLen += strArray[i].length();
		}

		extractor.ensureCapacity(extractor.length() + this.totalSize + totalStrLen);
		char[] msgSegment;
		int i = 0;

		while (true) {
			msgSegment = this.msgSegments[i];

			if (msgSegment.length > 0) {
				extractor.append(msgSegment, 0, msgSegment.length);
			}

			if (i == strArray.length) {
				break;
			}

			extractor.append(strArray[i++]);
		}
	}

} // End ParamString
