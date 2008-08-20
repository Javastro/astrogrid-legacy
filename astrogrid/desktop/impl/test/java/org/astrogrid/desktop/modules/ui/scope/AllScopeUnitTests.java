/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 31, 20074:47:46 PM
 */
public class AllScopeUnitTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite(
                "Test for org.astrogrid.desktop.modules.ui.scope");
        //$JUnit-BEGIN$
        suite.addTestSuite(ScopeHistoryProviderUnitTest.class);
        suite.addTestSuite(VotableContentHandlerUnitTest.class);
        suite.addTestSuite(AstroscopeFileObjectUnitTest.class);
        suite.addTestSuite(VizModelUnitTest.class);
        suite.addTestSuite(QueryResultsUnitTest.class);
        suite.addTestSuite(SsapRetrievalUnitTest.class);
        //$JUnit-END$
        return suite;
    }

}
