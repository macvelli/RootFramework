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

import root.lang.ParamString;
import root.util.Root;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class TaskError {

	// <><><><><><><><><><><><><><><> Constants ><><><><><><><><><><><><><><><>

	private static final ParamString paramToString = new ParamString("Task: {P}\nError: {P}");

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final String			 taskName;
	private final String			 errorMessage;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public TaskError(final Task<?> task, final String errorMessage) {
		this.taskName = task.getName();
		this.errorMessage = errorMessage;
	}

	public TaskError(final Task<?> task, final Throwable t) {
		this.taskName = task.getName();
		this.errorMessage = Root.getStackTraceAsString(t);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final String getErrorMessage() {
		return this.errorMessage;
	}

	@Override
	public final String toString() {
		return paramToString.format(this.taskName, this.errorMessage);
	}

} // End TaskError
