package org.astrogrid.desktop.modules.plastic;

import java.io.IOException;
import java.util.Properties;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.TestingFinder;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.outgoing.policies.StandardXmlRpcPolicy;

/**
 * System tests for interacting with a hub via xml-rpc
 * @author jdt
 * TODO these tests are currently failing due to errors in the plastic library
 *
 */
public class XMLRPCListenerIntegrationTest extends AbstractPlasticBaseNotDeaf {

    protected PlasticHubListener getHub() throws Exception {
        ACR acr = new TestingFinder().find();
        //Make sure that the hub is loaded and started
        PlasticHubListener listener = (PlasticHubListener) acr.getService(PlasticHubListener.class); 
        //We actually want to return a proxy to this, that does all the comms by xmlrpc.
        StandardXmlRpcPolicy policy = new StandardXmlRpcPolicy();
        PlasticHubListener hub = policy.getHub();
        if (hub==null) throw new IOException("No hub is running");
        return hub;
    }

    protected TestPlasticApplication getApplication(Properties appData, MessageHandler handler) {
        return new TestListenerXMLRPC(appData, handler);
    }

    /**
     * Deliberately override this test and do a nop, since there's a bug
     * in the plastic library.
     * TODO - restore this test when the plastic lib is fixed.
     *
     */
    public void testHardened() {
        System.out.println("testHardened has been deliberately removed for the XMLRPC protocol.");
}
}
