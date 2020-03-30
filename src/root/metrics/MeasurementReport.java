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
public final class MeasurementReport implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private long lowestTotal;
	private final MapHashed<String, Measurement> measurementMap;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public MeasurementReport(final MapHashed<String, Measurement> measurementMap) {
		this.lowestTotal = Long.MAX_VALUE;
		this.measurementMap = measurementMap;

		Measurement measure;
		for (final MapEntry<String, Measurement> mapEntry : measurementMap) {
			measure = mapEntry.getValue();

			if (measure.statistics.sum() < this.lowestTotal) {
				this.lowestTotal = measure.statistics.sum();
			}
		}
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		Measurement measure;
		int i = 0;

		extractor.append("\nBenchmarks:\n");

		for (final MapEntry<String, Measurement> mapEntry : this.measurementMap) {
			measure = mapEntry.getValue();

			if (i++ > 0) {
				extractor.append('\n');
			}

			measure.extract(extractor);

			if (measure.statistics.sum() > this.lowestTotal) {
				final Percent percentDiff = new Percent(measure.statistics.sum() - this.lowestTotal, this.lowestTotal);

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

} // End MeasurementReport
