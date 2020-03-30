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

import root.lang.ParamString;

/**
 * A {@link RuntimeException} that is thrown when more elements are attempted to be added to a
 * fixed-size Root Data Structure and there is not enough capacity to accommodate the elements.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class InsufficientCapacityException extends RuntimeException {

	// <><><><><><><><><><><><><><><> Constants ><><><><><><><><><><><><><><><>

	private static final long serialVersionUID = 7442619868863922768L;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public InsufficientCapacityException(final int offered, final int capacity) {
		super(ParamString.formatMsg("Attempt to add {P} elements with available capacity of {P} failed", offered,
				capacity));
	}

} // End InsufficientCapacityException
