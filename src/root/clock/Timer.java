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
 * TODO http://www.howtogeek.com/244996/how-to-use-the-alarm-timer-and-stopwatch-on-android/
 *
 * How to Use the Timer - You can set up multiple timers in the Clock app, making this more useful
 * than your standard kitchen timer, which can usually, at most, only time two things at the same
 * time. To use the timer, tap the timer icon at the top of the screen. Set the amount of time for
 * the timer using the number pad on the right. Be sure to enter zeros as needed. For example, to
 * set a timer for 10 minutes, tap 1000 on the number pad. If you only tap 10 you will end up with
 * 10 seconds on your timer, not 10 minutes. You can see the number of hours, minutes, and seconds
 * on the digital readout on the left as you type the time. To start the timer, tap the red Start
 * button at the bottom. - Just like with the alarms, you can have multiple timers, so you might
 * want to give them names so you know which timer is timing which activity. To add a label to the
 * current timer, tap Label. - There are a couple of settings you can change for timers. To access
 * these settings, tap the menu button and tap Settings on the popup menu as described earlier. The
 * Timer Expired ringtone is set by default as the sound used when the timer expires. If you want to
 * change the ringtone, tap “Timer ringtone.
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Timer {

	// <><><><><><><><><><><><><>< Attributes ><><><><><><><><><><><><><><><><>

	/** The number of milliseconds before the {@link Timer} expires */
	private long numMillisToExpiration;

	/** The start time in milliseconds of the {@link Timer} */
	private long startTimeInMillis;

	// <><><><><><><><><><><><><> Constructors <><><><><><><><><><><><><><><><>

	/**
	 * Creates a {@link Timer} instance initialized with the start time from
	 * {@link java.lang.System#currentTimeMillis()} and an expiration of zero milliseconds.
	 */
	public Timer() {
		this(0);
	}

	/**
	 * Creates a {@link Timer} instance initialized with the start time from
	 * {@link java.lang.System#currentTimeMillis()} and the supplied expiration value.
	 *
	 * @param numMillisToExpiration
	 *            The number of milliseconds before the {@link Timer} expires.
	 */
	public Timer(final long numMillisToExpiration) {
		this.startTimeInMillis = System.currentTimeMillis();
		this.numMillisToExpiration = numMillisToExpiration;
	}

	// <><><><><><><><><><><><>< Public Methods ><><><><><><><><><><><><><><><>

	/**
	 * Returns the elapsed time since the start time of the {@link Timer}.
	 *
	 * @return the elapsed time since the start time of the {@link Timer}
	 */
	public final long elapsed() {
		return System.currentTimeMillis() - this.startTimeInMillis;
	}

	/**
	 * Returns the expiration of this {@link Timer}.
	 *
	 * @return the expiration of this {@link Timer}
	 */
	public final long getExpiration() {
		return this.numMillisToExpiration;
	}

	/**
	 * Returns <code>true</code> if the elapsed time of this {@link Timer} is greater than or equal
	 * to the expiration value.
	 * <p>
	 * <b>NOTE:</b> The {@link Timer} only expires if the expiration value is greater than zero.
	 *
	 * @return <code>true</code> if {@link Timer} has expired, <code>false</code> otherwise
	 */
	public final boolean hasExpired() {
		return this.numMillisToExpiration > 0 && this.elapsed() >= this.numMillisToExpiration;
	}

	/**
	 * Returns <code>true</code> if the elapsed time of this {@link Timer} is greater than or equal
	 * to the expiration value.
	 * <p>
	 * <b>NOTE:</b> This is a convenience method for when you have already called
	 * {@link java.lang.System#currentTimeMillis()} in the calling code so that the value may be
	 * used to evaluate multiple {@link Timer} instances at once.
	 *
	 * @param currentTime
	 *            The current time which is used to calculate the elapsed time of this {@link Timer}
	 * @return <code>true</code> if {@link Timer} has expired, <code>false</code> otherwise
	 */
	public final boolean hasExpired(final long currentTime) {
		return this.numMillisToExpiration > 0 && currentTime - this.startTimeInMillis >= this.numMillisToExpiration;
	}

	/**
	 * Returns the amount of time remaining before the timer expires.
	 *
	 * @return the amount of time remaining before the timer expires
	 */
	public final long remaining() {
		return this.numMillisToExpiration > 0 ? this.numMillisToExpiration - this.elapsed() : 0L;
	}

	/**
	 * Returns the amount of time remaining before the timer expires.
	 * <p>
	 * <b>NOTE:</b> This is a convenience method for when you have already called
	 * {@link java.lang.System#currentTimeMillis()} in the calling code so that the value may be
	 * used to evaluate multiple {@link Timer} instances at once.
	 *
	 * @param currentTime
	 *            The current time which is used to calculate the elapsed time of this {@link Timer}
	 * @return the amount of time remaining before the timer expires
	 */
	public final long remaining(final long currentTime) {
		return this.numMillisToExpiration > 0 ? this.numMillisToExpiration - (currentTime - this.startTimeInMillis)
				: 0L;
	}

	/**
	 * Resets the {@link Timer} start time to the value returned from
	 * {@link java.lang.System#currentTimeMillis()}
	 */
	public final void reset() {
		this.startTimeInMillis = System.currentTimeMillis();
	}

	/**
	 * Resets the {@link Timer} start time to the specified current time.
	 * <p>
	 * <b>NOTE:</b> This is a convenience method for when you have already called
	 * {@link java.lang.System#currentTimeMillis()} in the calling code so that the value may be
	 * used to evaluate multiple {@link Timer} instances at once.
	 *
	 * @param currentTime
	 *            The current time to assign to the {@link Timer} start time
	 */
	public final void reset(final long currentTime) {
		this.startTimeInMillis = currentTime;
	}

	/**
	 * Sets the {@link Timer} expiration.
	 *
	 * @param numMillisToExpiration
	 *            The number of milliseconds before the {@link Timer} expires.
	 */
	public final void setExpiration(final long numMillisToExpiration) {
		this.numMillisToExpiration = numMillisToExpiration;
	}

} // End Timer
