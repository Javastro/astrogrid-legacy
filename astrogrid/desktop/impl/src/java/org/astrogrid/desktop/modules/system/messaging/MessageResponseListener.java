/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.util.EventListener;

/** Listener for Message Responses.
 * @see MessageResponseEvent
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:04:04 AM
 */
public interface MessageResponseListener extends EventListener {
    public void messageResponse(MessageResponseEvent me);
}
