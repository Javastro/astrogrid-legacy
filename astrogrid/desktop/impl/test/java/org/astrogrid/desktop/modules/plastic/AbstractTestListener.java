/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.List;
import java.util.Properties;

import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.EchoHandler;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;

/**
 * Generic test listener, to be subclassed by xml-rpc, rmi versions etc.
 * @author jdt
 *
 */
public abstract class AbstractTestListener implements TestPlasticApplication {

    public AbstractTestListener(Properties metaData, String comms) {
        if (metaData==null) metaData = new Properties();
        final String name = metaData.getProperty(TestPlasticApplication.NAME,"TestListener"+comms);
        final String desc = metaData.getProperty(TestPlasticApplication.DESC,"A testing client that uses "+comms+" for comms");
        final String ivorn = metaData.getProperty(TestPlasticApplication.IVORN,"");
        final String logoUrl = metaData.getProperty(TestPlasticApplication.LOGOURL,"");
        final String version = metaData.getProperty(TestPlasticApplication.VERSION,PlasticListener.CURRENT_VERSION);
        
        
        handler = new EchoHandler();
        MessageHandler handler1= new StandardHandler(name,desc,ivorn,logoUrl,version);
        handler.setNextHandler(handler1);
    }

    /**
     * The usual plastic client method.
     * @param sender
     * @param message
     * @param args
     * TODO this whole class will need an overhaul when we update xml-rpc libs.  In particular, the Vector should => a List
     */
    public Object perform(URI sender, URI message, List args) {
        return handler.perform(sender, message, args);
    }

    protected MessageHandler handler;

    /** Add a new message handler to the start of the chain */
    public synchronized void addHandler(MessageHandler h) {
        h.setNextHandler(handler);
        handler = h;
    }

    public List getMessages() {
        return handler.getHandledMessages();
    }

    /* (non-Javadoc)
     * @see org.astrogrid.desktop.modules.plastic.TestPlasticApplication#isDeaf()
     */
    public boolean isDeaf() {
        return false;
    }

}
