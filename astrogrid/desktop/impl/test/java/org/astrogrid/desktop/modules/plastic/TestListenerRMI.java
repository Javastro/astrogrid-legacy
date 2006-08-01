/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.List;
import java.util.Properties;

import org.astrogrid.desktop.modules.plastic.AbstractPlasticBase.TestPlasticApplication;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.EchoHandler;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;

public class TestListenerRMI implements TestPlasticApplication, PlasticListener {

    private MessageHandler handler;

    public URI registerWith(PlasticHubListener hub, String name) {
        return hub.registerRMI(name, getMessages(), this);
    }

    public TestListenerRMI(Properties metaData) {
        if (metaData==null) metaData = new Properties();
        final String name = metaData.getProperty(NAME,"TestListenerRMI");
        final String desc = metaData.getProperty(DESC,"A testing client that uses rmi for comms");
        final String ivorn = metaData.getProperty(IVORN,"");
        final String logoUrl = metaData.getProperty(LOGOURL,"");
        final String version = metaData.getProperty(VERSION,PlasticListener.CURRENT_VERSION);
        
        handler = new EchoHandler();
        MessageHandler handler1= new StandardHandler(name,desc,ivorn,logoUrl,version);
        handler.setNextHandler(handler1);

    }
    public List getMessages() {
        return handler.getHandledMessages();
    }

    public void addHandler(MessageHandler h) {
        h.setNextHandler(handler);
        handler=h;
        
    }

    public Object perform(URI arg0, URI arg1, List arg2) {
        return handler.perform(arg0,arg1,arg2);
    }


    
}