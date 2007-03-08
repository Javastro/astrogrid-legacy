/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.BorderLayout;
import java.awt.datatransfer.Transferable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections.IteratorUtils;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.dialogs.registry.RegistryGooglePanel;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.voexplorer.resource.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.voexplorer.resource.folders.ResourceFoldersView;
import org.astrogrid.desktop.modules.ui.voexplorer.tasks.TaskAggregate;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ListSelection.Listener;

import com.l2fprod.common.swing.JTaskPane;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 3, 200712:06:37 AM
 */
public class RegistryView extends ViewController implements ListSelectionListener, Listener {
	private static final String RESOURCES_VIEW = "Resources";
	/**
	 * 
	 */
	public RegistryView(UIComponent parent, EventList foldersList
			, RegistryGooglePanel regChooser
			, IterableObjectBuilder taskPaneBuilder) {
	    
		this.regChooser = regChooser;
		this.regChooser.parent.set(parent);
	    // attach ourself to this reg chooser, to listen for selection changes.
	    regChooser.getListSelection().addSelectionListener(this); // listen to selection changes
	    regChooser.getCurrentResourceModel().addListSelectionListener(this); // listen to currently selected resource
		

	    this.resourcesFolders = new ResourceFoldersView(foldersList,parent);
	    resourcesFolders.addListSelectionListener(this);
	    JPanel buttons = resourcesFolders.getControlPanel();
	    hierarchyPanel = new JPanel();
	    hierarchyPanel.setName(RESOURCES_VIEW); // names are used as keys to display appropriate content in main pane.
	    hierarchyPanel.setLayout(new BorderLayout());
	    hierarchyPanel.add(resourcesFolders,BorderLayout.CENTER);
	    hierarchyPanel.add(buttons,BorderLayout.SOUTH);
	    hierarchyPanel.setBorder(BorderFactory.createEmptyBorder());
	    
	    scopeActions =new JTaskPane();
	    
	    // create the groups for this pane.
	    groups = (TaskAggregate[]) IteratorUtils.toArray(taskPaneBuilder.creationIterator(),TaskAggregate.class);
	    // add the groups in.
	    for (int i = 0; i < groups.length; i++) {
	    	groups[i].uiParent.set(parent);
	    	scopeActions.add(groups[i]);
	    	final JMenu m = groups[i].createMenu();
	    	if (m != null) {
	    		regChooser.getPopup().add(m);
	    	}
	    }
	    scopeActions.revalidate();
	    scopeActions.repaint();	 
	    
	    
	    
	}
	
	 private final TaskAggregate[] groups;
	private final ResourceFoldersView resourcesFolders;
	private final JTaskPane scopeActions;
    private final RegistryGooglePanel regChooser;	
    private final JPanel hierarchyPanel;
	
	public JComponent getActionsPanel() {
		return scopeActions;
	}

	public JComponent getHierarchiesPanel() {
		return hierarchyPanel;
	}

	public JComponent getMainPanel() {
		return regChooser;
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
	private void notifyResourceTasks() {
		Transferable tran = this.regChooser.getSelectionTransferable();
		if (tran == null) {
			for (int i = 0; i < groups.length; i++) {
				groups[i].noneSelected();
			}
		} else {
			for (int i = 0; i < groups.length ; i++) {
				groups[i].selected(tran);
			}
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
}
