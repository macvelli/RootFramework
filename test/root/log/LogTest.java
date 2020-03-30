package root.log;

import static org.junit.Assert.fail;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

class LogTest {

	@Test
	public void testConfiguration() {
		Log.init("exampleLogging.properties");

		Log log = new Log(getClass());

		// System.out.println(au.com.forward.logging.Logging.currentConfiguration());

		for (int i = 0; i < 100; i++) {
			log.error("Hello world!");
			log.warn("This is a warning!");
			log.info("Give me some informantion!");
			log.debug("Everything is working as expected");
			log.trace("Nothing to see here...");
		}

		try {
			String lol = null;
			lol.charAt(0);
		} catch (NullPointerException e) {
			log.error("Whoops!", e);
		}
	}

	@Test
	public void testLog4JHandler() {
		Log.init("exampleLog4J.properties");
		PropertyConfigurator.configure("Z:/projects/Root/src/log4j.properties");

		final Log log = new Log(getClass());

		// System.out.println(au.com.forward.logging.Logging.currentConfiguration());

		for (int i = 0; i < 100; i++) {
			log.error("Hello world!");
			log.warn("This is a warning!");
			log.info("Give me some informantion!");
			log.debug("Everything is working as expected");
			log.trace("Nothing to see here...");
		}

		try {
			String lol = null;
			lol.charAt(0);
		} catch (NullPointerException e) {
			log.error("Whoops!", e);
		}

		log.warn("Ok so what's up?");
	}

	@Test
	public void testClose() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetHandler() {
		fail("Not yet implemented");
	}

	@Test
	public void testDebugStringObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testDebugStringThrowable() {
		fail("Not yet implemented");
	}

	@Test
	public void testErrorStringObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testErrorStringThrowable() {
		fail("Not yet implemented");
	}

	@Test
	public void testInfoStringObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testInfoStringThrowable() {
		fail("Not yet implemented");
	}

	@Test
	public void testTraceStringObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testTraceStringThrowable() {
		fail("Not yet implemented");
	}

	@Test
	public void testWarnStringObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testWarnStringThrowable() {
		fail("Not yet implemented");
	}

} // End LogTest
