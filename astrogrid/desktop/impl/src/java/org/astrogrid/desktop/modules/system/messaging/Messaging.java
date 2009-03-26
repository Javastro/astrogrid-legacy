/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import javax.swing.JMenu;

import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;

import ca.odell.glazedlists.CompositeList;



/** Facade that hides details of various messaging systems
 *  - Plastic, SAMP, or a Plastic+SAMP hybrid
 *  All other VODesktop code should just use the facilities of this interface - not the underlying plastic, samp or tupperware classes.
 *  @todo factor out part of this into an abstract messaging API for AR. 
 *  
 *  delayed continuation used as a parent class - used for initialization / connect-to-hub code
 *  
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 12, 200911:42:02 AM
 */
public interface Messaging extends DelayedContinuation {
    
    /** event list of all known targets */
    CompositeList<ExternalMessageTarget> getTargetList();
   
    /** adds appropriate connect / disconnect messages to the interop menu */
    void populateInteropMenu(JMenu interopMenu);

    
}
