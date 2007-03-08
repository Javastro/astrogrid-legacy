/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.resource.folders;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.system.UIImpl;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.io.FileList;
import ca.odell.glazedlists.io.GlazedListsIO;
import ca.odell.glazedlists.swing.EventListModel;

/** View and controller for a set of resource 'playlists' - both smart and hand-picked.
 * 
 * requirements - smart folders and hand-picked.
 * 				- indicate expected size of each folder?
 * 				- drag and drop
 * 				- add new items
 * 				- delete items
 * 				- persist list contents.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20077:15:55 PM
 */
public class ResourceFoldersView extends JList implements DropTargetListener, ListSelectionListener, ActionListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(ResourceFoldersView.class);

/**
 * 
 */
public ResourceFoldersView(EventList folderList, UIComponent parent) {
	this.parent = parent;
	CSH.setHelpIDString(this, "resourceFolders");	
	setDropTarget(new DropTarget(this,this));
	setBorder(emptyBorder);
	this.folderList = folderList;
	
	setModel(new EventListModel(folderList));
	setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	setSelectedIndex(0);
	addListSelectionListener(this);
	setCellRenderer(new DefaultListCellRenderer() {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			ResourceFolder f = (ResourceFolder)value;
			l.setText(f.getName());
			l.setIcon(f.getIcon());
			return l;
		}
	});
	// build the button bar.
	add = new JButton(IconHelper.loadIcon("editadd16.png"));
	add.setToolTipText("Add a new resource list or smart resource list");
	add.addActionListener(this);
	
	remove = new JButton(IconHelper.loadIcon("editremove16.png"));
	remove.setToolTipText("Remove a resouce list");
	remove.setEnabled(false);
	remove.addActionListener(this);
	
	properties = new JButton(IconHelper.loadIcon("edit16.png"));
	properties.setToolTipText("Edit a resouce list");
	properties.setEnabled(false);
	properties.addActionListener(this);
	buttonPanel = new JPanel(new FlowLayout());
	CSH.setHelpIDString(buttonPanel, "resourceFolders.buttons");
	buttonPanel.add(add);
	buttonPanel.add(remove);
	buttonPanel.add(properties);

}

private final JButton add;
private final JButton remove;
private final JButton properties;
private final JPanel buttonPanel;
private final UIComponent parent;
private final EventList folderList;
protected static Border emptyBorder = BorderFactory.createEmptyBorder();
protected static Border dropBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);

/** access a control panel of buttons that manipulate this list */
public JPanel getControlPanel() {
	return buttonPanel;
}

private final static DataFlavor[] inputFlavors = new DataFlavor[]{
	VoDataFlavour.LOCAL_RESOURCE_LIST
	,VoDataFlavour.LOCAL_RESOURCE
	,VoDataFlavour.RESOURCE_LIST
	,VoDataFlavour.RESOURCE
	//@todo add in these types later.
//	,VoDataFlavour.LOCAL_URI_LIST
//	,VoDataFlavour.URI_LIST
//	,VoDataFlavour.LOCAL_URI
//	,VoDataFlavour.URI
};

private final static Predicate dragPredicate = new Predicate() {
	public boolean evaluate(Object arg0) {
		return ArrayUtils.contains(inputFlavors,arg0);
	}
};

// methods of drop targeet listener.
public void dragEnter(DropTargetDragEvent dtde) {
	if (CollectionUtils.exists(dtde.getCurrentDataFlavorsAsList(),dragPredicate)) {
		dtde.acceptDrag(DnDConstants.ACTION_LINK);
		this.setBorder(dropBorder);
	}
}

public void dragExit(DropTargetEvent dte) {
	setBorder(emptyBorder);
}

public void dragOver(DropTargetDragEvent dtde) {
	// unused
}

public void dropActionChanged(DropTargetDragEvent dtde) {
	// unused
}
public void drop(DropTargetDropEvent dtde) {
	setBorder(emptyBorder);
	Transferable t = dtde.getTransferable();
	if (CollectionUtils.exists(dtde.getCurrentDataFlavorsAsList(),dragPredicate)) {
		dtde.acceptDrop(DnDConstants.ACTION_LINK);
		try {
			List resources;
			if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE_LIST)) {
				resources = (List)t.getTransferData(VoDataFlavour.LOCAL_RESOURCE_LIST);
			} else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE)) {
				resources = Collections.singletonList(t.getTransferData(VoDataFlavour.LOCAL_RESOURCE));
			} else if (t.isDataFlavorSupported(VoDataFlavour.RESOURCE_LIST)){
				resources = (List)t.getTransferData(VoDataFlavour.RESOURCE_LIST);				
			} else {
				resources = Collections.singletonList(t.getTransferData(VoDataFlavour.RESOURCE));				
			}
			// now need to work out which item resources have been dropped upon...
			Point p = dtde.getLocation();
			int closestIndex = locationToIndex(p);
			if (closestIndex != -1 &&  getCellBounds(closestIndex,closestIndex).contains(p)  && folderList.get(closestIndex) instanceof DumbResourceFolder) {
				// dropped on an existing dumb folder
				DumbResourceFolder f = (DumbResourceFolder) folderList.get(closestIndex);
				for (int i = 0; i < resources.size(); i++) {
					f.getResourceSet().add(((Resource)resources.get(i)).getId());
					// do this to force update.
					folderList.set(closestIndex,f);
				}
			} else { // dropped in space -  create new folder.
				DumbResourceFolder f = new DumbResourceFolder("new resource list");
				for (int i = 0; i < resources.size(); i++) {
					f.getResourceSet().add(((Resource)resources.get(i)).getId());
				}
				folderList.add(f);
			}
			dtde.dropComplete(true);
		} catch (Exception e) {
			dtde.dropComplete(false);
		}
	} else {
		dtde.rejectDrop();
	}
}

// listen to myself
public void valueChanged(ListSelectionEvent e) {
	if (e.getSource() != this || e.getValueIsAdjusting()) {
		return; // ignore
	}
	ResourceFolder f = (ResourceFolder) getSelectedValue();
	remove.setEnabled(f != null && ! f.isFixed());
	properties.setEnabled(f != null && ! f.isFixed());
}

// called when button is pressed
public void actionPerformed(ActionEvent e) {
	if (e.getSource() == add) {
		Object[] vals = new Object[]{"static","filter","query"};
		Object v = JOptionPane.showInputDialog(this,"Which type of list to create","New resource list",JOptionPane.QUESTION_MESSAGE,null,vals,vals[0]);
		if (v == null) { 
			return;
		}
		ResourceFolder f;
		if ("filter".equals(v)) {
			f = new FilterResourceFolder();
			((FilterResourceFolder)f).setFilter(JOptionPane.showInputDialog(this,"Enter a filter","$r/vr:identifier &= 'ivo://org.astrogrid*'"));
		} else if ("query".equals(v)) {
			f = new QueryResourceFolder();
			((QueryResourceFolder)f).setQuery(JOptionPane.showInputDialog(this,"Enter a query","")); //@todo make this a textPane box.			
		} else {
			f = new DumbResourceFolder();
		}
		f.setName(JOptionPane.showInputDialog(this,"Choose a name for this list","smart".equals(v) ? "new smart list" : "new resource list"));
		folderList.add(f);
	} else if (e.getSource() == remove) {
		ResourceFolder f = (ResourceFolder)getSelectedValue();
		if (f != null) {
			folderList.remove(f);
			setSelectedIndex(0);
		}
	} else if(e.getSource() == properties) {
		ResourceFolder f = (ResourceFolder)getSelectedValue();
		int ix = getSelectedIndex();
		if (f != null) {
				f.setName(JOptionPane.showInputDialog(this,"Rename this list",f.getName()));
			if (f instanceof FilterResourceFolder) {
				((FilterResourceFolder)f).setFilter(JOptionPane.showInputDialog(this,"Edit the filter",	((FilterResourceFolder)f).getFilter()));
				
			}
			// although we're aliasing, do this to notify the folder list that things have changed.
			folderList.set(ix,f);
		}
	}
}



}
