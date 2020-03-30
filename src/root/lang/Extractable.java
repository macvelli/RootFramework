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

import root.annotation.Todo;

/**
 * Reflection is cool, but <b>Extraction is way cooler!</b>
 * <p>
 * Consider the following way-too-common snippet of code:
 * <p>
 *
 * <pre>
 * <code>StringBuilder builder = new StringBuilder();
 * builder.append(foo);</code>
 * </pre>
 *
 * Here is all that happens in this two-line piece of code:
 * <ol>
 * <li><code>foo.toString()</code> is called</li>
 * <ol type="a">
 * <li>Another <code>StringBuilder</code> (or some other means of capturing the <code>char[]</code> data of <code>foo</code>) is created and populated
 * in <code>foo.toString()</code></li>
 * <li><code>StringBuilder.toString()</code> is called and returned from <code>foo.toString()</code></li>
 * </ol>
 * <li>The {@link String} returned from <code>foo.toString()</code> is appended to the <code>builder</code></li>
 * </ol>
 * So <code>builder.append(foo)</code> creates another <code>StringBuilder</code> and creates a {@link String} where both objects are quickly
 * discarded because all I want are the <code>char[]</code> contents of <code>foo</code> to get into <code>builder</code>. Oh, and the
 * <code>char[]</code> contents of <code>foo</code> <b>are copied three times</b>: once to populate the internal <code>StringBuilder</code>, once to
 * populate the {@link String}, and lastly once to populate <code>builder</code>.
 * <p>
 * I don't think I have to emphasize that <b>four object creations</b> (one <code>StringBuilder</code>, one {@link String}, and their respective
 * encapsulated <code>char[]</code> arrays) and <b>three <code>char[]</code> copies</b> for <code>builder.append(foo)</code> is a complete waste of
 * both memory and processing power.
 * <p>
 * Instead, what if we just pass the <code>builder</code> into <code>foo</code>? But wait, we cannot say <code>foo.toString(builder)</code>. This is
 * where the {@link Extractable} interface comes in. <b>Anything</b> that implements this interface works just as you expect with an {@link Extractor}
 * such as {@link StringExtractor} where <code>extractor.append(foo)</code> will pass the {@link StringExtractor} directly into <code>foo</code> via
 * the {@link #extract(StringExtractor)} method.
 * <p>
 * Now, there are <b>zero superfluous object creations</b> and the <code>char[]</code> data from <code>foo</code> <b>is only populated once</b>. Boom!
 * <p>
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Todo("Make every possible class in the Root Framework implement this interface")
public interface Extractable {

	void extract(StringExtractor extractor);

} // End Extractable
