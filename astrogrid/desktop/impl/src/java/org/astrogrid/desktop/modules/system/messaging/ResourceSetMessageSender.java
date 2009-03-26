/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.util.List;

/** Message to exchange a resource set.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:20:38 AM
 */
public interface ResourceSetMessageSender extends MessageSender {
    
    void sendResourceSet(List<URI> resourceList, String setName);

}
