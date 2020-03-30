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
package root.pool;

import root.lang.Extractable;
import root.lang.ParamString;

/**
 * A {@link RuntimeException} that is thrown when a pool has been closed and is no longer servicing object acquisition requests.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class ClosedForBusinessException extends RuntimeException {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final long serialVersionUID = -674302199541120459L;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public ClosedForBusinessException(final Extractable[] debugParams) {
		super(ParamString.formatMsg("The {P} object pool has been closed", debugParams));
	}

} // End ClosedForBusinessException
