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

import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 * https://www.smashingmagazine.com/2012/06/form-field-validation-errors-only-approach/
 *
 * The thing to keep in mind about data validation is that it is best to do it at the source of data input. For Web applications, this means to do the
 * validation in-browser before sending the data to the server. For mobile applications, it means doing the validation in-app before sending it to the
 * server. Of course, client-side validations can be circumvented so it is best to <b>ALWAYS</b> perform validation on the server-side as well just to
 * make sure no jokers get past the client-side validations and screw stuff up in your database.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public abstract class VerificationError implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	protected final String errorMessage;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	protected VerificationError(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append(this.errorMessage);
	}

	public abstract Object getValue();

	@Override
	public final String toString() {
		return this.errorMessage;
	}

} // End VerificationError
