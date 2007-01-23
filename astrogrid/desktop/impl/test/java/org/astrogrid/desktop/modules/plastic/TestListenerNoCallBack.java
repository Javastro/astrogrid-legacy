/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.votech.plastic.CommonMessageConstants;
import org.votech.plastic.PlasticHubListener;
import org.votech.plastic.PlasticListener;
import org.votech.plastic.incoming.handlers.MessageHandler;
/**
 * Not really a listener at all...since I don't listen.
 * @author jdt
 *
 */
public class TestListenerNoCallBack  implements TestPlasticApplication, PlasticListener {
    private String name = "Deaf";
    public TestListenerNoCallBack(Properties appData) {
        if (appData!=null) {
            this.name = appData.getProperty(TestPlasticApplication.NAME, this.name);
        } 
    }

    public URI registerWith(PlasticHubListener hub) {
        return hub.registerNoCallBack(getName());
    }
    private List empty = new ArrayList();
    public List getMessages() {
        return empty;
    }

    public void appendHandler(MessageHandler h) {
        // do nothing
        
    }

    public Object perform(URI arg0, URI arg1, List arg2) {
        // do nothing
        return CommonMessageConstants.RPCNULL;
    }

    public boolean isDeaf() {
        return true;
    }

    public String getName() {
        return name;
    }

    
    



    
}