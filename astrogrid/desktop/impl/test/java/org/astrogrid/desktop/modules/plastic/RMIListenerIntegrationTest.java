package org.astrogrid.desktop.modules.plastic;

import java.util.Properties;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.TestingFinder;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.outgoing.policies.BlockingPolicy;
import org.votech.plastic.outgoing.policies.HubStartPolicy;
/**
 * Just like a cut-down RMIListenerUnitTest, but we force the comms to be by RMI (in the unit test, they're actually in-process)
 * @author jdt
 *
 */
public class RMIListenerIntegrationTest extends AbstractPlasticBaseNotDeaf {

    

    protected PlasticHubListener getHub() throws Exception {
        ACR acr = new TestingFinder().find();
        //Make sure that the hub is loaded and started
        PlasticHubListener listener = (PlasticHubListener) acr.getService(PlasticHubListener.class);
        
        
        //Here's the trick - we're going to ignore the listener we just got....that one will be using the
        //in-process ACR
        HubStartPolicy hubStartPolicy = new BlockingPolicy();
        
        return hubStartPolicy.getHub();
    }

    protected TestPlasticApplication getApplication(Properties appData, MessageHandler handler) {
        return new TestListenerRMI(appData, handler);
    }
	
	

}
