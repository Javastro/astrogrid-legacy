package org.astrogrid.desktop.modules.util;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley
 * @since Aug 25, 20061:34:48 AM
 */
public class TablesImplUnitTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		this.tables = new TablesImpl();
		this.input = this.getClass().getResource("siap.votable");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	TablesImpl tables;
	URL input;

	/** simplest - just echo to screen.
	 * Test method for {@link org.astrogrid.desktop.modules.util.TablesImpl#convert(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	public void testConvert() throws IOException {
		tables.convert(input.toString(),"votable","-","ascii");
		
	}
	
	// more advanced. check we can stream through, from votable to votable.
	// then compare results.
	/* doesn't work - as stil outputs a votable1.1, while input is a v1.0
	 * 
	 */
//	public void testConvert1() throws IOException, SAXException, ParserConfigurationException {
//		File of = File.createTempFile(this.getClass().getName(),".vot");
//		of.deleteOnExit();
//		tables.convert(input.toString(),"votable",of.toString(),"votable");
//		XMLAssert.assertXMLEqual("input and output aren't the same"
//				,new InputStreamReader(input.openStream())
//				,new FileReader(of));
//	}

}
