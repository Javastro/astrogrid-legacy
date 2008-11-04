/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.datatransfer.Transferable;
import java.util.Iterator;

import javax.swing.JPopupMenu;

import org.astrogrid.desktop.modules.ui.actions.Activity;

/** Manages a set of activities.
 * Adjusts  their activation state, etc.
 * and also provides access to them so that they can be added to menus, etc.
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

    
    /** access the last value passed to setSelection() 
     * will be null if clearSelection() has since been called.
     * 
     */
    public Transferable getCurrentSelection();
    

    /** retrieve a named activity from the manager. */
    public Activity getActivity(Class activityClass);
 
    /** iterate through all the acitivites contained in this manager */
    public Iterator iterator();
    
    /** access a shared popup menu that provides operations over the current
     * selection 
     * 
     * @return
     */
    public JPopupMenu getPopupMenu();
    
    
    /** acccess a task pane that lists the available activities */
    public com.l2fprod.common.swing.JTaskPane getTaskPane();

}