/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.hivemind.service.ObjectProvider;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** View for storage.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 3, 200712:17:50 AM
 */
public class StorageView extends ViewController {
	private static final String STORAGE_VIEW = "Storage";
	/**
	 * 
	 */
	public StorageView(UIComponent parent) {
		folders = new JLabel("storage roots (myspacce, vospace, file) and folders to go here");
	    folders.setName(STORAGE_VIEW);

	    actions =  new JLabel("Actions on files to go here");
	    main =  new JLabel("myspace / file / vospace browser to go here");

	}
	private final JComponent  folders;
	private final JComponent actions;
	private final JComponent main;
	
	public JComponent getActionsPanel() {
		return actions;
	}

	public JComponent getHierarchiesPanel() {
		return folders;
	}

	public String getName() {
		return STORAGE_VIEW;
	}

	public JComponent getMainPanel() {
		return main;
	}

}
