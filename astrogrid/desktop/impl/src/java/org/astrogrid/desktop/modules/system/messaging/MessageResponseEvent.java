/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/** Event fired to indicate a message response.
 * @see MessageResponseListener
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:05:23 AM
 * @implement
 */
public class MessageResponseEvent {
    
    
    /**
     * @return the msgID
     */
    public MessageID getMsgID() {
        return this.msgID;
    }

    /**
     * @param msgID the id of the message this is a response to.
     */
    public MessageResponseEvent(final MessageID msgID) {
        super();
        this.msgID = msgID;
    }

    private final MessageID msgID;

}
