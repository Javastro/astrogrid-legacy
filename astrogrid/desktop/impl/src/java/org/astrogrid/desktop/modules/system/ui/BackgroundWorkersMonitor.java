/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import org.astrogrid.desktop.modules.ui.UIComponent;

/** Progress monitor UI for background tasks.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 23, 20072:20:17 PM
 */
public interface BackgroundWorkersMonitor {
    
    /** display all running processes in the monitor
     * displaying ui if not else visible, else just tofronting.
     */
    public void showAll() ;
        
    /** show the processes not associated with any particular window
     * will display ui if not already visible.
     *  */
    public void showSystem();
    
    /** show the processes associated with a particular window 
     * will display ui if not already visible.
     * */
    public void showProcessesFor(UIComponent window);

}
