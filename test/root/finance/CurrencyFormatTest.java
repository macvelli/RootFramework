package root.finance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

class CurrencyFormatTest {

	private CurrencyFormat formatter = new CurrencyFormat();

	@Test
	public void testPositiveFormat() {
		assertEquals("$0.09", formatter.formatMsg(new Money(9)));
		assertEquals("$0.75", formatter.formatMsg(new Money(75)));
		assertEquals("$1.30", formatter.formatMsg(new Money(130)));
		assertEquals("$452.73", formatter.formatMsg(new Money(45273)));
		assertEquals("$1,698.25", formatter.formatMsg(new Money(169825)));
		assertEquals("$27,379.50", formatter.formatMsg(new Money(2737950)));
		assertEquals("$352,415.72", formatter.formatMsg(new Money(35241572)));
		assertEquals("$2,352,415.72", formatter.formatMsg(new Money(235241572)));
		assertEquals("$21,352,415.72", formatter.formatMsg(new Money(2135241572)));
	}

	@Test
	public void testNegativeFormat() {
		assertEquals("($0.09)", formatter.formatMsg(new Money(-9)));
		assertEquals("($0.75)", formatter.formatMsg(new Money(-75)));
		assertEquals("($1.30)", formatter.formatMsg(new Money(-130)));
		assertEquals("($452.73)", formatter.formatMsg(new Money(-45273)));
		assertEquals("($1,698.25)", formatter.formatMsg(new Money(-169825)));
		assertEquals("($27,379.50)", formatter.formatMsg(new Money(-2737950)));
		assertEquals("($352,415.72)", formatter.formatMsg(new Money(-35241572)));
		assertEquals("($2,352,415.72)", formatter.formatMsg(new Money(-235241572)));
		assertEquals("($21,352,415.72)", formatter.formatMsg(new Money(-2135241572)));
	}

} // End CurrencyFormatTest
