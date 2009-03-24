/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.util.Set;


/** Opaque reference to a messaging target that may be internal to the application or
 * an external application.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:17:12 AM
 */
public interface MessageTarget {

    /** list the understood message types 
     * 
     * @return a list of message type classes 
     */
    public abstract Set<MessageType<?>> acceptedMessageTypes();

    /** test whether a particular message type is accepted by this message target
     * 
     * @return
     */
    public abstract boolean accepts(MessageType<?> mType);

    /** Create a message sender for the particular type that will send 
     * messages to this target. 
     * 
     * @param <M>
     * @param mType
     * @return an instance of the mType that can be used to send messages to this target.
     * @throws UnsupportedOperationException if this message type is not supprted (check using accepts first)
     */
    public abstract <S extends MessageSender> S createMessageSender(
            final MessageType<S> mType) throws UnsupportedOperationException;

    
    
    
}