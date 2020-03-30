package root.finance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

class MoneyTest {

	@Test
	public void testHashCode() {
		assertEquals(350, new Money(350).hashCode());
	}

	@Test
	public void testMoneyInt() {
		Money m = new Money(350);
		assertEquals("3.50", m.toString());
	}

	// @Test
	// public void testMoneyString() {
	// Money m = new Money("3.50");
	// assertEquals("3.50", m.toString());
	//
	// m = new Money("10");
	// assertEquals("10.00", m.toString());
	//
	// m = new Money("7.5");
	// assertEquals("7.50", m.toString());
	// }

	// @Test(expected=InvalidFormatException.class)
	// public void testInvalidMoneyStringA() {
	// new Money("3..50");
	// }

	// @Test(expected=InvalidFormatException.class)
	// public void testInvalidMoneyStringB() {
	// new Money("3.500");
	// }

	// @Test(expected=InvalidFormatException.class)
	// public void testInvalidMoneyStringC() {
	// new Money("3.A0");
	// }

	@Test
	public void testMoneyMoneyArray() {
		assertEquals("10.25", new Money(new Money(350), new Money(675)).toString());
	}

	// @Test
	// public void testGetAmount() {
	// assertEquals(350, new Money("3.50").getAmount());
	// }

	@Test
	public void testAdd() {
		// assertEquals("10.25", new MoneyLong(350).add(new
		// MoneyLong(675)).toString());
	}

	@Test
	public void testCreditMoney() {
		// MoneyLong m = new MoneyLong(350);
		// m.credit(new MoneyLong(675));
		// assertEquals("10.25", m.toString());
	}

	// @Test
	// public void testCreditString() {
	// Money m = new Money(350);
	// m.credit("6.75");
	// assertEquals("10.25", m.toString());
	// }

	@Test
	public void testDebitMoney() {
		// MoneyLong m = new MoneyLong(1025);
		// m.debit(new MoneyLong(675));
		// assertEquals("3.50", m.toString());
	}

	// @Test
	// public void testDebitString() {
	// Money m = new Money(1025);
	// m.debit("6.75");
	// assertEquals("3.50", m.toString());
	// }

	@Test
	public void testDivideInt() {
		assertEquals("7500.00", new Money(9000000).divide(12).toString());
	}

	@Test
	public void testDivideMoney() {
		assertEquals("150.00%", new Money(9000000).divide(new Money(6000000)).toString());
	}

	@Test
	public void testEqualsObject() {
		assertTrue(new Money(9000000).equals(new Money(9000000)));
		assertFalse(new Money(9000000).equals(new Money(6000000)));
	}

	@Test
	public void testMultiplyInt() {
		assertEquals("90000.00", new Money(750000).multiply(12).toString());
	}

	@Test
	public void testMultiplyPercent() {
		assertEquals("90000.00", new Money(6000000).multiply(new Percent("150.00")).toString());
	}

	@Test
	public void testSubtract() {
		// assertEquals("3.50", new MoneyLong(1025).subtract(new
		// MoneyLong(675)).toString());
	}

	@Test
	public void testCompareTo() {
		Money m = new Money(350);
		Money n = new Money(675);
		assertEquals(-1, m.compareTo(n));
		assertEquals(1, n.compareTo(m));
		assertEquals(0, m.compareTo(new Money(350)));
	}

	@Test
	public void testExtract() {
		// StringExtractor chars = new StringExtractor();
		// MoneyLong m = new MoneyLong(-2147483648);
		// m.extract(chars);
		// assertEquals("-21474836.48", chars.toString());
		// chars.clear();
		// m = new MoneyLong(5248791);
		// m.extract(chars);
		// assertEquals("52487.91", chars.toString());
	}

	@Test
	public void testToString() {
		assertEquals("-21474836.48", new Money(-2147483648).toString());
		assertEquals("52487.91", new Money(5248791).toString());
	}

} // End MoneyTest
