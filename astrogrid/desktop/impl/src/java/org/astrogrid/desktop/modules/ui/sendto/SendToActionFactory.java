/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

/** Interface to a factory for send-to-actions
 * facory useful when list of actions is unknown at compile time, or varies at runtime.
 * @author Noel Winstanley
 * @since Jun 22, 20061:31:48 AM
 */
public interface SendToActionFactory {

	/** sets the menu this factory is to add items to 
	 * @param m the menu to add to
	 * @param startingPoint- separator after which to add items.*/
	void setMenu(JPopupMenu m, JSeparator startingPoint);
}
