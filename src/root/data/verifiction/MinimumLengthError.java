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

import root.lang.FastInteger;
import root.lang.FastString;
import root.lang.ParamString;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class MinimumLengthError extends VerificationError {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final ParamString errorMessage = new ParamString("{P} minimum length is {P} characters");

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final String value;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public MinimumLengthError(final String value, final String fieldName, final int minLength) {
		super(errorMessage.format(new FastString(fieldName), FastInteger.valueOf(minLength)));

		this.value = value;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final String getValue() {
		return this.value;
	}

} // End MinimumLengthError
