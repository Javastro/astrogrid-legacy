package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.event.MouseEvent;

import javax.swing.ListModel;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

/** Extends {@code OperableFilesList} with navigation between folders.
 * 
 * New abilities: navigate around the file hierarchy by double-cicking
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
	public NavigableFilesList(final FileNavigator navigator) {
		super(navigator.getIcons(),navigator.getModel());
        this.navigator = navigator;

	}

	
// override part of the mouse listener interface.
	@Override
    public void mouseClicked(final MouseEvent e) {
		 if(e.getClickCount() == 2){
		     final int index = locationToIndex(e.getPoint());
		     final ListModel dlm = getModel();
		     ensureIndexIsVisible(index);
		     final FileObject item = (FileObject)dlm.getElementAt(index);
		     if (item == null) {
		    	 return;
		     }
		     try {
		     if (item.getType().hasChildren()) {
		    	 navigator.move(item);
		     }
		     } catch (final FileSystemException ex) {
		    	 //@todo report or recover
		     }
		 } else {
		     super.mouseClicked(e);
		 }
	}


	
}