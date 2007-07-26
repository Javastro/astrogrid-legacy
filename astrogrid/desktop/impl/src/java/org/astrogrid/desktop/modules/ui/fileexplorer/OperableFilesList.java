/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ListModel;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventSelectionModel;

/** extends files list with 
 *   - ability to select files.
 *   - make this a source for drag and drop (but not a sink)
 *    - the ability to select items and  show a right-click popup.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 24, 200711:58:12 AM
 */
public class OperableFilesList extends FilesList implements MouseListener{

    private final FileModel fileModel;

    /**
     * @param files
     * @param currentSelection selection model - it's up to the 
     * client of this class to listen for notifications on this model.
     * @param icons
     */
    public OperableFilesList( IconFinder icons, FileModel fileModel) {
        super(fileModel.getFiles(),  icons);
        this.fileModel = fileModel;
        setSelectionModel(fileModel.getSelection());      
        this.addMouseListener(this);        
        
        // dnd
        fileModel.enableDragAndDropFor(this);
        setDragEnabled(true);        
    }

    
 // mouse listener interface.
    public void mouseClicked(MouseEvent e) {
    
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
    

}