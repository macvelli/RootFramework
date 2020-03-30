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
package root.lang;

import java.util.Iterator;

/**
 * This interface provides a means to {@link #lock()} and {@link #unlock()} the underlying concurrent data structure so that it <b>does not</b> change
 * out from underneath you while performing your iteration.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 *
 * @param <T>
 *            The class type of the element being iterated over
 */
public interface ConcurrentIterator<T> extends Iterator<T> {

	void lock();

	void unlock();

} // End ConcurrentIterator
