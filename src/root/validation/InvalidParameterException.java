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

import root.annotation.Todo;
import root.lang.Extractable;
import root.lang.ParamString;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public class InvalidParameterException extends RuntimeException {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final long serialVersionUID = -3576232050732201866L;

	private static final ParamString message = new ParamString("{P}({P} {P}): {P}");

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	/**
	 *
	 * @param methodName
	 * @param paramType
	 * @param paramName
	 * @param msg
	 */
	public InvalidParameterException(final String methodName, final Class<?> paramType, final String paramName, final String msg) {
		super(message.format(methodName, paramType.getName(), paramName, msg));
	}

	/**
	 *
	 * @param methodName
	 * @param paramType
	 * @param paramName
	 * @param msg
	 * @param extArray
	 */
	@Todo("Interesting there are two calls to ParamString.format(), wonder if there is a way to consolidate those into one call?")
	public InvalidParameterException(final String methodName, final Class<?> paramType, final String paramName, final String msg,
			final Extractable... extArray) {
		super(message.format(methodName, paramType.getName(), paramName, ParamString.formatMsg(msg, extArray)));
	}

	/**
	 *
	 * @param methodName
	 * @param paramType
	 * @param paramName
	 * @param msg
	 * @param objArray
	 */
	@Todo("Interesting there are two calls to ParamString.format(), wonder if there is a way to consolidate those into one call?")
	public InvalidParameterException(final String methodName, final Class<?> paramType, final String paramName, final String msg,
			final Object... objArray) {
		super(message.format(methodName, paramType.getName(), paramName, ParamString.formatMsg(msg, objArray)));
	}

} // End InvalidParameterException
