package root.finance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import root.lang.StringExtractor;
import root.validation.InvalidFormatException;

class PercentTest {

	@Test
	public void testHashCode() {
		assertEquals(350, new Percent("3.50%").hashCode());
	}

	@Test
	public void testPercentString() {
		Percent p = new Percent("3.50%");
		assertEquals("3.50%", p.toString());

		p = new Percent("10%");
		assertEquals("10.00%", p.toString());

		p = new Percent("7.5");
		assertEquals("7.50%", p.toString());
	}

	@Test(expected = InvalidFormatException.class)
	public void testInvalidPercentStringA() {
		new Percent("3..50");
	}

	@Test(expected = InvalidFormatException.class)
	public void testInvalidPercentStringB() {
		new Percent("3.500");
	}

	@Test(expected = InvalidFormatException.class)
	public void testInvalidPercentStringC() {
		new Percent("3.A0");
	}

	@Test
	public void testPercentMoneyMoney() {
		assertEquals("150.00%", new Percent(new Money(9000000), new Money(6000000)).toString());
	}

	@Test
	public void testCompareTo() {
		Percent m = new Percent("3.50%");
		Percent n = new Percent("6.75%");
		assertEquals(-1, m.compareTo(n));
		assertEquals(1, n.compareTo(m));
		assertEquals(0, m.compareTo(new Percent("3.5")));
	}

	@Test
	public void testEqualsObject() {
		assertTrue(new Percent("3.5").equals(new Percent("3.5%")));
		assertFalse(new Percent("3.50%").equals(new Percent("6.75")));
	}

	@Test
	public void testMultiplyMoney() {
		assertEquals("90000.00", new Percent("150.00").multiply(new Money(6000000)).toString());
	}

	@Test
	public void testExtract() {
		StringExtractor chars = new StringExtractor();
		Percent p = new Percent("-21474836.48%");
		p.extract(chars);
		assertEquals("-21474836.48%", chars.toString());
		chars.clear();
		p = new Percent("329.45");
		p.extract(chars);
		assertEquals("329.45%", chars.toString());
	}

	@Test
	public void testToString() {
		assertEquals("-21474836.48%", new Percent("-21474836.48").toString());
		assertEquals("52487.91%", new Percent("52487.91%").toString());
	}

} // End PercentTest
