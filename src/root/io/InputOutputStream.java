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
package root.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param T
 *            The type of values handled by the {@link InputOutputStream}
 */
public interface InputOutputStream<T> {

	InputStream getInputStream();

	OutputStream getOutputStream();

	int getSize();

	T getValues();

	void reset();

	void writeTo(OutputStream os) throws IOException;

} // End InputOutputStream
