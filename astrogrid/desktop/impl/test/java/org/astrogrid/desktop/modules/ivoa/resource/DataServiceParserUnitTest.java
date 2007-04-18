/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import javax.xml.stream.XMLInputFactory;

import junit.framework.TestCase;

/** Tests parsing of data service resources, and the subclass Catalog Service resources
 * 
 * DataService - covers resourcews previously called SkyService.
 * 		- although many cone services evolve into this too.
 * CatalogService - covers resources prevously called TabularSkyService - vizier stuff.
 * FIXME implement
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
	public void testDummy() throws Exception {
		System.err.println("implement some tsts in " + this.getClass().getName());
		
	}

}
