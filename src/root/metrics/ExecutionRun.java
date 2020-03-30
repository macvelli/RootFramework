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
package root.metrics;

import root.lang.Constants;
import root.lang.Extractable;
import root.lang.StringExtractor;
import root.time.Duration;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ExecutionRun implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	final String benchmark;
	final long start;

	long duration;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	ExecutionRun(final String benchmark) {
		this.benchmark = benchmark;
		this.start = System.nanoTime();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append(this.benchmark).appendArray(Constants.colonSpace);

		Duration.formatNanos(this.duration, extractor);
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor();
		this.extract(extractor);
		return extractor.toString();
	}

	// <><><><><><><><><><><><><>< Package Methods ><><><><><><><><><><><><><>

	final void stop() {
		this.duration = System.nanoTime() - this.start;
	}

} // End ExecutionRun
