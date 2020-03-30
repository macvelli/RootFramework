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

import root.adt.ListArray;

/**
 * Provides a means to accumulate multiple thrown {@link Exception}s into a single class which you can then turn around and throw once your processing
 * is complete.
 * <p>
 * Originally, this class was to help with accumulating validation errors. <b><span style="color: red;">However, exception processing is
 * <i>extremely</i> slow</span></b> so I would <b><i>not</i></b> recommend using exception processing to identify validation errors in your
 * application.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class BatchException extends Exception implements Iterable<Throwable> {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final long serialVersionUID = 4011098842854477382L;

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private int messageLength;
	private final ListArray<Throwable> exceptions;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public BatchException() {
		this.exceptions = new ListArray<Throwable>();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void add(final Throwable t) {
		this.exceptions.add(t);
		this.messageLength += t.getMessage().length();
	}

	public final void checkThrow() throws BatchException {
		if (this.exceptions.getSize() > 0) {
			throw this;
		}
	}

	@Override
	public final String getMessage() {
		final StringExtractor extractor = new StringExtractor(this.messageLength + this.exceptions.size());

		for (final Throwable t : this.exceptions) {
			if (extractor.length() > 0) {
				extractor.append('\n');
			}

			extractor.append(t.getMessage());
		}

		return extractor.toString();
	}

	@Override
	public final Itemizer<Throwable> iterator() {
		return this.exceptions.iterator();
	}

} // End BatchException
