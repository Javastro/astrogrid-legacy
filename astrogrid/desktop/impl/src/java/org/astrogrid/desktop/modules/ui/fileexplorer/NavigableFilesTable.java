/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Extends {@code OperableFilesTable} with navigation between folders.
 * 
 * New abilities: navigate around the file hierarchy by double-cicking
 * on a folder.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 29, 20071:40:57 AM
 */
public class NavigableFilesTable extends OperableFilesTable implements MouseListener{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(NavigableFilesTable.class);
    private final FileNavigator navigator;


    public NavigableFilesTable( final FileNavigator navigator) {
        super(navigator.getModel());
        this.navigator = navigator;

	}
	
	/** traverse on a double-click */
	@Override
    public void mouseClicked(final MouseEvent e) {
		 if(e.getClickCount() == 2){
		     final int row = rowAtPoint(e.getPoint());
		     final FileObjectView item = fileModel.getChildrenList().get(row);
		     if (item == null) {
		    	 return;
		     }
		     // I thought I'd need to convert index scehems first
		     // seems not
		    	 if (item.getType().hasChildren()) {
		    		 navigator.move(item);
		    	 }
		 } else {
		     super.mouseClicked(e);
		 }
	}


	

	
	
}
