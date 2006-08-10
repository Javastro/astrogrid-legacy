package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.votech.plastic.PlasticHubListener;

/**
 * Register and unregister lots of applications and see what breaks.
 * @author jdt
 *
 */
public class SlightlyStressfulIntegrationTest extends TestCase {
    private PlasticHubListener hub;
    protected PlasticHubListener getHub() throws Exception {
        ACR acr = new Finder().find();
        //Make sure that the hub is loaded and started
        PlasticHubListener listener = (PlasticHubListener) acr.getService(PlasticHubListener.class); 
        return listener;
    }
    
    public void setUp() throws Exception {
        super.setUp();
        hub = getHub();
    }
    public void testRegUnReg() {
        AbstractTestListener monitor = new TestListenerRMI(null);
        monitor.registerWith(hub,"monitor");
        
        for (int i=0;i<10000;++i) {
            AbstractTestListener grunt = new TestListenerRMI(null);
            grunt.registerWith(hub,"grunt");
        }
        
        List ids = hub.getRegisteredIds();
        for (Iterator it = ids.iterator();it.hasNext();) {
            URI id = (URI) it.next();
            hub.unregister(id);
        }
        
        
        
        
    }

}
