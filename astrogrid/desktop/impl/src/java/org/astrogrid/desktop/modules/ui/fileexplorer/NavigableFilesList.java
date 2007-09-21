package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.event.MouseEvent;

import javax.swing.ListModel;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

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