/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import javax.swing.ImageIcon;

/**
 * Opaque reference to a target (e.g. plastic application, samp message consumer) for
 * a message.
 * 
 * This interface represents an external target - it supports the same mmethods as 
 * the internal message target, plus various bits of descriptive metadata.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 12, 200912:31:28 PM
 */
public interface ExternalMessageTarget extends MessageTarget {
    /** icon used to represent the target */
    public ImageIcon getIcon();
    /** name of the target */
    public String getName();
    /** a unique identifier for the target */
    public String getId();
    /** description */
    public String getDescription();

      
    
}
