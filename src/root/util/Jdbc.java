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
package root.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Jdbc {

	public static final void close(final Connection con) {
		try {
			con.close();
		} catch (final Exception ex) {
		}
	}

	public static final void close(final Connection con, final Statement stmt) {
		try {
			stmt.close();
		} catch (final Exception ex) {
		}

		try {
			con.close();
		} catch (final Exception ex) {
		}
	}

	public static final void close(final Connection con, final Statement stmt, final ResultSet rs) {
		try {
			rs.close();
		} catch (final Exception ex) {
		}

		try {
			stmt.close();
		} catch (final Exception ex) {
		}

		try {
			con.close();
		} catch (final Exception ex) {
		}
	}

	public static final void close(final Statement stmt) {
		try {
			stmt.close();
		} catch (final Exception ex) {
		}
	}

	public static final void close(final Statement stmt, final ResultSet rs) {
		try {
			rs.close();
		} catch (final Exception ex) {
		}

		try {
			stmt.close();
		} catch (final Exception ex) {
		}
	}

} // End Jdbc
