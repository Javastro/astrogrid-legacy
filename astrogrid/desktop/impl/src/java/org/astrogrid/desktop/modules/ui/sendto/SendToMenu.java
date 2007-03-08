/**
 * 
 */
package org.astrogrid.desktop.modules.ui.sendto;

import java.awt.Component;

import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.dnd.PreferredTransferable;


/** interface to a a shared 'send-to' menu component - used to pass data to other 
 * uitilies for vieing.
 * @author Noel Winstanley
 * @since Jun 19, 20062:30:08 PM
 */
public interface SendToMenu {

	/** display the popup menu, customized to one particular atom of data, and process user requests
	 * 
	 * @param atom the obejct the menu is going to operate upon.
	 * @param parent the UI component in which to report background task progress.
	 * @param invoker the conponent the menu has been invoke on. Assumed to be 
	 * contained within <tt>parent</tt>
	 * @param x the x position to display the menu
	 * @param y the y position to display the menu
	 */
	void show(PreferredTransferable atom, UIComponent parent, Component invoker, int x, int y);
	// individual kinds of menu action.

}