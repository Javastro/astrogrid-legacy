package org.astrogrid.desktop.modules.plastic;

import java.io.IOException;
import java.util.Properties;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.outgoing.policies.StandardXmlRpcPolicy;

/**
 * System tests for interacting with a hub via rmi, with an unresponding app.
 * @author jdt
 *
 */
public class DeafListenerSystemTest extends AbstractPlasticBase {

    protected PlasticHubListener getHub() throws Exception {
        ACR acr = new Finder().find();
        //Make sure that the hub is loaded and started
        PlasticHubListener listener = (PlasticHubListener) acr.getService(PlasticHubListener.class); 
        return listener;
    }

    protected TestPlasticApplication getApplication(Properties appData) {
        return new TestListenerNoCallBack();
    }

}
