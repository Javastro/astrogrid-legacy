/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.voexplorer.folders.DumbResourceFolder;
import org.astrogrid.desktop.modules.ui.voexplorer.folders.FilterResourceFolder;
import org.astrogrid.desktop.modules.ui.voexplorer.folders.QueryResourceFolder;
import org.astrogrid.desktop.modules.ui.voexplorer.folders.ResourceFolder;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventListModel;

/** View and controller for a set of resource 'playlists' - both smart and hand-picked.
 * 
@todo estimate and display size of each folder.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20077:15:55 PM
 */
public class ResourceFoldersList extends JList implements DropTargetListener, ListSelectionListener, ActionListener, MouseListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(ResourceFoldersList.class);

/**
 * 
 * @param folderList an event list of {@link ResourceFolder}
 * @param parent the ui component to report background tasks to.
 */
public ResourceFoldersList(EventList folderList, UIComponent parent) {
	this.parent = parent;
	CSH.setHelpIDString(this, "resourceFolders");	
	setDropTarget(new DropTarget(this,this));
	setBorder(EMPTY_BORDER);
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
	add = new JMenuItem("New",IconHelper.loadIcon("editadd16.png"));
	add.setToolTipText("Add a new resource list or smart resource list");
	add.addActionListener(this);
	
	remove = new JMenuItem("Delete",IconHelper.loadIcon("editremove16.png"));
	remove.setToolTipText("Remove a resouce list");
	remove.setEnabled(false);
	remove.addActionListener(this);
	
	properties = new JMenuItem("Properties",IconHelper.loadIcon("edit16.png"));
	properties.setToolTipText("Edit a resouce list");
	properties.setEnabled(false);
	properties.addActionListener(this);
	popup = new JPopupMenu();
	popup.add(add);
	popup.add(remove);
	popup.add(properties);
	
	addMouseListener(this);

}

private final JMenuItem add;
private final JMenuItem remove;
private final JMenuItem properties;
private final JPopupMenu popup;
private final UIComponent parent;
private final EventList folderList;
public static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
public static final Border DROP_BORDER = BorderFactory.createBevelBorder(BevelBorder.LOWERED);


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
		this.setBorder(DROP_BORDER);
	}
}

public void dragExit(DropTargetEvent dte) {
	setBorder(EMPTY_BORDER);
}

public void dragOver(DropTargetDragEvent dtde) {
	// unused
}

public void dropActionChanged(DropTargetDragEvent dtde) {
	// unused
}
public void drop(DropTargetDropEvent dtde) {
	setBorder(EMPTY_BORDER);
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
			final String newName = JOptionPane.showInputDialog(this,"Rename this list",f.getName());
			if (newName == null) {
				return;
			}
			f.setName(newName);
			if (f instanceof FilterResourceFolder) {
				((FilterResourceFolder)f).setFilter(JOptionPane.showInputDialog(this,"Edit the filter",	((FilterResourceFolder)f).getFilter()));
				
			}
			// although we're aliasing, do this to notify the folder list that things have changed.
			folderList.set(ix,f);
		}
	}
}
// listen for popup menu trigger.
public void mouseClicked(MouseEvent e) {
}

public void mouseEntered(MouseEvent e) {
}

public void mouseExited(MouseEvent e) {
}

public void mousePressed(MouseEvent e) {
	checkForTriggerEvent( e );
}

public void mouseReleased(MouseEvent e) {
	checkForTriggerEvent( e);
}

// determine whether event should trigger popup menu
private void checkForTriggerEvent( MouseEvent event ){
   if ( event.isPopupTrigger() ) {
      popup.show( event.getComponent(),
         event.getX(), event.getY() );
   }
}


}
