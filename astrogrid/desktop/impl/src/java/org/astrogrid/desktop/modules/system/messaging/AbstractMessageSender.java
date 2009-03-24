/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/** Abstract class for implementing message senders.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 15, 20093:24:04 PM
 */
public abstract class AbstractMessageSender implements MessageSender {

    /**
     * @param target
     */
    public AbstractMessageSender(final MessageTarget target) {
        super();
        this.target = target;
    }

    private final MessageTarget target;
    private ExternalMessageTarget source;
    private MessageResponseListener mrl; 
    
    
    public final void setSource(final ExternalMessageTarget source) {
        this.source = source;
    }
    
    /** access the source of this message */
    protected final ExternalMessageTarget getSource() {
        return this.source;
    }
    
    public final MessageTarget getTarget() {
        return target;
    }

    public final void setMessageResponseListener(final MessageResponseListener mrl) {
        this.mrl = mrl;
    }
    
    protected final MessageResponseListener getListener() {
        return this.mrl;
    }

}
