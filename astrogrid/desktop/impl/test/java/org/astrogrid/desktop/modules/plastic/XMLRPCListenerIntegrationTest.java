package org.astrogrid.desktop.modules.plastic;

import java.io.IOException;
import java.util.Properties;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.outgoing.policies.StandardXmlRpcPolicy;

/**
 * System tests for interacting with a hub via xml-rpc
 * @author jdt
 *
 */
public class XMLRPCListenerIntegrationTest extends AbstractPlasticBaseNotDeaf {

    protected PlasticHubListener getHub() throws Exception {
        ACR acr = new Finder().find();
        //Make sure that the hub is loaded and started
        PlasticHubListener listener = (PlasticHubListener) acr.getService(PlasticHubListener.class); 
        //We actually want to return a proxy to this, that does all the comms by xmlrpc.
        StandardXmlRpcPolicy policy = new StandardXmlRpcPolicy();
        PlasticHubListener hub = policy.getHub();
        if (hub==null) throw new IOException("No hub is running");
        return hub;
    }

    protected TestPlasticApplication getApplication(Properties appData) {
        return new TestListenerXMLRPC(appData);
    }

}
