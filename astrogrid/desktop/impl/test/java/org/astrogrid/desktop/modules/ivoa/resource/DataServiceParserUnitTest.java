/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import javax.xml.stream.XMLInputFactory;

import junit.framework.TestCase;

/** Tests parsing of data service resources, and the subclass Catalog Service resources
 * 
 * DataService - covers resourcews previously called SkyService.
 * CatalogService - covers resources prevously called TabularSkyService
 * @todo implement
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 200711:51:20 PM
 */
public class DataServiceParserUnitTest extends TestCase {


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