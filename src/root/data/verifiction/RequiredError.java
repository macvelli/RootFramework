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

import root.lang.FastString;
import root.lang.ParamString;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class RequiredError extends VerificationError {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final ParamString errorMessage = new ParamString("{P} is required");

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final Object value;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public RequiredError(final Object value, final String fieldName) {
		super(errorMessage.format(new FastString(fieldName)));

		this.value = value;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final Object getValue() {
		return this.value;
	}

} // End RequiredError
