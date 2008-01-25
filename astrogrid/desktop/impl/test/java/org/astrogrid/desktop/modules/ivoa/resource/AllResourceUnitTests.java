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
		suite.addTestSuite(TimeServiceParserUnitTest.class);  // needs more samples.		
		suite.addTestSuite(SpectrumServiceParserUnitTest.class);  // needs more samples.
		suite.addTestSuite(CeaParserUnitTest.class); 
		return suite;
	}

}
