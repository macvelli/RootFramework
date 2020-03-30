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
package root.data.verifiction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import root.adt.SetCharArray;
import root.util.Root;

/**
 * This domain verifier validates a domain based upon its list of dot-separated DNS labels, each label being limited to a length of 63 characters and
 * contains characters that include: *
 * <ul>
 * <li>-</li>
 * <li>0 thru 9</li>
 * <li>A thru Z and a thru z</li>
 * </ul>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class DomainVerifier {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	/**
	 * Valid characters for a domain include:
	 * <ul>
	 * <li>-</li>
	 * <li>0 thru 9</li>
	 * <li>A thru Z and a thru z</li>
	 * </ul>
	 * <p>
	 * <b>NOTE:</b> Two periods cannot come next to each other
	 */
	private static final boolean[] validDomainChar = {
			// ASCII 0 thru 15
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
			// ASCII 16 thru 31
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
			// ASCII 32 thru 47
			false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false,
			// ASCII 48 thru 63
			true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false,
			// ASCII 64 thru 79
			false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			// ASCI 80 thru 95
			true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false,
			// ASCII 96 thru 111
			false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			// ASCII 112 thru 127
			true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false };

	private static final SetCharArray validTLDSet = new SetCharArray();

	static {
		final InputStream is = Root.getResourceAsStream(DomainVerifier.class, "tld.properties");

		try (final BufferedReader tld = Root.getBufferedReader(is)) {
			for (String line = tld.readLine(); line != null; line = tld.readLine()) {
				if (line.length() > 0 && line.charAt(0) != '#') {
					validTLDSet.add(line.toCharArray());
				}
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public static final VerificationError verify(final String domain, final String fieldName) {
		final int domainLength = domain.length();

		// Minimum length of two characters
		if (domain.length() < 2) {
			return new MinimumLengthError(domain, fieldName, 2);
		}

		// Maximum length of 253 characters
		if (domainLength > 253) {
			return new MaximumLengthError(domain, fieldName, 253);
		}

		// Domain cannot begin or end with a -
		if (domain.charAt(0) == '-' || domain.charAt(domainLength - 1) == '-') {
			return new FormatError(domain, fieldName, "Domain cannot begin or end with a dash");
		}

		final char[][] labels = Root.split(domain, '.');

		// Must have at least two labels to the domain
		if (labels.length < 2) {
			return new FormatError(domain, fieldName, "Domain must have at least two labels");
		}

		// Verify each label in the domain
		for (final char[] label : labels) {
			// Each label cannot be greater than 63 characters
			if (label.length > 63) {
				return new FormatError(domain, fieldName, "Domain label length is greater than 63 characters");
			}

			for (final char ch : label) {
				if (ch < 128 && !validDomainChar[ch]) {
					return new FormatError(domain, fieldName, "Invalid character detected");
				}
			}
		}

		return null;
	}

} // End DomainVerifier
