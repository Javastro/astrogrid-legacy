/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.resource;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley
 * @since Aug 2, 20061:34:17 AM
 */
public class AllResourceUnitTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Unit test for resource parser and renderer");
		suite.addTestSuite(ResourceStreamParserUnitTest.class); 
		suite.addTestSuite(ResourceParserUnitTest.class);
		suite.addTestSuite(AuthorityParserUnitTest.class); 
		suite.addTestSuite(OrganisationParserUnitTest.class); 	
		suite.addTestSuite(DataCollectionParserUnitTest.class);  		
		suite.addTestSuite(ServiceParserUnitTest.class); 
		suite.addTestSuite(ConeParserUnitTest.class); 
		suite.addTestSuite(SiapParserUnitTest.class);
		suite.addTestSuite(RegistryServiceParserUnitTest.class);
		// done down to here.
		
		suite.addTestSuite(SpectrumServiceParserUnitTest.class); 
		suite.addTestSuite(TimeServiceParserUnitTest.class); 
		suite.addTestSuite(CeaParserUnitTest.class); 
		suite.addTestSuite(DataServiceParserUnitTest.class);
		// catalog service.. - i.e. vizier.
		// and the other kind - whatever it's called.
		return suite;
	}

}
