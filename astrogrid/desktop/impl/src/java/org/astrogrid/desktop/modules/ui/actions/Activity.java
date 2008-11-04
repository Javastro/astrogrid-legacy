/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.datatransfer.Transferable;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.astrogrid.desktop.modules.ui.UIComponent;

import com.l2fprod.common.swing.JTaskPaneGroup;

/** An activity is an operation that the user can invoke on the current selection.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 16, 20076:30:28 PM
 */
public interface Activity extends Action {

	/** called by owning group when there's no current selection */
	public void noneSelected();

	/** tests whether 
	 protected abstract boolean invokable(Resource r);
	 /** called by owning group when the selection changes
	 * it's up to the ResourceTask itself to inspect the list and determine
	 * whether it's applicable or not.
	 * 
	 * it's also responsible for holding onto the resources that it's 
	 * able to operate over (might be a subset of all the resources). 
	 * so that these are available when actionPerformed() is called.
	 */

	public void selected(Transferable t);

	
	// methods to add to UI components
	/** add this activity to a task pane */
	public void addTo(JTaskPaneGroup grp);
	
	/** add this activity to a task pane in the specified position */
	public void addTo(JTaskPaneGroup grp, int pos);
	
	/** add this activity to a menu */
	public void addTo(JMenu menu);
	/** add this activity to a popup menu */
	public void addTo(JPopupMenu menu);

	
	/** set the ui parent - used for task reporting, etc */
	public void setUIParent(UIComponent up);

	/** marker inteface for activities which should appear in the 'info' section */
	public interface Info {
	}
	
	/** marker interface for activities that shouldn't appear in the context menu */
	public interface NoContext {
	}
	/** marker interface for activites that shouldn't appear in the task pane */
	public interface NoTask {
	}
}