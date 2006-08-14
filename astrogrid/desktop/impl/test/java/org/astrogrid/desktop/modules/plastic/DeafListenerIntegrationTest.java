package org.astrogrid.desktop.modules.plastic;

import java.util.Properties;

import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.votech.plastic.PlasticHubListener;

/**
 * System tests for interacting with a hub "via rmi" (actually will go in-process), with an unresponding app.
 * @author jdt
 *
 */
public class DeafListenerIntegrationTest extends AbstractPlasticBase {

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
