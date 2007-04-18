/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.datatransfer.Transferable;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections.IteratorUtils;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.voexplorer.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.voexplorer.tasks.MyTaskPaneGroup;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ListSelection.Listener;

import com.l2fprod.common.swing.JTaskPane;

/** View for the registry explorer.
 * 
 * Uses the work that's already been done with the RegistryGooglePanel,
 * and combines this with a 'bookmarks' view and set of actions.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 3, 200712:06:37 AM
 */
public class RegistryView extends AbstractView implements ListSelectionListener, Listener {
	private static final String RESOURCES_VIEW = "Resources";
	/**
	 * 
	 */
	public RegistryView(ExplorerWindow parent, EventList foldersList
			, RegistryGooglePanel regChooser) {
		super(parent);
	// resources folders.
		this.resourcesFolders = new ResourceFoldersList(foldersList,parent);
		resourcesFolders.addListSelectionListener(this);
		resourcesFolders.setName(RESOURCES_VIEW);
	    
	// main view.
		this.regChooser = regChooser;
		this.regChooser.parent.set(parent);
	    // attach ourself to this reg chooser, to listen for selection changes.
	    regChooser.getListSelection().addSelectionListener(this); // listen to selection changes
	    regChooser.getCurrentResourceModel().addListSelectionListener(this); // listen to currently selected resource
		regChooser.setPopup(getParent().getPopupMenu());
	    mainButtons = regChooser.getToolbar();

	}
	private final ResourceFoldersList resourcesFolders;
    private final RegistryGooglePanel regChooser;	
    private final JComponent mainButtons; 


	public JComponent getHierarchiesPanel() {
		return resourcesFolders;
	}

	public JComponent getMainPanel() {
		return regChooser;
	}


	public JComponent getHierarchiesButtons() {
	return null;
	}

	public JComponent getMainButtons() {
		return mainButtons;
	}
	
	public String getName() {
		return RESOURCES_VIEW;
	}

	   //TODO need to merge these two together somehow.
	   // need to work out what kind of selection I should be respondiong to.
	   // for the demo, just use the 'value Changed' one.
	   /** called when selected items change. - not used. might as well remove this feature. */
	public void selectionChanged(int arg0, int arg1) {
		notifyResourceTasks();
	}

	/**
	 * 
	 */
	public void notifyResourceTasks() {
		Transferable tran = this.regChooser.getSelectionTransferable();
		if (tran == null) {
			getParent().clearSelection();
		} else {
			getParent().setSelection(tran);
		}
	}
	// listens to clicks on resource Folders and registry google.
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		if (e.getSource() == resourcesFolders) {
			ResourceFolder f =  (ResourceFolder)resourcesFolders.getSelectedValue();
			if (f != null) { 
				getParent().clearSelection();
				f.display(this.regChooser);
			}
		} else { // assume it's the table then.
			notifyResourceTasks();
		}

	}

	/** called to display a specific set of resouces in this view.
	 * @param uriList
	 */
	public void displayResources(List uriList) {
		regChooser.displayIdSet(uriList);
	}

	// called when visibility of this view changes.
	public void setVisible(boolean b) {
		resourcesFolders.setEnabled(b);
		regChooser.setEnabled(b);
		mainButtons.setEnabled(b);
		notifyResourceTasks();
	}
}
