package org.astrogrid.desktop.modules.ui.sendto;

import javax.swing.Action;

import org.astrogrid.desktop.modules.ui.UIComponent;


/** interface to an action customizd to a preferredTransferable */
public interface SendToAction extends Action {
	/** 'applies the action to a transferable - which determines wither it 
	 * it is enabled or not (if it's applicable), and sets the target object when the action is fired
	 * @param atom the value.
	 * @param comp  the component displaying the menu
	 */
	public void applyTo(PreferredTransferable atom, UIComponent comp);
	
}