package org.astrogrid.desktop.modules.plastic;

import junit.framework.TestCase;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.Configuration;
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
    private String origSetting;
    private Configuration conf;

    @Override
    public void setUp() throws Exception {
        super.setUp();       
        final ACR acr = new TestingFinder().find();
        //Make sure that the hub is loaded and started
        final PlasticHubListener listener = (PlasticHubListener) acr.getService(PlasticHubListener.class); 
        assertNotNull(listener);
    }
    
    public void testPlaskit() {
        try {
            HubTester.testHub();
            
        } catch (final Throwable e) {
            e.printStackTrace();
            fail("One of the plaskit tests failed with error "+e.getMessage());
        }
    }

}
