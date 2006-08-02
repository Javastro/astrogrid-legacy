/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.Properties;

import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;

public class TestListenerRMI extends AbstractTestListener implements TestPlasticApplication, PlasticListener {

    public URI registerWith(PlasticHubListener hub, String name) {
        return hub.registerRMI(name, getMessages(), this);
    }

    public TestListenerRMI(Properties metaData) {
       super(metaData,"RMI");

    }



    
}