package org.astrogrid.desktop.modules.plastic;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.votech.plastic.PlasticHubListener;

import uk.ac.starlink.plastic.HubTester;
/**
 * A simple wrapper around the tests
 * provided by Mark Taylor in his plaskit library.
 * @todo make this work again.
 * @author jdt
 *
 */
public class PlaskitIntegrationTest extends InARTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();       
        final ACR acr =getACR();
        //Make sure that the hub is loaded and started
        assertServiceExists(PlasticHubListener.class,"plastic.hub");
    }
    
    public void testPlaskit() {
        try {
            HubTester.testHub();
            
        } catch (final Throwable e) {
            e.printStackTrace();
            fail("One of the plaskit tests failed with error "+e.getMessage());
        }
    }
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(PlaskitIntegrationTest.class));
    }

}
