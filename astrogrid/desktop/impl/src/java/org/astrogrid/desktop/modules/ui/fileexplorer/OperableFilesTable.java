/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ActionMap;
import javax.swing.TransferHandler;

import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;

/** 
 * Extends {@code FilesTable} with operations on files.
 * 
 * 
 * abilities: allow selection, drag-n-drop and right-click popup menu
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 24, 20071:33:35 PM
 */
public class OperableFilesTable extends FilesTable implements MouseListener {


    protected final Filemodel fileModel;

    /**
     * @param icons
     * @param fileModel
     */
    public OperableFilesTable(final Filemodel fileModel) {
        super(fileModel.getChildrenList());
        this.fileModel = fileModel;
        //@todo selection doesn't seem to show up.
        setSelectionModel(fileModel.getSelection());        
        addMouseListener(this);
        
        //dnd
        fileModel.enableDragAndDropFor(this);
        setDragEnabled(true);
        
        // populate the action map with named actions, so they can be triggered from menu.
        final ActionMap aMap = getActionMap();
        aMap.put(UIComponentMenuBar.EditMenuBuilder.COPY,TransferHandler.getCopyAction());
        aMap.put(UIComponentMenuBar.EditMenuBuilder.PASTE,TransferHandler.getPasteAction());
        aMap.put(UIComponentMenuBar.EditMenuBuilder.SELECT_ALL,aMap.get("selectAll"));        
        aMap.put(UIComponentMenuBar.EditMenuBuilder.CLEAR_SELECTION,aMap.get("clearSelection"));        
        
    }

    @Override
    public final String getToolTipText(final MouseEvent e) {
    	final String tip = null;
    	final java.awt.Point p = e.getPoint();
    	final int rowIndex = rowAtPoint(p);
    	final FileObjectView item = fileModel.getChildrenList().get(rowIndex);
    	return createFileTableToolTip(item);
    }

    /** overridable.
     * @param item
     * @return
     */
    protected String createFileTableToolTip(final FileObjectView item) {
        return FilesList.createToolTipFromFileObject(item);
    }
    
    
    public void mouseEntered(final MouseEvent e) {
    }

    public void mouseExited(final MouseEvent e) {
    }

    public void mousePressed(final MouseEvent e) {
        fileModel.maybeShowPopupMenu(e);
    }

    public void mouseReleased(final MouseEvent e) {
        fileModel.maybeShowPopupMenu(e);        
    }

    public void mouseClicked(final MouseEvent e) {
    }

}