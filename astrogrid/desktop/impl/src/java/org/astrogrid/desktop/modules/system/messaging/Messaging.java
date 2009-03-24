/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import ca.odell.glazedlists.EventList;



/** Facade that hides details of various messaging systems
 *  - Plastic, SAMP, or a Plastic+SAMP hybrid
 *  All other VODesktop code should just use the facilities of this interface - not the underlying plastic, samp or tupperware classes.
 *  @todo factor out part of this into an abstract messaging API for AR. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 12, 200911:42:02 AM
 */
public interface Messaging {
    
    /** event list of all known targets */
    EventList<ExternalMessageTarget> getTargetList();
   
    // connect / disconnect internal and external hubs.
    // maybe should just have a 'connect' and 'disconnect' action
    // and let internal logic work out wha'ts the right thing to do.
    
    // finally - message handling.
    // not that we handle many messages really.
    // add strong-typed 'resource set received' interfaces later 
    
    
}
