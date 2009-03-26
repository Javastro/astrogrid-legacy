/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import javax.swing.Action;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 25, 200910:27:22 AM
 */
public interface Samp {

    
    /** access an action that can be used to connect to a plastic hub */
    public Action connectAction();
    
    /** access an action that can be used to disconnect from a hub */
    public Action disconnectAction();
    
    /** access an action that can be used to start an internal hub */
    public Action startInternalHubAction();

    public Action showMonitorAction();
    
}
