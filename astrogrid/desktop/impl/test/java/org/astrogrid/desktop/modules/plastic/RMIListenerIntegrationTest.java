package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.common.namegen.InMemoryNameGen;
import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.HubMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;
import org.votech.plastic.managers.PlasticManager;
import org.votech.plastic.outgoing.policies.BlockingPolicy;
import org.votech.plastic.outgoing.policies.HubStartPolicy;
import org.votech.plastic.outgoing.policies.StandardRMIPolicy;

import EDU.oswego.cs.dl.util.concurrent.DirectExecutor;
/**
 * Just like a cut-down RMIListenerUnitTest, but we force the comms to be by RMI (in the unit test, they're actually in-process)
 * @author jdt
 *
 */
public class RMIListenerIntegrationTest extends AbstractPlasticBaseNotDeaf {

    

    protected PlasticHubListener getHub() throws Exception {
        ACR acr = new Finder().find();
        //Make sure that the hub is loaded and started
        PlasticHubListener listener = (PlasticHubListener) acr.getService(PlasticHubListener.class);
        
        
        //Here's the trick - we're going to ignore the listener we just got....that one will be using the
        //in-process ACR
        HubStartPolicy hubStartPolicy = new BlockingPolicy();
        
        return hubStartPolicy.getHub();
    }

    protected TestPlasticApplication getApplication(Properties appData) {
        return new TestListenerRMI(appData);
    }
	
	

}
