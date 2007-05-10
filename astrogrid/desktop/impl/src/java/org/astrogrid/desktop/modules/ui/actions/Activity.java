/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.datatransfer.Transferable;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.astrogrid.desktop.modules.ui.UIComponent;

import com.l2fprod.common.swing.JTaskPaneGroup;

/** An activity / set of activities that is possible upon the current selection
 * in voexplorer. 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 16, 20076:30:28 PM
 */
public interface Activity {

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
	 * @param r
	 */

	public void selected(Transferable t);

	
	// methods to add to UI components
	/** add this activity to a task pane */
	public void addTo(JTaskPaneGroup grp);
	/** add this activity to a menu */
	public void addTo(JMenu menu);
	/** add this activity to a popup menu */
	public void addTo(JPopupMenu menu);
	
	/** the logical 'section' of the UI that this activity belongs to */
	public String getSection();
	
	/** set the ui parent - used for task reporting, etc */
	public void setUIParent(UIComponent up);
	
	// some pre-defined sections - not an exhaustive list.
	public final static String USE_SECTION = "use";
	public final static String NEW_SECTION = "new";
	public final static String INFO_SECTION = "info";
	public final static String PLASTIC_SECTION = "plastic";
	public final static String SCRIPT_SECTION = "script";
	public final static String EXPORT_SECTION = "export";
	
}