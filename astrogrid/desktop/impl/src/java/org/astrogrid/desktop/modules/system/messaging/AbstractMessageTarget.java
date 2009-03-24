/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.util.Set;

/** Abstract class for a message target.
 * provides storage for message types, and implements basic methods.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 16, 200910:28:32 AM
 */
public abstract class AbstractMessageTarget implements MessageTarget {

    private final Set<MessageType<?>> mtypes;
    
    /**
     * @param mtypes
     */
    public AbstractMessageTarget(final Set<MessageType<?>> mtypes) {
        super();
        this.mtypes = mtypes;
    }

    public final Set<MessageType<?>> acceptedMessageTypes() {
        return mtypes;
    }

    public final boolean accepts(final MessageType<?> type) {
        return mtypes.contains(type);
    }


    
}
