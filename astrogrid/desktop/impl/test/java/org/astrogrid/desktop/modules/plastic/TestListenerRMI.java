/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.Properties;

import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.MessageHandler;

public class TestListenerRMI extends AbstractTestListener implements TestPlasticApplication, PlasticListener {

    public URI registerWith(PlasticHubListener hub) {
        return hub.registerRMI(getName(), getMessages(), this);
    }

    public TestListenerRMI(Properties metaData, MessageHandler handler) {
       super(metaData,"RMI", handler);

    }

   



    
}