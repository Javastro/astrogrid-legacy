package org.astrogrid.desktop.modules.ui.fileexplorer;

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

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

/** Main-pane component for StorageView that displays a list of files.
 * 
 * Extends OperableFilesList with the ability to navigate around the file hierarchy by double-cicking
 * on a folder.
 * 
 *  
 *  
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 20071:32:10 AM
 */
public class NavigableFilesList extends OperableFilesList {

    private final FileNavigator navigator;

	/**
	 * @param files
	 */
	public NavigableFilesList(FileNavigator navigator) {
		super(navigator.getIcons(),navigator.getModel());
        this.navigator = navigator;

	}

	
// override part of the mouse listener interface.
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
		     if (item.getType().hasChildren()) {
		    	 navigator.move(item);
		     }
		     } catch (FileSystemException ex) {
		    	 //@todo report or recover
		     }
		 }
	}


	
}