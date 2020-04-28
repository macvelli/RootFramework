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

import root.adt.ListArray;
import root.adt.MapHashed;
import root.lang.Extractable;
import root.lang.StringExtractor;
import root.validation.InvalidParameterException;

/**
 *
 * How to Use the Stopwatch - The Clock app has a simple but useful stopwatch you can use to time activities. To access it, tap the stopwatch icon at
 * the top of the screen. The stopwatch does not need any set up before use, so tap the Start button to start it. - The stopwatch allows you to record
 * lap times, which is basically stopping the stopwatch as specific points, recording each time you stop the stopwatch. Tap the lap button each time
 * you want to record a lap time, for example, when someone your timing completes a lap around a track. - Each lap time is recorded either next to the
 * running time (landscape mode) or below it (portrait mode). While the stopwatch is running, the Start button is the Pause button, which you can use
 * to temporarily stop the stopwatch. - To reset the stopwatch to zero, tap the circular arrow icon. While the stopwatch is paused, you can tap the
 * Share button on the bottom-right to share the time and lap times with someone, upload to a cloud service, or one of many sharing options.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Stopwatch_old implements Extractable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final Profiler total;
	private final ListArray<Profiler> profilerList;
	private final MapHashed<String, Profiler> profilerMap;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Stopwatch_old(final String totalProfilerLabel) {
		this.total = new Profiler(totalProfilerLabel);
		this.profilerList = new ListArray<>();
		this.profilerMap = new MapHashed<>();

		this.profilerList.add(this.total);
		this.profilerMap.put(totalProfilerLabel, this.total);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void addNumProcessed(final String label, final int processed) {
		final Profiler profiler = this.profilerMap.get(label);

		if (profiler == null) {
			throw new InvalidParameterException("addNumProcessed", String.class, "label", "No such profiler by label: {P}", label);
		}

		profiler.getResultStats().add(processed);
		this.total.getResultStats().add(processed);
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		if (this.profilerList.size() == 1) {
			this.profilerList.get(0).extract(extractor);
		} else {
			this.total.extract(extractor);

			extractor.append("\n\n*******************************************************************************");

			for (final Profiler p : this.profilerList) {
				extractor.append('\n');
				p.extract(extractor);
			}

			extractor.append("\n\n*******************************************************************************");
		}
	}

	public final void failed(final String label) {
		final Profiler profiler = this.profilerMap.get(label);

		if (profiler == null) {
			throw new InvalidParameterException("failed", String.class, "label", "No such profiler by label: {P}", label);
		}

		profiler.failed = true;
		this.total.failed = true;
	}

	public final String freeMemory(final String taskName) {
		final StringExtractor extractor = new StringExtractor();

		extractor.append(taskName).append(':').append(' ');
		extractor.append(Runtime.getRuntime().freeMemory() >> 10).append('K');

		return extractor.toString();
	}

	public final int getNumProcessed() {
		return (int) this.total.getResultStats().sum();
	}

	public final Profiler getTotal() {
		return this.total;
	}

	public final boolean hasFailed() {
		return this.total.failed;
	}

	public final void mark(final String label) {
		final Profiler profiler = this.profilerMap.get(label);

		if (profiler == null) {
			throw new InvalidParameterException("mark", String.class, "label", "No such profiler by label: {P}", label);
		}

		final long currentTimeMillis = System.currentTimeMillis();
		profiler.stop = currentTimeMillis;
		this.total.accrue(profiler.start, profiler.stop);

		profiler.accrue();
		profiler.start = currentTimeMillis;
	}

	public final void start() {
		if (!this.total.running) {
			this.total.running = true;
			this.total.start = System.currentTimeMillis();
		}
	}

	public final void start(final String label) {
		Profiler profiler = this.profilerMap.get(label);

		if (profiler == null) {
			profiler = new Profiler(label);
			this.profilerList.add(profiler);
			this.profilerMap.put(label, profiler);
		}

		final long currentTimeMillis = System.currentTimeMillis();

		if (!profiler.running) {
			profiler.running = true;
			profiler.start = currentTimeMillis;
		}

		if (!this.total.running) {
			this.total.running = true;
			this.total.start = currentTimeMillis;
		}
	}

	public final void stop() {
		final Profiler profiler = this.profilerList.last();

		if (profiler.running) {
			profiler.stop = System.currentTimeMillis();
			profiler.running = false;

			this.total.accrue(profiler.start, profiler.stop);
			profiler.accrue();
		}

		if (this.total.running) {
			this.total.running = false;
		}
	}

	public final void stop(final String label) {
		final Profiler profiler = this.profilerMap.get(label);

		if (profiler == null) {
			throw new InvalidParameterException("stop", String.class, "label", "No such profiler by label: {P}", label);
		}

		if (profiler.running) {
			profiler.stop = System.currentTimeMillis();
			profiler.running = false;

			this.total.accrue(profiler.start, profiler.stop);
			profiler.accrue();
		}
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(192 + (this.profilerList.size() + 1) * 320);
		this.extract(extractor);
		return extractor.toString();
	}

} // End Stopwatch
