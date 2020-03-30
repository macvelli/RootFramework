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

import root.util.Root;

/**
 * This email address verifier basically follows the conventions for a valid Gmail address.
 * <p>
 * <a href="https://en.wikipedia.org/wiki/Email_address">Email Address</a>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class EmailAddressVerifier {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	/**
	 * Valid characters for the local-part of an email address include:
	 * <ul>
	 * <li>+ - . _</li>
	 * <li>0 thru 9</li>
	 * <li>A thru Z and a thru z</li>
	 * </ul>
	 * <p>
	 * <b>NOTE:</b> Two periods cannot come next to each other
	 */
	private static final boolean[] validLocalPartChar = {
			// ASCII 0 thru 15
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
			// ASCII 16 thru 31
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,
			// ASCII 32 thru 47
			false, false, false, false, false, false, false, false, false, false, false, true, false, true, true, false,
			// ASCII 48 thru 63
			true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false,
			// ASCII 64 thru 79
			false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			// ASCII 80 thru 95
			true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, true,
			// ASCII 96 thru 111
			false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			// ASCII 112 thru 127
			true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false };

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public static final VerificationError verify(final String emailAddress, final String fieldName) {
		// Minimum length of six characters
		if (emailAddress.length() < 6) {
			return new MinimumLengthError(emailAddress, fieldName, 6);
		}

		// Maximum length of 254 characters
		if (emailAddress.length() > 254) {
			return new MaximumLengthError(emailAddress, fieldName, 254);
		}

		final char[][] emailComponents = Root.split(emailAddress, '@');

		// Must have two components to the email address
		if (emailComponents.length != 2) {
			return new FormatError(emailAddress, fieldName, "Must have one @ character");
		}

		final char[] localPart = emailComponents[0];

		// Verify the local-part is formatted properly
		if (localPart.length == 0) {
			return new FormatError(emailAddress, fieldName, "Missing local part of email address");
		}

		if (localPart.length > 64) {
			return new FormatError(emailAddress, fieldName, "Local part length is greater than 64 characters");
		}

		boolean wasLastCharAPeriod = false;
		for (final char ch : localPart) {
			if (ch < 128 && !validLocalPartChar[ch]) {
				return new FormatError(emailAddress, fieldName, "Invalid character detected");
			}

			if (wasLastCharAPeriod && ch == '.') {
				return new FormatError(emailAddress, fieldName, "Cannot have .. together");
			}

			wasLastCharAPeriod = ch == '.';
		}

		// Verify the domain part is formatted properly
		return DomainVerifier.verify(Root.newInstance(emailComponents[1]), fieldName);
	}

} // End EmailAddressVerifier
