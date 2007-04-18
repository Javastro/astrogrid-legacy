/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import javax.swing.JComponent;

/** Abstract class that represents a single instance of 
 * a particular view.
 * provides access to the components that make up this view, thile this
 * object itself performs the controlling functions for this view.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 2, 200711:59:20 PM
 */
public abstract class AbstractView {
	
	protected AbstractView(ExplorerWindow parent) {
		this.parent = parent;
	}
	private final ExplorerWindow parent;
	public ExplorerWindow getParent() {
		return parent;
	}
	/** name of the view - human readable, and unique */
	public abstract String getName();
	/** get the component to place in the outlook bar */
	public abstract JComponent getHierarchiesPanel();
	/** get the component to place in the main panel - may be null */
	public abstract JComponent getMainPanel();
	/** get the component to place in the menubar location  - maybe null */
	public abstract JComponent getMainButtons();
	
	/** called by the view host to notify this view when it is visible */
	public abstract void setVisible(boolean b);
}
