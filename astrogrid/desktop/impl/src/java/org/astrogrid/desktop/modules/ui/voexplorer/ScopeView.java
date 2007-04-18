/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import javax.swing.JComponent;

/** View for spatial / temporal search - astroscope replacement.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 200710:49:23 AM
 */
public class ScopeView extends AbstractView{

	public ScopeView(ExplorerWindow parent) {
		super(parent);
	}
	public JComponent getHierarchiesButtons() {
		return null;
	}

	public JComponent getHierarchiesPanel() {
		return null;
	}

	public JComponent getMainButtons() {
		return null;
	}

	public JComponent getMainPanel() {
		return null;
	}

	public String getName() {
		return SPATIAL_VIEW;
	}

	private static final String SPATIAL_VIEW = "Space & Time";
	
	public void setVisible(boolean b) {
	}

}
