/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.util.List;

import org.astrogrid.samp.Response;

/** Message to exchange a resource set.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:20:38 AM
 */
public interface ResourceSetMessageSender extends MessageSender {
    
    Response sendResourceSet(List<URI> resourceList, String setName);

}
