/*
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.io.IOException;

import org.apache.xmlrpc.XmlRpcHandler;
import org.votech.plastic.PlasticHubListener;

/**
 *Union interface. exposes the xmlrpc handler aspect of the plastic hub impl.
 * @date 22-Nov-2005
 */
public interface PlasticHubListenerInternal extends PlasticHubListener, XmlRpcHandler{

    /** start this plastic hub */
    public void start() throws IOException;
    
}
