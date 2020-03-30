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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import root.lang.FastString;
import root.lang.ParamString;
import root.log.Log;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public abstract class HttpFilter implements Filter {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	private static final Log log = new Log(HttpFilter.class);

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public void destroy() {
		// Default implementation does nothing
	}

	@Override
	public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest req = (HttpServletRequest) request;
		final HttpServletResponse resp = (HttpServletResponse) response;

		try {
			this.execute(req, resp);
		} catch (final Exception e) {
			final String errorMsg = ParamString.formatMsg("{P} had a fatal error while attempting to process HTTP request:\n{P}",
					new FastString(this.getClass().getName()), new HttpRequestDebugger(req));

			log.error(errorMsg, e);
			throw new ServletException(errorMsg, e);
		}

		chain.doFilter(request, response);

		this.afterChain(req, resp);
	}

	@Override
	public void init(final FilterConfig config) throws ServletException {
		// Default implementation does nothing
	}

	// <><><><><><><><><><><><><> Protected Methods <><><><><><><><><><><><><>

	protected void afterChain(final HttpServletRequest request, final HttpServletResponse response) {
		// Default implementation does nothing
	}

	protected abstract void execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception;

} // End HttpFilter
