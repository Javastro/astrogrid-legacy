/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.datatransfer.Transferable;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

/**interface to a component that manages activities,
 * and provides access that displays these activities.
 * 
 * 
 * @see ActivityFactory
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 24, 200712:19:49 PM
 */
public interface ActivitiesManager {

    /** notify the manager than nothing has been selected */
    public void clearSelection();

    

    /** notify the manager that the following transferable is the current selection */
    public void setSelection(Transferable tran);

    /** access a shared popup menu that provides operations over the current
     * selection 
     * 
     * @return
     */
    public JPopupMenu getPopupMenu();
    
    
    /** acccess a task pane that lists the available activities */
    public com.l2fprod.common.swing.JTaskPane getTaskPane();
    
    /** access a menu that lists the available acticities */
    public JMenu getMenu();
}