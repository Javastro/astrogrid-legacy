/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.util.List;

/** Unmarshall the representation of a message, and invoke the associated
 * handler.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 20097:29:27 PM
 */
public interface MessageUnmarshaller<S extends MessageSender> {
    /** unmarshal the raw inputs and pass into the handler */
    public Object handle(ExternalMessageTarget source,List rawInputs,S handler) throws Exception;
}
