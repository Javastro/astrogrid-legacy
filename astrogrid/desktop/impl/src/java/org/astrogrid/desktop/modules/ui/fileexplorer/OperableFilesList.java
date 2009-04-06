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
 * Extends {@code FilesList} with operations on files.
 * 
 Abilities
 *   - select files.
 *   - make this a source for drag and drop (but not a sink)
 *    - the ability to select items and  show a right-click popup.
 * 
 * @author Noel.Winstanley@manchest er.ac.uk
 * @since Jul 24, 200711:58:12 AM
 */
public class OperableFilesList extends FilesList implements MouseListener{

    private final Filemodel fileModel;

    /**
     * @param files
     * @param currentSelection selection model - it's up to the 
     * client of this class to listen for notifications on this model.
     * @param icons
     */
    public OperableFilesList(  final Filemodel fileModel) {
        super(fileModel.getChildrenList());
        this.fileModel = fileModel;
        setSelectionModel(fileModel.getSelection());      
        this.addMouseListener(this);        
        
        // dnd
        fileModel.enableDragAndDropFor(this);
        setDragEnabled(true);    
        
        // populate the action map with named actions, so they can be triggered from menu.
        final ActionMap aMap = getActionMap();
        aMap.put(UIComponentMenuBar.EditMenuBuilder.COPY,TransferHandler.getCopyAction());
        aMap.put(UIComponentMenuBar.EditMenuBuilder.PASTE,TransferHandler.getPasteAction());
        aMap.put(UIComponentMenuBar.EditMenuBuilder.SELECT_ALL,aMap.get("selectAll"));        
        aMap.put(UIComponentMenuBar.EditMenuBuilder.CLEAR_SELECTION,aMap.get("clearSelection"));        
    }

    
 // mouse listener interface.
    public void mouseClicked(final MouseEvent e) {
    
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
    

}