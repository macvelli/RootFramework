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

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class RequiredVerifier {

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public static final VerificationError verify(final Object obj, final String fieldName) {
		// Error out if object is required but missing
		if (obj == null) {
			return new RequiredError(obj, fieldName);
		}

		return null;
	}

	public static final VerificationError verify(final String string, final String fieldName) {
		// Error out if string is required but missing
		if (string == null || string.length() == 0) {
			return new RequiredError(string, fieldName);
		}

		return null;
	}

} // End RequiredVerifier
