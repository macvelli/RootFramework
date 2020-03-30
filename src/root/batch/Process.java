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
package root.batch;

import root.adt.ListArray;
import root.clock.Stopwatch_old;
import root.log.Log;
import root.thread.ThreadPool;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The type of {@link root.batch.Task} associated with this {@link Process}
 */
public class Process<T extends Task<?>> {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Log log = new Log(Process.class);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private boolean hasErrors;

	final Stopwatch_old stopwatch;
	final ListArray<T> taskList;

	private final String processName;
	private final ThreadPool threadPool;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Process(final String processName, final ThreadPool threadPool) {
		this.processName = processName;
		this.threadPool = threadPool;
		this.stopwatch = new Stopwatch_old(processName);
		this.taskList = new ListArray<T>();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void execute() {
		Process.log.info("Executing {P} tasks against a thread pool with capacity of {P}", this.taskList.size(), this.threadPool.getCapacity());

		this.stopwatch.start();

		// Execute all Tasks using the ThreadPool
		this.threadPool.execute(this.taskList);
	}

	public final String getName() {
		return this.processName;
	}

	public final Stopwatch_old getStopwatch() {
		return this.stopwatch;
	}

	public final ListArray<T> getTasks() {
		return this.taskList;
	}

	public final boolean hasErrors() {
		return this.hasErrors;
	}

	public final void join() {
		for (final T task : this.taskList) {
			try {
				task.join();

				if (task.hasErrors()) {
					this.hasErrors = true;
				}
			} catch (final InterruptedException e) {
			}
		}

		this.stopwatch.stop();
		this.logResults();
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private void logResults() {
		log.info(this.stopwatch.toString());
	}

} // End Process
