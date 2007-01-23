package org.astrogrid.desktop.modules.plastic;

import junit.framework.TestCase;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.TestingFinder;
import org.votech.plastic.PlasticHubListener;

import uk.ac.starlink.plastic.HubTester;
/**
 * A simple wrapper around the tests
 * provided by Mark Taylor in his plaskit library.
 * 
 * @author jdt
 *
 */
public class PlaskitIntegrationTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();
        ACR acr = new TestingFinder().find();
        //Make sure that the hub is loaded and started
        PlasticHubListener listener = (PlasticHubListener) acr.getService(PlasticHubListener.class); 
        assertNotNull(listener);
    }
    
    public void testPlaskit() {

        try {
            HubTester.testHub();
            
        } catch (Throwable e) {
            assertTrue("One of the plaskit tests failed with error "+e.getMessage(), false);
        }
    }

}
