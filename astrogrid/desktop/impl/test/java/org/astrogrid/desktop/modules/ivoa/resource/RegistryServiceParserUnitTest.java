/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import javax.xml.stream.XMLInputFactory;

import junit.framework.TestCase;

/** Tests parsing of  registry service resources.
 * @todo implement
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 200711:51:20 PM
 */
public class RegistryServiceParserUnitTest extends TestCase {


	protected void setUp() throws Exception {
		super.setUp();
		fac = XMLInputFactory.newInstance();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		fac = null;
	}
	XMLInputFactory fac ;

}
