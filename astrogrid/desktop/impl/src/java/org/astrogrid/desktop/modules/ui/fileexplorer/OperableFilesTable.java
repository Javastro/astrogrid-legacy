/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.commons.vfs.FileObject;

/** display a table of files, and allow selection, drag-n-drop and right-click popup menu
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 24, 20071:33:35 PM
 */
public class OperableFilesTable extends FilesTable implements MouseListener {


    protected final FileModel fileModel;

    /**
     * @param icons
     * @param fileModel
     */
    public OperableFilesTable(IconFinder icons, FileModel fileModel) {
        super(fileModel.getFiles(),icons);
        this.fileModel = fileModel;
        //@todo selection doesn't seem to show up.
        setSelectionModel(fileModel.getSelection());        
        addMouseListener(this);
        
        //dnd
        fileModel.enableDragAndDropFor(this);
        setDragEnabled(true);        
    }

    public String getToolTipText(MouseEvent e) {
    	String tip = null;
    	java.awt.Point p = e.getPoint();
    	int rowIndex = rowAtPoint(p);
    	FileObject item = (FileObject)fileModel.getFiles().get(rowIndex);
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