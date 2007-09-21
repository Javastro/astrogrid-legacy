/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

/** extension of operable files table which allows navigation between folders.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 29, 20071:40:57 AM
 */
public class NavigableFilesTable extends OperableFilesTable implements MouseListener{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(NavigableFilesTable.class);
    private final FileNavigator navigator;


    public NavigableFilesTable( FileNavigator navigator) {
        super(navigator.getIcons(),navigator.getModel());
        this.navigator = navigator;

	}
	
	/** traverse on a double-click */
	public void mouseClicked(MouseEvent e) {
		 if(e.getClickCount() == 2){
		     int row = rowAtPoint(e.getPoint());
		     FileObject item = (FileObject)fileModel.getChildrenList().get(row);
		     if (item == null) {
		    	 return;
		     }
		     // I thought I'd need to convert index scehems first
		     // seems not
		     try {
		    	 if (item.getType().hasChildren()) {
		    		 navigator.move(item);
		    	 }
		     } catch (FileSystemException ex) {
		    	 // @todo report?
		     }
		 }		
	}


	

	
	
}
