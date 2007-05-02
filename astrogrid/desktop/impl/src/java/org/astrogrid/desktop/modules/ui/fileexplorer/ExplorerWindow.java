/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.datatransfer.Transferable;

import javax.swing.JPopupMenu;

import org.astrogrid.desktop.modules.ui.UIComponent;

/** Extension to the standard UIComponent interface that provides 
 * interface to the additional functionality provided by the VOExplorer window.
 * 
 * This interface is passed into various View implementations - e.g. RegistryView.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 16, 20074:30:44 PM
 */
public interface ExplorerWindow extends UIComponent {
	
	
	/** notify the explorer of the current selection - represented as a transferable */
	public void setSelection(Transferable trans);
	
	/** clear the current selection */
	public void clearSelection();
	
	
	/** access the popup menu, populated with items based on the current selection */
	JPopupMenu getPopupMenu();

}
