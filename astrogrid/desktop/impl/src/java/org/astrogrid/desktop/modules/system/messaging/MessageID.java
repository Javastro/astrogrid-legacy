/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

/** A unique identifier for a message.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 18, 20092:12:58 PM
 */
public class MessageID {
    
    private final MessageTarget source;
    private final long id;
    /** Get the source (sender) of this message.
     * @return the source
     */
    public MessageTarget getSource() {
        return this.source;
    }
    
    public long getId() {
            return this.id;
    }

    /**
     * @param source
     */
    public MessageID(final MessageTarget source, final long id) {
        super();
        this.id = id;
        this.source = source;
    }

}
