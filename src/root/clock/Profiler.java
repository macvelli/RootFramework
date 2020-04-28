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
package root.clock;

import java.math.BigDecimal;
import java.math.RoundingMode;

import root.annotation.Todo;
import root.lang.Extractable;
import root.lang.StringExtractor;
import root.math.Statistics;
import root.time.Duration;
import root.time.Timestamp;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Todo("Ratios! How to implement, and what do you compare against?")
public final class Profiler implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	boolean failed;
	boolean running;

	long begin;
	long end;

	long start;
	long stop;

	private final String label;
	private final Statistics time;
	private final Statistics results;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Profiler(final String label) {
		this.label = label;
		this.time = new Statistics();
		this.results = new Statistics();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void accrue() {
		this.time.add(this.stop - this.start);
		this.updateMetrics(this.start, this.stop);
	}

	public final void accrue(final long start, final long stop) {
		this.time.add(stop - start);
		this.updateMetrics(start, stop);
	}

	public final void accrue(final long start, final long stop, final int numResults) {
		this.time.add(stop - start);
		this.updateMetrics(start, stop);
		this.results.add(numResults);
	}

	public final long elapsed() {
		return end - begin;
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append("\nProfiler: ").append(this.label).append(' ').append(this.failed ? "failed" : "succeeded");

		extractor.append("\nExecution Started: ");
		Timestamp.format(this.begin, extractor);

		extractor.append("\nExecution Completed: ");
		Timestamp.format(this.end, extractor);

		extractor.append("\nTotal Execution Time: ");
		Duration.format(this.time.sum(), extractor);
		extractor.append(' ');
		this.time.extract(extractor);

		if (this.results.sum() > 0) {
			extractor.append("\nNumber of records processed: ").append(this.results.sum());
		}
	}

	public final long getBegin() {
		return this.begin;
	}

	public final long getEnd() {
		return this.end;
	}

	public final Statistics getResults() {
		return this.results;
	}

	public final String getThroughput() {
		final double throughput = this.results.sum() / (this.time.sum() / 1000.0d);

		return new BigDecimal(throughput).setScale(2, RoundingMode.HALF_DOWN).toString() + "/second";
	}

	public final Statistics getTimeStats() {
		return this.time;
	}

	public final boolean isRunning() {
		return this.running;
	}

	public final void start() {
		if (!this.running) {
			this.running = true;
			this.start = System.currentTimeMillis();
		}
	}

	public final void stop() {
		if (this.running) {
			this.stop = System.currentTimeMillis();
			this.running = false;

			this.accrue();
		}
	}

	@Override
	public String toString() {
		final StringExtractor extractor = new StringExtractor(320);
		this.extract(extractor);
		return extractor.toString();
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private void updateMetrics(final long start, final long stop) {
		if (this.start == 0) {
			this.start = start;
		}

		if (this.stop < stop) {
			this.stop = stop;
		}

		if (this.begin == 0) {
			this.begin = start;
		}

		if (this.end < stop) {
			this.end = stop;
		}
	}

} // End Profiler
