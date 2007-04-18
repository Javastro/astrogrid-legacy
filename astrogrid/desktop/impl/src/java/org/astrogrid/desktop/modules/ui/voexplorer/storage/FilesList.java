package org.astrogrid.desktop.modules.ui.voexplorer.storage;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.voexplorer.StorageView;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

/** Main-pane component for StorageView that displays a list of files.
 * 
 * much more to come here
 *  -- drag and drop
 *  -- right-click menu from actions
 *  
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20071:32:10 AM
 */
public class FilesList extends JList implements MouseListener{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(FilesList.class);

	private final StorageView view;

	/**
	 * @param files
	 */
	public FilesList(EventList files, EventSelectionModel currentSelection, StorageView view, final IconFinder icons, final FileViewDnDManager dnd) {
		super();
		this.view = view;
		setModel(new EventListModel(files));
		
		// listen to mouse clicks on myself - trap double clicks.
		this.addMouseListener(this);
		CSH.setHelpIDString(this,"files.list");
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		setVisibleRowCount(-1);
			
		setSelectionModel(currentSelection);
		
		setCellRenderer(new DefaultListCellRenderer() {
			Dimension dim = new Dimension(100,50); //@todo calculate this more nicely.
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			//@todo optimize this- I redo this every time.
			l.setVerticalTextPosition(JLabel.BOTTOM);
			l.setHorizontalTextPosition(JLabel.CENTER);
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setPreferredSize(dim);
			FileObject f = (FileObject)value;
			final String name = f.getName().getBaseName();
			l.setText(WordUtils.wrap(name,14));
			l.setIcon(icons.find(f)); 
			return l;
		}
		});
		
		// dnd
		dnd.enableDragAndDropFor(this);
		setDragEnabled(true);
			
	}
	public String getToolTipText(MouseEvent e) {
	     int index = locationToIndex(e.getPoint());
	     ListModel dlm = getModel();
	     ensureIndexIsVisible(index);
	     FileObject item = (FileObject)dlm.getElementAt(index);
		if (item == null) {
			return "";
		}
		//@todo add more info in here later.
		return item.getName().getFriendlyURI();
	}
	
// mouse listener interface.
	public void mouseClicked(MouseEvent e) {
		 if(e.getClickCount() == 2){
		     int index = locationToIndex(e.getPoint());
		     ListModel dlm = getModel();
		     ensureIndexIsVisible(index);
		     FileObject item = (FileObject)dlm.getElementAt(index);
		     if (item == null) {
		    	 return;
		     }
		     try {
		     if (item.getType() == FileType.FOLDER) {
		    	 view.move(item);
		     }
		     } catch (FileSystemException ex) {
		    	 //@todo report or recover
		     }
		 }
	}
	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
	}


	public void mousePressed(MouseEvent e) {
		checkForTriggerEvent(e);
	}

	public void mouseReleased(MouseEvent e) {
		checkForTriggerEvent(e);		
	}
	
//	 determine whether event should trigger popup menu
	private void checkForTriggerEvent( MouseEvent event ){
	   if ( event.isPopupTrigger() ) {
		   view.notifyStorageTasks();		   
	      view.getParent().getPopupMenu().show( event.getComponent(),
	         event.getX(), event.getY() );
	   }
	}	

	
}