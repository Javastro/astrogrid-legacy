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
		suite.addTestSuite(ResourceStreamParserUnitTest.class); //written
		suite.addTestSuite(ResourceParserUnitTest.class); // written
		suite.addTestSuite(AuthorityParserUnitTest.class); // written
		suite.addTestSuite(OrganisationParserUnitTest.class); // written.

		suite.addTestSuite(DataCollectionParserUnitTest.class); // written. phew.

		suite.addTestSuite(ServiceParserUnitTest.class); //written.
		suite.addTestSuite(ConeParserUnitTest.class); // will do, still more to fill in.
		suite.addTestSuite(SiapParserUnitTest.class);
		suite.addTestSuite(SpectrumServiceParserUnitTest.class); // leave for now
		suite.addTestSuite(TimeServiceParserUnitTest.class); // leave for now
		suite.addTestSuite(CeaParserUnitTest.class);  // ok for now.
		suite.addTestSuite(DataServiceParserUnitTest.class);
		suite.addTestSuite(RegistryServiceParserUnitTest.class);
		return suite;
	}

}
