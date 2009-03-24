/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

/** Implementation of messaging that only supports plastic.
 * Relies on tupperwareImpl being around to do the heavy lifting.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 13, 20096:52:11 PM
 */
public class PlasticOnlyMessagingImpl implements Messaging {

    private final EventList<ExternalMessageTarget> targetList = new BasicEventList<ExternalMessageTarget>();
    
    public EventList<ExternalMessageTarget> getTargetList() {
        return targetList;
    }

}
