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
package root.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

import root.adt.ListArrayLong;
import root.adt.ListArrayLongSorted;
import root.adt.MapHashed;
import root.finance.Percent;
import root.lang.Extractable;
import root.lang.StringExtractor;

/**
 *
 * @author Edward Smith
 * @version 0.5
 * @since 0.5
 */
public final class Statistics implements Extractable {

	// <><><><><><><><><><><><><><><> Constants <><><><><><><><><><><><><><><>

	public static final char[] labelCount = { ' ', 'c', 'o', 'u', 'n', 't', ',', ' ' };
	public static final char[] labelTotal = { ' ', 't', 'o', 't', 'a', 'l', ',', ' ' };
	public static final char[] labelAverage = { ' ', 'a', 'v', 'e', 'r', 'a', 'g', 'e', ',', ' ' };
	public static final char[] labelMinMax = { ' ', 'm', 'i', 'n', '/', 'm', 'a', 'x', ')' };

	// <><><><><><><><><><><><><><><> Attributes <><><><><><><><><><><><><><><>

	private long max;
	private long min;
	private long sum;
	private final ListArrayLong longList;

	// <><><><><><><><><><><><><><>< Constructors ><><><><><><><><><><><><><><>

	public Statistics() {
		this.min = Long.MAX_VALUE;
		this.longList = new ListArrayLong();
	}

	public Statistics(final int capacity) {
		this.min = Long.MAX_VALUE;
		this.longList = new ListArrayLong(capacity);
	}

	// <><><><><><><><><><><><><><> Public Methods <><><><><><><><><><><><><><>

	public final void add(final long n) {
		this.longList.add(n);

		// Adjust max value if necessary
		if (n > this.max) {
			this.max = n;
		}

		// Adjust min value if necessary
		if (n < this.min) {
			this.min = n;
		}

		// Adjust the sum value
		this.sum += n;
	}

	public final double correl(final Statistics y) {
		return this.covar(y) / (this.stdev() * y.stdev());
	}

	public final int count() {
		return this.longList.getSize();
	}

	public final double covar(final Statistics y) {
		final double xm = this.meanDouble();
		final double ym = y.meanDouble();
		double sum = 0.0;

		for (int i = 0; i < this.longList.getSize(); i++) {
			sum += (this.longList.get(i) - xm) * (y.longList.get(i) - ym);
		}

		return sum / this.longList.getSize();
	}

	@Override
	public final void extract(final StringExtractor extractor) {
		extractor.append('(').append(this.count()).appendArray(labelCount);

		extractor.append(this.sum).appendArray(labelTotal);

		extractor.append(this.meanDouble()).appendArray(labelAverage);

		extractor.append(this.min).append('/').append(this.max).appendArray(labelMinMax);
	}

	public final double intercept(final Statistics y) {
		return (y.sum - this.slope(y) * this.sum) / this.longList.getSize();
	}

	public final long max() {
		return this.max;
	}

	public final double meanDouble() {
		return this.divide(this.sum, this.longList.getSize());
	}

	public final long meanLong() {
		return this.sum / this.longList.getSize();
	}

	public final double median() {
		final ListArrayLongSorted sorted = new ListArrayLongSorted(this.longList);
		final int half = sorted.getSize() >> 1;

		if ((sorted.getSize() & 0x01) == 0) {
			return this.divide(sorted.get(half - 1) + sorted.get(half), 2);
		}

		return sorted.get(half);
	}

	public final long min() {
		return this.min;
	}

	public final long mode() {
		long mode = 0;
		int numTimes = 0;
		final ListArrayLongSorted sorted = new ListArrayLongSorted(this.longList);

		long n = 0;
		int j = 0;
		final long[] longValues = sorted.getValues();
		for (int i = 0; i < sorted.getSize(); i++) {
			final long l = longValues[i];

			if (n != l) {
				if (j > numTimes) {
					mode = n;
					numTimes = j;
				}

				n = l;
				j = 1;
			} else {
				j++;
			}
		}

		if (numTimes < 2) {
			throw new IllegalStateException("Mode value not available");
		}

		return mode;
	}

	public final Percent percent(final Statistics part) {
		return new Percent(part.sum, this.sum);
	}

	public final long range() {
		return this.max - this.min;
	}

	public final double[] rank() {
		final MapHashed<Long, Double> ties = new MapHashed<>(this.longList.getSize());
		final ListArrayLongSorted sorted = new ListArrayLongSorted(this.longList);
		final double[] ranks = new double[this.longList.getSize()];

		long l;
		int i = 0;
		final long[] longValues = this.longList.getValues();
		for (int k = 0; k < this.longList.getSize(); k++) {
			l = longValues[k];

			if (ties.containsKey(l)) {
				ranks[i] = ties.get(l);
			} else {
				int rank = sorted.indexOf(l) + 1;
				int n = 1;
				int j = rank;

				while (j < sorted.getSize() && sorted.get(j++) == l) {
					rank += j;
					n++;
				}

				if (n > 1) {
					ranks[i] = this.divide(rank, n);
					ties.put(l, ranks[i]);
				} else {
					ranks[i] = rank;
				}
			}

			i++;
		}

		return ranks;
	}

	public final String regression(final Statistics y) {
		final StringBuilder builder = new StringBuilder("y = ");

		builder.append(this.intercept(y));
		final double slope = this.slope(y);

		if (slope >= 0.0) {
			builder.append(" + ").append(slope);
		} else {
			builder.append(" - ").append(Math.abs(slope));
		}

		return builder.append('x').toString();
	}

	public final double rms() {
		double sum = 0.0;

		final long[] longValues = this.longList.getValues();
		for (int i = 0; i < this.longList.getSize(); i++) {
			sum += longValues[i] * longValues[i];
		}

		return Math.sqrt(sum / this.longList.getSize());
	}

	public final double slope(final Statistics y) {
		return (this.longList.getSize() * this.sumprod(y) - this.sum * y.sum) / (this.longList.getSize() * this.sumsq() - this.sum * this.sum);
	}

	public final double stdev() {
		return this.stdev(true);
	}

	public final double stdev(final boolean population) {
		double stdev = 0;
		final double mean = this.meanDouble();

		final long[] longValues = this.longList.getValues();
		for (int i = 0; i < this.longList.getSize(); i++) {
			final double d = longValues[i] - mean;
			stdev += d * d;
		}

		if (population) {
			stdev /= this.longList.getSize();
		} else {
			stdev /= this.longList.getSize() - 1;
		}

		return Math.sqrt(stdev);
	}

	public final long sum() {
		return this.sum;
	}

	@Override
	public final String toString() {
		final StringExtractor extractor = new StringExtractor(96);
		this.extract(extractor);
		return extractor.toString();
	}

	// <><><><><><><><><><><><><>< Private Methods ><><><><><><><><><><><><><>

	private double divide(final long dividend, final long divisor) {
		return divisor > 0 ? new BigDecimal(dividend).divide(new BigDecimal(divisor), 6, RoundingMode.HALF_DOWN).doubleValue() : 0.0d;
	}

	private long sumprod(final Statistics y) {
		long sum = 0;

		for (int i = 0; i < this.longList.getSize(); i++) {
			sum += this.longList.get(i) * y.longList.get(i);
		}

		return sum;
	}

	private long sumsq() {
		long sum = 0;

		final long[] longValues = this.longList.getValues();
		for (int i = 0; i < this.longList.getSize(); i++) {
			sum += longValues[i] * longValues[i];
		}

		return sum;
	}

} // End Stat
