package root.xml;

/**
 * TODO: + Remake this with the new XMLParser paradigm
 * 
 * @author esmith
 */
class XMLParserTest {

	// @Test
	// public void testParseAttributeXML() {
	// String xml = "<Test foo=\"Bar\"/>";
	// Element e = XMLParser.parse(xml);
	//
	// assertEquals("Test", e.getName());
	// assertNull(e.getValue());
	// Attribute attr = e.getAttributes().get(0);
	// assertEquals("foo", attr.getName());
	// assertEquals("Bar", attr.getValue());
	// }
	//
	// @Test
	// public void testParseMultiAttributeXML() {
	// String xml = "<Test foo=\"Bar\" xyz=\"123\"/>";
	// Element e = XMLParser.parse(xml);
	//
	// assertEquals("Test", e.getName());
	// assertNull(e.getValue());
	// List<Attribute> attrs = e.getAttributes();
	// Attribute attr = attrs.get(0);
	// assertEquals("foo", attr.getName());
	// assertEquals("Bar", attr.getValue());
	// attr = attrs.get(1);
	// assertEquals("xyz", attr.getName());
	// assertEquals("123", attr.getValue());
	// }
	//
	// @Test
	// public void testParseNestedElementXML() {
	// String xml = "<Test><foo>Bar</foo></Test>";
	// Element e = XMLParser.parse(xml);
	//
	// assertEquals("Test", e.getName());
	// assertNull(e.getValue());
	// e = e.getFirstChild();
	// assertEquals("foo", e.getName());
	// assertEquals("Bar", e.getValue());
	// assertNull(e.getFirstChild());
	// }
	//
	// @Test
	// public void testNestedEmptyElementXML() {
	// String xml = "<Test><foo/></Test>";
	// Element e = XMLParser.parse(xml);
	//
	// assertEquals("Test", e.getName());
	// assertNull(e.getValue());
	// e = e.getFirstChild();
	// assertEquals("foo", e.getName());
	// assertNull(e.getValue());
	// assertTrue(e.getAttributes().isEmpty());
	// assertNull(e.getFirstChild());
	// }
	//
	// @Test
	// public void testNestedAttributeXML() {
	// String xml = "<Test><foo xyz=\"123\"/></Test>";
	// Element e = XMLParser.parse(xml);
	//
	// assertEquals("Test", e.getName());
	// assertNull(e.getValue());
	// e = e.getFirstChild();
	// assertEquals("foo", e.getName());
	// assertNull(e.getValue());
	// Attribute attr = e.getAttributes().get(0);
	// assertEquals("xyz", attr.getName());
	// assertEquals("123", attr.getValue());
	// assertNull(e.getFirstChild());
	// }
	//
	// @Test
	// public void testNestedAttributeWithValueXML() {
	// String xml = "<Test><foo xyz=\"123\">Bar</foo></Test>";
	// Element e = XMLParser.parse(xml);
	//
	// assertEquals("Test", e.getName());
	// assertNull(e.getValue());
	// e = e.getFirstChild();
	// assertEquals("foo", e.getName());
	// assertEquals("Bar", e.getValue());
	// Attribute attr = e.getAttributes().get(0);
	// assertEquals("xyz", attr.getName());
	// assertNull(e.getFirstChild());
	// }

} // End XMLParserTest
