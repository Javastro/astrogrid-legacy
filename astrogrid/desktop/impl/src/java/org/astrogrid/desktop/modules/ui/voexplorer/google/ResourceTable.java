package org.astrogrid.desktop.modules.ui.voexplorer.google;


import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.TableModel;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.dnd.ResourceListTransferable;
import org.astrogrid.desktop.modules.ui.dnd.ResourceTransferable;
import org.astrogrid.desktop.modules.votech.VoMonInternal;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableModel;


/** A table for displaying resource titles
 * @todo add cut,copy,paste support later.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 12, 20077:33:02 PM
 */
public class ResourceTable extends JTable implements DragGestureListener, DragSourceListener, MouseListener {
	private final List items;
	private final VoMonInternal vomon;
	private final DragSource dragSource;

	/**
	 * 
	 * @param dm the table model to show.
	 * @param items list (probablby a glazed list) of items that the table model is based upon.
	 * @param vomon monitoring information.
	 */
	public ResourceTable(TableModel dm, List items, VoMonInternal vomon) {
		super(dm);
		this.items = items;
		this.vomon = vomon;
		setAutoResizeMode(AUTO_RESIZE_NEXT_COLUMN);
		setBorder(BorderFactory.createEmptyBorder());	
		setShowGrid(false);
        getFormat().configureColumnModel(getColumnModel());
		
		// drag and drop.
		resourceImage = IconHelper.loadIcon("doc16.png").getImage();
		this.dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this,DnDConstants.ACTION_LINK,this);
		setTransferHandler( new ResourceTableTransferHandler());
		setDragEnabled(true);
		//right-click menu.
		popup = new JPopupMenu();
		//1.5 only :( setComponentPopupMenu(popup);
		addMouseListener(this);
	}

	/** construct a tool tip, based on vomon information */
	public String getToolTipText(MouseEvent e) {
		java.awt.Point p = e.getPoint();
		int rowIndex = rowAtPoint(p);
		int colIndex = columnAtPoint(p);
		int realColumnIndex = convertColumnIndexToModel(colIndex);
		
		if (rowIndex > -1) { 
			Resource ri =(Resource) items.get(rowIndex);  // weakness - possiblility of getting the wrong tooltip if the list is rapidly updating. not the end of the world.
            return getFormat().getToolTipText(ri, realColumnIndex);
        } else { 
			return super.getToolTipText(e);
		}
	}

    /**
     * Returns the TableFormat associated with this table.
     *
     * @return  resource table format
     */
    private ResourceTableFomat getFormat() {
        return (ResourceTableFomat) ((EventTableModel) getModel()).getTableFormat();
    }
	
	private JPopupMenu popup;
	
	public void setPopup(JPopupMenu p) {
		this.popup = p;
	}
	public static final Point OFFSET = new Point(8,8);
	public static  final Image RESOURCES_IMAGE = IconHelper.loadIcon("multiplefile16.png").getImage();
	private final Image resourceImage;
	// listen for drag gestures.
	public void dragGestureRecognized(DragGestureEvent dge) {
		Transferable trans = getSelectionTransferable();
		try {
			dge.startDrag(DragSource.DefaultLinkDrop,trans instanceof ResourceListTransferable ? RESOURCES_IMAGE : resourceImage,OFFSET,trans,this); 
		} catch (InvalidDnDOperationException e) {
		    //ignored
			}
	}
	
	private class ResourceTableTransferHandler extends TransferHandler {
		public int getSourceActions(JComponent c) {
			return DnDConstants.ACTION_LINK;
		}
		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			return false;
		}
		protected Transferable createTransferable(JComponent c) {
			return getSelectionTransferable();
		}
	}
		
	
	public Transferable getSelectionTransferable() {
		final EventList selected = ((EventSelectionModel)getSelectionModel()).getSelected();
		switch (selected.size()) {
		case 0:
			// ignore
			return null;
		case 1:
			return new ResourceTransferable((Resource)selected.get(0));
		default:
			return  new ResourceListTransferable(selected);
		} 
	}

	// drag source listener - unsure whether there's anything I want to listen to here..
	public void dragDropEnd(DragSourceDropEvent dsde) {
	    //ignored
	}

	public void dragEnter(DragSourceDragEvent dsde) {
	    //ignored
	}

	public void dragExit(DragSourceEvent dse) {
	    //ignored
	}

	public void dragOver(DragSourceDragEvent dsde) {
	    //ignored
	}

	public void dropActionChanged(DragSourceDragEvent dsde) {
	    //ignored
	}

	// mouse listener interface.
	public void mouseClicked(MouseEvent e) {
	    //ignored
	}

	public void mouseEntered(MouseEvent e) {
	    //ignored
	}

	public void mouseExited(MouseEvent e) {
	    //ignored
	}

	public void mousePressed(MouseEvent e) {
		maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}
	
	 private void maybeShowPopup(MouseEvent e) {
		 //@todo change selection if the click didn't happen in the selection.
	        if (e.isPopupTrigger()) {
	            popup.show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }

}
