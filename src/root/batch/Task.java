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

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <R>
 *            the type of result this {@link Task} will produce
 */
public abstract class Task<R> implements Runnable {

	// <><><><><><><><><><><><><><><>< Constants <><><><><><><><><><><><><><><>

	private static final Log log = new Log(Task.class);

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private R result;

	private final String taskName;
	private final Stopwatch_old stopwatch;
	private final ListArray<TaskError> errorList;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	protected Task(final String taskName, final Process<Task<R>> process) {
		this.taskName = taskName;
		this.stopwatch = process.stopwatch;
		this.errorList = new ListArray<TaskError>();

		process.taskList.add(this);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final ListArray<TaskError> getErrors() {
		return this.errorList;
	}

	public final String getName() {
		return this.taskName;
	}

	public final R getResult() {
		return this.result;
	}

	public final Stopwatch_old getStopwatch() {
		return this.stopwatch;
	}

	public final boolean hasErrors() {
		return this.errorList.size() > 0;
	}

	public final void join() throws InterruptedException {
		Thread.currentThread().join();
	}

	@Override
	public final void run() {
		this.stopwatch.start(this.taskName);

		try {
			this.result = this.execute();
		} catch (final Throwable t) {
			this.stopwatch.failed(this.taskName);
			this.addError(new TaskError(this, t));
			log.error("An error has occurred while executing Task {P}", t, this.taskName);
		}

		this.stopwatch.stop(this.taskName);
	}

	// <><><><><><><><><><><><><>< Protected Methods <><><><><><><><><><><><><>

	protected final void addError(final TaskError taskError) {
		this.errorList.add(taskError);
	}

	protected abstract R execute() throws Throwable;

} // End Task
