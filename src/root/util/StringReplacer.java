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

import root.adt.CollectorCharArray;
import root.annotation.Todo;
import root.lang.Constants;
import root.lang.Itemizer;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
@Todo({ "Need static methods to replace/remove in a single call w/out building up an entire StringReplacer object",
		"I know I can come up with a better algorithm if I use some additional data structures and maybe a Replace class definition" })
public final class StringReplacer {

	public static void main(final String[] args) {
		final StringReplacer replacer = new StringReplacer();
		replacer.replace("Foo", "BarWilly");
		replacer.replace("ABC", "Don't Hate the Playa");
		replacer.replace("123", "789");
		replacer.remove('\'');

		System.out.println(replacer.process("This has nothing to replace"));
		System.out.println(replacer.process("This has Foo to replace"));
		System.out.println(replacer.process("This has Foo to replace as well as ABC"));
		System.out.println(replacer.process("This has Foo to replace, ABC to replace, and 123 to replace"));
		System.out.println(replacer.process("This has Foo, ABC, and 123 to replace AB12Fo"));
		System.out.println(replacer.process("ABC123FooWoah"));
		System.out.println(replacer.process("FooFooFooFoo123123123ABCFooABC123"));
		System.out.println(replacer.process("Zaxby's"));
	}

	private final CollectorCharArray targets;

	private final CollectorCharArray replacements;

	public StringReplacer() {
		this.targets = new CollectorCharArray();
		this.replacements = new CollectorCharArray();
	}

	public String process(final String str) {
		final StringExtractor builder = new StringExtractor(str.length());
		final Itemizer<char[]> i = this.targets.iterator();
		final char[] c = Root.toCharArray(str);
		char[] t, r;

		// Build the replacement String
		int k, l, offset = 0;
		for (int j = 0; j < c.length; j++) {
			for (i.reset(); i.hasNext();) {
				t = i.next();
				if (c.length >= t.length + j) {
					for (k = j, l = 0; l < t.length && c[k++] == t[l]; l++) {
						;
					}
					if (l == t.length) {
						// Found match
						if (offset < j) {
							builder.append(c, offset, j - offset);
						}
						r = this.replacements.get(i.getIndex());
						if (r != Constants.emptyCharArray) {
							builder.append(r);
						}
						offset = k;
						j = k - 1;
						break;
					}
				}
			}
		}

		// Return the original String if no replacements were made
		if (offset == 0) {
			return str;
		}

		// Add the end of the String since the last replacement
		if (offset < c.length) {
			builder.append(c, offset, c.length - offset);
		}

		return builder.toString();
	}

	public void remove(final char... target) {
		this.targets.add(target);
		this.replacements.add(Constants.emptyCharArray);
	}

	public void remove(final CharSequence target) {
		this.targets.add(target);
		this.replacements.add(Constants.emptyCharArray);
	}

	public void remove(final String target) {
		this.targets.add(target);
		this.replacements.add(Constants.emptyCharArray);
	}

	public void replace(final CharSequence target, final CharSequence replacement) {
		this.targets.add(target);
		if (Root.notEmpty(replacement)) {
			this.replacements.add(replacement);
		} else {
			this.replacements.add(Constants.emptyCharArray);
		}
	}

	public void replace(final String target, final char... replacement) {
		this.targets.add(target);
		this.replacements.add(Root.notEmpty(replacement) ? replacement : Constants.emptyCharArray);
	}

	public void replace(final String target, final String replacement) {
		this.targets.add(target);
		if (Root.notEmpty(replacement)) {
			this.replacements.add(replacement);
		} else {
			this.replacements.add(Constants.emptyCharArray);
		}
	}

} // End StringReplacer
