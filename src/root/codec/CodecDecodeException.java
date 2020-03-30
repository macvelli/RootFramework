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

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class CodecDecodeException extends RuntimeException {

	// <><><><><><><><><><><><><><><> Constants ><><><><><><><><><><><><><><><>

	private static final long serialVersionUID = 1877208314469404691L;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public CodecDecodeException(final char c, final int pos) {
		// TODO: Replace StringBuilder with faster Root implementation
		super(new StringBuilder(60).append("Attempt to decode character ")
			.append(c)
			.append(" at position ")
			.append(pos)
			.append(" failed")
			.toString());
	}

	public CodecDecodeException(final String s) {
		// TODO: Replace StringBuilder with faster Root implementation
		super(new StringBuilder(25 + s.length()).append("Invalid codec input: ")
			.append(s)
			.toString());
	}

} // End CodecDecodeException
