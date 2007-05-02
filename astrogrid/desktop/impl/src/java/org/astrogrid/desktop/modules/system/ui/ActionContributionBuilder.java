/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.actions.Activity;

import com.l2fprod.common.swing.JTaskPane;

/** populates widgets with hivemind-defined action structures.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 26, 20073:19:58 PM
 */
public interface ActionContributionBuilder {
	/**
	 * construct UI components for all the listed actions.
	 * @param parent the parent window for thee following components
	 * @param popup a popup menu that will show these actions
	 * @param actionsPanel a task pane that shows actions
	 * @param actions a menu that shows actions.
	 * @return
	 */
	public Activity[] buildActions(UIComponent parent, JPopupMenu popup, JTaskPane actionsPanel, JMenu actions);
		
}