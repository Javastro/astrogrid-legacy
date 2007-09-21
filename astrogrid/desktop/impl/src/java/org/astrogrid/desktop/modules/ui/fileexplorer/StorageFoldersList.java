/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.Component;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.folders.Folder;
import org.astrogrid.desktop.modules.ui.folders.StorageFolder;
import org.astrogrid.desktop.modules.ui.voexplorer.ResourceLists;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventListModel;

/** view and controller for a set of storage 'roots' - either roots of mounted filesystems,
 * or shortcuts to favorite locations within these mounts. - add a divider between roots and 
 * favorites.. Wonder whether I should represent this as 2 lists, or a single list?
 * - single list, but add a filter over the top of it to sort items depending on type.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 200710:24:52 PM
 */
public class StorageFoldersList extends JList implements DropTargetListener, ListSelectionListener, ActionListener, MouseListener {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(StorageFoldersList.class);

	public StorageFoldersList(EventList folderList, UIComponent parent,FileSystemManager vfs) {
		this.parent = parent;
		CSH.setHelpIDString(this,"storageFolders");
		this.folderList = folderList;
		setDropTarget(new DropTarget(this,DnDConstants.ACTION_LINK,this));
		
		setBorder(ResourceLists.EMPTY_BORDER);
		setModel(new EventListModel(folderList));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setSelectedIndex(0);
		addListSelectionListener(this);
		setCellRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
				Folder f = (Folder)value;				
				l.setText(f == null ? "" : f.getName());
				l.setIcon(f == null ? null : f.getIcon());
				return l;
			}
		});	
		// build a button bar.
		add = new JMenuItem("New",IconHelper.loadIcon("editadd16.png"));
		add.setToolTipText("Add a mount or favorite folder");
		add.addActionListener(this);
		
		remove = new JMenuItem("Remove",IconHelper.loadIcon("editremove16.png"));
		remove.setToolTipText("Remove a favorite or mount");
		remove.setEnabled(false);
		remove.addActionListener(this);
		
		properties = new JMenuItem("Properties",IconHelper.loadIcon("edit16.png"));
		properties.setToolTipText("Edit a favorite or mount");
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
	private final EventList folderList;
	private final UIComponent parent;

	private final static DataFlavor[] inputFlavors = new DataFlavor[]{
		VoDataFlavour.LOCAL_FILEOBJECT
	//	,VoDataFlavour.LOCAL_FILEOBJECT_LIST don't know what to do with a multiple selection at the moment
		,VoDataFlavour.LOCAL_URI
		,VoDataFlavour.LOCAL_URL
	//	,VoDataFlavour.URL
//		,VoDataFlavour.URI_LIST
	};

	private final static Predicate dragPredicate = new Predicate() {
		public boolean evaluate(Object arg0) {
			return ArrayUtils.contains(inputFlavors,arg0);
		}
	};

	public void dragEnter(DropTargetDragEvent dtde) {
		if (CollectionUtils.exists(dtde.getCurrentDataFlavorsAsList(),dragPredicate)) {
			dtde.acceptDrag(DnDConstants.ACTION_LINK);
			this.setBorder(ResourceLists.DROP_BORDER);
		}		
	}
	public void dragExit(DropTargetEvent dte) {
		setBorder(ResourceLists.EMPTY_BORDER);		
	}
	public void dragOver(DropTargetDragEvent dtde) {
		//unused
	}
	public void drop(DropTargetDropEvent dtde) {
		setBorder(ResourceLists.EMPTY_BORDER);
		Transferable t = dtde.getTransferable();
		if (CollectionUtils.exists(dtde.getCurrentDataFlavorsAsList(),dragPredicate)) {
			dtde.acceptDrop(DnDConstants.ACTION_LINK);
			try {
				StorageFolder sf = null;
				if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT)) {
					FileObject fo = (FileObject)t.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT);
					if (fo.getType().hasChildren()) {
						sf =new StorageFolder();
						sf.setFile(fo);
						sf.setName(fo.getName().getBaseName());
						sf.setUriString(fo.getName().getURI());
					} // rejects non-folders.
				} else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI) ){
					URI u = (URI)t.getTransferData(VoDataFlavour.LOCAL_URI);
					sf = new StorageFolder();
					sf.setName(u.toString());
					sf.setUriString(u.toString());
				} else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URL)) {
					URL u = (URL)t.getTransferData(VoDataFlavour.LOCAL_URL);
					sf = new StorageFolder();
					sf.setName(u.toString());
					sf.setUriString(u.toString());					
				} else {
					logger.warn("Unknown type of transferable " + t);
				}
				if (sf != null) {
					folderList.add(sf);		
					dtde.dropComplete(true);
				} else {
					dtde.dropComplete(false);
				}
			} catch (Exception e) {
				dtde.dropComplete(false);
			}
		} else {
			dtde.rejectDrop();
		}

	}
	public void dropActionChanged(DropTargetDragEvent dtde) {
		//ignored
	}
	// listening to myself.
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() != this || e.getValueIsAdjusting()) {
			return; // ignore
		}
		Folder f = (Folder) getSelectedValue();
		remove.setEnabled(f != null);
		properties.setEnabled(f != null);		
	}
	public void actionPerformed(ActionEvent e) {
		// process add/ remove/ properties calls.
		if (e.getSource() == add) {
			StorageFolder f = new StorageFolder();
			String n = JOptionPane.showInputDialog(this,"Choos a name for this folder");
			if (n == null) {
				return;
			}
			f.setName(n);
			while(true) {
				try {
					String s = JOptionPane.showInputDialog(this,"Enter the URI for this folder");
					if (s == null) {
						return;
					}
					f.setUriString(s);
					break;
				} catch (URISyntaxException ex) {
					JOptionPane.showMessageDialog(this,ex.getMessage(),"Invalid URI",JOptionPane.ERROR_MESSAGE);
				}
			}
			folderList.add(f);
		} else if (e.getSource() == remove) {
			Folder f = (Folder)getSelectedValue();
			if (f != null) {
				folderList.remove(f);
				setSelectedIndex(0);
			}
		} else if(e.getSource() == properties) {
			StorageFolder f = (StorageFolder)getSelectedValue();
			int ix = getSelectedIndex();
			if (f != null) {
				final String newName = JOptionPane.showInputDialog(this,"Rename this folder",f.getName());
				if (newName == null) {
					return;
				}				
				f.setName(newName);
				while(true) {
					try {
						String s = JOptionPane.showInputDialog(this,"Edit the URI for this folder",f.getUriString());
						if (s == null) {
							break; // user pressed cancel - probably just wante to edit the name.
						}
						f.setUriString(s);
						break;
					} catch (URISyntaxException ex) {
						JOptionPane.showMessageDialog(this,ex.getMessage(),"Invalid URI",JOptionPane.ERROR_MESSAGE);
					}
				}				
				// although we're aliasing, do this to notify the folder list that things have changed.
				folderList.set(ix,f);
			}
		}		
	}
//	 listen for popup menu trigger.
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

//	 determine whether event should trigger popup menu
	private void checkForTriggerEvent( MouseEvent event ){
	   if ( event.isPopupTrigger() ) {
	      popup.show( event.getComponent(),
	         event.getX(), event.getY() );
	   }
	}	

}
