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
package root.adt;

/**
 * This exception is thrown when calling code attempts to add an element to a full data structure.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class DataStructureFullException extends RuntimeException {

	// <><><><><><><><><><><><><><><> Constants ><><><><><><><><><><><><><><><>

	private static final long serialVersionUID = 252630709529084060L;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public DataStructureFullException() {
		super("Attempt to add element to a full data structure failed");
	}

} // End DataStructureFullException
