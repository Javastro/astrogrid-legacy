/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/util/xml/sax/junit/Attic/JUnitTestCase.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/18 01:38:14 $</cvs:author>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 * $Log: JUnitTestCase.java,v $
 * Revision 1.2  2003/06/18 01:38:14  dave
 * Fixed bug in XML parser tests
 *
 * Revision 1.1  2003/06/17 15:17:34  dave
 * Added links to live MySpace, including initial XML parser for lookup results
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.util.xml.sax.junit ;

import java.io.InputStream ;
import java.io.IOException ;

import org.xml.sax.SAXException ;
import org.xml.sax.SAXParseException ;

import org.astrogrid.portal.util.xml.sax.SAXElementHandler  ;
import org.astrogrid.portal.util.xml.sax.SAXDocumentHandler ;
import org.astrogrid.portal.util.xml.sax.SAXAttributeHandler ;
import org.astrogrid.portal.util.xml.sax.SAXCharacterHandler ;

import junit.framework.TestCase ;

/**
 * JUnits tests for the SAX XML parser tools.
 *
 */
public class JUnitTestCase
	extends TestCase
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	private static boolean DEBUG_FLAG = true ;

	/**
	 * Our expected result.
	 * Not thread safe !
	 *
	 */
	protected Object expected ;

	/**
	 * Our test result.
	 * Not thread safe !
	 *
	 */
	protected Object result ;

	/**
	 * Check empty document handling.
	 *
	 */
	public void test000()
		throws SAXException, IOException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("JUnit test") ;
		if (DEBUG_FLAG) System.out.println("  Class : " + this.getClass().getName()) ;
		if (DEBUG_FLAG) System.out.println("  Test  : test000()") ;

		//
		// Create our input stream.
		InputStream stream = this.getClass().getResourceAsStream("test.000.xml") ;
		//
		// Initialise our expected result.
		Exception expected = new SAXParseException("Document root element is missing.", null) ;
		//Exception expected = new SAXParseException("Premature end of file.", null) ;
		//
		// Create our DocumentHandler.
		SAXDocumentHandler parser = new SAXDocumentHandler() ;
		//
		// Try parsing the test file.
		try {
			parser.parse(stream) ;
			fail("SAXParseException expected.") ;
			}
		//
		// Catch the expected exception.
		catch (SAXException exception)
			{
			//
			// Check we got the right exception.
			assertEquals(exception.getClass(),   expected.getClass())   ;
			//
			// Message is diffrent in JDK-1.4.1 and JDK-1.4.2
			//assertEquals(exception.getMessage(), expected.getMessage()) ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Check unknown element handling.
	 *
	 */
	public void test001()
		throws SAXException, IOException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("JUnit test") ;
		if (DEBUG_FLAG) System.out.println("  Class : " + this.getClass().getName()) ;
		if (DEBUG_FLAG) System.out.println("  Test  : test001()") ;

		//
		// Create our input stream.
		InputStream stream = this.getClass().getResourceAsStream("test.001.xml") ;
		//
		// Initialise our expected result.
		Exception expected = new SAXParseException("Unknown element \"suprise\"", null) ;
		//
		// Create our DocumentHandler.
		SAXDocumentHandler parser = new SAXDocumentHandler() ;
		//
		// Add our element parser.
		parser.addElementHandler(
			new SAXElementHandler("alpha")
			) ;
		//
		// Try parsing the test file.
		try {
			parser.parse(stream) ;
			fail("SAXException expected.") ;
			}
		//
		// Catch the expected exception.
		catch (SAXException exception)
			{
			//
			// Check we got the right exception message.
			assertEquals(exception.getClass(),   expected.getClass())   ;
			assertEquals(exception.getMessage(), expected.getMessage()) ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Check valid element handling.
	 *
	 */
	public void test002()
		throws SAXException, IOException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("JUnit test") ;
		if (DEBUG_FLAG) System.out.println("  Class : " + this.getClass().getName()) ;
		if (DEBUG_FLAG) System.out.println("  Test  : test002()") ;

		//
		// Create our input stream.
		InputStream stream = this.getClass().getResourceAsStream("test.002.xml") ;
		//
		// Initialise our expected results.
		expected = "alpha" ;
		result   = null ;
		//
		// Create our DocumentHandler.
		SAXDocumentHandler parser = new SAXDocumentHandler() ;
		//
		// Add our element parser.
		parser.addElementHandler(
			new SAXElementHandler("alpha")
				{
				protected void startElement()
					throws SAXException
					{
					result = this.getName() ; ;
					}
				}
			) ;
		//
		// Try parsing the test file.
		parser.parse(stream) ;
		//
		// Check we got the right element.
		assertEquals(expected, result) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Check unexpected attribute handling.
	 *
	 */
	public void test003()
		throws SAXException, IOException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("JUnit test") ;
		if (DEBUG_FLAG) System.out.println("  Class : " + this.getClass().getName()) ;
		if (DEBUG_FLAG) System.out.println("  Test  : test003()") ;

		//
		// Create our input stream.
		InputStream stream = this.getClass().getResourceAsStream("test.003.xml") ;
		//
		// Initialise our expected result.
		Exception expected = new SAXParseException("Unknown attribute \"beta\"", null) ;
		//
		// Create our DocumentHandler.
		SAXDocumentHandler parser = new SAXDocumentHandler() ;
		//
		// Add our element parser.
		parser.addElementHandler(
			new SAXElementHandler("alpha")
				{
				protected void startElement()
					throws SAXException
					{
					result = this.getName() ; ;
					}
				}
			) ;
		//
		// Try parsing the test file.
		try {
			parser.parse(stream) ;
			fail("SAXException expected.") ;
			}
		//
		// Catch the expected exception.
		catch (SAXException exception)
			{
			//
			// Check we got the right exception message.
			assertEquals(exception.getClass(),   expected.getClass())   ;
			assertEquals(exception.getMessage(), expected.getMessage()) ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Check valid attribute handling.
	 *
	 */
	public void test005()
		throws SAXException, IOException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("JUnit test") ;
		if (DEBUG_FLAG) System.out.println("  Class : " + this.getClass().getName()) ;
		if (DEBUG_FLAG) System.out.println("  Test  : test005()") ;

		//
		// Create our input stream.
		InputStream stream = this.getClass().getResourceAsStream("test.005.xml") ;
		//
		// Initialise our expected results.
		expected = "Alphabetical" ;
		result   = null ;
		//
		// Create our DocumentHandler.
		SAXDocumentHandler parser = new SAXDocumentHandler() ;
		//
		// Add our element parser.
		parser.addElementHandler(
			new SAXElementHandler("alpha")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								result = text ;
								}
							}
						);
					}
				}
			) ;
		//
		// Try parsing the test file.
		parser.parse(stream) ;
		//
		// Check we got the right element.
		assertEquals(expected, result) ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}
	}
