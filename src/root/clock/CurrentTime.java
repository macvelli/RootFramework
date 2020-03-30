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
package root.clock;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class CurrentTime implements Runnable {

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	public volatile long currentTime;

	private final long	 numMillisToSleep;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public CurrentTime(final int numSecondsToSleep) {
		this.numMillisToSleep = numSecondsToSleep * 1000;
		this.currentTime = System.currentTimeMillis();

		new Thread(this).start();
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	@Override
	public final void run() {
		while (true) {
			try {
				Thread.sleep(this.numMillisToSleep);
			} catch (final InterruptedException e) {
			}

			this.currentTime = System.currentTimeMillis();
		}
	}

} // End CurrentTime
