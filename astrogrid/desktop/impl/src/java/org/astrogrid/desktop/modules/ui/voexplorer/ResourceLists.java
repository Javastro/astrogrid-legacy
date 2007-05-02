/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
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
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;
import org.astrogrid.desktop.modules.ui.folders.SmartList;
import org.astrogrid.desktop.modules.ui.folders.StaticList;
import org.astrogrid.desktop.modules.ui.folders.XQueryList;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventListModel;

/** View and controller for a set of resource lists - both smart and hand-picked.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20077:15:55 PM
 */
public class ResourceLists extends JList implements DropTargetListener,ActionListener, MouseListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(ResourceLists.class);

/**
 * 
 * @param folderList an event list of {@link ResourceList}
 * @param parent the ui component to report background tasks to.
 */
public ResourceLists(EventList folderList, VOExplorerImpl  parent) {
	this.parent = parent;
	CSH.setHelpIDString(this, "resourceLists");	
	setDropTarget(new DropTarget(this,this));
	setBorder(EMPTY_BORDER);
	this.folderList = folderList;
	
	setModel(new EventListModel(folderList));
	setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	setSelectedIndex(0);
	setCellRenderer(new DefaultListCellRenderer() {
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			ResourceFolder f = (ResourceFolder)value;
			if (f.isFixed()) {
				l.setText("<html><b>" + f.getName());
			} else {
				l.setText(f.getName());
			}
			l.setIcon(f.getIcon());
			return l;
		}
	});
	// build the button bar.
	add = new JMenuItem("New",IconHelper.loadIcon("editadd16.png"));
	add.setToolTipText("Create a new list or smartlist");
	add.addActionListener(this);
	
	duplicate = new JMenuItem("Duplicate",IconHelper.loadIcon("editcopy16.png"));
	duplicate.setToolTipText("Duplicate this list");
	duplicate.setEnabled(false);	
	duplicate.addActionListener(this);
	
	remove = new JMenuItem("Delete",IconHelper.loadIcon("editremove16.png"));
	remove.setToolTipText("Remove this list");
	remove.setEnabled(false);
	remove.addActionListener(this);
	
	properties = new JMenuItem("Edit",IconHelper.loadIcon("edit16.png"));
	properties.setToolTipText("Edit this list");
	properties.setEnabled(false);
	properties.addActionListener(this);
	
	popup = new JPopupMenu("Edit Lists");
	popup.add(add);
	popup.addSeparator();
	popup.add(remove);
	popup.add(properties);
	popup.add(duplicate);
	
	addMouseListener(this);
}

private final JMenuItem add;
private final JMenuItem remove;
private final JMenuItem properties;
private final JMenuItem duplicate;
private final JPopupMenu popup;
private final VOExplorerImpl parent;
private final EventList folderList;
public static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();
public static final Border DROP_BORDER = BorderFactory.createBevelBorder(BevelBorder.LOWERED);


private final static DataFlavor[] inputFlavors = new DataFlavor[]{
	VoDataFlavour.LOCAL_RESOURCE_ARRAY
	,VoDataFlavour.LOCAL_RESOURCE
	,VoDataFlavour.RESOURCE_ARRAY
	,VoDataFlavour.RESOURCE
	,VoDataFlavour.LOCAL_URI
	,VoDataFlavour.LOCAL_URI_ARRAY
	,VoDataFlavour.URI_LIST
	,VoDataFlavour.PLAIN
	,VoDataFlavour.STRING

};

private final static Predicate dragPredicate = new Predicate() {
	public boolean evaluate(Object arg0) {
		return ArrayUtils.contains(inputFlavors,arg0);
	}
};

// methods of drop targeet listener.
public void dragEnter(DropTargetDragEvent dtde) {
	if (logger.isDebugEnabled()) {	
		logger.debug("Drag entered" + dtde.getCurrentDataFlavorsAsList());

	}
	if (CollectionUtils.exists(dtde.getCurrentDataFlavorsAsList(),dragPredicate)) {
		logger.debug("Drag accepted");
		dtde.acceptDrag(DnDConstants.ACTION_LINK);
		this.setBorder(DROP_BORDER);
	}
}

public void dragExit(DropTargetEvent dte) {
	logger.debug("Drag exited");
	setBorder(EMPTY_BORDER);
}

public void dragOver(DropTargetDragEvent dtde) {
	// unused
}

public void dropActionChanged(DropTargetDragEvent dtde) {
	// unused
}
public void drop(DropTargetDropEvent dtde) {
	logger.debug("Dropping" + dtde);
	setBorder(EMPTY_BORDER);
	Transferable t = dtde.getTransferable();
	if (CollectionUtils.exists(dtde.getCurrentDataFlavorsAsList(),dragPredicate)) {
		dtde.acceptDrop(DnDConstants.ACTION_LINK);
		logger.debug("Accepting");
		try {
			if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE_ARRAY)) {
				handleResourceDrop(dtde, (Resource[])t.getTransferData(VoDataFlavour.LOCAL_RESOURCE_ARRAY));				
			} else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_RESOURCE)) {
				handleResourceDrop(dtde, new Resource[]{(Resource)t.getTransferData(VoDataFlavour.LOCAL_RESOURCE)});				
			} else if (t.isDataFlavorSupported(VoDataFlavour.RESOURCE_ARRAY)){
				handleResourceDrop(dtde, (Resource[])t.getTransferData(VoDataFlavour.RESOURCE_ARRAY));				
			} else if (t.isDataFlavorSupported(VoDataFlavour.RESOURCE)){
				handleResourceDrop(dtde, new Resource[]{(Resource)t.getTransferData(VoDataFlavour.RESOURCE)});
			} else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI)) {
				handleUriDrop(dtde,new URI[]{(URI)t.getTransferData(VoDataFlavour.LOCAL_URI)});
			} else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI_ARRAY)) {
				handleUriDrop(dtde,(URI[])t.getTransferData(VoDataFlavour.LOCAL_URI_ARRAY));
			} else if (t.isDataFlavorSupported(VoDataFlavour.URI_LIST)) {
				URI[] ids = convertUnknownToUriList(t.getTransferData(VoDataFlavour.URI_LIST));
				handleUriDrop(dtde,ids);				
			} else if (t.isDataFlavorSupported(VoDataFlavour.PLAIN)) {
				URI[] ids = convertUnknownToUriList(t.getTransferData(VoDataFlavour.PLAIN));
				handleUriDrop(dtde,ids);
			} else if ( t.isDataFlavorSupported(VoDataFlavour.STRING)) {
				URI[] ids = convertUnknownToUriList(t.getTransferData(VoDataFlavour.STRING));
				handleUriDrop(dtde,ids);				
			} 

		} catch (Exception e) {
			logger.debug("Drop failed",e);
			dtde.dropComplete(false);
		}
	} else {
		logger.debug("rejecting");
		dtde.rejectDrop();
	}
}

/** try and convert something unknown into a list of URIs.
 * assume it's going to be a stream / reader / string, with a uri on each line.
 * @param o
 * @return a list of the resource ids we've managed to parse from it. Will be ivo:// format - but not guaranteed that they 
 * point to actual resources.
 * @throws IOException 
 */
private URI[] convertUnknownToUriList(Object o) throws InvalidArgumentException, IOException{
	if (o == null) {
		throw new InvalidArgumentException("null dropped - how odd");
	}
	BufferedReader r = null;
	if (o instanceof String) {
		r = new BufferedReader(new StringReader((String)o));
	} else if (o instanceof InputStream ) {
		r = new BufferedReader(new InputStreamReader((InputStream)o));
	} else if (o instanceof Reader) {
		r = new BufferedReader((Reader)o);
	} else {
		throw new InvalidArgumentException("Unknow type dropped " + o.getClass().getName());
	}
	try {
		List result = new ArrayList();
		String line;
		while ((line = r.readLine()) != null) {
			try {
				URI u = new URI(line);
				if (! u.getScheme().equals("ivo")) { // we only want ivo uris.
					continue;
				}
				result.add(u);
			} catch (URISyntaxException e) {
				logger.debug("Dropping " + line);
			}
		}
		if (result.isEmpty()) {
			throw new InvalidArgumentException("No resource ids found");
		}
		return (URI[])result.toArray(new URI[result.size()]);
	} finally {
		if (r != null) {
			try {
				r.close();
			} catch (IOException e) {
				//netch
			}
		}
	}
}

/** build a new folder from a dropped list of resources */
private void handleResourceDrop(DropTargetDropEvent dtde, Resource[] resources) {
	URI[] uris = new URI[resources.length];
	for (int i = 0; i < resources.length; i++) {
		uris[i] = resources[i].getId();
	}
	handleUriDrop(dtde,uris);
}

/** build a new folder from a list of dropped uris.
 * @param dtde
 * @param ids
 */
private void handleUriDrop(DropTargetDropEvent dtde, URI[] ids) {
	if (ids == null || ids.length == 0) {
		logger.debug("Empty ids list passed in - bailing out here");
		return;
	}
	// now need to work out which item resources have been dropped upon...
	Point p = dtde.getLocation();
	int closestIndex = locationToIndex(p);
	if (closestIndex != -1 &&  getCellBounds(closestIndex,closestIndex).contains(p)  
			&& folderList.get(closestIndex) instanceof StaticList) {
		// dropped on an existing dumb folder
		StaticList f = (StaticList) folderList.get(closestIndex);
		for (int i = 0; i < ids.length; i++) {
			f.getResourceSet().add(ids[i]);
			// do this to force update - write back to disk.
			folderList.set(closestIndex,f);
		}
	} else { // dropped in space or on smartlist -  create new folder.
		StaticList f = new StaticList();
		for (int i = 0; i < ids.length; i++) {
			f.getResourceSet().add(ids[i]);
		}
		folderList.add(f);
	}
	dtde.dropComplete(true);	
}

/** either add or update the folder in the list. (causing it to be persisted to disk)*/
public void store(ResourceFolder f) {
	int ix = folderList.indexOf(f);
	if (ix != -1) {
		folderList.set(ix,f);
	} else {
		folderList.add(f);
	}
}

// called when button is pressed
public void actionPerformed(ActionEvent e) {
	ResourceFolder f = (ResourceFolder)getSelectedValue();	
	int ix = getSelectedIndex();
	if (e.getSource() == add) {
		Object[] vals = new Object[]{"List","Smart list","XQuery list"};
		Object v = JOptionPane.showInputDialog(this,"Which kind of list do you want to create?","New list",JOptionPane.QUESTION_MESSAGE,null,vals,vals[0]);
		if (v == null) { 
			return;
		}
		if ("Smart list".equals(v)) {
			f = new SmartList();
		} else if ("XQuery list".equals(v)) {
			f = new XQueryList();
		} else {
			f =  new StaticList();
		}
		f.editAsNew(parent);
	} else if (e.getSource() == remove && f != null) {
		if (f != null) {
			folderList.remove(f);
			setSelectedIndex(0);
		}
	} else if (e.getSource() == duplicate && f != null) {
		if (f != null) {
			ResourceFolder dup = duplicate(f);
			dup.setFixed(false);
			dup.setName("copy of " + dup.getName());
			folderList.add(dup);
		}		
	} else if(e.getSource() == properties && f != null) {
		f.edit(parent);
	} 
}

// expensive way to do this, but don't want to monkey with 
// clone(), etc - and at least this operation won't happen often.
private ResourceFolder duplicate(ResourceFolder f) {
	try {
	ByteArrayOutputStream os = new ByteArrayOutputStream();
	XMLEncoder enc = new XMLEncoder(os);
	enc.writeObject(f);
	enc.close();
	ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
	XMLDecoder dec = new XMLDecoder(is);
	return (ResourceFolder)dec.readObject();
	} catch (Exception e) {
		// oh well. @todo report the error to the user.
		logger.error("Failed to duplicate",e);
		return null;
	}
}

// listen for popup menu trigger.
public void mouseClicked(MouseEvent e) {
	//checkForTriggerEvent( e );	
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
   if ( event.isPopupTrigger() ) { //|| SwingUtilities.isRightMouseButton(event) ) {
	  int ix = locationToIndex(event.getPoint());
	  Rectangle cellBounds = getCellBounds(ix,ix);
	  if (cellBounds != null &&	 cellBounds.contains(event.getPoint())) {
		  ResourceFolder f = (ResourceFolder)folderList.get(ix);
		  tailorActionsToResource(f);
	  } else {
		  tailorActionsToResource(null);
	  }
      popup.show( event.getComponent(),
         event.getX(), event.getY() );
   }
}

/** enable / disable actions depending on what's been currently selected */
private void tailorActionsToResource(ResourceFolder f) {
	remove.setEnabled(f != null && ! f.isFixed());
	properties.setEnabled(f != null && ! f.isFixed());
	duplicate.setEnabled(f != null);
}


}
