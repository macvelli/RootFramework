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

import root.adt.MapHashed;
import root.adt.StackArray;
import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 *
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Stopwatch implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final MapHashed<String, Measurement> measurementMap;
	private final StackArray<Measurement> measurementStack;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Stopwatch() {
		this.measurementMap = new MapHashed<>();
		this.measurementStack = new StackArray<>();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		final MeasurementReport measurementReport = new MeasurementReport(this.measurementMap);

		measurementReport.extract(extractor);
	}

	public final ExecutionRun getExecutionRun(final String benchmark) {
		final Measurement measure = this.measurementMap.get(benchmark);

		return measure.currentRun;
	}

	public final ExecutionRunReport getExecutionRunReport() {
		return new ExecutionRunReport(this.measurementMap);
	}

	public final Measurement getMeasurement(final String benchmark) {
		return this.measurementMap.get(benchmark);
	}

	public final void start(final String benchmark) {
		Measurement measure = this.measurementMap.get(benchmark);

		if (measure == null) {
			measure = new Measurement(benchmark);
			this.measurementMap.put(benchmark, measure);
		}

		this.measurementStack.push(measure);
		measure.start();
	}

	public final void stop() {
		final Measurement measure = this.measurementStack.pop();

		measure.stop();
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(96 * this.measurementMap.getSize());
		this.extract(extractor);
		return extractor.toString();
	}

} // End Stopwatch
