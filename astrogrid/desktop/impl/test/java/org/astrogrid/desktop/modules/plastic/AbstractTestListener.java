/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.List;
import java.util.Properties;

import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.MessageHandler;
import org.votech.plastic.incoming.handlers.StandardHandler;

/**
 * Generic test listener, to be subclassed by xml-rpc, rmi versions etc.
 * @author jdt
 *
 */
public abstract class AbstractTestListener implements TestPlasticApplication {

    public AbstractTestListener(Properties metaData, String comms, MessageHandler defaultHandler) {
        if (metaData==null) metaData = new Properties();
        name = metaData.getProperty(TestPlasticApplication.NAME,"TestListener"+comms);
        final String desc = metaData.getProperty(TestPlasticApplication.DESC,"A testing client that uses "+comms+" for comms");
        final String ivorn = metaData.getProperty(TestPlasticApplication.IVORN,"");
        final String logoUrl = metaData.getProperty(TestPlasticApplication.LOGOURL,"");
        final String version = metaData.getProperty(TestPlasticApplication.VERSION,PlasticListener.CURRENT_VERSION);
        
        System.out.println("Creating a test listener:");
        System.out.println("name "+name);
        System.out.println("desc "+desc);
        System.out.println("ivorn "+ivorn);
        System.out.println("logoUrl "+logoUrl);
        System.out.println("version "+version);
        
        
        StandardHandler handler1 = new StandardHandler(name,desc,ivorn,logoUrl,version);
        if (defaultHandler==null) {
            handler =handler1;
        } else {
            handler = defaultHandler;
            handler.appendHandler(handler1);
        }
        

    }
    
    public String getName() {
        return name;
    }

    /**
     * The usual plastic client method.
     * @param sender
     * @param message
     * @param args
     */
    public Object perform(URI sender, URI message, List args) {
        return handler.perform(sender, message, args);
    }

    protected MessageHandler handler;
    private String name;

    /** Add a new message handler to the start of the chain */
    public synchronized void appendHandler(MessageHandler h) {
        h.appendHandler(handler);
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
