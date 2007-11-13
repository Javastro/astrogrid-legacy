/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ActionMap;
import javax.swing.TransferHandler;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;

/** display a table of files, and allow selection, drag-n-drop and right-click popup menu
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 24, 20071:33:35 PM
 */
public class OperableFilesTable extends FilesTable implements MouseListener {


    protected final Filemodel fileModel;

    /**
     * @param icons
     * @param fileModel
     */
    public OperableFilesTable(IconFinder icons, Filemodel fileModel) {
        super(fileModel.getChildrenList(),icons);
        this.fileModel = fileModel;
        //@todo selection doesn't seem to show up.
        setSelectionModel(fileModel.getSelection());        
        addMouseListener(this);
        
        //dnd
        fileModel.enableDragAndDropFor(this);
        setDragEnabled(true);
        
        // populate the action map with named actions, so they can be triggered from menu.
        ActionMap aMap = getActionMap();
        aMap.put(UIComponentMenuBar.EditMenuBuilder.COPY,TransferHandler.getCopyAction());
        aMap.put(UIComponentMenuBar.EditMenuBuilder.PASTE,TransferHandler.getPasteAction());
        aMap.put(UIComponentMenuBar.EditMenuBuilder.SELECT_ALL,aMap.get("selectAll"));        
        aMap.put(UIComponentMenuBar.EditMenuBuilder.CLEAR_SELECTION,aMap.get("clearSelection"));        
        
    }

    public String getToolTipText(MouseEvent e) {
    	String tip = null;
    	java.awt.Point p = e.getPoint();
    	int rowIndex = rowAtPoint(p);
    	FileObject item = (FileObject)fileModel.getChildrenList().get(rowIndex);
    	if (item == null) {
    		return "";
    	}
    	//@todo add more info in here later.
    	return item.getName().getFriendlyURI();
    }
    
    
    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        fileModel.maybeShowPopupMenu(e);
    }

    public void mouseReleased(MouseEvent e) {
        fileModel.maybeShowPopupMenu(e);        
    }

    public void mouseClicked(MouseEvent e) {
    }

}