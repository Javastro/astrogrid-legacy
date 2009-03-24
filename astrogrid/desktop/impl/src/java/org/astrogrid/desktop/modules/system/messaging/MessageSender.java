/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/** Abstract interface of all message types.
 * 
 * Each subclass represents a particular type of message, and also 
 * provides methods to exchange messages of this type.
 * 
 * So classes of this type represent the mssage type, while instances 
 * provide methods to actually send the message.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 200911:02:00 AM
 */
public interface MessageSender {
   
    /** set the source of this message
     * 
     *only called to provide the source when this message sender is 
     *invoked on an internal message target. 
     *  */
    void setSource(ExternalMessageTarget source);
    
    /** access the target that this instance will send mesasages to */
    MessageTarget getTarget();
    
    /** set a listener to be notified for message responses
     * only one listener can be set. re-setting overrides the previous one.
     * no need to provide a listener if we do not care about result.
     *  */
    void setMessageResponseListener(MessageResponseListener mrl);

}
