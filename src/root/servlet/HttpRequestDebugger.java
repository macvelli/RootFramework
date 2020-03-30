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
package root.servlet;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class HttpRequestDebugger implements Extractable {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final char[] paramSeparator = { ' ', '=', ' ' };

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private final HttpServletRequest httpRequest;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public HttpRequestDebugger(final HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append(this.httpRequest.getMethod())
				.append(' ')
				.append(this.httpRequest.getRequestURI())
				.append(' ')
				.append(this.httpRequest.getProtocol());

		String headerName;
		for (final Enumeration<String> e = this.httpRequest.getHeaderNames(); e.hasMoreElements();) {
			headerName = e.nextElement();

			extractor.append('\n').append(headerName).append(':');
			int j = 0;

			for (final Enumeration<String> f = this.httpRequest.getHeaders(headerName); f.hasMoreElements();) {
				if (j++ > 0) {
					extractor.addSeparator();
				}

				extractor.append(' ').append(f.nextElement());
			}
		}
		extractor.append('\n').append('\n');

		if (this.httpRequest.getQueryString() != null) {
			extractor.append(this.httpRequest.getQueryString()).append('\n');
		} else {
			String paramName;
			String[] paramValues;

			for (final Enumeration<String> e = this.httpRequest.getParameterNames(); e.hasMoreElements();) {
				paramName = e.nextElement();
				paramValues = this.httpRequest.getParameterValues(paramName);

				extractor.append(paramName).append(paramSeparator, 0, 3);
				extractor.append('{');

				if (paramValues.length > 0) {
					extractor.append(paramValues[0]);

					for (int i = 1; i < paramValues.length; i++) {
						extractor.addSeparator().append(paramValues[i]);
					}
				}

				extractor.append('}');
			}
		}
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(512);
		this.extract(extractor);
		return extractor.toString();
	}

} // End HttpRequestDebugger
