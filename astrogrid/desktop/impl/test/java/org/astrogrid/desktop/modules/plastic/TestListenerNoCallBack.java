/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

    public URI registerWith(PlasticHubListener hub, String name) {
        return hub.registerNoCallBack(name);
    }
    private List empty = new ArrayList();
    public List getMessages() {
        return empty;
    }

    public void addHandler(MessageHandler h) {
        // do nothing
        
    }

    public Object perform(URI arg0, URI arg1, List arg2) {
        // do nothing
        return CommonMessageConstants.RPCNULL;
    }

    public boolean isDeaf() {
        return true;
    }

    
    



    
}