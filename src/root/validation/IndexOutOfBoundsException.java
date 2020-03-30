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
package root.validation;

import root.lang.ParamString;

/**
 * A {@link RuntimeException} that is thrown when an index parameter passed into the function of a Root Data Structure is out of bounds.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class IndexOutOfBoundsException extends RuntimeException {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final long serialVersionUID = 8930543884997274522L;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public IndexOutOfBoundsException(final int index, final int size) {
		super(ParamString.formatMsg("Index: {P}, Size: {P}", index, size));
	}

} // End IndexOutOfBoundsException
