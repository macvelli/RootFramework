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

import root.adt.MapEntry;
import root.adt.MapHashed;
import root.finance.Percent;
import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class ExecutionRunReport implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private long lowestRun;
	private final MapHashed<String, Measurement> measurementMap;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public ExecutionRunReport(final MapHashed<String, Measurement> measurementMap) {
		this.lowestRun = Long.MAX_VALUE;
		this.measurementMap = measurementMap;

		ExecutionRun executionRun;
		for (final MapEntry<String, Measurement> mapEntry : measurementMap) {
			executionRun = mapEntry.getValue().currentRun;

			if (executionRun.duration < this.lowestRun) {
				this.lowestRun = executionRun.duration;
			}
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		ExecutionRun executionRun;
		int i = 0;

		for (final MapEntry<String, Measurement> mapEntry : this.measurementMap) {
			executionRun = mapEntry.getValue().currentRun;

			if (i++ > 0) {
				extractor.append('\n');
			}

			executionRun.extract(extractor);

			if (executionRun.duration > this.lowestRun) {
				final Percent percentDiff = new Percent(executionRun.duration - this.lowestRun, this.lowestRun);

				extractor.append(" [+");
				percentDiff.extract(extractor);
				extractor.append(']');
			}
		}
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor();
		this.extract(extractor);
		return extractor.toString();
	}

} // End ExecutionRunReport
